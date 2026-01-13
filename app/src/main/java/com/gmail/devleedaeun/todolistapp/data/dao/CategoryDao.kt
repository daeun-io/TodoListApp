package com.gmail.devleedaeun.todolistapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gmail.devleedaeun.todolistapp.data.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun addCategory(category: Category)
    @Delete
    suspend fun deleteCategory(category: Category)
    @Update
    suspend fun updateCategory(category: Category)
    @Query("SELECT * from `category`")
    fun getAllCategories(): Flow<List<Category>>
}