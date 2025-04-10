package com.example.meucarro.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meucarro.services.http.auth.dto.request.UserRequest
import com.example.meucarro.services.http.RetrofitClient
import com.example.meucarro.services.http.auth.AuthService
import kotlinx.coroutines.launch


suspend fun createUser(
    context: Context,
    name: String,
    email: String,
    password: String,
    confirmPassword: String
): Result<Unit> {
    if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
        return Result.failure(Exception("Preencha todos os campos."))
    }

    if (password != confirmPassword) {
        return Result.failure(Exception("As senhas não coincidem."))
    }

    return try {
        val api = RetrofitClient.createService(AuthService::class.java, context)
        val userRequest = UserRequest(name, email, password, confirmPassword)
        api.createUser(userRequest)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}


@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

                Text(
                    text = "Criar Conta",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111418),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    placeholder = { Text("Digite seu nome") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Endereço de email") },
                    placeholder = { Text("Digite seu endereço de email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Criar senha") },
                    placeholder = { Text("Escolha sua senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar senha") },
                    placeholder = { Text("Confirme sua senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Text(
                    text = "Ao criar uma conta, você concorda com nossos termos.",
                    fontSize = 14.sp,
                    color = Color(0xFF637588),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Preencha todos os campos.",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@launch
                            }

                            if (password != confirmPassword) {
                                Toast.makeText(
                                    context,
                                    "As senhas não coincidem.",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@launch
                            }

                            val result = createUser(context, name, email, password, confirmPassword)
                            result.onSuccess {
                                Toast.makeText(
                                    context,
                                    "Conta criada com sucesso!",
                                    Toast.LENGTH_LONG
                                ).show()
                                onSignUpSuccess()
                            }.onFailure { e ->
                                Toast.makeText(
                                    context,
                                    "Erro ao criar conta: ${e.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
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
}
