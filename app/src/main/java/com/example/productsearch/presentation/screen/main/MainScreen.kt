package com.example.productsearch.presentation.screen.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.productsearch.R
import com.example.productsearch.domain.entity.Product
import com.example.productsearch.getApplicationComponent
import com.example.productsearch.presentation.ui.theme.LightGrey
import com.example.productsearch.presentation.ui.theme.RetryLoadDataButton

@Composable
fun MainScreen(
    onClickedCard: (Int, Boolean) -> Unit,
    onSearchProduct: () -> Unit,
) {
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
                nextDataIsLoading = currentState.nextDataIsLoading,
                isError = isError,
                onClickedCard = { onClickedCard(it, false) },
                onSearchProduct = onSearchProduct,
                onLoadNextData = { viewModel.loadNextProducts() },
                onCategorySelected = { viewModel.loadProductsByCategory(it) }
            )
        }

        ProductState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                RetryLoadDataButton { viewModel.loadNextProducts() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    items: () -> List<Product>,
    nextDataIsLoading: Boolean,
    isError: Boolean,
    onClickedCard: (Int) -> Unit,
    onSearchProduct: () -> Unit,
    onLoadNextData: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    val listState = rememberLazyGridState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { onSearchProduct() }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            FilterCategoryChip(onCategorySelected = onCategorySelected)
            LazyVerticalGrid(
                state = listState,
                modifier = Modifier
                    .fillMaxSize(),
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
                    ProductItem(
                        productItem = it,
                        onClickedCard = { itemId ->
                            onClickedCard(itemId)
                        }
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    if (isError) {
                        RetryLoadDataButton { onLoadNextData() }
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
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ProductItem(
    productItem: Product,
    onClickedCard: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClickedCard(productItem.id) }
    ) {
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
        Row() {
            Text(
                text = "${productItem.getPriceWithDiscount()}$",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "-${productItem.discountPercentage}%",
                color = Color.White,
                fontSize = 8.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Red)
                    .padding(3.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${productItem.price.toInt()}$",
                fontSize = 12.sp,
                color = Color.Gray,
                style = TextStyle(textDecoration = TextDecoration.LineThrough)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterCategoryChip(
    onCategorySelected: (String) -> Unit
) {
    var selected by remember { mutableStateOf("All") }
    val scrollState = rememberScrollState()
    val categories = listOf("All", "smartphones", "3333333", "444444444444444")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .horizontalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        categories.forEach { category ->
            FilterChip(
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                selected = selected == category,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.DarkGray,
                    selectedLabelColor = Color.White
                ),
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}
