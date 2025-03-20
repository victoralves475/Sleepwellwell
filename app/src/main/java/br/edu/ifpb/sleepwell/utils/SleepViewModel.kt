package br.edu.ifpb.sleepwell.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifpb.sleepwell.data.model.SleepRecord
import br.edu.ifpb.sleepwell.data.repository.SleepRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class SleepViewModel(
    private val repository: SleepRepository = SleepRepository()
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkYesterdaySleepRecord(userId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val yesterday = LocalDate.now().minusDays(1)
            val zone = ZoneId.systemDefault()
            val startOfDay = yesterday.atStartOfDay(zone).toEpochSecond() * 1000
            val endOfDay = yesterday.plusDays(1).atStartOfDay(zone).toEpochSecond() * 1000 - 1
            val record = repository.getSleepRecordForDate(userId, startOfDay, endOfDay)
            onResult(record != null)
        }
    }

    fun addSleepRecord(record: SleepRecord, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.addSleepRecord(record)
            onResult(success)
        }
    }

    fun computeSleepQualityPercentage(userId: String, onResult: (Int) -> Unit) {
        viewModelScope.launch {
            val records = repository.getSleepRecordsByUser(userId)
            val total = records.size
            val goodCount = records.count { it.quality == true }
            val percentage = if (total > 0) (goodCount * 100) / total else 0
            onResult(percentage)
        }
    }
    fun fetchSleepRecords(userId: String, onResult: (List<SleepRecord>) -> Unit) {
        viewModelScope.launch {
            val records = repository.getSleepRecordsByUser(userId)
            onResult(records)
        }
    }

}
