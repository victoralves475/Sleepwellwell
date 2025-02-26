package br.edu.ifpb.sleepwell.controller

import br.edu.ifpb.sleepwell.model.data.repository.UsuarioRepository
import br.edu.ifpb.sleepwell.model.entity.Usuario

class LoginController(private val repository: UsuarioRepository = UsuarioRepository()) {

    // Realiza o login chamando o repositório.
    // O callback retorna (true, usuario) se der certo ou (false, null) se falhar.
    fun realizarLogin(email: String, senha: String, callback: (Boolean, Usuario?) -> Unit) {
        repository.login(email, senha, callback)
    }
}
