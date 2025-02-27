package br.edu.ifpb.sleepwell.controller

import br.edu.ifpb.sleepwell.model.SessionManager
import br.edu.ifpb.sleepwell.model.data.repository.UsuarioRepository
import br.edu.ifpb.sleepwell.model.entity.Usuario

class LoginController(private val repository: UsuarioRepository = UsuarioRepository()) {
    fun realizarLogin(email: String, senha: String, callback: (Boolean, Usuario?) -> Unit) {
        repository.login(email, senha) { success, usuario ->
            if (success && usuario != null) {
                // Salva o usuário logado
                SessionManager.currentUser = usuario
            }
            callback(success, usuario)
        }
    }
}
