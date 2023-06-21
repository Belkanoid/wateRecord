package com.belkanoid.waterecord.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.belkanoid.waterecord.data.database.entity.RecordDb

@Dao
interface RecordDao {

    @Query("SELECT * FROM record_item")
    fun getRecordIdList() : List<RecordDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecordIdItem(shoppingItem: RecordDb)

    @Query("DELETE FROM record_item WHERE id=:recordId")
    suspend fun deleteRecordIdItem(recordId : Int)

    @Query("SELECT * FROM record_item WHERE id=:recordId LIMIT 1")
    suspend fun getRecordIdItem(recordId: Int) : RecordDb
}