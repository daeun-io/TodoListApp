package com.example.todolistapp.view

import com.example.todolistapp.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolistapp.viewmodel.TodoViewModel
import com.example.todolistapp.View
import com.example.todolistapp.data.Todo
import com.example.todolistapp.data.fireTodo
import com.example.todolistapp.ui.theme.TopAppBar
import com.example.todolistapp.ui.theme.drawerContent
import com.example.todolistapp.viewmodel.FireTodoViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDetailView(
    id: String,
    viewModel: TodoViewModel,
    fireViewModel: FireTodoViewModel,
    navController: NavController
){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val loginUser = fireViewModel.loginUser ?: null

    val todo by viewModel.getATodoByID(id.toLong()).collectAsState(initial = null)
    var isCompleted by remember(todo){ mutableStateOf(todo?.isCompleted ?: false) }

    fireViewModel.getATodoByID(id)
    val fireTodo by fireViewModel.todo.observeAsState(initial = null)
    var isCompleted2 by remember(fireTodo){ mutableStateOf(fireTodo?.completed ?: false) }

    // load the title of todo(RoomDB)
    if(id.toLong() != 0L && todo != null){
        viewModel.todoTitleState = todo!!.title
    }else{
        viewModel.todoTitleState = ""
    }

    // load the title of todo(firestore)
    if(id != "0" && fireTodo != null){
        fireViewModel.todoTitleState = fireTodo!!.title
    }else{
        fireViewModel.todoTitleState = ""
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { drawerContent(navController, loginUser) }
    ) {
        Scaffold(
            topBar = { TopAppBar(scope, drawerState) },
        ) {
            Column(
                modifier = Modifier.padding(it).wrapContentWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){

                Spacer(Modifier.height(10.dp))

                // RoomDb data: Add(id = 0L), Delete, Restore(isCompleted = true), Update(id != 0L, isCompleted = false)
                if(loginUser == null){
                    val idLong = id.toLong()
                    TodoTextField(
                        label = "Todo",
                        value = viewModel.todoTitleState,
                        onValueChanged = {viewModel.onTitleChanged(it)}
                    )
                    Spacer(Modifier.height(10.dp))
                    Row{
                        Button(onClick={
                            if(viewModel.todoTitleState.isNotEmpty() && isCompleted == false){
                                if(idLong != 0L){
                                    viewModel.updateTodo(
                                        Todo(id = idLong, title = viewModel.todoTitleState.trim())
                                    )
                                }else{
                                    viewModel.addTodo(
                                        Todo(title = viewModel.todoTitleState.trim())
                                    )
                                }
                            }
                            else if(viewModel.todoTitleState.isNotEmpty() && isCompleted == true){
                                viewModel.isRestoredChecked(Todo(id = idLong, title = viewModel.todoTitleState.trim()))
                            }
                            scope.launch{
                                navController.navigateUp()
                            }
                        }){
                            Text(
                                text = if(isCompleted == true) "Restore"
                                else if(isCompleted == false && idLong != 0L) stringResource(R.string.update)
                                else stringResource(R.string.add)
                            )

                        }
                        Spacer(Modifier.width(10.dp))
                        if(idLong!=0L){
                            Button(onClick = {
                                viewModel.deleteTodo(
                                    Todo(id= idLong, title= viewModel.todoTitleState))
                                scope.launch { navController.navigateUp() }
                            }
                            ){
                                Text(
                                    text = stringResource(R.string.delete)
                                )
                            }
                        }
                     }
                }else{
                    // Firestore data: Add(id = "0"), Delete, Restore(isCompleted2 = true), Update(id != "0", isCompleted2 = false)
                    TodoTextField(
                        label = "Todo",
                        value = fireViewModel.todoTitleState,
                        onValueChanged = {fireViewModel.onTitleChanged(it)}
                    )
                    Row{
                        Spacer(Modifier.height(10.dp))
                        Button(onClick={
                            if(fireViewModel.todoTitleState.isNotEmpty() && isCompleted2 == false){
                                if(id != "0"){
                                    fireViewModel.updateTodo(
                                        fireTodo(
                                            id = id,
                                            title = fireViewModel.todoTitleState.trim()
                                        )
                                    )
                                }else{
                                    fireViewModel.addTodo(
                                        fireTodo(id = "0", title = fireViewModel.todoTitleState.trim())
                                    )
                                }
                            }
                            else if(fireViewModel.todoTitleState.isNotEmpty() && isCompleted2 == true){
                                fireViewModel.isRestoredChecked(fireTodo(id = id, title = fireViewModel.todoTitleState.trim()))
                            }
                            scope.launch{
                                navController.navigateUp()
                            }
                        }){
                            Text(
                                text = if(isCompleted2 == true) "Restore"
                                else if(isCompleted2 == false && id != "0") stringResource(R.string.update)
                                else stringResource(R.string.add)
                            )

                        }
                        Spacer(Modifier.width(10.dp))
                        if(id != "0"){
                            Button(onClick = {
                                fireViewModel.deleteTodo(
                                    fireTodo(id=id, title= fireViewModel.todoTitleState))
                                scope.launch { navController.navigateUp() }
                            }
                            ){
                                Text(
                                    text = stringResource(R.string.delete)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TodoTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )
}