package br.edu.ifpb.sleepwell.view.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.sleepwell.controller.LoginController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    loginController: LoginController = LoginController()
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Fundo preto para todo o layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Coluna centralizada com espaçamento adequado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título em branco
            Text(
                text = "Bem-vindo ao SleepWell",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Subtítulo com tom de cinza claro
            Text(
                text = "Entre com seu email e senha",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Card para o formulário com fundo cinza escuro
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        textStyle = TextStyle(color = Color.White),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.DarkGray,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        label = { Text("Senha", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        textStyle = TextStyle(color = Color.White),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.DarkGray,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                loginController.realizarLogin(email.trim(), senha.trim()) { success, _ ->
                                    if (success) {
                                        onLoginSuccess()
                                    } else {
                                        Toast.makeText(context, "Login inválido!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Entrar", color = Color.Black)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Link de cadastro em branco
            TextButton(onClick = onNavigateToSignUp) {
                Text(
                    text = "Ainda não tem conta? Cadastre-se",
                    color = Color.White
                )
            }
        }
    }
}
