package br.edu.ifpb.sleepwell.model.data.repository

import br.edu.ifpb.sleepwell.model.entity.Dica
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

/**
 * DicaRepository é responsável por acessar e recuperar as dicas gerais do Firestore.
 *
 * Ele se comunica com a coleção "dicasGerais" e converte os documentos obtidos
 * em objetos do tipo Dica.
 */
class DicaRepository {

    // Instância do Firestore para acessar o banco de dados.
    private val db = FirebaseFirestore.getInstance()

    /**
     * Lista todas as dicas gerais armazenadas na coleção "dicasGerais".
     *
     * @param callback Função que recebe uma lista de objetos Dica.
     *                 Se a consulta for bem-sucedida, a lista conterá as dicas recuperadas;
     *                 caso contrário, será uma lista vazia.
     */
    fun ListarDicas(callback: (List<Dica>) -> Unit) {
        db.collection("dicasGerais").get()
            .addOnSuccessListener { document ->
                // Converte os documentos obtidos em uma lista de objetos Dica.
                val dicas = document.toObjects<Dica>()
                // Retorna a lista de dicas através do callback.
                callback(dicas)
            }
            .addOnFailureListener {
                // Em caso de falha na consulta, retorna uma lista vazia.
                callback(emptyList())
            }
    }
}
