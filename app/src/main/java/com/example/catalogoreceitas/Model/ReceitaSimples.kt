package com.example.catalogoreceitas.Model

data class ReceitaSimples(
    override val nome: String = "",
    override val descricao: String = "",
    override val tempoPreparo: Int = 0,
    override val ingredientes: List<String> = emptyList(),
    val nivelDificuldade: String = "Fácil"
) : Receita(nome, descricao, tempoPreparo, ingredientes) {

    override fun obterTipoReceita(): String {
        return "Receita Simples ($nivelDificuldade)"
    }
}