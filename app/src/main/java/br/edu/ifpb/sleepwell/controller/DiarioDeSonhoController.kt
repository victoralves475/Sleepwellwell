package br.edu.ifpb.sleepwell.controller

import br.edu.ifpb.sleepwell.model.SessionManager
import br.edu.ifpb.sleepwell.model.data.repository.UsuarioRepository
import br.edu.ifpb.sleepwell.model.entity.DiarioDeSonho
import br.edu.ifpb.sleepwell.model.entity.Usuario
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * DiárioDeSonhoController gerencia as operações relacionadas aos diários de sonho,
 * como adicionar, listar e excluir diários para o usuário autenticado.
 *
 * Esse controller utiliza o UsuarioRepository para interagir com o Firestore.
 */
class DiarioDeSonhoController(private val repository: UsuarioRepository = UsuarioRepository()) {

    /**
     * Adiciona um novo Diário de Sonho para o usuário autenticado.
     *
     * @param titulo Título do Diário de Sonho.
     * @param relato Relato do Diário de Sonho.
     * @param data Data do Diário de Sonho no formato "dd/MM/yyyy".
     * @param onSuccess Callback chamado em caso de sucesso na operação.
     * @param onFailure Callback chamado em caso de falha, passando a exceção.
     */
    fun adicionarDiarioDeSonho(
        titulo: String,
        relato: String,
        data: String,  // Data no formato "dd/MM/yyyy"
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Obtém o ID do usuário atualmente autenticado.
        val usuarioId = SessionManager.currentUser?.id ?: return

        // Cria um objeto DiarioDeSonho com os dados fornecidos.
        // Aqui, a data é salva exatamente no formato "dd/MM/yyyy".
        val diarioDeSonho = DiarioDeSonho(
            titulo = titulo,
            relato = relato,
            data = data
        )

        // Chama o método do repository para adicionar o diário de sonho ao Firestore.
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
     * @param onSuccess Callback que recebe uma lista de DiárioDeSonho em caso de sucesso.
     * @param onFailure Callback chamado em caso de falha, passando a exceção.
     */
    fun listarDiariosDeSonho(
        onSuccess: (List<DiarioDeSonho>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Obtém o ID do usuário atualmente autenticado.
        val usuarioId = SessionManager.currentUser?.id ?: return

        // Chama o método do repository para buscar os diários de sonho do usuário.
        repository.listarDiariosDeSonho(
            usuarioId,
            callback = { diarios -> onSuccess(diarios) }
        )
    }

    /**
     * Exclui um Diário de Sonho do usuário autenticado.
     *
     * @param diarioId ID do Diário de Sonho a ser excluído.
     * @param onSuccess Callback chamado em caso de sucesso.
     * @param onFailure Callback chamado em caso de falha, passando a exceção.
     */
    fun excluirDiarioDeSonho(
        diarioId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Obtém o ID do usuário atualmente autenticado.
        val usuarioId = SessionManager.currentUser?.id ?: return

        // Chama o método do repository para remover o diário de sonho do usuário.
        repository.removerDiarioDeSonho(
            usuarioId,
            diarioId,
            onSuccess,
            onFailure
        )
    }
}
