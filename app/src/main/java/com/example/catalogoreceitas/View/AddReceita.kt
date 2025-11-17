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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddReceitaScreen(
    navController: NavController,
    nomeDaReceitaParaEditar: String? // 1. Recebe o nome da receita (pode ser nulo)
) {
    val scope = rememberCoroutineScope()
    val receitasRepository = remember { ReceitasRepository() }
    val context = LocalContext.current

    // 2. Determina se a tela está em modo de edição
    val isEditing = nomeDaReceitaParaEditar != null

    // Estados dos campos do formulário
    var nomeReceita by remember { mutableStateOf("") }
    var descricaoReceita by remember { mutableStateOf("") }
    var ingredientesTexto by remember { mutableStateOf("") }
    var tempoPreparoOpcao by remember { mutableStateOf("curto") }
    var tipoReceita by remember { mutableStateOf("Simples") }

    // 3. Efeito que carrega os dados da receita se estiver em modo de edição
    LaunchedEffect(key1 = Unit) {
        if (isEditing && nomeDaReceitaParaEditar != null) {
            val decodedNome = URLDecoder.decode(nomeDaReceitaParaEditar, StandardCharsets.UTF_8.toString())

            // Busca a receita uma única vez para preencher o formulário
            val receitaEncontrada = receitasRepository.buscarReceitaPorNome(decodedNome).firstOrNull()

            if (receitaEncontrada != null) {
                // Preenche os estados com os dados da receita encontrada
                nomeReceita = receitaEncontrada.nome
                descricaoReceita = receitaEncontrada.descricao
                ingredientesTexto = receitaEncontrada.ingredientes.joinToString(", ")
                tipoReceita = receitaEncontrada.tipoClasse

                // Converte o tempo em minutos de volta para a opção do RadioButton
                tempoPreparoOpcao = when {
                    receitaEncontrada.tempoPreparo <= 20 -> "curto"
                    receitaEncontrada.tempoPreparo <= 60 -> "medio"
                    else -> "longo"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                // 4. Título dinâmico
                title = {
                    Text(
                        text = if (isEditing) "Editar Receita" else "Adicionar Nova Receita",
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
// --- Card 1: Campos de Texto ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // CAMPO DO NOME DA RECEITA (ADICIONAR ESTE TRECHO)
                    CaixaDeTexto(
                        modifier = Modifier.fillMaxWidth(),
                        value = nomeReceita,
                        onValueChange = { nomeReceita = it },
                        label = "Nome da Receita",
                        maxLines = 1,
                        keyboardType = KeyboardType.Text
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Campos existentes ---
                    CaixaDeTexto(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        value = descricaoReceita,
                        onValueChange = { descricaoReceita = it },
                        label = "Modo de Preparo",
                        maxLines = 5,
                        keyboardType = KeyboardType.Text
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CaixaDeTexto(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        value = ingredientesTexto,
                        onValueChange = { ingredientesTexto = it },
                        label = "Ingredientes (separados por vírgula)",
                        maxLines = 3,
                        keyboardType = KeyboardType.Text
                    )
                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            // --- Card 2: Opções de Seleção (código permanece o mesmo) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Classificação da Receita:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
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
                    Text("Tempo de Preparo Estimado:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
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
                        // A lógica de processamento dos dados é a mesma
                        val tempoEmMinutos = when (tempoPreparoOpcao) {
                            "curto" -> 20
                            "medio" -> 40
                            "longo" -> 80
                            else -> 20
                        }
                        val nivelDificuldadeString = tempoPreparoOpcao.replaceFirstChar { it.uppercase() }
                        val listaIngredientes = ingredientesTexto.split(',').map { it.trim() }.filter { it.isNotEmpty() }

                        // A função salvarReceita sobrescreve o documento se o nome/ID já existir
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
                            // Mensagem de sucesso dinâmica
                            val feedbackMessage = if (isEditing) "Receita atualizada com sucesso!" else "Receita salva com sucesso!"
                            Toast.makeText(context, feedbackMessage, Toast.LENGTH_SHORT).show()
                            navController.popBackStack() // Volta para a tela anterior
                        }
                    }
                }
            ) {
                // 6. Texto do botão dinâmico
                Text(
                    text = if (isEditing) "Salvar Alterações" else "Salvar Receita",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Componente auxiliar não precisa de alterações
@Composable
fun TempoRadioButton(label: String, selected: Boolean, onClick: () -> Unit, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
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
