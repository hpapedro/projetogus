package com.example.catalogoreceitas.Model

// Change from "data class" to "open class"
open class Receita(
    open val nome: String = "",    open val descricao: String = "",
    open val tempoPreparo: Int = 0,
    open val ingredientes: List<String> = emptyList()
) {
    // This function can remain as is
    open fun obterTipoReceita(): String {
        return "Receita Simples"
    }
}
