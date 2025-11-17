package com.example.catalogoreceitas.Repository

import com.example.catalogoreceitas.DataSource.DataSource
import com.example.catalogoreceitas.Model.Receita
import kotlinx.coroutines.flow.Flow

class ReceitasRepository() {
    private val dataSource = DataSource()

    suspend fun salvarReceita(
        nome: String,
        descricao: String,
        tempoPreparo: Int,
        ingredientes: List<String>,
        nivelDificuldade: String,
        tipoClasse: String // << 1. ADICIONE O PARÂMETRO AQUI
    ) {
        // 2. PASSE O PARÂMETRO ADIANTE PARA O DATASOURCE
        dataSource.salvarReceita(nome, descricao, tempoPreparo, ingredientes, nivelDificuldade, tipoClasse)
    }

    fun listarReceitas(): Flow<List<Receita>> {
        return dataSource.listarReceitas()
    }

    /**
     * ---- FUNÇÃO ADICIONADA AQUI ----
     * Esta é a função que estava faltando.
     * Ela chama a função correspondente no DataSource para buscar uma receita pelo nome.
     */
    fun buscarReceitaPorNome(nome: String): Flow<Receita?> {
        return dataSource.buscarReceitaPorNome(nome)
    }
}
