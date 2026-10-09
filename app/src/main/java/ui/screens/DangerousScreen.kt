package com.example.rootmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun DangerousScreen(
    onHomeClick: () -> Unit
) {
    var status by remember { mutableStateOf("") }
    var selectedAction by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "DANGEROUS SCREEN",
            color = Color.White
        )

        if (status.isNotEmpty()) {
            Text(
                text = status,
                color = Color.White
            )
        }

        Button(
            onClick = {
                selectedAction = "REBOOT"
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Reboot")
        }

        Button(
            onClick = {
                selectedAction = "REBOOT_RECOVERY"
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Reboot Recovery")
        }

        Button(
            onClick = {
                selectedAction = "REBOOT_BOOTLOADER"
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Reboot Bootloader")
        }

        Button(
            onClick = {
                selectedAction = "REBOOT_EDL"
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Reboot EDL / Download")
        }

        Button(
            onClick = {
                selectedAction = "KERNEL_PANIC"
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Kernel Panic")
        }

        Button(
            onClick = {
                selectedAction = "HARDKILL"
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Kernel Panic — Hardkill")
        }
    }

    if (selectedAction != null) {
        AlertDialog(
            onDismissRequest = {
                selectedAction = null
            },
            containerColor = Color.Black,
            title = {
                Text(
                    text = "⚠️ DANGER",
                    color = Color.Red
                )
            },
            text = {
                Text(
                    text = when (selectedAction) {
                        "REBOOT" -> "Are you sure you want to reboot the device?"
                        "REBOOT_RECOVERY" -> "Are you sure you want to reboot into recovery mode?"
                        "REBOOT_BOOTLOADER" -> "Are you sure you want to reboot into bootloader mode?"
                        "REBOOT_EDL" -> "Are you sure you want to reboot into EDL (Emergency Download) mode?"
                        "KERNEL_PANIC" -> "This action is intended to simulate a kernel panic. Continue?"
                        else -> "This action simulates a critical system failure. Continue?"
                    },
                    color = Color.White
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val action = selectedAction
                        selectedAction = null
                        if (action != null) {
                            scope.launch {
                                val result = withContext(Dispatchers.IO) {
                                    try {
                                        val cmd = when (action) {
                                            "REBOOT" -> "reboot"
                                            "REBOOT_RECOVERY" -> "reboot recovery"
                                            "REBOOT_BOOTLOADER" -> "reboot bootloader"
                                            "REBOOT_EDL" -> "reboot edl"
                                            "KERNEL_PANIC" -> "echo c > /proc/sysrq-trigger"
                                            "HARDKILL" -> "kill -9 1"
                                            else -> ""
                                        }
                                        if (cmd.isNotEmpty()) {
                                            val process = Runtime.getRuntime().exec(
                                                arrayOf("su", "-c", cmd)
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
                                        } else {
                                            "UNKNOWN ACTION"
                                        }
                                    } catch (e: Exception) {
                                        "ERROR: ${e.message}"
                                    }
                                }
                                status = result
                            }
                        }
                    }
                ) {
                    Text("CONFIRM", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedAction = null
                    }
                ) {
                    Text("CANCEL", color = Color.Cyan)
                }
            }
        )
    }
}
