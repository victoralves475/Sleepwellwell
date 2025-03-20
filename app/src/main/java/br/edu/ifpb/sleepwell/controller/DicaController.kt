package br.edu.ifpb.sleepwell.controller

import br.edu.ifpb.sleepwell.model.data.repository.DicaRepository
import br.edu.ifpb.sleepwell.model.entity.Dica

/**
 * DicaController é responsável por gerenciar as dicas (informações ou sugestões)
 * que serão exibidas no aplicativo.
 *
 * Ele utiliza o DicaRepository para recuperar as dicas armazenadas no banco de dados.
 */
class DicaController(private val repository: DicaRepository = DicaRepository()) {

    /**
     * Lista todas as dicas disponíveis.
     *
     * @param callback Função de callback que recebe uma lista de objetos Dica
     *                 quando a operação é concluída com sucesso.
     */
    suspend fun listarDicas():List<Dica>{
        // Chama o método ListarDicas do DicaRepository e repassa a lista de dicas para o callback
       return repository.ListarDicas ()
    }
}
