package com.example.catalogoreceitas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent // You only need this one
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catalogoreceitas.ui.theme.CatalogoReceitasTheme
import com.example.catalogoreceitas.View.AddReceitaScreen
import com.example.catalogoreceitas.View.DescricaoReceitaScreen
import com.example.catalogoreceitas.View.ListaReceitasScreen

// ... rest of your MainActivity class

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogoReceitasTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "ListaReceitas" // Rota inicial do aplicativo
                ) {
                    // 1. Rota para a Lista Principal
                    composable(
                        route = "ListaReceitas"
                    ) {
                        ListaReceitasScreen(navController)
                    }

                    // 2. Rota ÚNICA para Adicionar e Editar Receita
                    // Esta rota lida com os dois casos:
                    // - Navegar para "AddReceita" -> Adicionar nova receita (receitaNome será null)
                    // - Navegar para "AddReceita?receitaNome=Bolo" -> Editar receita "Bolo"
                    composable(
                        route = "AddReceita?receitaNome={receitaNome}",
                        arguments = listOf(
                            navArgument("receitaNome") {
                                type = NavType.StringType
                                nullable = true // Permite que o argumento seja nulo
                                defaultValue = null // Define o valor padrão como nulo
                            }
                        )
                    ) { backStackEntry ->
                        // Extrai o argumento (será null se estiver adicionando)
                        val nomeDaReceitaParaEditar = backStackEntry.arguments?.getString("receitaNome")

                        // Chama a tela de Adicionar/Editar, passando o nome (ou null)
                        // Lembre-se de atualizar a assinatura da função AddReceitaScreen para aceitar este segundo parâmetro
                        AddReceitaScreen(navController, nomeDaReceitaParaEditar)
                    }

                    // 3. Rota para Detalhes da Receita
                    // Esta rota deve receber o nome da receita como argumento obrigatório
                    composable(
                        route = "DescricaoReceita/{receitaNome}",
                        arguments = listOf(
                            navArgument("receitaNome") {
                                type = NavType.StringType
                                // Não precisa ser 'nullable' aqui, pois a tela de detalhes sempre espera um nome
                            }
                        )
                    ) { backStackEntry ->
                        // Extrai o argumento 'receitaNome'
                        val nomeDaReceita = backStackEntry.arguments?.getString("receitaNome")

                        // Chama a tela de detalhes, passando o argumento
                        // Adicionado um check de segurança, embora o argumento seja obrigatório
                        DescricaoReceitaScreen(navController, nomeDaReceita)
                    }
                }
            }
        }
    }
}
