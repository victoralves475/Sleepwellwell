package br.edu.ifpb.sleepwell.model.data.repository

import br.edu.ifpb.sleepwell.model.entity.DiarioDeSonho
import br.edu.ifpb.sleepwell.model.entity.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

/**
 * UsuarioRepository é responsável por gerenciar as operações de autenticação e
 * manipulação dos dados do usuário no Firestore, bem como gerenciar os diários de sonho
 * do usuário (criar, listar, atualizar e remover).
 */
class UsuarioRepository {

    // Instância do Firestore para acessar o banco de dados
    private val db = FirebaseFirestore.getInstance()

    /**
     * Realiza o login verificando se existe um usuário com o email informado e
     * se a senha corresponde à armazenada.
     *
     * @param email Email do usuário.
     * @param senha Senha do usuário.
     * @param callback Função de callback que retorna:
     *   - true e o objeto Usuario se o login for bem-sucedido;
     *   - false e null em caso de falha.
     */
    fun login(email: String, senha: String, callback: (Boolean, Usuario?) -> Unit) {
        db.collection("usuarios")
            .whereEqualTo("email", email.trim())
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    // Converte o primeiro documento encontrado em um objeto Usuario
                    val usuario = snapshot.documents[0].toObject<Usuario>()
                    // Verifica se o usuário existe e se a senha confere (após remover espaços)
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
     * @param usuario Objeto Usuario contendo os dados a serem salvos.
     * @param callback Função de callback que retorna true se o cadastro for realizado com sucesso;
     *                 false caso contrário.
     */
    fun criarUsuario(usuario: Usuario, callback: (Boolean) -> Unit) {
        // Gera um ID único para o novo usuário
        val id = db.collection("usuarios").document().id
        // Cria uma cópia do objeto com o ID gerado
        val novoUsuario = usuario.copy(id = id)
        // Salva o novo usuário na coleção "usuarios"
        db.collection("usuarios")
            .document(id)
            .set(novoUsuario)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    /**
     * Adiciona um novo Diário de Sonho na subcoleção "diarios" do usuário autenticado.
     *
     * @param usuarioId ID do usuário autenticado.
     * @param diarioDeSonho Objeto DiarioDeSonho contendo os dados do diário.
     * @param onSuccess Callback chamado em caso de sucesso.
     * @param onFailure Callback chamado em caso de falha, recebendo a exceção.
     */
    fun adicionarDiarioDeSonho(
        usuarioId: String,
        diarioDeSonho: DiarioDeSonho,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Gera um ID único para o novo diário na subcoleção "diarios"
        val diarioId = db.collection("usuarios")
            .document(usuarioId)
            .collection("diarios")
            .document().id

        // Cria um novo objeto Diário de Sonho com o ID atribuído
        val novoDiario = diarioDeSonho.copy(id = diarioId)

        // Salva o novo diário na subcoleção "diarios" do usuário
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
     * @param callback Função de callback que recebe uma lista de DiárioDeSonho.
     *                 Se a operação falhar, retorna uma lista vazia.
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
                // Mapeia os documentos para objetos DiarioDeSonho, ignorando os que não puderem ser convertidos
                val diarios = snapshot.documents.mapNotNull { it.toObject<DiarioDeSonho>() }
                callback(diarios)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    /**
     * Remove um Diário de Sonho específico da subcoleção "diarios" do usuário autenticado.
     *
     * @param usuarioId ID do usuário autenticado.
     * @param diarioId ID do diário que deve ser removido.
     * @param onSuccess Callback chamado em caso de sucesso.
     * @param onFailure Callback chamado em caso de falha, recebendo a exceção.
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
     * Atualiza um Diário de Sonho específico na subcoleção "diarios" do usuário autenticado.
     *
     * @param usuarioId ID do usuário autenticado.
     * @param diarioId ID do diário que deve ser atualizado.
     * @param diarioDeSonho Objeto Diário de Sonho com os dados atualizados.
     * @param onSuccess Callback chamado em caso de sucesso.
     * @param onFailure Callback chamado em caso de falha, recebendo a exceção.
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
