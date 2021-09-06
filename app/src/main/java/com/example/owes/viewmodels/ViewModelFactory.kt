package com.example.owes.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.owes.repository.DebtorRepository

class ViewModelFactory(
    private val repository: DebtorRepository,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DebtorViewModel(repository, application) as T
    }
}