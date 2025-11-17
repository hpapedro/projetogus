package com.example.catalogoreceitas.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.catalogoreceitas.Model.Receita
import com.example.catalogoreceitas.Repository.ReceitasRepository
import com.example.catalogoreceitas.ui.theme.Purple40
import kotlinx.coroutines.flow.Flow
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescricaoReceitaScreen(
    navController: NavController,
    receitaNome: String? // Marcado como anulável para segurança
) {
    // Se o nome da receita for nulo, mostra uma tela de erro.
    if (receitaNome == null) {
        Scaffold(topBar = { TopBarPadrao("Receita não encontrada", navController) }) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Não foi possível carregar a receita.")
            }
        }
        return
    }

    val repository = remember { ReceitasRepository() }

    // Decodifica o nome da receita que veio da URL.
    val decodedNome = remember(receitaNome) {
        try {
            URLDecoder.decode(receitaNome, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            receitaNome
        }
    }

    // Busca a receita e coleta o resultado como um estado do Compose.
    val receitaFlow: Flow<Receita?> = remember(decodedNome) {
        repository.buscarReceitaPorNome(decodedNome)
    }
    val receita by receitaFlow.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopBarPadrao(receita?.nome ?: "Carregando...", navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mostra um indicador de progresso enquanto a receita é carregada.
            if (receita == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                // Quando a receita for carregada, exibe os detalhes.
                DetalhesDaReceita(receita = receita!!)
            }
        }
    }
}

@Composable
private fun DetalhesDaReceita(receita: Receita) {
    // Column principal que permite a rolagem da tela.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Título da Receita
        Text(
            text = receita.nome,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Exibe o tipo da classe da receita (Ex: "Receita Simples", "Receita Complexa")
        Text(
            text = "Tipo: ${receita.tipoClasse}",
            style = MaterialTheme.typography.titleMedium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Informações Rápidas (Tempo e Nível)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tempo: ${receita.tempoPreparo} min",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            // CORRIGIDO: Usa 'nivelDificuldade' para mostrar o nível (Fácil, Médio, etc.)
            Text(
                text = "Nível: ${receita.nivelDificuldade}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Seção de Modo de Preparo
        Text(
            text = "Modo de Preparo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = receita.descricao,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Seção de Ingredientes
        Text(
            text = "Ingredientes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        receita.ingredientes.forEach { ingrediente ->
            Text(
                text = "• $ingrediente",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarPadrao(titulo: String, navController: NavController) {
    TopAppBar(
        title = { Text(titulo, color = Color.White) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
        }
    )
}
