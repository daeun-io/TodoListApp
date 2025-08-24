package com.example.todolistapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.Injection
import com.example.todolistapp.data.fireTodo
import com.example.todolistapp.data.fireTodoRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.example.todolistapp.data.Result
import com.google.firebase.auth.FirebaseUser

class FireTodoViewModel(
): ViewModel() {

    //call repository and firebaseAuth
    private val fireTodoRepository: fireTodoRepository
    private val auth = FirebaseAuth.getInstance()

    //uncompleted todo list
    private val _todos = MutableLiveData<List<fireTodo>>()
    val todos: LiveData<List<fireTodo>> get() = _todos

    // completed todo list
    private val _cTodos = MutableLiveData<List<fireTodo>>(emptyList())
    val cTodos: LiveData<List<fireTodo>> get() = _cTodos

    // single todo
    private val _todo = MutableLiveData<fireTodo>()
    val todo: LiveData<fireTodo> get() = _todo

    // current(login) user
    val loginUser: FirebaseUser? get() = auth.currentUser


    init{
        fireTodoRepository = fireTodoRepository(FirebaseAuth.getInstance(), Injection.instance())
        getTodoList()
        getCompletedTodoList()
    }


    // uncompleted todo list
    fun getTodoList(){
        viewModelScope.launch{
            fireTodoRepository.getAllTodo().collect {
                _todos.value = it
            }
        }
    }

    // completed todo list
    fun getCompletedTodoList(){
        viewModelScope.launch{
            fireTodoRepository.getCompletedTodo().collect{
                _cTodos.value = it
            }
        }
    }

    // title variable of todo
    var todoTitleState by mutableStateOf("")

    fun onTitleChanged(newString: String){
        todoTitleState = newString
    }

    // func when user checks that todo is completed
    fun isCompletedChecked(todo: fireTodo){
        viewModelScope.launch {
            //val todo = todo.copy(id=todo.id, title = todo.title, isCompleted = true, timestamp = System.currentTimeMillis() )

            todo.completed = true
            todo.timestamp = System.currentTimeMillis()
            fireTodoRepository.updateTodo(todo)
        }
    }

    // func when user restores todo from completed list
    fun isRestoredChecked(todo: fireTodo){
        viewModelScope.launch {
            //val todo = todo.copy(todo.id, todo.title, isCompleted = false, timestamp = 0L)
            todo.completed = false
            todo.timestamp = 0L
            fireTodoRepository.updateTodo(todo)
        }
    }


    fun getATodoByID(id:String){
        viewModelScope.launch{
            when(val result = fireTodoRepository.getATodoByID(id)){
                is Result.Success -> _todo.value = result.data
                is Result.Error -> {}
            }

        }
    }

    fun addTodo(todo: fireTodo){
        viewModelScope.launch { fireTodoRepository.addTodo(todo) }
    }

    fun updateTodo(todo: fireTodo){
        viewModelScope.launch{ fireTodoRepository.updateTodo(todo) }

    }

    fun deleteTodo(todo: fireTodo){
        viewModelScope.launch { fireTodoRepository.deleteTodo(todo) }
    }
}