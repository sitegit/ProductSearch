package com.example.productsearch.presentation.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.productsearch.R
import com.example.productsearch.domain.entity.Product
import com.example.productsearch.getApplicationComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onClickBack: () -> Unit,
    onProductClick: (Int) -> Unit,
) {
    val component = getApplicationComponent()
    val viewModel: SearchViewModel = viewModel(factory = component.getViewModelFactory())

    val state = viewModel.products.collectAsState()

    var query by remember { mutableStateOf("") }

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    SearchBar(
        modifier = Modifier.focusRequester(focusRequester),
        placeholder = { Text(text = stringResource(R.string.search)) },
        query = query,
        onQueryChange = { query = it },
        onSearch = { viewModel.searchProduct(query) },
        colors = SearchBarDefaults.colors(containerColor = Color.White),
        active = true,
        onActiveChange = {
            if (!it) onClickBack()
        },
        leadingIcon = {
            IconButton(onClick = onClickBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { viewModel.searchProduct(query) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        }
    ) {
        when (val searchState = state.value) {
            SearchState.EmptyResult -> {
                Text(
                    text = stringResource(R.string.empty_result),
                    modifier = Modifier.padding(8.dp)
                )
            }
            is SearchState.Error -> {
                Text(
                    text = stringResource(R.string.something_went_wrong),
                    modifier = Modifier.padding(8.dp)
                )
            }
            SearchState.Initial -> {}
            SearchState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            is SearchState.SuccessLoaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = searchState.result.products,
                        key = { it.id }
                    ) {
                        ProductsCard(
                            product = it,
                            onProductClick = onProductClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductsCard(
    product: Product,
    onProductClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onProductClick(product.id) }
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
        ) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.category)
        }
    }
}