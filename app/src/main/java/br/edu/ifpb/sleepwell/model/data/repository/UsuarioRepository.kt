package br.edu.ifpb.sleepwell.model.data.repository

import br.edu.ifpb.sleepwell.model.entity.DiarioDeSonho
import br.edu.ifpb.sleepwell.model.entity.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class UsuarioRepository {

    private val db = FirebaseFirestore.getInstance()

    /**
     * Realiza o login verificando se existe um usuário com o email informado e
     * se a senha coincide com a armazenada.
     *
     * @param email Email do usuário.
     * @param senha Senha do usuário.
     * @param callback Retorna (true, usuário) em caso de sucesso, ou (false, null) se falhar.
     */
    fun login(email: String, senha: String, callback: (Boolean, Usuario?) -> Unit) {
        db.collection("usuarios")
            .whereEqualTo("email", email.trim())
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val usuario = snapshot.documents[0].toObject<Usuario>()
                    if (usuario != null && usuario.senha.trim() == senha.trim()) {
                        callback(true, usuario)
                    } else {
                        callback(false, null)
                    }
                } else {
                    callback(false, null)
                }
            }
            .addOnFailureListener {
                callback(false, null)
            }
    }

    /**
     * Cria um novo usuário no Firestore.
     *
     * @param usuario Dados do usuário a ser criado.
     * @param callback Retorna true se o cadastro for realizado com sucesso; false caso contrário.
     */
    fun criarUsuario(usuario: Usuario, callback: (Boolean) -> Unit) {
        // Gera um ID único para o novo documento
        val id = db.collection("usuarios").document().id
        val novoUsuario = usuario.copy(id = id)
        db.collection("usuarios")
            .document(id)
            .set(novoUsuario)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }
    /**
     * Adiciona um novo Diário de Sonho na subcoleção do usuário.
     *
     * @param usuarioId ID do usuário autenticado.
     * @param diarioDeSonho Dados do diário de sonho a ser adicionado.
     * @param onSuccess Callback em caso de sucesso.
     * @param onFailure Callback em caso de falha.
     */
    fun adicionarDiarioDeSonho(
        usuarioId: String,
        diarioDeSonho: DiarioDeSonho,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Gera um ID único para o novo documento
        val diarioId = db.collection("usuarios")
            .document(usuarioId)
            .collection("diarios")
            .document().id

        // Cria um novo DiarioDeSonho com o ID atribuído
        val novoDiario = diarioDeSonho.copy(id = diarioId)

        // Salva o diário usando o ID gerado
        db.collection("usuarios")
            .document(usuarioId)
            .collection("diarios")
            .document(diarioId)
            .set(novoDiario)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Lista todos os Diários de Sonho do usuário autenticado.
     *
     * @param usuarioId ID do usuário autenticado.
     * @param callback Retorna a lista de diários em caso de sucesso; lista vazia em caso de falha.
     */
    fun listarDiariosDeSonho(
        usuarioId: String,
        callback: (List<DiarioDeSonho>) -> Unit
    ) {
        db.collection("usuarios")
            .document(usuarioId)
            .collection("diarios")
            .get()
            .addOnSuccessListener { snapshot ->
                val diarios = snapshot.documents.mapNotNull { it.toObject<DiarioDeSonho>() }
                callback(diarios)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    /**
     * Remove um Diário de Sonho específico.
     *
     * @param usuarioId ID do usuário autenticado.
     * @param diarioId ID do diário a ser removido.
     * @param onSuccess Callback em caso de sucesso.
     * @param onFailure Callback em caso de falha.
     */
    fun removerDiarioDeSonho(
        usuarioId: String,
        diarioId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("usuarios")
            .document(usuarioId)
            .collection("diarios")
            .document(diarioId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Atualiza um Diário de Sonho específico.
     *
     * @param usuarioId ID do usuário autenticado.
     * @param diarioId ID do diário a ser atualizado.
     * @param diarioDeSonho Dados atualizados do diário de sonho.
     * @param onSuccess Callback em caso de sucesso.
     * @param onFailure Callback em caso de falha.
     */
    fun atualizarDiarioDeSonho(
        usuarioId: String,
        diarioId: String,
        diarioDeSonho: DiarioDeSonho,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("usuarios")
            .document(usuarioId)
            .collection("diarios")
            .document(diarioId)
            .set(diarioDeSonho)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}

