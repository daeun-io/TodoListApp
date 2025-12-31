package com.gmail.devleedaeun.todolistapp.ui.main.todo

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.devleedaeun.todolistapp.R
import com.gmail.devleedaeun.todolistapp.TodoViewModel
import com.gmail.devleedaeun.todolistapp.data.entities.Todo
import com.gmail.devleedaeun.todolistapp.databinding.FragmentTodoBinding
import com.gmail.devleedaeun.todolistapp.ui.addTodo.AddTodoActivity
import com.gmail.devleedaeun.todolistapp.ui.main.todo.TodoRVAdapter.ViewHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TodoFragment: Fragment() {
    lateinit var binding: FragmentTodoBinding
    private val todoViewModel = TodoViewModel()
    private lateinit var todoList: List<Todo>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todoList = todoViewModel.todoList.value
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        val loginSpf = activity?.getSharedPreferences("login", MODE_PRIVATE)
        val storageSpf = activity?.getSharedPreferences("todo", MODE_PRIVATE)

        val todoAdapter = TodoRVAdapter(activity, todoList)
        binding.todoListRv.adapter = todoAdapter
        binding.todoListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        todoAdapter.setMyItemClickListener(object : TodoRVAdapter.MyItemClickListener{

            override fun moveToAddActivity(position: Int) {
                todoAdapter.moveToAddTodoActivity(position)
            }

            override fun checkCompleted(holder: ViewHolder, position: Int) {
                todoAdapter.checkTodoCompleted(holder, position)
            }
        })

        // Get Todos depending on their storage
        when(loginSpf?.getLong("user",0)){
            0L -> storageSpf?.edit()?.putString("storage", "android")?.apply()
            else -> {
                when(storageSpf?.getString("storage", "android")){
                    "android" -> {
                        binding.androidIb.setImageResource(R.drawable.baseline_check_circle_24)
                        binding.firebaseIb.setImageResource(R.drawable.outline_circle_24)
                    }
                    "firebase" -> {
                        binding.androidIb.setImageResource(R.drawable.outline_circle_24)
                        binding.firebaseIb.setImageResource(R.drawable.baseline_check_circle_24)
                    }
                }
                binding.androidIb.setOnClickListener {
                    binding.androidIb.setImageResource(R.drawable.baseline_check_circle_24)
                    binding.firebaseIb.setImageResource(R.drawable.outline_circle_24)
                    storageSpf?.edit()?.putString("storage", "android")?.apply()
                    observeTodoData(todoAdapter)
                }
                binding.firebaseIb.setOnClickListener {
                    binding.androidIb.setImageResource(R.drawable.outline_circle_24)
                    binding.firebaseIb.setImageResource(R.drawable.baseline_check_circle_24)
                    storageSpf?.edit()?.putString("storage", "firebase")?.apply()
                    observeTodoData(todoAdapter)
                }
            }
        }

        observeTodoData(todoAdapter)

        return binding.root
    }

    // Get Todo data depending on category and storage
    private fun observeTodoData(adapter: TodoRVAdapter){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                val menuSpf = activity?.getSharedPreferences("todo_bar", MODE_PRIVATE)
                val selectedCategory = menuSpf?.getString("selected", "All")
                val loginSpf = activity?.getSharedPreferences("login", MODE_PRIVATE)
                val user = loginSpf?.getLong("user", 0)
                val storageSpf = activity?.getSharedPreferences("todo", MODE_PRIVATE)
                val storage = storageSpf?.getString("storage", "android")

                when(storage){
                    "android" -> {
                        when(selectedCategory){
                            "All" -> {
                                todoViewModel.getAllTodo()
                                todoViewModel.todoList.collect{ adapter.loadTodo(it) }
                            }
                            "Completed" -> {
                                todoViewModel.getCompletedTodos()
                                todoViewModel.todoList.collect { adapter.loadTodo(it)}
                            }
                            "Important" -> {
                                todoViewModel.getImportantTodos()
                                todoViewModel.todoList.collect{ adapter.loadTodo(it) }
                            }
                            else -> {
                                todoViewModel.getTodosInName(selectedCategory ?: "All")
                                todoViewModel.todoList.collect{adapter.loadTodo(it)}
                            }
                        }
                    }
                    "firebase" -> {
                        when(selectedCategory){
                            "All" -> {
                                todoViewModel.getTodoFromFirebase(user ?: 0)
                                todoViewModel.todoList.collect { adapter.loadTodo(it) }
                            }
                            "Completed" -> {
                                todoViewModel.getCompletedTodoFromFirebase(user ?: 0)
                                todoViewModel.todoList.collect{adapter.loadTodo(it)}
                            }
                            "Important" -> {
                                todoViewModel.getImportantTodoFromFirebase(user ?: 0)
                                todoViewModel.todoList.collect{adapter.loadTodo(it)}
                            }
                            else -> {
                                todoViewModel.getTodoFromFirebaseInNames(user?:0, selectedCategory?:"All")
                                todoViewModel.todoList.collect { adapter.loadTodo(it) }
                            }
                        }
                    }
                }
            }
        }
    }
}