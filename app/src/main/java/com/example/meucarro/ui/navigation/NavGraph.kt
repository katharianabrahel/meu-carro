package com.example.meucarro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.meucarro.ui.screens.HomePage
import com.example.meucarro.ui.screens.LoginScreen
import com.example.meucarro.ui.screens.SignUpScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
            composable("home") {
            HomePage(navController)
            }

            composable("login") {
            LoginScreen(onSignUpClick = {
                navController.navigate("signup")
            },
                onLoginSuccess = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("signup") {
            SignUpScreen(onSignUpSuccess = {
                navController.navigate("login") {
                    popUpTo("signup") { inclusive = true }
                }
            })
        }
    }
}