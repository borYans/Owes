package com.example.owes.viewmodels

import androidx.lifecycle.*
import com.example.owes.data.model.entities.Debtor
import com.example.owes.data.model.entities.PartialPayment
import com.example.owes.repository.DebtorRepository
import com.example.owes.utils.Constants.NEGATIVE_NUMBER
import com.example.owes.utils.Constants.POSITIVE_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DebtorViewModel @Inject constructor(
    private val repository: DebtorRepository
): ViewModel() {

     private var _payments: MutableLiveData<Map<String, Int>> = MutableLiveData()
     private var _totalPaidAmount: MutableLiveData<Int> = MutableLiveData()


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
     fun getOneDebtor(debtorName: String) = repository.getSingleDebtor(debtorName)
     fun getPartialPaymentsForDebtor(debtorName: String) = repository.getPPayments(debtorName)


    private fun getIncomeMoneyAmount() = sumMoney(repository.getIncomeMoney())
    private fun getOutcomeMoneyAmount() = sumMoney(repository.getOutcomeMoney())

    private fun sumMoney(list: List<Int>): Int {
        var sum = 0
        for (coin in list) {
            sum += coin
        }
        return sum
    }

     fun calculateTotal(): LiveData<Map<String, Int>> {
         val positiveNumber = getIncomeMoneyAmount() - getOutcomeMoneyAmount()
         val negativeNumber = getOutcomeMoneyAmount() - getIncomeMoneyAmount()

        if (getIncomeMoneyAmount() >= getOutcomeMoneyAmount()) {
            _payments.postValue(mapOf(POSITIVE_NUMBER to (positiveNumber)))
        } else {
            _payments.postValue(mapOf(NEGATIVE_NUMBER to (negativeNumber)))
        }

        return _payments
    }


    fun deletePayment(debtor: Debtor) = repository.deleteDebtor(debtor)
    fun deletePPayment(partialPay: PartialPayment) = repository.deletepPayment(partialPay)




}