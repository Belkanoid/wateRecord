package com.belkanoid.waterecord.data.repository

import com.belkanoid.waterecord.data.database.dao.RecordDao
import com.belkanoid.waterecord.data.database.entity.RecordDb
import com.belkanoid.waterecord.domain.repository.RecordRepository
import com.belkanoid.waterecord.domain.entity.Record
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val recordDao: RecordDao,
): RecordRepository {
    override suspend fun getRecordList(): List<Record> {
        return recordDao.getRecordIdList().map { it.toRecord() }
    }

    override suspend fun addRecord(record: Record) {
        recordDao.addRecordIdItem(RecordDb(1, "sdfasd", 2343L, false))
    }

    override suspend fun editRecord(record: Record) {
        addRecord(record)
    }

    override suspend fun deleteRecord(recordId: Int) {
        recordDao.deleteRecordIdItem(recordId)
    }

    private fun RecordDb.toRecord() = Record(id = this.id, value = this.value, date =  this.date, isHot = this.isHot)
    private fun Record.toRecordDb() = RecordDb(id = this.id, value = this.value, date =  this.date, isHot = this.isHot)
}