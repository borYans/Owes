package com.example.owes.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.owes.application.MyApplication
import com.example.owes.data.model.entities.Debtor
import com.example.owes.data.model.entities.PartialPayment
import com.example.owes.repository.DebtorRepository
import com.example.owes.utils.Constants.NEGATIVE_NUMBER
import com.example.owes.utils.Constants.POSITIVE_NUMBER
import com.example.owes.utils.OwesSharedPrefs.initSharedPrefs
import com.example.owes.utils.OwesSharedPrefs.readFromPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class DebtorViewModel @Inject constructor(
    private val repository: DebtorRepository,
): ViewModel() {

     private var _payments: MutableLiveData<Map<String, Double>> = MutableLiveData()


    fun addDebtor(debtor: Debtor) {
            repository.insertDebtor(debtor)
    }

    fun addPartialPayment(partialPayment: PartialPayment) {
        repository.insertPPayment(partialPayment)
    }

    fun updateDebtor(debtor: Debtor) {
        repository.updateDebtor(debtor)
    }

     fun getAllPayments() = repository.getAllPayments()
     fun getAllPaidDebts() = repository.getAllPaidDebts()
     fun getOneDebtor(debtorId: Int) = repository.getSingleDebtor(debtorId)
     fun getPartialPaymentsForDebtor(debtorId: Int) = repository.getPPayments(debtorId)
     fun isDebtorAlreadyExist(debtorName: String) = repository.isDebtorExisting(debtorName)


    private fun getIncomeMoneyAmount() = sumMoney(repository.getIncomeMoney())
    private fun getOutcomeMoneyAmount() = sumMoney(repository.getOutcomeMoney())

    private fun sumMoney(list: List<Double>): Double {
        var sum = 0.0
        for (coin in list) {
            sum += coin
        }
        return sum
    }

     fun calculateTotal(): LiveData<Map<String, Double>> {
         val positiveNumber = getIncomeMoneyAmount() - getOutcomeMoneyAmount()
         val negativeNumber = getOutcomeMoneyAmount() - getIncomeMoneyAmount()

        if (getIncomeMoneyAmount() >= getOutcomeMoneyAmount()) {
            _payments.postValue(mapOf(POSITIVE_NUMBER to (positiveNumber)))
        } else {
            _payments.postValue(mapOf(NEGATIVE_NUMBER to (negativeNumber)))
        }

        return _payments
    }


    fun deletePayment(debtorId: Int) = repository.deleteDebtor(debtorId)
    fun deletePPayment(partialPay: PartialPayment) = repository.deletepPayment(partialPay)
    fun deletePpaymentsForExactDebtor(debtorId: Int) = repository.deletePpaymentsForDebtor(debtorId)





}