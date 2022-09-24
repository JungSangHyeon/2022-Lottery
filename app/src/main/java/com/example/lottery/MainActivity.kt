package com.example.lottery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lottery(
                modifier = Modifier.size(300.dp, 100.dp),
                showContentCallback = ::showContentCallback
            ){
                TODO()
            }
        }
    }

    private fun showContentCallback() {
        TODO("Not yet implemented")
    }
}