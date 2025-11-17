package com.example.catalogoreceitas.ItemReceita

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.catalogoreceitas.Model.Receita
import com.example.catalogoreceitas.ui.theme.RB_Green // Assumindo que você usa as cores do projeto base
import com.example.catalogoreceitas.ui.theme.RB_Red
import com.example.catalogoreceitas.ui.theme.RB_Yellow
import com.example.catalogoreceitas.ui.theme.White

@Composable
fun ItemReceita(
    receita: Receita,
    onNavigateToDetails: () -> Unit // Lambda para navegar ao tocar no ícone
) {
    // Obtém os dados da receita
    val nomeReceita = receita.nome
    val tempoPreparo = receita.tempoPreparo

    // Nova lógica de status baseada no TEMPO DE PREPARO
    val statusPreparo: String
    val corStatus: Color

    when {
        tempoPreparo <= 20 -> { // Curto: até 20 minutos
            statusPreparo = "Curto"
            corStatus = RB_Green
        }
        tempoPreparo <= 60 -> { // Médio: de 21 a 60 minutos
            statusPreparo = "Médio"
            corStatus = RB_Yellow
        }
        else -> { // Longo: mais de 60 minutos
            statusPreparo = "Longo"
            corStatus = RB_Red
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp), // Padding ajustado
        colors = CardDefaults.cardColors(White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Nome da receita
            Text(
                nomeReceita,
                fontWeight = FontWeight.ExtraBold, // Peso da fonte alterado
                color = Color.Black
            )

            // Tipo da receita (demonstra a Herança)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween, // Espaçamento entre elementos
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                // Informação do tempo de preparo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Tempo de Preparo:",
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    // Cartão de Status
                    Card(
                        colors = CardDefaults.cardColors(corStatus)
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                statusPreparo,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Exibe o tempo exato (opcional)
                    Text(
                        " (${tempoPreparo} min)",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                // Ícone para navegar para os detalhes
                IconButton(
                    onClick = onNavigateToDetails, // Usa o lambda passado
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Ver detalhes da receita.",
                        tint = Color.Gray,
                    )
                }
            }
        }
    }
}