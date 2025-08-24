package com.example.todolistapp.data

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDAO: TodoDAO) {

    suspend fun addTodo(todo: Todo){
        todoDAO.addTodo(todo)
    }

    fun getAllTodo(): Flow<List<Todo>> = todoDAO.getAllTodo()
    fun getATodoByID(id:Long): Flow<Todo> = todoDAO.getATodoByID(id)
    fun getCompletedTodo(): Flow<List<Todo>> = todoDAO.getCompletedTodo()

    suspend fun updateTodo(todo: Todo){
        todoDAO.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: Todo){
        todoDAO.deleteTodo(todo)
    }


}