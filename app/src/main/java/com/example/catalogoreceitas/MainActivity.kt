package com.example.catalogoreceitas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catalogoreceitas.ui.theme.CatalogoReceitasTheme // Importe o nome do seu Tema
import com.example.catalogoreceitas.View.AddReceitaScreen // Sua tela de Adicionar/Editar
import com.example.catalogoreceitas.View.DescricaoReceitaScreen // Sua tela de Detalhes
import com.example.catalogoreceitas.View.ListaReceitasScreen // Sua tela de Lista

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogoReceitasTheme { // Mantenha o nome do seu tema
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

                    // 2. Rota para Adicionar/Editar Receita (Descrição)
                    // Usamos o nome de arquivo que você indicou: AddReceita
                    composable(
                        route = "AddReceita"
                    ) {
                        AddReceitaScreen(navController)
                    }

                    // 3. Rota para Detalhes da Receita (DescricaoReceita)
                    // Esta rota deve receber o nome da receita como argumento na URL
                    composable(
                        // Define a rota com o parâmetro dinâmico {receitaNome}
                        route = "DescricaoReceita/{receitaNome}",
                        arguments = listOf(
                            navArgument("receitaNome") {
                                type = NavType.StringType
                                nullable = true // Permite que seja nulo, por segurança
                            }
                        )
                    ) { backStackEntry ->
                        // Extrai o argumento 'receitaNome'
                        val nome = backStackEntry.arguments?.getString("receitaNome")
                        // Chama a tela de detalhes, passando o argumento
                        DescricaoReceitaScreen(navController, nome ?: "")
                    }
                }
            }
        }
    }
}