package br.edu.ifpb.sleepwell.model.data.repository

import br.edu.ifpb.sleepwell.model.entity.Dica
import br.edu.ifpb.sleepwell.network.RetrofitClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * DicaRepository é responsável por acessar e recuperar as dicas gerais do Firestore.
 *
 * Ele se comunica com a coleção "dicasGerais" e converte os documentos obtidos
 * em objetos do tipo Dica.
 */
class DicaRepository {


    /**
     * Lista todas as dicas gerais armazenadas na coleção "dicasGerais".
     *
     * @param callback Função que recebe uma lista de objetos Dica.
     *                 Se a consulta for bem-sucedida, a lista conterá as dicas recuperadas;
     *                 caso contrário, será uma lista vazia.
     */
    suspend fun ListarDicas(callback: (List<Dica>) -> Unit) {
        suspend fun listarDicas(): List<Dica> {
            return try {
                RetrofitClient.instance.listarDicas()
            } catch (e: Exception) {
                emptyList() // Retorna uma lista vazia em caso de erro
            }
        }
    }
}
