package com.gmail.devleedaeun.todolistapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gmail.devleedaeun.todolistapp.data.entities.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert
    suspend fun addTodo(todo: Todo)
    @Delete
    suspend fun deleteTodo(todo: Todo)
    @Update
    suspend fun updateTodo(todo: Todo)
    @Query("SELECT * FROM `todo` WHERE isCompleted = false")
    fun getAllTodo(): Flow<List<Todo>>
    @Query("SELECT * FROM `todo` WHERE isCompleted = true")
    fun getCompletedTodos(): Flow<List<Todo>>
    @Query("SELECT * FROM `todo` WHERE isImportant = true AND isCompleted = false")
    fun getImportantTodos(): Flow<List<Todo>>
    @Query("SELECT * FROM `todo` WHERE category =:name AND isCompleted = false")
    fun getTodosInName(name: String): Flow<List<Todo>>
    @Query("DELETE FROM `todo` WHERE category =:name")
    suspend fun deleteTodosInName(name: String)
}