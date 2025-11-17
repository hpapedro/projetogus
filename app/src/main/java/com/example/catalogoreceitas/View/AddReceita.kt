package com.example.catalogoreceitas.View

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.catalogoreceitas.components.CaixaDeTexto // Seu componente
import com.example.catalogoreceitas.Repository.ReceitasRepository // Seu repositório
import com.example.catalogoreceitas.ui.theme.Purple40 // Cores do tema
import com.example.catalogoreceitas.ui.theme.RB_Green
import com.example.catalogoreceitas.ui.theme.RB_Red
import com.example.catalogoreceitas.ui.theme.RB_Yellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddReceitaScreen( // Nome da função corrigido para AddReceitaScreen
    navController: NavController
) {

    val scope = rememberCoroutineScope()
    val receitasRepository = ReceitasRepository()
    val context = LocalContext.current

    var nomeReceita by remember { mutableStateOf("") }
    var descricaoReceita by remember { mutableStateOf("") }
    var ingredientesTexto by remember { mutableStateOf("") }
    var tempoPreparoOpcao by remember { mutableStateOf("curto") }
    val nivelDificuldade = "Fácil"


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Adicionar Nova Receita",
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 100.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            CaixaDeTexto(
                Modifier.fillMaxWidth(),
                nomeReceita,
                { nomeReceita = it },
                "Nome da Receita (Obrigatório)",
                1,
                KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(20.dp))

            CaixaDeTexto(
                Modifier.fillMaxWidth().height(150.dp),
                descricaoReceita,
                { descricaoReceita = it },
                "Modo de Preparo",
                5,
                KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(20.dp))

            CaixaDeTexto(
                Modifier.fillMaxWidth().height(100.dp),
                ingredientesTexto,
                { ingredientesTexto = it },
                "Ingredientes (Separe por vírgulas)",
                3,
                KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Tempo de Preparo Estimado:", modifier = Modifier.padding(bottom = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                TempoRadioButton(
                    label = "Curto (≤20m)",
                    selected = tempoPreparoOpcao == "curto",
                    onClick = { tempoPreparoOpcao = "curto" },
                    color = RB_Green
                )
                TempoRadioButton(
                    label = "Médio (≤60m)",
                    selected = tempoPreparoOpcao == "medio",
                    onClick = { tempoPreparoOpcao = "medio" },
                    color = RB_Yellow
                )
                TempoRadioButton(
                    label = "Longo (>60m)",
                    selected = tempoPreparoOpcao == "longo",
                    onClick = { tempoPreparoOpcao = "longo" },
                    color = RB_Red
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(Purple40),
                onClick = {
                    if (nomeReceita.isEmpty()) {
                        Toast.makeText(context, "O Nome da Receita é obrigatório!", Toast.LENGTH_SHORT).show()
                    } else {
                        val tempoEmMinutos = when(tempoPreparoOpcao) {
                            "curto" -> 20
                            "medio" -> 40
                            "longo" -> 80
                            else -> 20
                        }

                        val listaIngredientes = ingredientesTexto.split(',')
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }

                        scope.launch(Dispatchers.IO) {
                            receitasRepository.salvarReceita(
                                nome = nomeReceita,
                                descricao = descricaoReceita,
                                tempoPreparo = tempoEmMinutos,
                                ingredientes = listaIngredientes,
                                nivelDificuldade = nivelDificuldade
                            )
                        }

                        scope.launch(Dispatchers.Main) {
                            Toast.makeText(context, "Receita salva com sucesso!", Toast.LENGTH_SHORT).show()
                            navController.navigate("ListaReceitas") {
                                popUpTo("ListaReceitas") { inclusive = true }
                            }
                        }
                    }
                }
            ) {
                Text("Salvar Receita", fontSize = 16.sp)
            }
        }
    }
}

// Componente auxiliar para os Radio Buttons
@Composable
fun TempoRadioButton(label: String, selected: Boolean, onClick: () -> Unit, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = color)
        )
        Text(text = label, fontSize = 12.sp)
    }
}