package br.edu.ifpb.sleepwell.view.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.sleepwell.R
import br.edu.ifpb.sleepwell.controller.LoginController
import kotlinx.coroutines.launch

/**
 * LoginScreen exibe a tela de autenticação do usuário.
 *
 * Nessa tela:
 * - É exibida uma imagem de fundo (onda azul) com um overlay preto translúcido para contraste.
 * - São exibidos os campos de entrada para email e senha.
 * - Um botão "Entrar" tenta efetuar o login usando as credenciais fornecidas.
 * - Caso o login seja bem-sucedido, o callback onLoginSuccess é acionado para redirecionar o usuário.
 * - Caso contrário, um Toast informa que o login é inválido.
 * - Também há um TextButton que permite a navegação para a tela de cadastro.
 *
 * @param onLoginSuccess Callback que é chamado quando o login é bem-sucedido.
 * @param onNavigateToSignUp Callback para navegar para a tela de cadastro.
 * @param loginController Instância do LoginController para gerenciar a autenticação.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    loginController: LoginController = LoginController()
) {
    // Estados para armazenar o email e a senha digitados
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    // Obtém o contexto atual para exibir Toasts
    val context = LocalContext.current
    // Escopo para lançar coroutines
    val scope = rememberCoroutineScope()

    // Box principal que preenche toda a tela
    Box(modifier = Modifier.fillMaxSize()) {
        // Exibe a imagem de fundo (onda azul)
        Image(
            painter = painterResource(id = R.drawable.wave_background), // Nome do arquivo de imagem na pasta res/drawable
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Overlay preto translúcido para melhorar o contraste da interface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
        ) {
            // Coluna que organiza os elementos verticalmente, centralizando-os
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título principal de boas-vindas
                Text(
                    text = "Bem-vindo ao SleepWell",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Subtítulo orientando o usuário
                Text(
                    text = "Entre com seu email e senha",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Card que contém os campos de entrada para autenticação
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Campo de entrada para o email
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", color = MaterialTheme.colorScheme.onSurface) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                cursorColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        // Campo de entrada para a senha, com transformação visual para ocultar caracteres
                        TextField(
                            value = senha,
                            onValueChange = { senha = it },
                            label = { Text("Senha", color = MaterialTheme.colorScheme.onSurface) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                cursorColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Botão "Entrar" que tenta efetuar o login utilizando as credenciais fornecidas.
                        Button(
                            onClick = {
                                scope.launch {
                                    loginController.realizarLogin(email.trim(), senha.trim()) { success, _ ->
                                        if (success) {
                                            // Se o login for bem-sucedido, aciona o callback para redirecionar o usuário
                                            onLoginSuccess()
                                        } else {
                                            // Caso contrário, exibe um Toast informando que o login é inválido
                                            Toast.makeText(context, "Login inválido!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Entrar", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Botão de texto que permite a navegação para a tela de cadastro
                TextButton(onClick = onNavigateToSignUp) {
                    Text(
                        text = "Ainda não tem conta? Cadastre-se",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
