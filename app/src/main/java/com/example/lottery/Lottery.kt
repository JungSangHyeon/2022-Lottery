package com.example.lottery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Lottery(
    modifier: Modifier,
    showContentCallback: ()->Unit,
    content: @Composable BoxScope.()->Unit
) = Box(
    modifier = modifier
) {
    content()
}