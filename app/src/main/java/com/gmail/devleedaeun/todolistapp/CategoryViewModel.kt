package com.gmail.devleedaeun.todolistapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.devleedaeun.todolistapp.data.entities.Category
import com.gmail.devleedaeun.todolistapp.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryRepository: CategoryRepository = TodoDatabase.categoryRepository
): ViewModel() {
    // List of categories
    private val _categoryList: MutableStateFlow<List<Category>?> = MutableStateFlow(null)
    val categoryList = _categoryList.asStateFlow()
    // List of categories' name
    private val _nameList: MutableStateFlow<List<String>?> = MutableStateFlow(null)
    val nameList = _nameList.asStateFlow()

    // RoomDB(My Android)
    fun addCategory(category: Category){
        viewModelScope.launch {
            categoryRepository.addCategory(category)
        }
    }
    fun updateCategory(category: Category){
        viewModelScope.launch {
            categoryRepository.updateCategory(category)
        }
    }
    fun deleteCategory(category: Category){
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
        }
    }
    fun getAllCategories(){
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                _categoryList.value = categories
                _nameList.value = categories.map{
                    it.name
                }
            }
        }
    }
}