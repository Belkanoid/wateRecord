package com.belkanoid.waterecord.domain.entity

data class Record(
    val value: String,
    val date: Long,
    val isHot: Boolean,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}

