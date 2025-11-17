package com.example.catalogoreceitas.DataSource


import com.example.catalogoreceitas.Model.Receita
import com.example.catalogoreceitas.Model.ReceitaSimples
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DataSource {

    private val db = FirebaseFirestore.getInstance()
    private val COLECAO_RECEITAS = "receitas"

    suspend fun salvarReceita(
        nome: String,
        descricao: String,
        tempoPreparo: Int,
        ingredientes: List<String>,
        nivelDificuldade: String,
        tipoClasse: String
    ) {
        val receitaMap = hashMapOf(
            "nome" to nome,
            "descricao" to descricao,
            "tempoPreparo" to tempoPreparo,
            "ingredientes" to ingredientes,
            "nivelDificuldade" to nivelDificuldade,
            "tipoClasse" to tipoClasse
        )
        db.collection(COLECAO_RECEITAS).document(nome).set(receitaMap).await()
    }
    fun listarReceitas(): Flow<List<Receita>> = callbackFlow {
        val listener = db.collection(COLECAO_RECEITAS)
            .orderBy("tempoPreparo", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val listaReceitas = querySnapshot.documents.mapNotNull {
                        it.toObject(ReceitaSimples::class.java)
                    }
                    // Emite a nova lista para os coletores do Flow
                    trySend(listaReceitas)
                }
            }
        awaitClose { listener.remove() }
    }

    fun buscarReceitaPorNome(nome: String): Flow<Receita?> = callbackFlow {
        val docRef = db.collection("receitas").document(nome)

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val receita = snapshot?.toObject(Receita::class.java)
            trySend(receita).isSuccess
        }

        awaitClose { listener.remove() }
    }
    fun excluirReceita(nomeReceita: String) {
        db.collection("receitas")
            .whereEqualTo("nome", nomeReceita)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    db.collection("receitas").document(document.id).delete()
                }
            }
            .addOnFailureListener {
            }
    }

}
