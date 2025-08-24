package com.example.todolistapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.Graph
import com.example.todolistapp.data.Result
import com.example.todolistapp.data.Todo
import com.example.todolistapp.data.TodoRepository
import com.example.todolistapp.data.User
import com.example.todolistapp.data.fireTodo
import com.example.todolistapp.data.fireTodoRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoViewModel(
    private val todoRepository: TodoRepository = Graph.todoRepository): ViewModel() {

        // title variable of todo
        var todoTitleState by mutableStateOf("")

    fun onTitleChanged(newString: String){
        todoTitleState = newString
    }

    lateinit var getAllTodo: Flow<List<Todo>>
    lateinit var getCompletedTodo: Flow<List<Todo>>

    init{
        viewModelScope.launch{
            getAllTodo = todoRepository.getAllTodo()
            getCompletedTodo = todoRepository.getCompletedTodo()
        }
    }

    // func when user checks that todo is completed
    fun isCompletedChecked(todo: Todo){
        viewModelScope.launch {
            //val todo = todo.copy(id=todo.id, title = todo.title, isCompleted = true, timestamp = System.currentTimeMillis() )
            todo.isCompleted = true
            todo.timestamp = System.currentTimeMillis()
            todoRepository.updateTodo(todo)
        }
    }

    // func when user restores todo from completed list
    fun isRestoredChecked(todo: Todo){
        viewModelScope.launch {
            //val todo = todo.copy(todo.id, todo.title, isCompleted = false, timestamp = 0L)
            todo.isCompleted = false
            todo.timestamp = 0L
            todoRepository.updateTodo(todo)
        }
    }




    fun getATodoByID(id:Long): Flow<Todo> {
        return todoRepository.getATodoByID(id)
    }

    fun addTodo(todo: Todo){
        viewModelScope.launch { todoRepository.addTodo(todo) }

    }

    fun updateTodo(todo: Todo){
        viewModelScope.launch{ todoRepository.updateTodo(todo) }

    }

    fun deleteTodo(todo: Todo){
        viewModelScope.launch { todoRepository.deleteTodo(todo) }
    }

}