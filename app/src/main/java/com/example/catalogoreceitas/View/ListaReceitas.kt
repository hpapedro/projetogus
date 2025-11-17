package com.example.catalogoreceitas.View

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.catalogoreceitas.ui.theme.Purple40 // Cores do tema
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.collectAsState
import com.example.catalogoreceitas.ItemReceita.ItemReceita // Seu novo componente de item
import com.example.catalogoreceitas.Repository.ReceitasRepository // Seu repositório de Receitas
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListaReceitasScreen( // Nome da função alterado
    navController: NavController
) {

    // Usa o repositório de Receitas
    val receitasRepository = ReceitasRepository()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Catálogo de Receitas", // Título alterado
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
        // Fundo em Cinza Claro (0xFFF0F0F0), diferente do preto do projeto base
        containerColor = Color(0xFFF0F0F0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navega para a rota de Adicionar Receita
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
    ) { paddingValues -> // Usamos paddingValues para respeitar o TopAppBar

        // Coleta o Flow de receitas do repositório
        // O valor inicial é uma MutableList vazia
        val listaReceitas = receitasRepository.listarReceitas().collectAsState(mutableListOf()).value

        LazyColumn (
            // Aplica o padding do Scaffold
            modifier = Modifier.padding(paddingValues)
        ) {
            // Usa a iteração moderna do Compose, que passa o objeto Receita
            items(listaReceitas){ receita ->
                ItemReceita(
                    receita = receita, // Passa a receita
                    onNavigateToDetails = {
                        // **CORREÇÃO APLICADA AQUI**
                        // Codifica o nome da receita para ser seguro para a URL
                        val encodedNome = URLEncoder.encode(receita.nome, StandardCharsets.UTF_8.toString())
                        // Navega para a tela de Detalhes (DescricaoReceita), passando o nome da receita como argumento
                        navController.navigate("DescricaoReceita/$encodedNome")
                    }
                )
            }
        }

    }
}