package com.example.owes.repository

import com.example.owes.data.db.Debtor
import com.example.owes.data.db.DebtorDao
import kotlinx.coroutines.*
import javax.inject.Inject

class DebtorRepository @Inject constructor(private val debtorDao: DebtorDao) {


     fun insertDebtor(debtor: Debtor) {
         CoroutineScope(Dispatchers.IO).launch {
             debtorDao.addDebtor(debtor)
         }
     }

     fun deleteDebtor(debtor: Debtor) = debtorDao.deleteDebtor(debtor)

    fun getAllPayments() = debtorDao.getAllDebtors()

    fun getIncomeMoney(): List<Int> = runBlocking {
        debtorDao.getIncomeAmount()
    }

    fun getOutcomeMoney(): List<Int> = runBlocking {
        debtorDao.getOutcomeAmount()
    }

}