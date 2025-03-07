package br.edu.ifpb.sleepwell.model.entity

import com.google.firebase.firestore.DocumentId

/**
 * Representa um usuário do aplicativo SleepWell.
 *
 * Essa data class armazena informações básicas do usuário, incluindo:
 * - id: Identificador único mapeado automaticamente pelo Firestore.
 * - nome: Nome completo do usuário.
 * - email: Endereço de email do usuário.
 * - senha: Senha do usuário (idealmente, essa informação deve ser armazenada de forma segura).
 * - diarioDeSonhoList: Lista de diários de sonho associados ao usuário.
 */
data class Usuario(
    @DocumentId val id: String? = null,
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val diarioDeSonhoList: List<DiarioDeSonho> = emptyList()
)
