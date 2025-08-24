package com.example.todolistapp.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolistapp.View
import com.google.firebase.auth.FirebaseUser

@Composable
fun drawerContent(navController: NavController, loginUser: FirebaseUser?){
    ModalDrawerSheet {
        Row(Modifier.padding(16.dp).clickable{navController.navigate(View.LoginScreen.route)}){
            Icon(Icons.Default.AccountCircle, "account")
            Spacer(Modifier.width(8.dp))
            if (loginUser == null) Text("My Account")
            else Text("Welcome ${loginUser.email}")
        }

        Text("Todo-List", modifier = Modifier.padding(16.dp)
            .clickable{navController.navigate(View.MainView.route)})
        Text("Completed", modifier = Modifier.padding(16.dp)
            .clickable{navController.navigate(View.CompleteView.route)})
    }
}