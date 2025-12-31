package com.gmail.devleedaeun.todolistapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gmail.devleedaeun.todolistapp.ui.main.category.AddCategoryDialog
import com.gmail.devleedaeun.todolistapp.ui.menu.MenuActivity
import com.gmail.devleedaeun.todolistapp.R
import com.gmail.devleedaeun.todolistapp.databinding.ActivityMainBinding
import com.gmail.devleedaeun.todolistapp.ui.addTodo.AddTodoActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set navController and its view
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.mainBnv.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{ _, destination, bundle ->
            when(destination.id){
                R.id.categoryFragment ->{
                    binding.todoMenuIb.visibility = View.GONE
                    binding.categoryBackIb.visibility = View.VISIBLE
                    binding.categoryMenuTv.visibility = View.VISIBLE
                    binding.todoTv.text = "CATEGORIES"
                    binding.todoAddFab.visibility = View.GONE
                }
                else -> {
                    binding.todoMenuIb.visibility = View.VISIBLE
                    binding.categoryBackIb.visibility = View.GONE
                    binding.categoryMenuTv.visibility = View.GONE
                    binding.todoTv.text = "All"
                    binding.todoAddFab.visibility = View.VISIBLE
                }
            }
        }

        binding.todoMenuIb.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        // Change text depending on selected category(TodoFragment)
        val spf = getSharedPreferences("todo_bar", MODE_PRIVATE)
        binding.todoTv.text = spf.getString("selected", "All")
        binding.todoTv.textSize = 24f

        binding.categoryBackIb.setOnClickListener { onBackPressed() }

        // Show dialog to add a category(CategoryFragment)
        binding.categoryMenuTv.setOnClickListener {
            val dialog = AddCategoryDialog(this)
            dialog.show()
        }
        // Start Activity to add a todo(TodoFragment)
        binding.todoAddFab.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivity(intent)
        }
    }
}
