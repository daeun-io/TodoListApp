package com.gmail.devleedaeun.todolistapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("todo")
data class Todo(
    var title: String? = null,
    var time: String? = null,
    var category: String? = null,
    var storage: String? = "My Android",
    var isImportant: Boolean = false,
    var isCompleted: Boolean = false,
    var userId: Long = 0,
    @PrimaryKey(autoGenerate = false) var id: Long = 0
)