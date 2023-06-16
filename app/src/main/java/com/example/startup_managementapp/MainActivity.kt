package com.example.startup_managementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.startup_managementapp.ui.theme.Startup_managementAppTheme
import androidx.compose.ui.unit.sp as sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Startup_managementAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Logo(name = "Startup Management", modifier = Modifier.padding(bottom = 100.dp))
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier,) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { /* TODO: Handle login button click */

            }) {
                Text(text = "Login")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { /* TODO: Handle signup button click */ }) {
                Text(text = "Signup")
            }
        }
    }
}

@Composable
fun Logo(name: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
//            color = Color.White
        )
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Startup_managementAppTheme {
        Logo(name = "Startup Management", modifier = Modifier.padding(bottom = 100.dp))
        Greeting("Android")
    }
}