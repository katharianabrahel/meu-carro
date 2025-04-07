package com.example.meucarro.ui.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meucarro.models.LoginResponse
import com.example.meucarro.models.LoginResquest
import com.example.meucarro.services.http.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(onSignUpClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    fun loginUser(context: Context, email: String, password: String) {
        val loginRequest = LoginResquest(email, password)
        val call: Call<LoginResponse> = RetrofitClient.instance.login(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.token
                    if (token != null) {
                        val sharedPreferences: SharedPreferences =
                            context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("auth_token", token)
                        editor.apply()
                    }
                    // Handle the response
                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle the failure
            }
        })
    }

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

            // Title
            Text(
                text = "Bem vindo!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111418),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Email Field
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
        }

        // Login Button
        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = { loginUser(context, email, password) },
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

            // Sign Up Link
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