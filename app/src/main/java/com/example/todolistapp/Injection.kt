package com.example.todolistapp

import com.google.firebase.firestore.FirebaseFirestore

// get instance of firestore
object Injection {
    private val instance: FirebaseFirestore by lazy{
        FirebaseFirestore.getInstance()
    }

    fun instance(): FirebaseFirestore{
        return instance
    }
}