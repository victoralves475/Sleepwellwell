package br.edu.ifpb.sleepwell.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DataMaskVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Pega somente os dígitos
        val digits = text.text.filter { it.isDigit() }

        // Aplica a máscara: dd/MM/yyyy
        val masked = buildString {
            for (i in digits.indices) {
                if (i == 2 || i == 4) append('/')
                append(digits[i])
            }
        }.take(10) // Limita a 10 caracteres: "dd/MM/yyyy"

        // Mapeia os offsets (cursor) entre o texto original e o transformado
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformed = offset
                if (offset > 1) transformed += 1
                if (offset > 3) transformed += 1
                return transformed
            }
            override fun transformedToOriginal(offset: Int): Int {
                var original = offset
                if (offset > 2) original -= 1
                if (offset > 5) original -= 1
                return original
            }
        }

        return TransformedText(AnnotatedString(masked), offsetMapping)
    }
}
