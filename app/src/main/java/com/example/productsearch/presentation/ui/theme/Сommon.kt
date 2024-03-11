package com.example.productsearch.presentation.ui.theme

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.productsearch.R

@Composable
fun RetryLoadDataButton(
    onLoadNextData: () -> Unit
) {
    Button(
        modifier = Modifier.wrapContentSize(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
        onClick = { onLoadNextData() }
    ) {
        Text(text = stringResource(R.string.retry_load))
    }
}
