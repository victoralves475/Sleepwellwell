package br.edu.ifpb.sleepwell.model.data.repository

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
}
