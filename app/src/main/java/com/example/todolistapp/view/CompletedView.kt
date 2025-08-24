package com.example.todolistapp.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.material3.Card
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.todolistapp.viewmodel.TodoViewModel
import com.example.todolistapp.View
import com.example.todolistapp.data.Todo
import com.example.todolistapp.data.fireTodo
import com.example.todolistapp.ui.theme.TopAppBar
import com.example.todolistapp.ui.theme.drawerContent
import com.example.todolistapp.viewmodel.FireTodoViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedView(
    navController: NavController,
    viewModel: TodoViewModel,
    fireViewModel: FireTodoViewModel
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val loginUser = fireViewModel.loginUser ?: null

    val completedTodoList = viewModel.getCompletedTodo.collectAsState(initial = listOf())
    val completedTodoList2 by fireViewModel.cTodos.observeAsState(initial = emptyList())


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { drawerContent(navController, loginUser) }
    ) {
        Scaffold(
            topBar = { TopAppBar(scope, drawerState) }
        ) {
            // Display RoomDb data
            if(loginUser == null){
                LazyColumn(Modifier.fillMaxSize().padding(it)){
                    items(completedTodoList.value, key = {todo -> todo.timestamp}){
                            todo ->
                        CompletedTodoItem(todo) {
                            navController.navigate(View.AddView.route + "/${todo.id}")
                        }
                    }
                }
                // Display firestore data
            } else{
                LazyColumn(Modifier.fillMaxSize().padding(it)) {
                    items(completedTodoList2, key = {todo -> todo.timestamp}){
                        todo ->
                        FireCompletedTodoItem(todo) {
                            navController.navigate(View.AddView.route + "/${todo.id}")
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CompletedTodoItem(todo: Todo,
             onClick:() -> Unit, ){
    Card(onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(top= 8.dp, start = 8.dp, end = 8.dp)){
        Column(modifier = Modifier.padding(16.dp)){
            Text(todo.title, fontWeight = FontWeight.ExtraBold)
            Text(text = formatTimestamp(todo.timestamp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FireCompletedTodoItem(todo: fireTodo,
                          onClick:() -> Unit, ){
    Card(onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(top= 8.dp, start = 8.dp, end = 8.dp)){
        Column(modifier = Modifier.padding(16.dp)){
            Text(todo.title, fontWeight = FontWeight.ExtraBold)
            Text(text = formatTimestamp(todo.timestamp))
        }
    }
}


// used to display completed time of todo
@RequiresApi(api = Build.VERSION_CODES.O)
private fun formatTimestamp(timestamp: Long): String{

    val todoDataTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
        ZoneId.systemDefault())
    val now = LocalDateTime.now()

    return when{
        // Today & Yesterday: display time(HH:mm)
        isSameDay(todoDataTime, now) -> "today ${formatTime(todoDataTime)}"
        isSameDay(todoDataTime.plusDays(1), now) -> "yesterday ${formatTime(todoDataTime)}"
        // other: display date
        else -> formateDate(todoDataTime)
    }
}
@RequiresApi(api = Build.VERSION_CODES.O)
private fun isSameDay(dateTime1: LocalDateTime, dateTime2: LocalDateTime): Boolean{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime1.format(formatter) == dateTime2.format(formatter)
}
@RequiresApi(api = Build.VERSION_CODES.O)
private fun formatTime(dateTime: LocalDateTime):String{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return formatter.format(dateTime)
}
@RequiresApi(api = Build.VERSION_CODES.O)
private fun formateDate(dateTime: LocalDateTime): String{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return formatter.format(dateTime)
}
