package com.example.productsearch.presentation.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.productsearch.domain.entity.Product
import com.example.productsearch.getApplicationComponent
import com.example.productsearch.presentation.ui.theme.LightGrey

@Composable
fun MainScreen() {
    val component = getApplicationComponent()
    val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
    val state = viewModel.screenState.collectAsState(ProductState.Initial)

    when (val currentState = state.value) {
        ProductState.Error -> {}
        ProductState.Initial -> {}
        ProductState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.DarkGray)
            }
        }
        is ProductState.Products -> {
            MainScreenContent(
                items = { currentState.products },
                nextDataIsLoading = currentState.nextDataIsLoading
            ) { viewModel.loadNextProducts() }
        }
    }
}

@Composable
private fun MainScreenContent(
    items: () -> List<Product>,
    nextDataIsLoading: Boolean,
    onLoadNextData: () -> Unit
) {
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = items(),
            key = { item ->
                item.id
            }
        ) {
            ProductItem(it)
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.DarkGray,
                        modifier = Modifier.size(34.dp)
                    )
                }
            } else {
                SideEffect {
                    onLoadNextData()
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ProductItem(
    productItem: Product
) {
    Column {
        Card(
            modifier = Modifier
                .height(160.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = LightGrey)
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = productItem.thumbnail,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = productItem.title, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = productItem.description,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Color.DarkGray,
            maxLines = 3, // Ограничивает текст тремя строками
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${productItem.price.toInt()}$",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}