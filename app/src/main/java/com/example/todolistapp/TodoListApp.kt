package com.example.todolistapp

import android.app.Application
import com.example.todolistapp.data.Graph

class TodoListApp: Application() {
    override fun onCreate(){
        super.onCreate()
        // Build RoomDB when app starts
        Graph.provide(this)
    }
}