package br.edu.ifpb.sleepwell.model.entity

import com.google.firebase.firestore.DocumentId

/**
 * Data class que representa um Diário de Sonho.
 *
 * Essa classe armazena as informações de um sonho registrado pelo usuário, incluindo
 * um identificador único (gerado pelo Firestore), o título, o relato e a data (no formato "dd/MM/yyyy").
 */
data class DiarioDeSonho (
    @DocumentId
    val id: String = "",     // Identificador único do documento no Firestore, mapeado automaticamente.
    val titulo: String = "", // Título do sonho.
    val relato: String = "", // Relato ou descrição do sonho.
    val data: String = ""    // Data do sonho, no formato "dd/MM/yyyy".
)
