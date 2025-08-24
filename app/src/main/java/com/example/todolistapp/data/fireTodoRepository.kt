package com.example.todolistapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

// Repository related with firestore
class fireTodoRepository(
    auth: FirebaseAuth,
    private val firestore: FirebaseFirestore) {

    // get current user email
    val uid = auth.currentUser?.email

    suspend fun addTodo(todo: fireTodo): Result<Unit>{
        try{
            if(uid != null){
                todo.id = System.currentTimeMillis().toString()
                todo.user = uid
                firestore.collection("todos").document(todo.id).set(todo).await()
                return Result.Success(Unit)
            }
            else return Result.Error(Exception("Not logged in"))
        }catch(e: Exception){
            return Result.Error(e)
        }
    }

    // Get a list of data for displaying
    fun getAllTodo(): Flow<List<fireTodo>> = callbackFlow {
        if(uid == null){
            trySend(emptyList())
            channel.close()
            return@callbackFlow
        }

        val subscription = firestore.collection("todos")
            .whereEqualTo("user", uid)
            .whereEqualTo("completed", false)
            .addSnapshotListener { querySnapshot, error ->
                if(error != null){
                    close(error)
                    return@addSnapshotListener
                }
                if(querySnapshot != null){
                    val todos = querySnapshot.documents.map{
                        document -> document.toObject(fireTodo::class.java)!!.copy(id = document.id)
                    }
                    trySend(todos)
                }

            }
        awaitClose { subscription.remove() }
    }


    suspend fun getCompletedTodo(): Flow<List<fireTodo>> = callbackFlow{
        if(uid == null){
            trySend(emptyList())
            channel.close()
            return@callbackFlow
        }

        val subscription = firestore.collection("todos")
            .whereEqualTo("user", uid)
            .whereEqualTo("completed", true)
            // display reversely according to timestamp
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, error ->
                if(error != null){
                    close(error)
                    return@addSnapshotListener
                }
                if(querySnapshot != null){
                    val todos = querySnapshot.documents.map{
                            document -> document.toObject(fireTodo::class.java)!!.copy(id= document.id)
                    }
                    trySend(todos)
                }

            }
        awaitClose { subscription.remove() }
    }

    // Get a single todo by id
    suspend fun getATodoByID(id:String): Result<fireTodo> {
        try{
            if(uid != null){
                val docSnapshot = firestore.collection("todos").document(id).get().await()
                val todo = docSnapshot.toObject(fireTodo::class.java)?.copy(id = docSnapshot.id)
                if(todo != null) return Result.Success(todo)
                else return Result.Error(Exception("No data"))
            }
            else return Result.Error(Exception("Not logged in"))
        }catch(e:Exception){
            return Result.Error(e)
        }
    }


    suspend fun updateTodo(todo: fireTodo):Result<Unit>{
        try{
            if(uid != null){
                todo.user = uid
                firestore.collection("todos").document(todo.id).set(todo).await()
                return Result.Success(Unit)
            }
            else return Result.Error(Exception("Not logged in"))
        }catch(e: Exception){
            return Result.Error(e)
        }
    }

    suspend fun deleteTodo(todo: fireTodo): Result<Unit>{
        try{
            if(uid != null){
                firestore.collection("todos").document(todo.id).delete().await()
                return Result.Success(Unit)
            }
            else return Result.Error(Exception("Not logged in"))
        }catch(e: Exception){
            return Result.Error(e)
        }
    }
}