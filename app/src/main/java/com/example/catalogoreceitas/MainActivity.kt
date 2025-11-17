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
import com.example.catalogoreceitas.ui.theme.CatalogoReceitasTheme
import com.example.catalogoreceitas.View.AddReceitaScreen
import com.example.catalogoreceitas.View.DescricaoReceitaScreen
import com.example.catalogoreceitas.View.ListaReceitasScreen



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogoReceitasTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "ListaReceitas"
                ) {

                    composable(
                        route = "ListaReceitas"
                    ) {
                        ListaReceitasScreen(navController)
                    }

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
                        val nomeDaReceitaParaEditar = backStackEntry.arguments?.getString("receitaNome")
                        AddReceitaScreen(navController, nomeDaReceitaParaEditar)
                    }

                    composable(
                        route = "DescricaoReceita/{receitaNome}",
                        arguments = listOf(
                            navArgument("receitaNome") {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->
                        val nomeDaReceita = backStackEntry.arguments?.getString("receitaNome")
                        DescricaoReceitaScreen(navController, nomeDaReceita)
                    }
                }
            }
        }
    }
}
