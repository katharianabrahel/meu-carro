package com.example.meucarro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.meucarro.services.database.user_preferences.UserPreferences
import com.example.meucarro.services.worker.scheduleReminder

@Composable
fun LaunchScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val isChecking = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val token = userPrefs.getToken()
        isChecking.value = false
        if (!token.isNullOrBlank()) {
            scheduleReminder(context)
            navController.navigate("home") {
                popUpTo("launch") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("launch") { inclusive = true }
            }
        }
    }

    if (isChecking.value) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}