package com.belkanoid.waterecord.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record_item")
data class RecordDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val value: String,
    val date: Long,
    val isHot: Boolean,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordDb

        if (id != other.id) return false
        if (value != other.value) return false
        if (date != other.date) return false
        if (isHot != other.isHot) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + value.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + isHot.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}
