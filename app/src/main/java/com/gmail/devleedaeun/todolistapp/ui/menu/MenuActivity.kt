package com.gmail.devleedaeun.todolistapp.ui.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.devleedaeun.todolistapp.CategoryViewModel
import com.gmail.devleedaeun.todolistapp.databinding.ActivityMenuBinding
import com.gmail.devleedaeun.todolistapp.ui.main.MainActivity
import com.gmail.devleedaeun.todolistapp.ui.start.StartActivity
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch
class MenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding
    private val categoryViewModel = CategoryViewModel()
    private var nameList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spf = getSharedPreferences("login", MODE_PRIVATE)
        val userName = spf.getString("name", "USER")
        binding.menuTv.text = "HELLO $userName"
        // Kakao Logout
        binding.menuTv.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if(error != null)
                    Log.e("Kakao/Logout", "Logout Failed, Token Revocation occurred in SDK", error)
                else{
                    Log.d("Kakao/Logout", "Logout Succeed, Token Revocation occurred in SDK")
                    spf.edit().remove("token").remove("user").remove("name").apply()
                    Toast.makeText(this, "Kakao Logout", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, StartActivity::class.java))
                }
            }
        }

        // Add default menu items
        if (nameList.isEmpty())
            nameList.add("All"); nameList.add("Important"); nameList.add("Completed")

        val todoBarRVAdapter = TodoBarRVAdapter(this, nameList)
        binding.menuRv.adapter = todoBarRVAdapter
        binding.menuRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todoBarRVAdapter.setMyItemClickListener(object : TodoBarRVAdapter.MyItemClickListener {
            override fun saveSelectedName(position: Int) {
                todoBarRVAdapter.saveSelectedName(position)
            }
        })
        getCategoryNames(todoBarRVAdapter)

        binding.menuDismissView.setOnClickListener { onBackPressed() }

    }

    private fun getCategoryNames(adapter: TodoBarRVAdapter){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.getAllCategories()
                categoryViewModel.nameList.collect {
                    if(it?.isNotEmpty() == true){
                        for(name in it)
                            if(!nameList.contains(name)) nameList.add(name)
                        for(i in 3..nameList.size){
                            if(nameList.size == (it.size + 3)) break
                            if(!it.contains(nameList[i])) nameList.removeAt(i)
                        }
                        adapter.loadCategory(nameList)
                    }
                }
            }
        }
    }
}