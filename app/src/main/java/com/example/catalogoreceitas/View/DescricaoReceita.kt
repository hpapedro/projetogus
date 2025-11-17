package com.example.catalogoreceitas.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
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
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescricaoReceitaScreen(
    navController: NavController,
    receitaNome: String?
) {
    if (receitaNome == null) {
        Scaffold(
            topBar = {
                TopBarPadrao(
                    titulo = "Receita não encontrada",
                    navController = navController,
                    onDelete = {},
                    onEdit = {}
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Não foi possível carregar a receita.")
            }
        }
        return
    }

    val repository = remember { ReceitasRepository() }
    val decodedNome = remember(receitaNome) {
        try {
            URLDecoder.decode(receitaNome, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            receitaNome
        }
    }

    val receitaFlow: Flow<Receita?> = remember(decodedNome) {
        repository.buscarReceitaPorNome(decodedNome)
    }
    val receita by receitaFlow.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopBarPadrao(
                titulo = receita?.nome ?: "Carregando...",
                navController = navController,
                onDelete = {
                    receita?.let { receitaParaExcluir ->
                        repository.excluirReceita(receitaParaExcluir.nome)
                        navController.popBackStack()
                    }
                },
                onEdit = {
                    receita?.let { receitaParaEditar ->
                        val encodedNome = URLEncoder.encode(receitaParaEditar.nome, StandardCharsets.UTF_8.toString())
                        navController.navigate("AddReceita?receitaNome=$encodedNome")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (receita == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                DetalhesDaReceita(receita = receita!!)
            }
        }
    }
}

@Composable
private fun DetalhesDaReceita(receita: Receita) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = receita.nome,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        SuggestionChip(
            onClick = { },
            label = {
                Text(
                    text = receita.tipoClasse,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            },
            colors = SuggestionChipDefaults.suggestionChipColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoItem(icon = Icons.Default.Schedule, label = "Tempo", value = "${receita.tempoPreparo} min")
                InfoItem(icon = Icons.Default.Star, label = "Nível", value = receita.nivelDificuldade)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(title = "Modo de Preparo")
        Text(
            text = receita.descricao,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.5
        )
        Spacer(modifier = Modifier.height(24.dp))
        Divider(thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(title = "Ingredientes")
        receita.ingredientes.forEach { ingrediente ->
            Text(
                text = "• $ingrediente",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
private fun InfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarPadrao(
    titulo: String,
    navController: NavController,
    onDelete: () -> Unit,
    onEdit: () -> Unit // <-- 3. PARÂMETRO onEdit ADICIONADO
) {
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
        },
        actions = {
            // 4. BOTÃO DE EDITAR ADICIONADO
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar Receita",
                    tint = Color.White
                )
            }
            // Ícone de deletar na barra de ações
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Deletar Receita",
                    tint = Color.White
                )
            }
        }
    )
}
