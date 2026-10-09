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
fun RootScreen(
    onHomeClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ROOT SCREEN",
            color = Color.Cyan
        )
        if (status.isNotEmpty()) {
            Text(
                text = status,
                color = Color.White
            )
        }
        Button(
            onClick = {

                scope.launch {

                    val result = withContext(Dispatchers.IO) {

                        try {
                            val process = Runtime.getRuntime().exec(
                                arrayOf("su", "-c", "setenforce 0")
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
            Text("SELinux permissive")
        }

        Button(
            onClick = {

                scope.launch {

                    val result = withContext(Dispatchers.IO) {

                        try {
                            val process = Runtime.getRuntime().exec(
                                arrayOf("su", "-c", "setenforce 1")
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
            Text("SELinux enforcing")
        }
        Button(
            onClick = {

                scope.launch {

                    val result = withContext(Dispatchers.IO) {

                        try {
                            val process = Runtime.getRuntime().exec(
                                arrayOf("su", "-c", "id")
                            )

                            val output = process.inputStream
                                .bufferedReader()
                                .readText()

                            val exitCode = process.waitFor()

                            if (exitCode == 0) {
                                "ROOT OK\n$output"
                            } else {
                                "ROOT FAILED"
                            }

                        } catch (e: Exception) {
                            "SU ERROR: ${e.message}"
                        }
                    }

                    status = result
                }
            }
        ) {
            Text("CHECK ROOT")
        }
        Button(
            onClick = {

                scope.launch {

                    val result = withContext(Dispatchers.IO) {

                        try {
                            val process = Runtime.getRuntime().exec(
                                arrayOf("su", "-c", "svc wifi disable; svc bluetooth disable; svc data disable; sleep 1; svc wifi enable; svc bluetooth enable; svc data enable")
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
            Text("restart sítí ")
        }
    }
}
