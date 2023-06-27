package com.belkanoid.waterecord.presentation.recordList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.waterecord.domain.entity.Record
import com.belkanoid.waterecord.domain.repository.RecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecordListViewModel @Inject constructor(
    private val repository: RecordRepository
): ViewModel() {


    val list = repository.getRecordList()

    fun deleteRecord(recordId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRecord(recordId)
        }
    }

    fun addRecord(record: Record) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRecord(record)
        }
    }

    fun editRecord(record: Record) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editRecord(record)
        }
    }
}