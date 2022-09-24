package com.example.lottery

import android.graphics.*
import android.graphics.Canvas as GraphicsCanvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.get

@Composable
fun Lottery(
    modifier: Modifier,
    contentGuardColor: Int = Color.LTGRAY,
    coinSize: Int = 80,
    xSamplingCount: Int = 5,
    ySamplingCount: Int = 5,
    isShowingJudgeFactor: Float = 0.75f,
    showContentCallback: ()->Unit,
    content: @Composable BoxScope.()->Unit
) = Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
) {
    val contentGuardSize = remember{ mutableStateOf<IntSize?>(null) }
    val contentGuard = rememberSaveable{ mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(contentGuardSize.value){
        initializeContentGuard(contentGuardSize, contentGuard, contentGuardColor)
    }

    val dragPoint = remember { mutableStateOf<Offset?>(null) }
    LaunchedEffect(dragPoint.value){
        erase(dragPoint, contentGuard, coinSize)
    }

    val isShowingContent = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(contentGuard.value){
        if(!isShowingContent.value){
            checkIsShowing(
                contentGuard,
                xSamplingCount, ySamplingCount,
                isShowingJudgeFactor, isShowingContent,
                showContentCallback
            )
        }
    }

    content()

    if(!isShowingContent.value){
        ContentGuard(dragPoint, contentGuard, contentGuardSize)
    }
}

@Composable
private fun BoxScope.ContentGuard(
    dragPoint: MutableState<Offset?>,
    contentGuard: MutableState<Bitmap?>,
    contentGuardSize: MutableState<IntSize?>
) = Canvas(
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
            if(contentGuard.value == null) // for save state
                contentGuardSize.value = it.size
        }
){
    contentGuard.value?.let {
        drawImage(it.asImageBitmap())
    }
}

private fun initializeContentGuard(
    contentGuardSize: MutableState<IntSize?>,
    contentGuard: MutableState<Bitmap?>,
    contentGuardColor: Int
) = contentGuardSize.value?.let {
    val tempBitmap = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
    GraphicsCanvas(tempBitmap).run {
        val canvasRect = Rect(0, 0, it.width, it.height)
        val backgroundPainter = Paint().apply { color = contentGuardColor }
        drawRect(canvasRect, backgroundPainter)
    }
    contentGuard.value = tempBitmap
}

private fun erase(
    dragPoint: MutableState<Offset?>,
    contentGuard: MutableState<Bitmap?>,
    coinSize: Int
) = dragPoint.value?.let { point ->
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

private fun checkIsShowing(
    contentGuard: MutableState<Bitmap?>,
    xSamplingCount: Int,
    ySamplingCount: Int,
    isShowingJudgeFactor: Float,
    isShowingContent: MutableState<Boolean>,
    showContentCallback: () -> Unit
) = contentGuard.value?.let {
    val samplingCount = xSamplingCount * ySamplingCount
    var erasedCount = 0

    val xSpace = it.width / (xSamplingCount + 1)
    val ySpace = it.height / (ySamplingCount + 1)
    (1 .. xSamplingCount).forEach { x ->
        (1 .. ySamplingCount).forEach { y ->
            val xPoint = x * xSpace
            val yPoint = y * ySpace
            if(Color.TRANSPARENT == it[xPoint, yPoint]) erasedCount++
        }
    }

    val erasePercent = erasedCount.toFloat()/samplingCount
    isShowingContent.value = erasePercent > isShowingJudgeFactor
    if(isShowingContent.value) showContentCallback()
}