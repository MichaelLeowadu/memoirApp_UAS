package com.android.memoirapp.common

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.memoirapp.data.JournalData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JournalViewModel : ViewModel() {

    private val _journals = MutableStateFlow<List<JournalData>>(emptyList())
    val journals: StateFlow<List<JournalData>> = _journals

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var listenerRegistration: ListenerRegistration? = null

    init {
        observeJournals()
    }

    private fun observeJournals() {
        _loading.value = true
        val currentUserId = auth.currentUser?.uid ?: return

        listenerRegistration = db.collection("Journal")
            .whereEqualTo("userId", currentUserId)
            .addSnapshotListener { snapshot, error ->
                _loading.value = false
                if (error != null) {
                    Log.e("Firestore", "Error listening to journals", error)
                    return@addSnapshotListener
                }

                val journalList = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(JournalData::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }?.sortedByDescending { it.timeStamp } ?: emptyList()

                _journals.value = journalList
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}