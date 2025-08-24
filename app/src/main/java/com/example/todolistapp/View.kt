package com.example.todolistapp

// Route of each screen for navigation
sealed class View(val route: String) {
    object MainView: View("main_view")
    object AddView: View("add_view")
    object CompleteView: View("complete_view")
    object LoginScreen: View("login_screen")
    object SignUpScreen: View("signup_screen")
}