package br.edu.ifpb.sleepwell.data.model

data class SleepRecord(
    val recordId: String = "",
    val userId: String = "",
    val date: Long = 0L,         // Armazenado como timestamp em milissegundos
    val quality: Boolean? = null // true = sono bom; false = sono ruim; null = não registrado
)
