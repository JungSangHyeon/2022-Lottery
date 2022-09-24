package com.example.lottery

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
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
                Box(
                   contentAlignment = Alignment.Center,
                   modifier = Modifier.matchParentSize()
                ){
                    Text(text = "hello world")
                }
            }
        }
    }

    private fun showContentCallback() {
        Toast.makeText(this, "WOW!", Toast.LENGTH_SHORT).show()
    }
}