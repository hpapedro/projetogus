package com.example.catalogoreceitas.View // Seu pacote View

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.catalogoreceitas.Repository.ReceitasRepository // Seu repositório
import com.example.catalogoreceitas.components.CaixaDeTexto // Seu componente
import com.example.catalogoreceitas.ui.theme.Purple40 // Cores do tema
import com.example.catalogoreceitas.ui.theme.RB_Green
import com.example.catalogoreceitas.ui.theme.RB_Red
import com.example.catalogoreceitas.ui.theme.RB_Yellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddReceitaScreen(
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val receitasRepository = remember { ReceitasRepository() }
    val context = LocalContext.current

    // ESTADOS (permanecem os mesmos)
    var nomeReceita by remember { mutableStateOf("") }
    var descricaoReceita by remember { mutableStateOf("") }
    var ingredientesTexto by remember { mutableStateOf("") }
    var tempoPreparoOpcao by remember { mutableStateOf("curto") }
    var tipoReceita by remember { mutableStateOf("Simples") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Adicionar Nova Receita",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40
                ),
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
        },
        containerColor = Color(0xFFF4F4F8)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // --- Card 1: Campos de Texto ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CaixaDeTexto(Modifier.fillMaxWidth(), nomeReceita, { nomeReceita = it }, "Nome da Receita *", 1, KeyboardType.Text)
                    Spacer(modifier = Modifier.height(16.dp))
                    CaixaDeTexto(Modifier.fillMaxWidth().height(120.dp), descricaoReceita, { descricaoReceita = it }, "Modo de Preparo", 5, KeyboardType.Text)
                    Spacer(modifier = Modifier.height(16.dp))
                    CaixaDeTexto(Modifier.fillMaxWidth().height(100.dp), ingredientesTexto, { ingredientesTexto = it }, "Ingredientes (separados por vírgula)", 3, KeyboardType.Text)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Card 2: Opções de Seleção ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // SELEÇÃO DE TIPO DE RECEITA (VERTICAL)
                    Text("Classificação da Receita:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))

                    // --- MUDANÇA AQUI: Row -> Column ---
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { tipoReceita = "Simples" }) {
                            RadioButton(selected = tipoReceita == "Simples", onClick = { tipoReceita = "Simples" }, colors = RadioButtonDefaults.colors(selectedColor = RB_Green))
                            Text("Simples")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { tipoReceita = "Complexa" }) {
                            RadioButton(selected = tipoReceita == "Complexa", onClick = { tipoReceita = "Complexa" }, colors = RadioButtonDefaults.colors(selectedColor = RB_Red))
                            Text("Complexa")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(20.dp))

                    // SELEÇÃO DE TEMPO DE PREPARO (VERTICAL)
                    Text("Tempo de Preparo Estimado:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))

                    // --- MUDANÇA AQUI: Row -> Column ---
                    Column(modifier = Modifier.fillMaxWidth()) {
                        TempoRadioButton(label = "Curto (≤20m)", selected = tempoPreparoOpcao == "curto", onClick = { tempoPreparoOpcao = "curto" }, color = RB_Green)
                        TempoRadioButton(label = "Médio (≤60m)", selected = tempoPreparoOpcao == "medio", onClick = { tempoPreparoOpcao = "medio" }, color = RB_Yellow)
                        TempoRadioButton(label = "Longo (>60m)", selected = tempoPreparoOpcao == "longo", onClick = { tempoPreparoOpcao = "longo" }, color = RB_Red)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // BOTÃO DE SALVAR
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                onClick = {
                    if (nomeReceita.isBlank()) {
                        Toast.makeText(context, "O Nome da Receita é obrigatório!", Toast.LENGTH_SHORT).show()
                    } else {
                        val tempoEmMinutos = when (tempoPreparoOpcao) {
                            "curto" -> 20
                            "medio" -> 40
                            "longo" -> 80
                            else -> 20
                        }
                        val nivelDificuldadeString = tempoPreparoOpcao.replaceFirstChar { it.uppercase() }
                        val listaIngredientes = ingredientesTexto.split(',').map { it.trim() }.filter { it.isNotEmpty() }

                        scope.launch(Dispatchers.IO) {
                            receitasRepository.salvarReceita(
                                nome = nomeReceita,
                                descricao = descricaoReceita,
                                tempoPreparo = tempoEmMinutos,
                                ingredientes = listaIngredientes,
                                nivelDificuldade = nivelDificuldadeString,
                                tipoClasse = tipoReceita
                            )
                        }

                        scope.launch(Dispatchers.Main) {
                            Toast.makeText(context, "Receita salva com sucesso!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                }
            ) {
                Text("Salvar Receita", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Componente auxiliar (seu código original, apenas com o Text clicável e um ajuste no modifier)
@Composable
fun TempoRadioButton(label: String, selected: Boolean, onClick: () -> Unit, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth() // Garante que a área clicável ocupe toda a largura
            .clickable(onClick = onClick)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = color)
        )
        Text(text = label, fontSize = 14.sp)
    }
}
