package com.gmail.devleedaeun.todolistapp.data.repository

import com.gmail.devleedaeun.todolistapp.data.dao.CategoryDao
import com.gmail.devleedaeun.todolistapp.data.entities.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {
    suspend fun addCategory(category: Category) = categoryDao.addCategory(category)
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
}