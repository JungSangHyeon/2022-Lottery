package com.example.lottery

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val doubleLightGray = Color(0xfff0f0f0)
    private val tossBlue5 = Color(0xff1b64da)

    private val show = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.wrapContentSize()
                ) {
                    if(show.value){
                        LotteryExample1()
                        LotteryExample2()
                        LotteryExample3()
                    }
                }
            }
        }
    }

    private fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun reset(){
        lifecycleScope.launch {
            show.value = false
            delay(300)
            show.value = true
        }
    }

    @Composable
    private fun LotteryExample1() = Lottery(
        modifier = Modifier
            .size(300.dp, 100.dp)
            .clip(RoundedCornerShape(20)),
        showContentCallback = { showToast("Hello World Showing") }
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .matchParentSize()
                .background(doubleLightGray)
        ){
            Text(
                text = "Hello World!",
                fontSize = 20.sp
            )
        }
    }

    @Composable
    private fun LotteryExample2() = Lottery(
        modifier = Modifier
            .size(300.dp, 300.dp)
            .clip(RoundedCornerShape(20)),
        coinSize = 120,
        showContentCallback = { showToast("Thumb Up Showing") }
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .matchParentSize()
                .background(doubleLightGray)
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_outline_thumb_up_24),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
    }

    @Composable
    private fun LotteryExample3() = Lottery(
        modifier = Modifier
            .size(300.dp, 100.dp)
            .clip(RoundedCornerShape(20)),
        coinSize = 120,
        showContentCallback = { showToast("Reset Button Showing") }
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .matchParentSize()
                .background(doubleLightGray)
        ){
            Text(
                text = "Reset",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(20))
                    .clickable { reset() }
                    .background(tossBlue5)
                    .padding(16.dp, 8.dp)
            )
        }
    }
}