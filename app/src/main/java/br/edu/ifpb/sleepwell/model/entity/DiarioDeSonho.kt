package br.edu.ifpb.sleepwell.model.entity

import com.google.firebase.firestore.DocumentId

data class DiarioDeSonho (
    @DocumentId
    val id: String? = null,
    val titulo: String = "",
    val relato: String = "",
    val data: String = ""
    val diarioDeSonho: List<DiarioDeSonho>
)