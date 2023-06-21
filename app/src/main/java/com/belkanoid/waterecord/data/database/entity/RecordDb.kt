package com.belkanoid.waterecord.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record_item")
data class RecordDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val value: String,
    val date: Long,
    val isHot: Boolean
)
