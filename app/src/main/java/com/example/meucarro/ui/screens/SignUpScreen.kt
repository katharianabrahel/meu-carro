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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
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
                text = "Criar Conta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111418),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Campos
            InputField(label = "Nome", placeholder = "Digite seu nome", value = name, onValueChange = { name = it })
            InputField(label = "Endereço de email", placeholder = "Digite seu endereço de email", value = email, onValueChange = { email = it })
            InputField(label = "Criar senha", placeholder = "Escolha sua senha", value = password, onValueChange = { password = it }, isPassword = true)

            // Termos
            Text(
                text = "By signing up, you agree to our terms.",
                fontSize = 14.sp,
                color = Color(0xFF637588),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
            )
        }

        // Botão
        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = { onSignUpSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1980E6))
            ) {
                Text(
                    text = "Criar conta",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Composable
fun InputField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val isValidPassword = if (isPassword) isPasswordValid(value) else true
    val passwordErrorMessage = if (isPassword && value.isNotEmpty() && !isValidPassword) {
        "A senha deve ter pelo menos 6 caracteres, 1 letra minúscula, 1 maiúscula e 1 número"
    } else {
        null
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF111418),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0xFF637588)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF0F2F4),
                unfocusedContainerColor = Color(0xFFF0F2F4),
                disabledContainerColor = Color(0xFFF0F2F4),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFF111418),
                focusedTextColor = Color(0xFF111418),
                unfocusedTextColor = Color(0xFF111418)
            )
        )
        if (passwordErrorMessage != null) {
            Text(
                text = passwordErrorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


fun isPasswordValid(password: String): Boolean {
    val hasLowercase = password.any { it.isLowerCase() }
    val hasUppercase = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasMinLength = password.length >= 6

    return hasLowercase && hasUppercase && hasDigit && hasMinLength
}
