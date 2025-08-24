package com.example.todolistapp.data

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private suspend fun saveUserToFirestore(user: User){
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun signUp(email: String, password: String, firstName:String, lastName: String): Result<Boolean>{
        try {
            // create user in firebase Authentication
            auth.createUserWithEmailAndPassword(email, password).await()

            // create user using User class
            val user = User(firstName, lastName, email)
            // save it in firestore
            saveUserToFirestore(user)

            return Result.Success(true)
        }catch (e: Exception){
            return Result.Error(e)
        }
    }

    fun login(email:String, password: String): Result<Boolean>{
        try{
            auth.signInWithEmailAndPassword(email,password)
            return Result.Success(true)
        }catch(e: Exception){
            return Result.Error(e)
        }
    }

    fun signOut(): Result<Boolean>{
        try{
            auth.signOut()
            return Result.Success(true)
        }catch(e: Exception){return Result.Error(e)}
    }

}