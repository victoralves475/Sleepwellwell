package br.edu.ifpb.sleepwell.model

import br.edu.ifpb.sleepwell.model.entity.Usuario

/**
 * SessionManager é um objeto singleton que gerencia a sessão do usuário no aplicativo.
 *
 * Esse objeto armazena o usuário atualmente autenticado (currentUser) para que as informações
 * possam ser acessadas em diferentes partes do aplicativo sem a necessidade de passá-las manualmente.
 */
object SessionManager {
    // Armazena o usuário atualmente logado. Se null, significa que nenhum usuário está autenticado.
    var currentUser: Usuario? = null
}
