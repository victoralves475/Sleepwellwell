package br.edu.ifpb.sleepwell.controller

import br.edu.ifpb.sleepwell.model.SessionManager
import br.edu.ifpb.sleepwell.model.data.repository.UsuarioRepository
import br.edu.ifpb.sleepwell.model.entity.Usuario

/**
 * LoginController gerencia o processo de autenticação de usuários.
 *
 * Ele utiliza o UsuarioRepository para validar as credenciais e,
 * em caso de sucesso, salva o usuário autenticado no SessionManager.
 */
class LoginController(private val repository: UsuarioRepository = UsuarioRepository()) {

    /**
     * Realiza o login do usuário usando o email e a senha fornecidos.
     *
     * @param email O email do usuário.
     * @param senha A senha do usuário.
     * @param callback Função de callback que retorna:
     *                 - success: true se a autenticação for bem-sucedida, false caso contrário.
     *                 - usuario: o objeto Usuario autenticado, ou null se ocorrer falha.
     */
    fun realizarLogin(email: String, senha: String, callback: (Boolean, Usuario?) -> Unit) {
        // Chama o método de login do repository para verificar as credenciais
        repository.login(email, senha) { success, usuario ->
            if (success && usuario != null) {
                // Se o login for bem-sucedido, armazena o usuário autenticado no SessionManager
                SessionManager.currentUser = usuario
            }
            // Retorna o resultado da autenticação por meio do callback
            callback(success, usuario)
        }
    }
}
