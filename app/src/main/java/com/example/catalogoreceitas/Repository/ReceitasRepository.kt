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
        tipoClasse: String //
    ) {
        dataSource.salvarReceita(nome, descricao, tempoPreparo, ingredientes, nivelDificuldade, tipoClasse)
    }

    fun listarReceitas(): Flow<List<Receita>> {
        return dataSource.listarReceitas()
    }
    fun buscarReceitaPorNome(nome: String): Flow<Receita?> {
        return dataSource.buscarReceitaPorNome(nome)
    }
    fun excluirReceita(nomeReceita: String) {
        dataSource.excluirReceita(nomeReceita)
    }



}
