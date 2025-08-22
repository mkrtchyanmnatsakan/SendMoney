package com.example.sendmoney.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface RequestDao {
    @Insert
    suspend fun insert(request: RequestEntity)

    @Query("SELECT * FROM RequestEntity")
    fun getAllRequests(): Flow<List<RequestEntity>>
}