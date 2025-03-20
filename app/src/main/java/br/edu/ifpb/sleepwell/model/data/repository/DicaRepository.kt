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

    suspend fun ListarDicas(): List<Dica> {
        return try {
            println("🔄 Enviando requisição para o JSON Server...")
            val response = RetrofitClient.instance.listarDicas()
            println("✅ Resposta recebida: $response")
            response
        } catch (e: Exception) {
            println("❌ Erro ao buscar dicas: ${e.message}")
            emptyList() // Retorna uma lista vazia em caso de erro
        }
    }
}
