package com.example.todolistapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// data in RoomDB
@Entity(tableName = "todo-table")
data class Todo(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo("todo-title")
    val title: String = "",

    @ColumnInfo("completed")
    var isCompleted: Boolean = false,           // whether you completed the work(todo)

    @ColumnInfo("timestamp")             // the time you finished the work(todo)
    var timestamp: Long = 0L

)
