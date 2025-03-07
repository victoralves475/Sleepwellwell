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
import br.edu.ifpb.sleepwell.controller.DiarioDeSonhoController
import br.edu.ifpb.sleepwell.utils.DataMaskVisualTransformation
import kotlinx.coroutines.launch

/**
 * AddDreamScreen é a tela onde o usuário pode registrar um novo sonho.
 *
 * Nessa tela, o usuário preenche os campos:
 * - Título do sonho
 * - Relato do sonho
 * - Data do sonho (no formato dd/MM/yyyy, com máscara aplicada)
 *
 * Se os campos estiverem preenchidos corretamente e o cadastro for bem-sucedido,
 * o callback onSaveSuccess() é acionado para redirecionar à tela DreamDiaryScreen.
 * Caso contrário, mensagens de erro são exibidas.
 *
 * onCancelClick: Callback para cancelar a ação e retornar à tela anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDreamScreen(
    onSaveSuccess: () -> Unit,  // Callback que deve redirecionar para a DreamDiaryScreen
    onCancelClick: () -> Unit
) {
    // Instancia o controller responsável por gerenciar os diários de sonho.
    val diarioController = DiarioDeSonhoController()
    // Estados para armazenar os valores dos campos de entrada.
    var titulo by remember { mutableStateOf("") }
    var relato by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    // Estados para mensagens de feedback.
    var mensagemSucesso by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf("") }
    // Contexto para exibir Toasts.
    val context = LocalContext.current
    // Escopo para chamadas de coroutine.
    val scope = rememberCoroutineScope()
    // Estado para controlar a navegação após salvar com sucesso.
    var navigateToDiary by remember { mutableStateOf(false) }

    // Se navigateToDiary for true, aciona o callback de navegação.
    if (navigateToDiary) {
        LaunchedEffect(Unit) {
            onSaveSuccess()
        }
    }

    // Layout principal: Um Box que preenche a tela com fundo definido pelo tema.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 60.dp, horizontal = 16.dp)
    ) {
        // Coluna que organiza os elementos verticalmente.
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Título da tela dividido em duas linhas para destaque.
            Text(
                text = "Registre seu",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sonho",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Card que agrupa os campos do formulário.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                // Coluna interna do Card com padding e alinhamento central.
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Campo para o Título do Sonho.
                    TextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Título do Sonho", color = MaterialTheme.colorScheme.onSurface) },
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
                    Spacer(modifier = Modifier.height(16.dp))
                    // Campo para o Relato do Sonho.
                    TextField(
                        value = relato,
                        onValueChange = { relato = it },
                        label = { Text("Relato do Sonho", color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            cursorColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Campo para a Data do Sonho com máscara.
                    TextField(
                        value = data,
                        onValueChange = { newValue ->
                            // Filtra somente os dígitos e permite no máximo 8 dígitos.
                            val digits = newValue.filter { it.isDigit() }
                            if (digits.length <= 8) {
                                data = newValue
                            }
                        },
                        label = { Text("Data do Sonho (01/01/2025)", color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            cursorColor = MaterialTheme.colorScheme.onSurface
                        ),
                        // Aplica a máscara para o formato de data
                        visualTransformation = DataMaskVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    // Botão de Salvar: Valida os campos e, se tudo estiver correto, aciona o cadastro.
                    Button(
                        onClick = {
                            scope.launch {
                                // Validação: verifica se todos os campos foram preenchidos.
                                if (titulo.trim().isEmpty() || relato.trim().isEmpty() || data.trim().isEmpty()) {
                                    Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                                    return@launch
                                }
                                // Chama o controller para adicionar o Diário de Sonho.
                                diarioController.adicionarDiarioDeSonho(
                                    titulo = titulo.trim(),
                                    relato = relato.trim(),
                                    data = data.trim(),
                                    onSuccess = {
                                        mensagemSucesso = "Diário salvo com sucesso!"
                                        mensagemErro = ""
                                        // Limpa os campos após o salvamento
                                        titulo = ""
                                        relato = ""
                                        data = ""
                                        // Ativa a navegação para a tela DreamDiaryScreen
                                        navigateToDiary = true
                                    },
                                    onFailure = { e ->
                                        mensagemErro = "Erro ao salvar: ${e.message}"
                                        mensagemSucesso = ""
                                    }
                                )
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
                        Text("Salvar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Botão de Cancelar: Chama a função onCancelClick para retornar à tela anterior.
            Button(
                onClick = onCancelClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancelar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Exibe mensagens de feedback de sucesso ou erro, se houver.
            if (mensagemSucesso.isNotEmpty()) {
                Text(text = mensagemSucesso, color = Color.Green, fontSize = 16.sp)
            }
            if (mensagemErro.isNotEmpty()) {
                Text(text = mensagemErro, color = MaterialTheme.colorScheme.error, fontSize = 16.sp)
            }
        }
    }
}
