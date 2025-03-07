package br.edu.ifpb.sleepwell.utils

/**
 * Função utilitária que formata uma string contendo apenas dígitos para o formato "dd/MM/yyyy".
 *
 * Se o input contiver exatamente 8 dígitos (correspondentes a "ddMMyyyy"), a função insere
 * as barras ("/") nos locais apropriados. Caso contrário, ela retorna o input original.
 *
 * Exemplo:
 *   Input: "12122025" → Output: "12/12/2025"
 *
 * @param input A string de entrada que deve conter a data sem formatação (apenas dígitos).
 * @return A data formatada no padrão "dd/MM/yyyy" ou o input original se a formatação não for possível.
 */
fun formatDate(input: String): String {
    // Filtra a string de entrada, mantendo apenas os caracteres que são dígitos
    val digits = input.filter { it.isDigit() }

    // Se a string filtrada tiver exatamente 8 dígitos, formata para "dd/MM/yyyy"
    return if (digits.length == 8) {
        "${digits.substring(0, 2)}/${digits.substring(2, 4)}/${digits.substring(4, 8)}"
    } else {
        // Caso contrário, retorna o input original sem formatação
        input
    }
}
