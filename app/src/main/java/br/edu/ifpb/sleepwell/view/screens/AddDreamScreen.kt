package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.sleepwell.controller.DiarioDeSonhoController
//import java.text.SimpleDateFormat
//import java.util.Locale

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDreamScreen(
    onSaveSuccess: () -> Unit,  // Navega para outra tela após salvar, se necessário
) {
    // Instancia o Controller diretamente na tela
    val diarioController = DiarioDeSonhoController()

    // Campos para título, relato e data
    var titulo by remember { mutableStateOf("") }
    var relato by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }

    // Estado para mensagens de feedback
    var mensagemSucesso by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título da Tela
        Text(
            text = "Novo Diário de Sonho",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo para Título do Sonho
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título do Sonho") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para Relato do Sonho
        OutlinedTextField(
            value = relato,
            onValueChange = { relato = it },
            label = { Text("Relato do Sonho") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para Data do Sonho
        OutlinedTextField(
            value = data,
            onValueChange = { data = it },
            label = { Text("Data do Sonho (dd/MM/yyyy)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(32.dp))
// Botão de Salvar
        Button(
            onClick = {
                // Chama o Controller diretamente para adicionar o Diário de Sonho
                diarioController.adicionarDiarioDeSonho(
                    titulo = titulo,
                    relato = relato,
                    data = data,
                    onSuccess = {
                        mensagemSucesso = "Diário salvo com sucesso!"
                        mensagemErro = ""
                        // Limpa os campos
                        titulo = ""
                        relato = ""
                        data = ""

                        // Se precisar navegar para outra tela após salvar
                        onSaveSuccess()
                    },
                    onFailure = { e ->
                        mensagemErro = "Erro ao salvar: ${e.message}"
                        mensagemSucesso = ""
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(text = "Salvar", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

// Mensagem de Sucesso
        if (mensagemSucesso.isNotEmpty()) {
            Text(text = mensagemSucesso, color = Color.Green)
        }

// Mensagem de Erro
        if (mensagemErro.isNotEmpty()) {
            Text(text = mensagemErro, color = MaterialTheme.colorScheme.error)
        }
    }
}