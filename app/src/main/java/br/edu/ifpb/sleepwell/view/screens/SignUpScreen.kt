package br.edu.ifpb.sleepwell.view.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import br.edu.ifpb.sleepwell.controller.SignUpController
import kotlinx.coroutines.launch

/**
 * SignUpScreen exibe a tela de cadastro do usuário.
 *
 * Essa tela apresenta:
 * - Uma imagem de fundo (onda azul) com um overlay translúcido para contraste.
 * - Um formulário contido em um Card para cadastro, com os campos de Nome, Email e Senha.
 * - Validação que impede o cadastro se algum campo estiver vazio ou se o email não for válido.
 * - Um botão "Cadastrar" que, ao ser clicado, tenta cadastrar o usuário e, em caso de sucesso,
 *   aciona o callback onSignUpSuccess() para redirecionar para a tela desejada.
 * - Um TextButton para navegar para a tela de login caso o usuário já tenha uma conta.
 *
 * @param onSignUpSuccess Callback acionado quando o cadastro for realizado com sucesso.
 * @param onNavigateToLogin Callback acionado para navegar para a tela de login.
 * @param signUpController Instância do controlador de cadastro (SignUpController) que gerencia
 *                         a comunicação com o Firestore.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    signUpController: SignUpController = SignUpController()
) {
    // Estados para armazenar os valores digitados pelo usuário
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    // Contexto para exibir Toasts
    val context = LocalContext.current
    // Escopo de coroutine para operações assíncronas (ex.: cadastro)
    val scope = rememberCoroutineScope()

    // Box principal que preenche a tela e define o fundo (com overlay)
    Box(modifier = Modifier.fillMaxSize()) {
        // Exibe a imagem de fundo (onda azul)
        Image(
            painter = painterResource(id = R.drawable.wave_background), // Nome do arquivo de imagem na pasta res/drawable
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Overlay translúcido para melhorar a legibilidade dos elementos
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
        ) {
            // Organiza o conteúdo do formulário em uma Column centralizada
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título da tela
                Text(
                    text = "Cadastro",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Subtítulo com instrução
                Text(
                    text = "Preencha os campos para criar sua conta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Card que agrupa os campos do formulário
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
                        // Campo de entrada para o nome
                        TextField(
                            value = nome,
                            onValueChange = { nome = it },
                            label = { Text("Nome", color = MaterialTheme.colorScheme.onSurface) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                cursorColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
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
                        // Campo de entrada para a senha (oculta o texto digitado)
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
                        // Botão de cadastro que realiza validações antes de enviar os dados
                        Button(
                            onClick = {
                                scope.launch {
                                    // Verifica se algum campo está vazio
                                    if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
                                        Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                                        return@launch
                                    }
                                    // Valida se o email tem o formato correto utilizando Patterns.EMAIL_ADDRESS
                                    if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                                        Toast.makeText(context, "Email inválido!", Toast.LENGTH_SHORT).show()
                                        return@launch
                                    }
                                    // Chama o controller para cadastrar o usuário
                                    signUpController.cadastrarUsuario(
                                        nome = nome.trim(),
                                        email = email.trim(),
                                        senha = senha.trim()
                                    ) { sucesso ->
                                        if (sucesso) {
                                            Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                            // Chama o callback onSignUpSuccess para redirecionar a outra tela
                                            onSignUpSuccess()
                                        } else {
                                            Toast.makeText(context, "Falha no cadastro!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cadastrar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Botão para navegar para a tela de login
                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "Já tem conta? Cadastre-se",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
