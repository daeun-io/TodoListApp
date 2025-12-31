package com.gmail.devleedaeun.todolistapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gmail.devleedaeun.todolistapp.data.dao.CategoryDao
import com.gmail.devleedaeun.todolistapp.data.dao.TodoDao
import com.gmail.devleedaeun.todolistapp.data.entities.Category
import com.gmail.devleedaeun.todolistapp.data.entities.Todo
import com.gmail.devleedaeun.todolistapp.data.repository.CategoryRepository
import com.gmail.devleedaeun.todolistapp.data.repository.TodoRepository

@Database(entities = [Todo::class, Category::class], version = 10)
abstract class TodoDatabase: RoomDatabase(){
    abstract fun todoDao(): TodoDao
    abstract fun categoryDao(): CategoryDao
    companion object{
        private lateinit var database: TodoDatabase
        fun getTodoDatabase(context: Context) {
            database = Room.databaseBuilder(context, TodoDatabase::class.java, "todos")
                .fallbackToDestructiveMigration()
                .build()
        }
        val todoRepository by lazy{
            TodoRepository(todoDao = database.todoDao())
        }
        val categoryRepository by lazy{
            CategoryRepository(database.categoryDao())
        }
    }
}