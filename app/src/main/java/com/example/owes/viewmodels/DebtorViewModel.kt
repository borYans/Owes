package com.example.owes.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.example.owes.data.db.Debtor
import com.example.owes.data.db.DebtorDatabase
import com.example.owes.repository.DebtorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebtorViewModel @Inject constructor(
    val repository: DebtorRepository
): ViewModel() {

    private var _payments: MutableLiveData<List<Debtor>> = MutableLiveData()

    fun addDebtor(debtor: Debtor) {
            repository.insertDebtor(debtor)
    }

    fun getAllPayments() = repository.getAllPayments()
}