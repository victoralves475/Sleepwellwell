package br.edu.ifpb.sleepwell.controller

import br.edu.ifpb.sleepwell.model.data.repository.UsuarioRepository
import br.edu.ifpb.sleepwell.model.entity.Usuario

/**
 * SignUpController é responsável por gerenciar o processo de cadastro de novos usuários.
 *
 * Ele utiliza o UsuarioRepository para criar um novo usuário no banco de dados (por exemplo, Firestore)
 * com os dados fornecidos.
 */
class SignUpController(
    private val repository: UsuarioRepository = UsuarioRepository()
) {

    /**
     * Cadastra um novo usuário utilizando os dados fornecidos.
     *
     * @param nome O nome do usuário a ser cadastrado.
     * @param email O email do usuário.
     * @param senha A senha do usuário.
     * @param callback Função de callback que recebe um Boolean indicando se o cadastro foi bem-sucedido.
     */
    fun cadastrarUsuario(nome: String, email: String, senha: String, callback: (Boolean) -> Unit) {
        // Cria uma instância de Usuario com os dados informados
        val usuario = Usuario(nome = nome, email = email, senha = senha)
        // Chama o método do repository para criar o usuário no banco de dados,
        // repassando o callback para retornar o resultado da operação.
        repository.criarUsuario(usuario, callback)
    }
}
