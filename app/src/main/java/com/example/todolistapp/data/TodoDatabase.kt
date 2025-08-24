package com.example.todolistapp.data

import androidx.room.migration.AutoMigrationSpec
import androidx.room.Database
import androidx.room.RoomDatabase

import androidx.room.AutoMigration
//class MyAutoMigration : AutoMigrationSpec

// database of RoomDB
@Database(
    entities = [Todo::class],
    version = 2,
    exportSchema = true,
   /*autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = MyAutoMigration::class)
    ]
    */
)

abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDAO
}
