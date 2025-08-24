package com.example.sendmoney.data.repository

import com.example.sendmoney.db.RequestDao
import com.example.sendmoney.db.RequestEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val requestDao: RequestDao) {
    suspend fun insertRequest(request: RequestEntity) {
        requestDao.insert(request)
    }

    fun getAllRequests(): Flow<List<RequestEntity>> {
        return requestDao.getAllRequests()
    }
}