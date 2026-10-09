package com.example.rootmanager.ui.screens

import android.app.ActivityManager
import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HWScreen(
    onHomeClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("SYSTEM STATUS: LIVE MONITORING (10Hz)") }
    var memInfo by remember { mutableStateOf("Scanning RAM...") }
    var storageInfo by remember { mutableStateOf("Scanning SSD/Storage...") }
    var cpuInfo by remember { mutableStateOf("Scanning CPU Cores & Frequencies...") }
    
    var ramProgress by remember { mutableStateOf(0f) }
    var storageProgress by remember { mutableStateOf(0f) }

    // Neon wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "neon_wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    fun loadHwInfo() {
        // RAM Info
        try {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfoApp = ActivityManager.MemoryInfo()
            actManager.getMemoryInfo(memInfoApp)
            val totalRamMb = memInfoApp.totalMem / (1024 * 1024)
            val availRamMb = memInfoApp.availMem / (1024 * 1024)
            val usedRamMb = totalRamMb - availRamMb
            if (totalRamMb > 0) {
                ramProgress = (usedRamMb.toFloat() / totalRamMb.toFloat()).coerceIn(0f, 1f)
            }
            memInfo = "Total RAM: ${totalRamMb} MB\nUsed RAM: ${usedRamMb} MB\nAvailable RAM: ${availRamMb} MB"
        } catch (e: Exception) {
            memInfo = "Error reading RAM: ${e.message}"
        }

        // Storage Info
        try {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            val availableBlocks = stat.availableBlocksLong
            val totalGb = (totalBlocks * blockSize) / (1024 * 1024 * 1024)
            val freeGb = (availableBlocks * blockSize) / (1024 * 1024 * 1024)
            val usedGb = totalGb - freeGb
            if (totalGb > 0) {
                storageProgress = (usedGb.toFloat() / totalGb.toFloat()).coerceIn(0f, 1f)
            }
            storageInfo = "Internal Storage (SSD/Flash):\nTotal: ${totalGb} GB\nUsed: ${usedGb} GB\nFree: ${freeGb} GB"
        } catch (e: Exception) {
            storageInfo = "Error reading Storage: ${e.message}"
        }
    }

    // Auto-refresh 10 times a second (every 100ms)
    LaunchedEffect(Unit) {
        while (true) {
            loadHwInfo()
            delay(100)
        }
    }

    // CPU Info (refresh 10 times a second)
    LaunchedEffect(Unit) {
        while (true) {
            val cpuResult = withContext(Dispatchers.IO) {
                try {
                    val sb = StringBuilder()
                    val cpuCount = Runtime.getRuntime().availableProcessors()
                    sb.append("Active CPU Cores: $cpuCount\n\n")
                    
                    for (i in 0 until cpuCount) {
                        val freqFile = File("/sys/devices/system/cpu/cpu$i/cpufreq/scaling_cur_freq")
                        if (freqFile.exists()) {
                            val freqMhz = freqFile.readText().trim().toLongOrNull()?.div(1000)
                            sb.append("• CPU Core $i: ${freqMhz ?: "N/A"} MHz\n")
                        } else {
                            sb.append("• CPU Core $i: Online\n")
                        }
                    }
                    
                    val cpuinfoFile = File("/proc/cpuinfo")
                    if (cpuinfoFile.exists()) {
                        val model = cpuinfoFile.readLines().firstOrNull { it.contains("model name") || it.contains("Hardware") || it.contains("Processor") }
                        if (model != null) {
                            sb.append("\nHardware: $model")
                        }
                    }
                    sb.toString()
                } catch (e: Exception) {
                    "Error reading CPU info: ${e.message}"
                }
            }
            cpuInfo = cpuResult
            delay(100)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "⚡ CYBERNETIC HW MATRIX (10Hz) ⚡",
            color = Color.Cyan,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )

        // Neon Wavy Animation Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .border(1.dp, Color.Magenta, RoundedCornerShape(12.dp))
                .background(Color.Black, RoundedCornerShape(12.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                
                // Cyan Wave
                val path1 = Path().apply {
                    var x = 0f
                    while (x <= width) {
                        val y = (height / 2f) + sin(x * 0.03f + phase) * 18f + cos(x * 0.015f + phase * 0.5f) * 10f
                        if (x == 0f) moveTo(x, y) else lineTo(x, y)
                        x += 4f
                    }
                }
                drawPath(path1, Color.Cyan, style = Stroke(width = 2.5f.dp.toPx()))

                // Magenta Wave
                val path2 = Path().apply {
                    var x = 0f
                    while (x <= width) {
                        val y = (height / 2f) + cos(x * 0.025f - phase) * 15f + sin(x * 0.02f - phase * 0.8f) * 12f
                        if (x == 0f) moveTo(x, y) else lineTo(x, y)
                        x += 4f
                    }
                }
                drawPath(path2, Color.Magenta, style = Stroke(width = 2f.dp.toPx()))
            }
            Text(
                text = "NEON FREQ OSCILLATOR",
                color = Color.Green,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 4.dp)
            )
        }

        if (status.isNotEmpty()) {
            Text(
                text = status,
                color = Color.Yellow,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // RAM Card with Progress Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, Color.Cyan, RoundedCornerShape(12.dp))
                .background(Color.Black, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Text(text = "📊 RAM (MEMORY MATRIX)", color = Color.Cyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { ramProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color.Cyan,
                trackColor = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = memInfo, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
        }

        // Storage Card with Progress Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, Color.Magenta, RoundedCornerShape(12.dp))
                .background(Color.Black, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Text(text = "💾 STORAGE (SSD / FLASH)", color = Color.Magenta, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { storageProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color.Magenta,
                trackColor = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = storageInfo, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
        }

        // CPU Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, Color.Green, RoundedCornerShape(12.dp))
                .background(Color.Black, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Text(text = "⚡ CPU & CLOCK SPEEDS", color = Color.Green, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = cpuInfo, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
        }

        // Action Buttons
        Button(
            onClick = {
                scope.launch {
                    val res = withContext(Dispatchers.IO) {
                        try {
                            val p = Runtime.getRuntime().exec(arrayOf("su", "-c", "echo performance > /sys/devices/system/cpu/cpu*/cpufreq/scaling_governor"))
                            val exitCode = p.waitFor()
                            if (exitCode == 0) "GOVERNOR: MAXIMUM PERFORMANCE" else "GOVERNOR COMMAND FAILED"
                        } catch (e: Exception) {
                            "ERROR: ${e.message}"
                        }
                    }
                    status = res
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green, contentColor = Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("OVERCLOCK / PERFORMANCE MODE", fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = {
                scope.launch {
                    val res = withContext(Dispatchers.IO) {
                        try {
                            val p = Runtime.getRuntime().exec(arrayOf("su", "-c", "echo powersave > /sys/devices/system/cpu/cpu*/cpufreq/scaling_governor"))
                            val exitCode = p.waitFor()
                            if (exitCode == 0) "GOVERNOR: ECO POWERSAVE" else "GOVERNOR COMMAND FAILED"
                        } catch (e: Exception) {
                            "ERROR: ${e.message}"
                        }
                    }
                    status = res
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ECO POWERSAVE MODE", fontWeight = FontWeight.Bold)
        }
    }
}
