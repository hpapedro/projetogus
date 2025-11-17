
package com.example.catalogoreceitas.Model

data class ReceitaComplexa(
    override val nome: String = "",
    override val descricao: String = "",
    override val tempoPreparo: Int = 0,
    override val ingredientes: List<String> = emptyList(),
    override val nivelDificuldade: String = "Complexa"
) : Receita(nome, descricao, tempoPreparo, ingredientes) {

    override fun obterTipoReceita(): String {
        return "Receita Complexa ($nivelDificuldade)"
    }
}
