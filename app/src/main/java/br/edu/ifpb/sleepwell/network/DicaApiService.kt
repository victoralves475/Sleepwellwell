package br.edu.ifpb.sleepwell.network
import br.edu.ifpb.sleepwell.model.entity.Dica
import retrofit2.Call
import retrofit2.http.GET

interface DicaApiService {
    @GET("dicas")
    suspend fun listarDicas(): List<Dica>
}

