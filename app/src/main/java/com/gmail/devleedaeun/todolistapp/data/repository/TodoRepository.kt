package com.gmail.devleedaeun.todolistapp.data.repository

import android.util.Log
import com.gmail.devleedaeun.todolistapp.data.dao.TodoDao
import com.gmail.devleedaeun.todolistapp.data.entities.Todo
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TodoRepository(private val todoDao: TodoDao){

    // RoomDB(My Android)
    suspend fun addTodo(todo: Todo) = todoDao.addTodo(todo)
    suspend fun deleteTodo(todo: Todo) = todoDao.deleteTodo(todo)
    suspend fun updateTodo(todo: Todo) = todoDao.updateTodo(todo)
    fun getAllTodo():Flow<List<Todo>> = todoDao.getAllTodo()
    fun getCompletedTodos():Flow<List<Todo>> = todoDao.getCompletedTodos()
    fun getImportantTodos():Flow<List<Todo>> = todoDao.getImportantTodos()
    fun getTodosInName(name:String):Flow<List<Todo>> = todoDao.getTodosInName(name)
    suspend fun deleteTodosInName(name:String)= todoDao.deleteTodosInName(name)

    // Firebase(server)
    fun addTodoAtFirebase(todo: Todo, user:Long, id: Long){
        val todoRef = Firebase.database.getReference("todos").child(user.toString())
        todoRef.child(id.toString()).setValue(todo)
    }
    fun updateTodoAtFirebase(todo: Todo, user: Long, id: Long){
        val todoRef = Firebase.database.getReference("todos").child(user.toString())
        todoRef.child(id.toString()).setValue(todo)
    }
    fun deleteTodoAtFirebase(user: Long, id:Long){
        val todoRef = Firebase.database.getReference("todos").child(user.toString())
        todoRef.child(id.toString()).removeValue()
    }
    fun getTodoFromFirebase(user:Long): Flow<List<Todo>> = callbackFlow {
        val todoRef = Firebase.database.getReference("todos").child(user.toString())
        val todoList = mutableListOf<Todo>()
        val listener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(child in snapshot.children){
                    val todo = child.getValue(Todo::class.java)
                    if(todo?.isCompleted == false){
                        todoList.add(todo)
                    }
                }
                trySend(todoList as List<Todo>)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
                Log.d("Firebase/Error", "Fail to load data")
            }
        }
        todoRef.addValueEventListener(listener)
        awaitClose { todoRef.removeEventListener(listener) }
    }
    fun getCompletedTodoFromFirebase(user:Long): Flow<List<Todo>> = callbackFlow {
        val todoRef = Firebase.database.getReference("todos").child(user.toString())
        val todoList = mutableListOf<Todo>()
        val listener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(child in snapshot.children){
                    val todo = child.getValue(Todo::class.java)
                    if(todo?.isCompleted == true){
                        todoList.add(todo)
                    }
                }
                trySend(todoList as List<Todo>)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
                Log.d("Firebase/Error", "Fail to load data")
            }
        }
        todoRef.addValueEventListener(listener)
        awaitClose { todoRef.removeEventListener(listener) }
    }
    fun getImportantTodoFromFirebase(user:Long): Flow<List<Todo>> = callbackFlow {
        val todoRef = Firebase.database.getReference("todos").child(user.toString())
        val todoList = mutableListOf<Todo>()
        val listener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(child in snapshot.children){
                    val todo = child.getValue(Todo::class.java)
                    if(todo?.isImportant == true){
                        todoList.add(todo)
                    }
                }
                trySend(todoList as List<Todo>)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
                Log.d("Firebase/Error", "Fail to load data")
            }
        }
        todoRef.addValueEventListener(listener)
        awaitClose { todoRef.removeEventListener(listener) }
    }
    fun getTodoFromFirebaseInNames(user:Long, name: String): Flow<List<Todo>> = callbackFlow {
        val todoRef = Firebase.database.getReference("todos").child(user.toString())
        val todoList = mutableListOf<Todo>()
        val listener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(child in snapshot.children){
                    val todo = child.getValue(Todo::class.java)
                    if(todo?.category == name){
                        todoList.add(todo)
                    }
                }
                trySend(todoList as List<Todo>)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
                Log.d("Firebase/Error", "Fail to load data")
            }
        }
        todoRef.addValueEventListener(listener)
        awaitClose { todoRef.removeEventListener(listener) }
    }
    fun deleteTodosFromFirebaseInName(user:Long, name:String){
        val todoRef = Firebase.database.getReference("todos").child(user.toString())
        val listener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(child in snapshot.children){
                    val todo = child.getValue(Todo::class.java)
                    if(todo?.category == name){
                        todoRef.child(todo.id.toString()).removeValue()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase/Error","Failed to Delete Todos")
            }

        }
        todoRef.addListenerForSingleValueEvent(listener)
    }
}