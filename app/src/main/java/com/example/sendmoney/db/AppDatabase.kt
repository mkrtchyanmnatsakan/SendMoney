package com.example.sendmoney.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RequestEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun requestDao(): RequestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sendmoney_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}