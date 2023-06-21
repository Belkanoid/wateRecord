package com.belkanoid.waterecord.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.belkanoid.waterecord.data.database.dao.RecordDao
import com.belkanoid.waterecord.data.database.entity.RecordDb

@Database(entities = [RecordDb::class], version = 1, exportSchema = false)
abstract class RecordDatabase: RoomDatabase() {

    abstract fun recordDao() : RecordDao

    companion object {
        private var INSTANCE: RecordDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "record_list.db"

        fun getInstance(application: Application): RecordDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
            }

            val db = Room.databaseBuilder(
                application,
                RecordDatabase::class.java,
                DB_NAME
            )
                .build()

            INSTANCE = db
            return db
        }
    }

}