package com.example.lottery

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Canvas as GraphicsCanvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

@Composable
fun Lottery(
    modifier: Modifier,
    contentGuardColor: Int = Color.LTGRAY,
    showContentCallback: ()->Unit,
    content: @Composable BoxScope.()->Unit
) = Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
) {
    val contentGuardSize = remember{ mutableStateOf<IntSize?>(null) }
    val contentGuard = remember{ mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(contentGuardSize.value){
        contentGuardSize.value?.let {
            val tempBitmap = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
            GraphicsCanvas(tempBitmap).run {
                val canvasRect = Rect(0, 0, it.width, it.height)
                val backgroundPainter = Paint().apply { color = contentGuardColor }
                drawRect(canvasRect, backgroundPainter)
            }
            contentGuard.value = tempBitmap
        }
    }

    content()

    Canvas(
        modifier = Modifier
            .matchParentSize()
            .onGloballyPositioned {
                contentGuardSize.value = it.size
            }
    ){
        contentGuard.value?.let {
            drawImage(it.asImageBitmap())
        }
    }
}