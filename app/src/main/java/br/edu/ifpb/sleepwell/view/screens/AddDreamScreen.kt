package br.edu.ifpb.sleepwell.view.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.sleepwell.controller.DiarioDeSonhoController
import br.edu.ifpb.sleepwell.utils.DataMaskVisualTransformation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDreamScreen(
    onSaveSuccess: () -> Unit,  // Navega para outra tela após salvar, se necessário
    onCancelClick: () -> Unit
) {
    val diarioController = DiarioDeSonhoController()
    var titulo by remember { mutableStateOf("") }
    var relato by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var mensagemSucesso by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Fundo preto conforme o tema dark
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 60.dp, horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Título da Tela: dois textos para diferenciar "Registre seu" e "Sonho"
            Text(
                text = "Registre seu",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sonho",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Card para agrupar os campos do formulário
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                    OutlinedTextField(
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
                    OutlinedTextField(
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
                    OutlinedTextField(
                        value = data,
                        onValueChange = { data = it },
                        label = { Text("Data do Sonho (dd/MM/yyyy)", color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            cursorColor = MaterialTheme.colorScheme.onSurface
                        ),
                        visualTransformation = DataMaskVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    // Botão de Salvar
                    Button(
                        onClick = {
                            scope.launch {
                                diarioController.adicionarDiarioDeSonho(
                                    titulo = titulo,
                                    relato = relato,
                                    data = data,
                                    onSuccess = {
                                        mensagemSucesso = "Diário salvo com sucesso!"
                                        mensagemErro = ""
                                        titulo = ""
                                        relato = ""
                                        data = ""
                                        onSaveSuccess()
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
            // Botão de Cancelar
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
            // Mensagens de Feedback
            if (mensagemSucesso.isNotEmpty()) {
                Text(text = mensagemSucesso, color = Color.Green, fontSize = 16.sp)
            }
            if (mensagemErro.isNotEmpty()) {
                Text(text = mensagemErro, color = MaterialTheme.colorScheme.error, fontSize = 16.sp)
            }
        }
    }
}
