package com.example.todolistapp.data

import android.content.Context
import androidx.room.Room

// builder of RoomDB
object Graph {
    lateinit var database: TodoDatabase

    val todoRepository by lazy{
        TodoRepository(todoDAO = database.todoDao())
    }

    fun provide(context: Context){
        database = Room.databaseBuilder(context,
            TodoDatabase::class.java, "todolist.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}