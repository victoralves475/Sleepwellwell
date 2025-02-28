package br.edu.ifpb.sleepwell.controller

import br.edu.ifpb.sleepwell.model.data.repository.DicaRepository
import br.edu.ifpb.sleepwell.model.entity.Dica

class DicaController (private val repository: DicaRepository = DicaRepository()){
    fun listarDicas(callback: (List<Dica>) -> Unit) {
        repository.ListarDicas { dicas ->
            callback(dicas)
        }

    }
}