package com.example.todolistapp.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todolistapp.View
import com.example.todolistapp.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
){

    var email by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val loginUser = viewModel.loginUser ?: null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                navController.navigateUp()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            if(loginUser == null){
                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    label = {Text("Email")},
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    label = {Text("Password")},
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    visualTransformation = PasswordVisualTransformation()
                )

                Button(onClick={
                    viewModel.login(email, password)
                    Toast.makeText(context, "Logged in", Toast.LENGTH_LONG).show()
                    navController.navigate(View.MainView.route)
                },Modifier.fillMaxWidth().padding(8.dp)){
                    Text("Login")
                }
                Spacer(Modifier.height(16.dp))
                Row{
                    Text("Don't have an account? ")
                    Text("Sign up", fontWeight = ExtraBold,
                        modifier = Modifier.clickable(){navController.navigate(View.SignUpScreen.route)})
                }

            }

            else{
                Column(
                    modifier = Modifier.fillMaxSize().padding(it),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Want to Log out?", fontSize = 20.sp)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {
                        viewModel.signOut()
                        Toast.makeText(context, "Signed out successfully", Toast.LENGTH_LONG).show()
                        navController.navigate(View.MainView.route)
                    }){
                        Text("Log Out", fontSize = 20.sp)
                    }
                }
        }
        }
    }
}