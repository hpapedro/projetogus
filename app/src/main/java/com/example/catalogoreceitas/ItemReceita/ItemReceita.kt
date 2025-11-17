package com.example.catalogoreceitas.ItemReceita

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete // Importa o ícone de lixeira
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catalogoreceitas.Model.Receita
import com.example.catalogoreceitas.ui.theme.Purple40
import com.example.catalogoreceitas.ui.theme.Purple80

@Composable
fun ItemReceita(
    receita: Receita,
    onNavigateToDetails: () -> Unit,
    onDelete: () -> Unit // <-- ADICIONE ESTE PARÂMETRO AQUI
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // 1. Cabeçalho Colorido com Título e Botão de Excluir
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Purple80, Purple40)
                        )
                    )
            ) {
                // Conteúdo clicável que leva aos detalhes
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onNavigateToDetails() } // Clique na área do título
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = receita.nome,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        modifier = Modifier.weight(1f) // Garante que o texto não empurre o botão
                    )
                }

                // Botão de excluir alinhado à direita
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(onClick = onDelete) { // Usa a função onDelete recebida
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Excluir Receita",
                            tint = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // 2. Corpo do Card (também clicável)
            Column(
                modifier = Modifier
                    .clickable { onNavigateToDetails() }
                    .padding(16.dp)
            ) {
                Text(
                    text = receita.descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 3. Informações de Tempo e Tipo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoComIcone(
                        icone = Icons.Outlined.AccessTime,
                        texto = "${receita.tempoPreparo} min"
                    )
                    InfoComIcone(
                        icone = Icons.Filled.Tune,
                        texto = receita.tipoClasse
                    )
                }
            }
        }
    }
}

// Composable auxiliar (permanece o mesmo)
@Composable
private fun InfoComIcone(icone: androidx.compose.ui.graphics.vector.ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icone,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
