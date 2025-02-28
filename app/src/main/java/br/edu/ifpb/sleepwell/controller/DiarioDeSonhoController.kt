package br.edu.ifpb.sleepwell.controller

import br.edu.ifpb.sleepwell.model.SessionManager
import br.edu.ifpb.sleepwell.model.data.repository.UsuarioRepository
import br.edu.ifpb.sleepwell.model.entity.DiarioDeSonho
import br.edu.ifpb.sleepwell.model.entity.Usuario

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class DiarioDeSonhoController(private val repository: UsuarioRepository = UsuarioRepository()) {

    /**
     * Adiciona um novo Diário de Sonho para o usuário autenticado.
     *
     * @param titulo Título do Diário de Sonho.
     * @param relato Relato do Diário de Sonho.
     * @param data Data do Diário de Sonho no formato "dd/MM/yyyy".
     * @param onSuccess Callback em caso de sucesso.
     * @param onFailure Callback em caso de falha.
     */
    fun adicionarDiarioDeSonho(
        titulo: String,
        relato: String,
        data: String,  // Data no formato "dd/MM/yyyy"
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val usuarioId = SessionManager.currentUser?.id ?: return

        // Cria o objeto DiarioDeSonho com a data como String no formato "dd/MM/yyyy"
        val diarioDeSonho = DiarioDeSonho(
            titulo = titulo,
            relato = relato,
            data = data  // Salva a data exatamente como "dd/MM/yyyy"
        )

        // Chama o método do UsuarioRepository para salvar
        repository.adicionarDiarioDeSonho(
            usuarioId,
            diarioDeSonho,
            onSuccess,
            onFailure
        )
    }

    /**
     * Lista todos os Diários de Sonho do usuário autenticado.
     *
     * @param onSuccess Callback com a lista de diários em caso de sucesso.
     * @param onFailure Callback em caso de falha.
     */
    fun listarDiariosDeSonho(
        onSuccess: (List<DiarioDeSonho>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val usuarioId = SessionManager.currentUser?.id ?: return

        repository.listarDiariosDeSonho(usuarioId) { diarios ->
            onSuccess(diarios)
        }
    }
}