package com.gmail.devleedaeun.todolistapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gmail.devleedaeun.todolistapp.R

@Entity("category")
data class Category(
    var name: String = "",
    var iconColor: Int = R.color.categoryIconDefault,
    var userId:Long=0L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
)