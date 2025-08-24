package com.example.todolistapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.material3.Card
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import com.example.todolistapp.R
import com.example.todolistapp.viewmodel.TodoViewModel
import com.example.todolistapp.View
import com.example.todolistapp.data.Todo
import com.example.todolistapp.data.fireTodo
import com.example.todolistapp.ui.theme.TopAppBar
import com.example.todolistapp.ui.theme.drawerContent
import com.example.todolistapp.viewmodel.FireTodoViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    navController: NavController,
    viewModel: TodoViewModel,
    fireViewModel: FireTodoViewModel,
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val loginUser = fireViewModel.loginUser ?: null

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { drawerContent(navController,loginUser) }
    ) {
        Scaffold(
            topBar = { TopAppBar(scope, drawerState) },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(all=20.dp),
                    contentColor = Color.White,
                    onClick = {
                        navController.navigate(View.AddView.route + "/0")
                    }
                ) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "add")
                }
            }
        ) {
            val todoList2 by fireViewModel.todos.observeAsState(initial = emptyList())
            val todoList = viewModel.getAllTodo.collectAsState(initial = listOf())

            // Display RoomDb data
            if(loginUser == null){
                LazyColumn(Modifier.fillMaxSize().padding(it)) {
                    items(todoList.value, key = { todo -> todo.id }) { todo ->
                        TodoItem(
                            todo = todo,
                            onClick = {
                                val id = todo.id
                                navController.navigate(View.AddView.route + "/${id}")
                            },
                            onCheckedChange = {
                                viewModel.isCompletedChecked(todo)
                            })

                    }
                }
            }else{
                // Display firestore data
                LazyColumn(Modifier.fillMaxSize().padding(it)){
                    items(todoList2, key = {todo -> todo.id}) { todo ->
                        FireTodoItem(
                            todo = todo,
                            onClick = {
                                val id = todo.id
                                navController.navigate(View.AddView.route + "/$id")
                            },
                            onCheckedChange = {
                                fireViewModel.isCompletedChecked(todo)
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo,
             onClick:() -> Unit,
             onCheckedChange: () -> Unit){
    // checkButton
    var painterRes by remember{
        mutableIntStateOf(R.drawable.outline_check_box_outline_blank_24)
    }
    // state that checkButton is clicked or not
    var isChecking by remember { mutableStateOf(false) }

    // if checked launch effects
    if (isChecking) {
        LaunchedEffect(Unit) {
            painterRes = R.drawable.baseline_check_box_24       // change emoticon
            delay(500)
            onCheckedChange()
            isChecking = false      // reset the state
        }
    }

    Card(onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(top= 8.dp, start = 8.dp, end = 8.dp)){
        Row(modifier = Modifier.padding(16.dp)){
            Icon(painter = painterResource(painterRes),
                contentDescription = "unchecked",
                modifier = Modifier.clickable{
                    isChecking = true
                }
            )
            Spacer(Modifier.width(8.dp))
            Text(todo.title, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun FireTodoItem(todo: fireTodo,
             onClick:() -> Unit,
             onCheckedChange: () -> Unit){

    var painterRes by remember{
        mutableIntStateOf(R.drawable.outline_check_box_outline_blank_24)
    }
    var isChecking by remember { mutableStateOf(false) }

    if (isChecking) {
        LaunchedEffect(Unit) {
            painterRes = R.drawable.baseline_check_box_24
            delay(500)
            onCheckedChange()
            isChecking = false
        }
    }
    Card(onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(top= 8.dp, start = 8.dp, end = 8.dp)){
        Row(modifier = Modifier.padding(16.dp)){
            Icon(painter = painterResource(painterRes),
                contentDescription = "unchecked",
                modifier = Modifier.clickable{
                    isChecking = true
                }
            )
            Spacer(Modifier.width(8.dp))
            Text(todo.title, fontWeight = FontWeight.ExtraBold)
        }

    }
}
