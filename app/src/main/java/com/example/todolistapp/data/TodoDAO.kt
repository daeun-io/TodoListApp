package com.example.todolistapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TodoDAO{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addTodo(todoEntity: Todo)

    // Search todo list which is not completed
    @Query("Select * from `todo-table` where completed = false")
    abstract fun getAllTodo(): Flow<List<Todo>>

    @Update
    abstract suspend fun updateTodo(todoEntity: Todo)

    @Delete
    abstract suspend fun deleteTodo(todoEntity: Todo)

    // get a single data by id
    @Query("Select * from `todo-table` where id =:id")
    abstract fun getATodoByID(id: Long): Flow<Todo>

    // Search todo list which is completed and order by timestamp reversely
    @Query("Select * from `todo-table` where completed = true ORDER BY timestamp DESC")
    abstract fun getCompletedTodo(): Flow<List<Todo>>

}