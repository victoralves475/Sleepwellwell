package br.edu.ifpb.sleepwell.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * DataMaskVisualTransformation aplica uma máscara de formatação para datas no formato "dd/MM/yyyy".
 *
 * Ela filtra apenas os dígitos da entrada e, conforme o número de dígitos digitados,
 * insere automaticamente as barras "/" nos locais corretos.
 */
class DataMaskVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Filtra a entrada para obter apenas os dígitos e limita a 8 caracteres,
        // correspondendo a "ddMMyyyy".
        val trimmed = text.text.filter { it.isDigit() }.take(8)

        // Constrói a string de saída aplicando a máscara "dd/MM/yyyy".
        // Dependendo da quantidade de dígitos inseridos, as barras são adicionadas:
        // - Se houver pelo menos 1 dígito, exibe os dois primeiros (ou menos, se não houver dois dígitos).
        // - Se houver pelo menos 3 dígitos, insere uma barra e exibe os próximos dois.
        // - Se houver pelo menos 5 dígitos, insere outra barra e exibe o restante.
        val output = buildString {
            if (trimmed.length >= 1) {
                // Adiciona os dois primeiros dígitos ou menos, conforme disponível.
                append(trimmed.substring(0, minOf(2, trimmed.length)))
            }
            if (trimmed.length >= 3) {
                // Insere a primeira barra após os dois primeiros dígitos.
                append("/")
                // Adiciona os dígitos de índice 2 a 3 (ou até 4, se disponíveis).
                append(trimmed.substring(2, minOf(4, trimmed.length)))
            }
            if (trimmed.length >= 5) {
                // Insere a segunda barra após os quatro primeiros dígitos.
                append("/")
                // Adiciona o restante dos dígitos (a partir do índice 4).
                append(trimmed.substring(4))
            }
        }

        // Cria um mapeamento de offsets para garantir que o cursor se comporte corretamente
        // entre o texto original (sem máscara) e o texto transformado (com máscara).
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Garante que o offset não ultrapasse o número de dígitos inseridos.
                val effectiveOffset = offset.coerceAtMost(trimmed.length)
                return when {
                    effectiveOffset <= 2 -> effectiveOffset // Sem barras até o 2º dígito
                    effectiveOffset <= 4 -> effectiveOffset + 1 // Uma barra adicionada após 2 dígitos
                    else -> effectiveOffset + 2 // Duas barras adicionadas após 4 dígitos
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Inverte o mapeamento removendo os caracteres de máscara ("\/").
                return when {
                    offset <= 2 -> offset
                    offset <= 5 -> offset - 1
                    else -> offset - 2
                }
            }
        }

        // Retorna o texto transformado com a máscara e o mapeamento de offsets para o cursor.
        return TransformedText(AnnotatedString(output), offsetMapping)
    }
}
