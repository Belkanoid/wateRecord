package com.belkanoid.waterecord.presentation.recordList.adapter

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import com.belkanoid.waterecord.R
import com.belkanoid.waterecord.domain.entity.Record

class RecordListAdapter: ListAdapter<Record, RecordItemHolder>(RecordDiffUtil()) {

    var onClickRecordItemListener: ((Record) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordItemHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.record_list_item, parent, false)

        return RecordItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecordItemHolder, position: Int) {
        Log.d("LOLi", position.toString())
        val item = currentList[position]
        val diff = if (position == 0) "" else currentList[position-1].value
        holder.apply {
            bind(item, diff)
            itemView.setOnClickListener {
                it.findViewById<ImageView>(R.id.record_image_value).apply {
                    visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
            }
            itemView.setOnLongClickListener{
                onClickRecordItemListener?.invoke(item)
                true
            }

        }
    }
}