package com.example.owes.repository

import androidx.lifecycle.LiveData
import com.example.owes.data.db.Debtor
import com.example.owes.data.db.DebtorDao
import com.example.owes.data.db.DebtorDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DebtorRepository(
    private val debtorDatabase: DebtorDatabase
    )
{


    fun addDebtor(debtor: Debtor) {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDatabase.debtorDao().addDebtor(debtor)
        }
    }

    fun getAllPayments(): LiveData<List<Debtor>> {
        return debtorDatabase.debtorDao().getAllDebtors()
    }
}