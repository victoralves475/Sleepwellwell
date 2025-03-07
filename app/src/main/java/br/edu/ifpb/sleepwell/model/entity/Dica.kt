package br.edu.ifpb.sleepwell.model.entity

import com.google.firebase.firestore.DocumentId

/**
 * Classe que representa uma Dica.
 *
 * Essa entidade armazena informações sobre uma dica ou sugestão que pode ser exibida no aplicativo.
 *
 * @property id Identificador único do documento no Firestore. A anotação @DocumentId faz com que o Firestore
 *             mapeie automaticamente o ID do documento para este atributo.
 * @property titulo Título da dica.
 * @property descricao Descrição ou conteúdo detalhado da dica.
 */
class Dica (
    @DocumentId val id: String? = null,
    val titulo: String = "",
    val descricao: String = ""
)
