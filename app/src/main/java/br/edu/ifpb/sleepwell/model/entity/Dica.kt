package br.edu.ifpb.sleepwell.model.entity

import com.google.firebase.firestore.DocumentId

class Dica (
    @DocumentId val id: String? = null,
    val titulo: String = "",
    val descricao: String = ""
)