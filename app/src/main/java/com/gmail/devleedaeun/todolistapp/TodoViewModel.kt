package com.gmail.devleedaeun.todolistapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.devleedaeun.todolistapp.data.entities.Todo
import com.gmail.devleedaeun.todolistapp.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(
    private val todoRepository: TodoRepository = TodoDatabase.todoRepository
): ViewModel() {
    private val _todoList = MutableStateFlow(emptyList<Todo>())
    //private val _todoList = MutableLiveData<List<Todo>>(emptyList())
    val todoList: StateFlow<List<Todo>> = _todoList.asStateFlow()
    //val todoList: LiveData<List<Todo>> get() = _todoList

    // RoomDB(My Android)
    fun addTodo(todo: Todo){viewModelScope.launch{
        todoRepository.addTodo(todo)}
    }
    fun updateTodo(todo:Todo){
        viewModelScope.launch{todoRepository.updateTodo(todo)}
    }
    fun deleteTodo(todo:Todo){
        viewModelScope.launch{todoRepository.deleteTodo(todo)}
    }
    fun getAllTodo(){
        viewModelScope.launch{
            todoRepository.getAllTodo().collect {
                _todoList.value = it
            }
        }
    }
    fun getCompletedTodos(){
        viewModelScope.launch {
            todoRepository.getCompletedTodos().collect {
                _todoList.value = it
            }
        }
    }
    fun getImportantTodos(){
        viewModelScope.launch {
            todoRepository.getImportantTodos().collect {
                _todoList.value = it
            }
        }
    }
    fun getTodosInName(name:String){
        viewModelScope.launch {
            todoRepository.getTodosInName(name).collect {
                _todoList.value = it
            }
        }
    }
    fun deleteTodosInName(name:String){
        viewModelScope.launch {
            todoRepository.deleteTodosInName(name)
        }
    }

    // Firebase(Server)
    fun addTodoAtFirebase(todo: Todo, user: Long, id:Long){
        viewModelScope.launch{
            todoRepository.addTodoAtFirebase(todo, user, id)
        }
    }
    fun updateTodoAtFirebase(todo: Todo, user: Long, id:Long){
        viewModelScope.launch {
            todoRepository.updateTodoAtFirebase(todo, user, id)
        }
    }
    fun deleteTodoAtFirebase(user:Long, id:Long){
        viewModelScope.launch { todoRepository.deleteTodoAtFirebase(user, id) }
    }
    fun deleteTodosFromFirebaseInName(user:Long, name:String){
        viewModelScope.launch { todoRepository.deleteTodosFromFirebaseInName(user, name) }
    }
    fun getTodoFromFirebase(user: Long){
        viewModelScope.launch { todoRepository.getTodoFromFirebase(user).collect { _todoList.value = it } }
    }
    fun getCompletedTodoFromFirebase(user: Long){
        viewModelScope.launch { todoRepository.getCompletedTodoFromFirebase(user).collect { _todoList.value = it } }
    }
    fun getImportantTodoFromFirebase(user: Long){
        viewModelScope.launch { todoRepository.getImportantTodoFromFirebase(user).collect { _todoList.value = it} }
    }
    fun getTodoFromFirebaseInNames(user:Long, name: String){
        viewModelScope.launch { todoRepository.getTodoFromFirebaseInNames(user, name).collect { _todoList.value = it } }
    }
}