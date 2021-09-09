package com.example.owes.repository

import androidx.lifecycle.LiveData
import com.example.owes.data.db.Debtor
import com.example.owes.data.db.DebtorDao
import com.example.owes.data.db.DebtorDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DebtorRepository @Inject constructor(
    private val debtorDao: DebtorDao
    )
{

     fun insertDebtor(debtor: Debtor) {
         CoroutineScope(Dispatchers.IO).launch {
             debtorDao.addDebtor(debtor)
         }
     }
    suspend fun deleteDebtor(debtor: Debtor) = debtorDao.deleteDebtor(debtor)

    fun getAllPayments() = debtorDao.getAllDebtors()
}