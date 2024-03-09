package com.example.productsearch.presentation.screen.main

import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val isError = viewModel.errorState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.toastEventsFlow.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    when (val currentState = state.value) {
        ProductState.Initial -> {}
        ProductState.Loading -> {
            if (isError) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Не удалось загрузить данные",
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.DarkGray)
                }
            }
        }
        is ProductState.Products -> {
            MainScreenContent(
                items = { currentState.products },
                nextDataIsLoading = currentState.nextDataIsLoading,
                isError = isError,
                onLoadNextData = { viewModel.loadNextProducts() }
            )
        }

        ProductState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier.wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    onClick = { viewModel.loadNextProducts() }
                ) {
                    Text(text = "Повторить попытку")
                }
            }
        }
    }
}

@Composable
private fun MainScreenContent(
    items: () -> List<Product>,
    nextDataIsLoading: Boolean,
    isError: Boolean,
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
            if (isError) {
                Button(
                    modifier = Modifier.wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    onClick = { onLoadNextData() }
                ) {
                    Text(text = "Повторить попытку")
                }
            } else if (nextDataIsLoading) {
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
        Text(
            text = productItem.title,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = productItem.description,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = Color.DarkGray,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${productItem.price.toInt()}$",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}