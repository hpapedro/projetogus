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

    // --- FUNÇÕES DE ESCRITA (CORRIGIDA) ---

    /**
     * Salva uma receita. A função agora é 'suspend' para aguardar a conclusão.
     * Lança uma exceção em caso de falha.
     */
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

        // Usa await() para esperar a operação ser concluída de forma assíncrona
        db.collection(COLECAO_RECEITAS).document(nome).set(receitaMap).await()
    }

    // --- FUNÇÕES DE LEITURA (CORRIGIDAS) ---

    /**
     * Lista todas as receitas e escuta atualizações em tempo real.
     * Usa callbackFlow para converter o listener do Firebase em um Flow.
     */
    fun listarReceitas(): Flow<List<Receita>> = callbackFlow {
        val listener = db.collection(COLECAO_RECEITAS)
            .orderBy("tempoPreparo", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    // Em caso de erro, fecha o Flow com a exceção
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
        // Garante que o listener seja removido quando o Flow for cancelado
        awaitClose { listener.remove() }
    }


    /**
     * Busca uma única receita pelo nome. A função agora é 'suspend'.
     */
    fun buscarReceitaPorNome(nome: String): Flow<Receita?> = callbackFlow {
        // Busca o documento cujo ID é o próprio nome da receita
        val docRef = db.collection("receitas").document(nome)

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            // Converte o documento para o objeto Receita
            // Se o documento não existir, toObject retornará null
            val receita = snapshot?.toObject(Receita::class.java)
            trySend(receita).isSuccess
        }

        awaitClose { listener.remove() }
    }

    // ... (dentro da classe DataSource)

    // Função para excluir um documento no Firestore com base no nome da receita
    fun excluirReceita(nomeReceita: String) {
        // Busca o documento cujo campo 'nome' corresponde ao nome da receita
        db.collection("receitas")
            .whereEqualTo("nome", nomeReceita)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Se encontrar, percorre os resultados (geralmente será apenas um)
                for (document in querySnapshot.documents) {
                    // Deleta o documento encontrado
                    db.collection("receitas").document(document.id).delete()
                }
            }
            .addOnFailureListener {
                // Opcional: Lidar com falhas na exclusão
            }
    }

}
