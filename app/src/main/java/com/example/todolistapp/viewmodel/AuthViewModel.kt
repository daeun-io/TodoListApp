package com.example.todolistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.Injection
import com.example.todolistapp.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val userRepository: UserRepository
    private val auth = FirebaseAuth.getInstance()

    init{
        userRepository = UserRepository(
            auth = auth,
            firestore = Injection.instance()
        )
    }

    val loginUser: FirebaseUser? get() = auth.currentUser
    fun signUp(email:String, password: String, firstName: String, lastName: String){
        viewModelScope.launch{
            userRepository.signUp(email, password, firstName, lastName)
        }
    }

    fun login(email: String, password: String){
        viewModelScope.launch{
            userRepository.login(email, password)
        }
    }


    fun signOut(){
        viewModelScope.launch{
            userRepository.signOut()
        }
    }


}