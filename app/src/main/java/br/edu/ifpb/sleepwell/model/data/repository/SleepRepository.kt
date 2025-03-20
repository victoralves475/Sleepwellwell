package br.edu.ifpb.sleepwell.data.repository

import br.edu.ifpb.sleepwell.data.model.SleepRecord
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SleepRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun addSleepRecord(record: SleepRecord): Boolean {
        return try {
            val docRef = firestore.collection("sleep_records").document()
            val newRecord = record.copy(recordId = docRef.id)
            docRef.set(newRecord).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getSleepRecordForDate(userId: String, startTime: Long, endTime: Long): SleepRecord? {
        return try {
            val querySnapshot = firestore.collection("sleep_records")
                .whereEqualTo("userId", userId)
                .whereGreaterThanOrEqualTo("date", startTime)
                .whereLessThanOrEqualTo("date", endTime)
                .get().await()
            querySnapshot.documents.firstOrNull()?.toObject(SleepRecord::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getSleepRecordsByUser(userId: String): List<SleepRecord> {
        return try {
            val querySnapshot = firestore.collection("sleep_records")
                .whereEqualTo("userId", userId)
                .get().await()
            querySnapshot.documents.mapNotNull { it.toObject(SleepRecord::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
