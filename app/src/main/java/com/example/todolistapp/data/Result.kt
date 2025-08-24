package com.example.todolistapp.data

// result of the process in firestore
sealed class Result<out T> {
    data class Success<out T> (val data: T): Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()
}