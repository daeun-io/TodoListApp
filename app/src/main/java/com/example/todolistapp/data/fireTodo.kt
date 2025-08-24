package com.example.todolistapp.data

import com.google.firebase.firestore.PropertyName

// data in firestore
data class fireTodo(

    var id: String = "0",
    val title: String = "",
    var user: String = "",

    //@get:PropertyName("completed")
    //@set:PropertyName("completed")
    var completed: Boolean = false,         // whether you completed the work(todo)

    var timestamp: Long = 0L                // the time you finished the work(todo)
)