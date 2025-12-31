package com.gmail.devleedaeun.todolistapp.ui.start

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gmail.devleedaeun.todolistapp.CategoryViewModel
import com.gmail.devleedaeun.todolistapp.R
import com.gmail.devleedaeun.todolistapp.data.entities.Category
import com.gmail.devleedaeun.todolistapp.databinding.ActivityStartBinding
import com.gmail.devleedaeun.todolistapp.ui.main.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.LoginUiMode
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {
    lateinit var binding: ActivityStartBinding
    val categoryViewModel = CategoryViewModel()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install(Load) SplashScreen
        installSplashScreen()
        // Set to basic theme after Splashscreen
        setTheme(R.style.Theme_TodoListApp)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get permission to use notification in this app
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)

        // Kakao Login
        val spf = getSharedPreferences("login", MODE_PRIVATE)
        binding.startKakaoLoginIb.setOnClickListener {
            UserApiClient.instance.loginWithKakaoAccount(this){ token, error ->
                if(error != null){
                    Toast.makeText(this, "Kakao Login Error", Toast.LENGTH_SHORT).show()
                }
                else if(token != null){
                    // Get user information in kakao
                    UserApiClient.instance.me{ user, error ->
                        if(error != null)
                            Log.e("Kakao/Info", "Failed to get user info", error)
                        else if(user != null){
                            spf.edit()
                                .putString("token", token.toString())
                                .putLong("user", user.id?:0)
                                .putString("name", user.kakaoAccount?.profile?.nickname)
                                .apply()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }
            }
        }
        // Start without login
        binding.startWithoutLoginTv.setOnClickListener {
            // Kakao logout if it is already login
            if(spf.getLong("user",0)!=0L){
                UserApiClient.instance.logout { error ->
                    if(error != null)
                        Log.e("Kakao/Logout", "Failed", error)
                    else{
                        Log.d("Kakao/Logout", "Succeed")
                        spf.edit()
                            .remove("token")
                            .remove("name")
                            .putLong("user", 0)
                            .apply()
                    }

                }
            }
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}