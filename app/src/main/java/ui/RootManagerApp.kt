package com.example.rootmanager.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rootmanager.ui.screens.DangerousScreen
import com.example.rootmanager.ui.screens.HWScreen
import com.example.rootmanager.ui.screens.HomeScreen
import com.example.rootmanager.ui.screens.RootScreen

@Composable
fun RootManagerApp() {

    var screen by remember {
        mutableStateOf("home")
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black,
                contentColor = Color.Cyan,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Cyan,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                NavigationBarItem(
                    selected = screen == "home",
                    onClick = {
                        screen = "home"
                    },
                    icon = {
                        Text("⌂")
                    },
                    label = {
                        Text("Home")
                    }
                )

                NavigationBarItem(
                    selected = screen == "root",
                    onClick = {
                        screen = "root"
                    },
                    icon = {
                        Text("⚡")
                    },
                    label = {
                        Text("Misc")
                    }
                )

                NavigationBarItem(
                    selected = screen == "hw",
                    onClick = {
                        screen = "hw"
                    },
                    icon = {
                        Text("💻")
                    },
                    label = {
                        Text("HW")
                    }
                )

                NavigationBarItem(
                    selected = screen == "Dangerous",
                    onClick = {
                        screen = "Dangerous"
                    },
                    icon = {
                        Text("⚠\uFE0F")
                    },
                    label = {
                        Text("Danger")
                    }
                )

            }
        }
    ) { paddingValues ->

        when (screen) {

            "home" -> {
                HomeScreen(
                    onRootClick = {
                        screen = "root"
                    }
                )
            }

            "root" -> {
                RootScreen(
                    onHomeClick = {
                        screen = "home"
                    }
                )
            }

            "hw" -> {
                HWScreen(
                    onHomeClick = {
                        screen = "home"
                    }
                )
            }

            "Dangerous" -> {
                DangerousScreen(
                    onHomeClick = {
                        screen = "home"
                    }
                )
            }
        }
    }
}
