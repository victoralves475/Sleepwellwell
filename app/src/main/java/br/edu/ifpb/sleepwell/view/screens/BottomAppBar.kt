package br.edu.ifpb.sleepwell.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.BottomAppBar as M3BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * BottomAppBar compõe uma barra inferior com ícones para navegação.
 *
 * Essa função exibe três ícones distribuídos horizontalmente:
 * - Um ícone para acessar o perfil (chama onProfileClick).
 * - Um ícone para ir para a tela Home, exibido dentro de um Surface estilizado,
 *   para dar ênfase visual (chama onHomeClick).
 * - Um ícone para realizar logout (chama onLogout).
 *
 * Os ícones são dispostos em uma Row com Arrangement.SpaceBetween para que
 * fiquem uniformemente espaçados ao longo da largura total da tela.
 */
@Composable
fun BottomAppBar(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit
) {
    // Utiliza o BottomAppBar do Material3 com cor de fundo e conteúdo definidos pelo tema.
    M3BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        // Row que distribui os ícones com espaço entre eles e centraliza verticalmente.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botão que representa o perfil do usuário.
            // Quando clicado, chama onProfileClick.
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            }
            // Botão central que representa a navegação para a Home.
            // O ícone é colocado dentro de um Surface para dar uma aparência semelhante a um FAB ou botão destacado.
            IconButton(
                onClick = onHomeClick,
                modifier = Modifier.size(56.dp) // Define tamanho fixo para criar um botão mais destacado.
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = MaterialTheme.shapes.medium, // Pode ser substituído por RoundedCornerShape para alterar o arredondamento.
                    color = MaterialTheme.colorScheme.primary // Fundo do botão usando a cor primária do tema.
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = MaterialTheme.colorScheme.onPrimary, // Cor do ícone, contrastando com o fundo primário.
                        modifier = Modifier.padding(12.dp) // Padding interno para centralizar o ícone.
                    )
                }
            }
            // Botão para realizar logout.
            // Quando clicado, chama onLogout.
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Sair"
                )
            }
        }
    }
}
