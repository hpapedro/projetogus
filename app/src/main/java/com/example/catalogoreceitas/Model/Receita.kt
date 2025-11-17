package com.example.catalogoreceitas.Model

open class Receita(
    open val nome: String = "",    open val descricao: String = "",
    open val tempoPreparo: Int = 0,
    open val ingredientes: List<String> = emptyList(),

    open val nivelDificuldade: String = "",

    open val tipoClasse: String = ""

) {
    open fun obterTipoReceita(): String {
        return "Receita Simples"
    }
}
