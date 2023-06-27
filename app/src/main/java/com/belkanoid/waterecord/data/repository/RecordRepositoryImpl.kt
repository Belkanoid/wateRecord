package com.belkanoid.waterecord.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.map
import com.belkanoid.waterecord.data.database.dao.RecordDao
import com.belkanoid.waterecord.data.database.entity.RecordDb
import com.belkanoid.waterecord.domain.repository.RecordRepository
import com.belkanoid.waterecord.domain.entity.Record
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val recordDao: RecordDao,
): RecordRepository {
    override fun getRecordList(): LiveData<List<Record>> = recordDao.getRecordIdList().map { list ->
        list.map { it.toRecord() }
    }

    override suspend fun addRecord(record: Record) {
        recordDao.addRecordIdItem(record.toRecordDb())
    }

    override suspend fun editRecord(record: Record) {
        addRecord(record)
    }

    override suspend fun deleteRecord(recordId: Int) {
        recordDao.deleteRecordIdItem(recordId)
    }

    private fun RecordDb.toRecord() = Record(id = this.id, value = this.value, date =  this.date, isHot = this.isHot, image = this.image)
    private fun Record.toRecordDb() = RecordDb(id = this.id, value = this.value, date =  this.date, isHot = this.isHot, image = this.image)
}