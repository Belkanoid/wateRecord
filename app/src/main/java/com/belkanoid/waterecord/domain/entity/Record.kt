package com.belkanoid.waterecord.domain.entity

data class Record(
    val value: String,
    val date: Long,
    val isHot: Boolean,
    val image: ByteArray,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Record

        if (value != other.value) return false
        if (date != other.date) return false
        if (isHot != other.isHot) return false
        if (!image.contentEquals(other.image)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + isHot.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + id
        return result
    }
}

