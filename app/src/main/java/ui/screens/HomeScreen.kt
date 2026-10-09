package com.example.rootmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    onRootClick: () -> Unit
) {

    var status by remember {
        mutableStateOf("ROOT NOT CHECKED")
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = status,
            color = Color.Cyan
        )


        Button(
            onClick = {

                scope.launch {

                    val result = withContext(Dispatchers.IO) {

                        try {
                            val process = Runtime.getRuntime().exec(
                                arrayOf("su", "-c", "dumpsys battery set level 100")
                            )

                            val output = process.inputStream
                                .bufferedReader()
                                .readText()

                            process.waitFor()

                            output
                        } catch (e: Exception) {
                            "ERROR: ${e.message}"
                        }
                    }

                    status = result
                }
            }
        ) {
            Text(
                text = "fully charge please",
                color = Color.Magenta
            )
        }

        Button(
            onClick = {

                scope.launch {

                    val result = withContext(Dispatchers.IO) {

                        try {
                            val process = Runtime.getRuntime().exec(
                                arrayOf("su", "-c", "dumpsys battery set level 1000")
                            )

                            val output = process.inputStream
                                .bufferedReader()
                                .readText()

                            val exitCode = process.waitFor()

                            if (exitCode == 0) {
                                output.ifBlank { "SUCCESS" }
                            } else {
                                "COMMAND FAILED"
                            }

                        } catch (e: Exception) {
                            "ERROR: ${e.message}"
                        }
                    }

                    status = result
                }
            },

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan,
                contentColor = Color.White
            )
        ) {
            Text("charge please")
        }
        Button(
            onClick = {

                scope.launch {

                    val result = withContext(Dispatchers.IO) {

                        try {
                            val process = Runtime.getRuntime().exec(
                                arrayOf("su", "-c", "dumpsys battery reset")
                            )

                            val output = process.inputStream
                                .bufferedReader()
                                .readText()

                            val exitCode = process.waitFor()

                            if (exitCode == 0) {
                                output.ifBlank { "SUCCESS" }
                            } else {
                                "COMMAND FAILED"
                            }

                        } catch (e: Exception) {
                            "ERROR: ${e.message}"
                        }
                    }

                    status = result
                }
            },

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Black
            )
        ) {
            Text("reset baterie")
        }
        Button(
            onClick = {

                scope.launch {

                    val result = withContext(Dispatchers.IO) {

                        try {
                            val process = Runtime.getRuntime().exec(
                                arrayOf("su", "-c", "su -c \"pkill -TERM -f com.android.systemui\"")
                            )

                            val output = process.inputStream
                                .bufferedReader()
                                .readText()

                            val exitCode = process.waitFor()

                            if (exitCode == 0) {
                                output.ifBlank { "SUCCESS" }
                            } else {
                                "COMMAND FAILED"
                            }

                        } catch (e: Exception) {
                            "ERROR: ${e.message}"
                        }
                    }

                    status = result
                }
            },

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text("restart systemUI ")
        }
    }
}