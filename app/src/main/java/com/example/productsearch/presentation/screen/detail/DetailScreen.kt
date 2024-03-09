package com.example.productsearch.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.productsearch.R
import com.example.productsearch.getApplicationComponent
import com.example.productsearch.presentation.ui.theme.RetryLoadDataButton

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(
    itemId: Int
) {
    val component = getApplicationComponent()
    val viewModel: DetailViewModel = viewModel(factory = component.getViewModelFactory())

    LaunchedEffect(itemId) {
        viewModel.getInfo(itemId)
    }

    val state = viewModel.product.collectAsState()

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
            Column {
                Text(text = "id: ${currentState.product.id}")
                Text(text = "brand: ${currentState.product.brand}")
                GlideImage(
                    model = currentState.product.thumbnail,
                    contentDescription = null
                )
            }

        }
    }
}