package com.belkanoid.waterecord.domain.repository

import com.belkanoid.waterecord.domain.entity.Record

interface RecordRepository {

    suspend fun getRecordList(): List<Record>

    suspend fun addRecord(record: Record)

    suspend fun editRecord(record: Record)

    suspend fun deleteRecord(recordId: Int)
}