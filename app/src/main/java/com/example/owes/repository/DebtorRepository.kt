package com.example.owes.repository

import com.example.owes.data.model.Debtor
import com.example.owes.data.db.DebtorDao
import kotlinx.coroutines.*
import javax.inject.Inject

class DebtorRepository @Inject constructor(private val debtorDao: DebtorDao) {


     fun insertDebtor(debtor: Debtor) {
         CoroutineScope(Dispatchers.IO).launch {
             debtorDao.addDebtor(debtor)
         }
     }

     fun deleteDebtor(debtor: Debtor)  {
         CoroutineScope(Dispatchers.IO).launch {
             debtorDao.deleteDebtor(debtor)
         }
     }

    fun updateDebtor(debtor: Debtor) {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDao.updateDebtor(debtor)
        }
    }

     fun getAllPayments() = debtorDao.getAllUnpaidDebtors()
     fun getAllPaidDebts() = debtorDao.getAllPaidDebtors()

     fun getSingleDebtor(debtorName: String): Debtor = runBlocking {
         debtorDao.getSingleDebtor(debtorName)
     }


    fun getIncomeMoney(): List<Int> = runBlocking {
        debtorDao.getIncomeAmount()
    }


    fun getOutcomeMoney(): List<Int> = runBlocking {
        debtorDao.getOutcomeAmount()
    }


}