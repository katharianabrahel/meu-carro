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
import com.example.meucarro.models.UserRequest
import com.example.meucarro.models.UserResponse
import com.example.meucarro.services.http.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun createUser(
    context: Context,
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
        onError("Preencha todos os campos.")
        return
    }

    if (password != confirmPassword) {
        onError("As senhas não coincidem.")
        return
    }

    val userRequest = UserRequest(name, email, password, confirmPassword)
    val call: Call<UserResponse> = RetrofitClient.instance.createUser(userRequest)

    call.enqueue(object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onError("Erro ao criar conta: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            onError("Erro de conexão: ${t.localizedMessage}")
        }
    })
}

@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

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

                // Campos de entrada
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
                    text = "By signing up, you agree to our terms.",
                    fontSize = 14.sp,
                    color = Color(0xFF637588),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        createUser(
                            context,
                            name,
                            email,
                            password,
                            confirmPassword,
                            onSuccess = {
                                Toast.makeText(context, "Conta criada com sucesso!", Toast.LENGTH_LONG).show()
                                onSignUpSuccess()
                            },
                            onError = { message ->
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            }
                        )
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
