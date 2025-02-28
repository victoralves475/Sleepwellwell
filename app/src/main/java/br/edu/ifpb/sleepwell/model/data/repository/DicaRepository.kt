package br.edu.ifpb.sleepwell.model.data.repository

import br.edu.ifpb.sleepwell.model.entity.Dica
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

class DicaRepository {

    private val db = FirebaseFirestore.getInstance()

    fun ListarDicas(callback: (List<Dica>) -> Unit) {
        db.collection("dicasGerais").get()
            .addOnSuccessListener { document ->
                val dicas = document.toObjects<Dica>()
                callback(dicas)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
}