package com.example.catalogoreceitas.View

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.catalogoreceitas.ItemReceita.ItemReceita
import com.example.catalogoreceitas.Repository.ReceitasRepository
import com.example.catalogoreceitas.ui.theme.Purple40
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListaReceitasScreen(
    navController: NavController
) {
    val receitasRepository = remember { ReceitasRepository() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Catálogo de Receitas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40
                )
            )
        },
        containerColor = Color(0xFFF0F0F0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("AddReceita")
                },
                containerColor = Purple40
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Ícone de adicionar receita.",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->

        val listaReceitas = receitasRepository.listarReceitas().collectAsState(mutableListOf()).value

        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(listaReceitas, key = { it.nome }) { receita ->
                ItemReceita(
                    receita = receita,
                    onNavigateToDetails = {
                        val encodedNome = URLEncoder.encode(receita.nome, StandardCharsets.UTF_8.toString())
                        navController.navigate("DescricaoReceita/$encodedNome")
                    },
                    onDelete = {
                        receitasRepository.excluirReceita(receita.nome)
                    }
                )
            }
        }
    }
}
