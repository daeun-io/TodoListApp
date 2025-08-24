package com.example.todolistapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolistapp.view.AddEditDetailView
import com.example.todolistapp.view.CompletedView
import com.example.todolistapp.view.MainView
import com.example.todolistapp.ui.theme.TodoListAppTheme
import com.example.todolistapp.view.LoginScreen
import com.example.todolistapp.view.SignupScreen
import com.example.todolistapp.viewmodel.AuthViewModel
import com.example.todolistapp.viewmodel.FireTodoViewModel
import com.example.todolistapp.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    name: String,
    modifier: Modifier,
    todoViewModel: TodoViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    fireTodoViewModel: FireTodoViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = View.MainView.route
    ) {
        composable(View.MainView.route) {
            MainView(navController, todoViewModel, fireTodoViewModel)
        }

        composable(View.AddView.route + "/{id}",
            arguments = listOf(navArgument("id"){
                type = NavType.StringType
                defaultValue = "0"
                nullable = false
            })){
            entry ->
            val id = entry.arguments!!.getString("id") ?: "0"
            AddEditDetailView(id = id, viewModel= todoViewModel, fireViewModel = fireTodoViewModel, navController = navController)
        }

        composable(View.CompleteView.route){
            CompletedView(navController, todoViewModel, fireTodoViewModel)
        }

        composable(View.SignUpScreen.route){
            SignupScreen(navController, authViewModel)
        }

        composable(View.LoginScreen.route){
            LoginScreen(navController, authViewModel)
        }

    }
}