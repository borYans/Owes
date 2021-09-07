package com.example.owes.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.example.owes.data.db.Debtor
import com.example.owes.data.db.DebtorDatabase
import com.example.owes.repository.DebtorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DebtorViewModel(
    private val repository: DebtorRepository,
    app: Application
): AndroidViewModel(app) {

    private var _payments: MutableLiveData<List<Debtor>> = MutableLiveData()

    fun addDebtor(debtor: Debtor) {
        viewModelScope.launch {
            repository.addDebtor(debtor)
        }
    }

    fun getAllPayments() = repository.getAllPayments()
}