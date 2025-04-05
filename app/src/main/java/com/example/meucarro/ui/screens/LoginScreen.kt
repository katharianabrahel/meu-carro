package com.example.meucarro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(onSignUpClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // TopBar
            Text(
                text = "Meu Carro",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111418),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            // Título
            Text(
                text = "Bem vindo!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111418),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Campo de Email
            InputField(
                label = "Email",
                placeholder = "Digite seu email",
                value = email,
                onValueChange = { email = it }
            )

            // Campo de Senha
            InputField(
                label = "Password",
                placeholder = "Digite sua senha",
                value = password,
                onValueChange = { password = it },
                isPassword = true
            )
        }

        // Botão Login
        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = { /* ação de login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1980E6))
            ) {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Link para criar conta
            TextButton(
                onClick = onSignUpClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Não tem uma conta? Cadastre-se.",
                    color = Color(0xFF637588),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
            }
        }
    }
}


