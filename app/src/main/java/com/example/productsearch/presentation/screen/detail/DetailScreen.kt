package com.example.productsearch.presentation.screen.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.productsearch.R
import com.example.productsearch.domain.entity.Product
import com.example.productsearch.getApplicationComponent
import com.example.productsearch.presentation.ui.theme.RetryLoadDataButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    itemId: Int,
    isFlag: Boolean,
    onBackPressed: (Boolean) -> Unit
) {
    val component = getApplicationComponent()
    val viewModel: DetailViewModel = viewModel(factory = component.getViewModelFactory())

    LaunchedEffect(itemId) {
        viewModel.getInfo(itemId)
    }

    val state = viewModel.product.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed(isFlag) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val currentState = state.value) {
            DetailState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.load_error))
                    Spacer(modifier = Modifier.height(8.dp))
                    RetryLoadDataButton { viewModel.getInfo(itemId) }
                }
            }
            DetailState.Initial -> {}
            DetailState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.DarkGray)
                }
            }
            is DetailState.Products -> {
                DetailContent(currentState.product, innerPadding = innerPadding)
            }
        }
    }
    BackHandler(onBack = { onBackPressed(isFlag) })
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun DetailContent(
    item: Product,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
    ) {
        GlideImage(
            model = item.thumbnail,
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )
        Text(
            text = item.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Row(Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "${item.getPriceWithDiscount()}$",
                fontSize = 28.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "-${item.discountPercentage}%",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Red)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${item.price.toInt()}$",
                fontSize = 19.sp,
                color = Color.Gray,
                style = TextStyle(textDecoration = TextDecoration.LineThrough)
            )
        }
        Text(
            text = stringResource(R.string.about_the_product),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp)
        )
        Text(
            text = item.description,
            fontSize = 16.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Product code: ${item.id}",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Brand: ${item.brand}",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Rating: ${item.rating}",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Left in stock: ${item.stock}",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}