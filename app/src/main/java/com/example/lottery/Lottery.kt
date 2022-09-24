package com.example.lottery

import android.graphics.*
import android.util.Log
import android.graphics.Canvas as GraphicsCanvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

@Composable
fun Lottery(
    modifier: Modifier,
    contentGuardColor: Int = Color.LTGRAY,
    coinSize: Int = 80,
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

    val dragPoint = remember { mutableStateOf<Offset?>(null) }

    LaunchedEffect(dragPoint.value){
        dragPoint.value?.let { point ->
            contentGuard.value?.let { contentGuardBitmap ->
                val tempBitmap = Bitmap.createBitmap(contentGuardBitmap.width, contentGuardBitmap.height, Bitmap.Config.ARGB_8888)
                GraphicsCanvas(tempBitmap).run {
                    drawBitmap(contentGuardBitmap, 0f, 0f, null)
                    val eraseRect = RectF(
                        point.x - coinSize / 2,
                        point.y - coinSize / 2,
                        point.x + coinSize / 2,
                        point.y + coinSize / 2
                    )
                    val erasePainter = Paint().apply {
                        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                    }
                    drawOval(eraseRect, erasePainter)
                }
                contentGuard.value = tempBitmap
            }
        }
    }

    content()

    Canvas(
        modifier = Modifier
            .matchParentSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragPoint.value = it
                    },
                    onDrag = { change, dragAmount ->
                        change.consumeAllChanges()
                        dragPoint.value = dragPoint.value?.plus(dragAmount) ?: dragPoint.value
                    }
                )
            }
            .onGloballyPositioned {
                contentGuardSize.value = it.size
            }
    ){
        contentGuard.value?.let {
            drawImage(it.asImageBitmap())
        }
    }
}