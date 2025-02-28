package br.edu.ifpb.sleepwell.model.entity

import com.google.firebase.firestore.DocumentId

data class
Usuario(
    @DocumentId val id: String? = null,
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val diarioDeSonhoList: List<DiarioDeSonho> = emptyList()
)
