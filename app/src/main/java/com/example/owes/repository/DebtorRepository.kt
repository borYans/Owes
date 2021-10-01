package com.example.owes.repository

import com.example.owes.data.model.entities.Debtor
import com.example.owes.data.db.DebtorDao
import com.example.owes.data.model.entities.PartialPayment
import com.example.owes.data.model.relations.DebtorWithPPayments
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtorRepository @Inject constructor(private val debtorDao: DebtorDao): DebtorRepositoryImpl {


     fun insertDebtor(debtor: Debtor) {
         CoroutineScope(Dispatchers.IO).launch {
             debtorDao.addDebtor(debtor)
         }
     }

    fun insertPPayment(partialPayment: PartialPayment) {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDao.addPPayment(partialPayment)
        }
    }

     fun deleteDebtor(debtor: Debtor)  {
         CoroutineScope(Dispatchers.IO).launch {
             debtorDao.deleteDebtor(debtor)
         }
     }

    fun deletepPayment(ppayment: PartialPayment)  {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDao.deletePartialPayment(ppayment)
        }
    }


    fun updateDebtor(debtor: Debtor) {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDao.updateDebtor(debtor)
        }
    }

    override fun getAllDebtors() = runBlocking {
        debtorDao.getAllDebtors()
    }

     fun getAllPayments() = debtorDao.getAllUnpaidDebtors()
     fun getAllPaidDebts() = debtorDao.getAllPaidDebtors()
     fun getSingleDebtor(debtorName: String) = debtorDao.getSingleDebtor(debtorName)
     fun getPPayments(debtorName: String) = debtorDao.getPPaymentsForDebtor(debtorName)

    fun getIncomeMoney(): List<Int> = runBlocking {
        debtorDao.getIncomeAmount()
    }


    fun getOutcomeMoney(): List<Int> = runBlocking {
        debtorDao.getOutcomeAmount()
    }


}