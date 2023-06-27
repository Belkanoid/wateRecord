package com.belkanoid.waterecord.presentation.recordList.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belkanoid.waterecord.R
import com.belkanoid.waterecord.domain.entity.Record
import com.belkanoid.waterecord.presentation.toBitmap
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.Date

class RecordItemHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val value : TextView = itemView.findViewById(R.id.record_value)
    private val date : TextView = itemView.findViewById(R.id.record_date)
    private val type : TextView = itemView.findViewById(R.id.record_type)
    private val diff : TextView = itemView.findViewById(R.id.record_difference)
    private val image : ImageView = itemView.findViewById(R.id.record_image_value)

    fun bind(record: Record, diff: String) {
        value.text = record.value
        date.text = convertLongToTime(record.date)
        type.text = if(record.isHot) "гораячая" else "холодная"
        type.setTextColor(
            if(record.isHot) Color.parseColor("#AF2300") else Color.parseColor("#008295")
        )
        image.setImageBitmap(record.image.toBitmap())
        this.diff.text = try {
            "+${abs(record.value.toLong() - diff.toLong())}"
        }catch (e: Exception) {
            "+0"
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd MMM yyyy")
        return format.format(date)
    }

}