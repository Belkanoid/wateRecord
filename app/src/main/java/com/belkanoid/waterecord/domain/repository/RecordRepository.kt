package com.belkanoid.waterecord.domain.repository

import androidx.lifecycle.LiveData
import com.belkanoid.waterecord.domain.entity.Record

interface RecordRepository {

    fun getRecordList(): LiveData<List<Record>>

    suspend fun addRecord(record: Record)

    suspend fun editRecord(record: Record)

    suspend fun deleteRecord(recordId: Int)
}