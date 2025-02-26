package br.edu.ifpb.sleepwell.controller

import br.edu.ifpb.sleepwell.model.data.repository.UsuarioRepository
import br.edu.ifpb.sleepwell.model.entity.Usuario

class SignUpController(
    private val repository: UsuarioRepository = UsuarioRepository()
) {
    fun cadastrarUsuario(nome: String, email: String, senha: String, callback: (Boolean) -> Unit) {
        val usuario = Usuario(nome = nome, email = email, senha = senha)
        repository.criarUsuario(usuario, callback)
    }
}
