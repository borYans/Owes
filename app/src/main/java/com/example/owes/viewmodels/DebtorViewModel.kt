package com.example.owes.viewmodels

import androidx.lifecycle.*
import com.example.owes.data.db.Debtor
import com.example.owes.repository.DebtorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DebtorViewModel @Inject constructor(
    val repository: DebtorRepository
): ViewModel() {

     private var _payments: MutableLiveData<Map<String, Int>> = MutableLiveData()


    fun addDebtor(debtor: Debtor) {
            repository.insertDebtor(debtor)
    }

    fun getAllPayments() = repository.getAllPayments()

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
            _payments.postValue(mapOf("POSITIVE_NUMBER" to (positiveNumber)))
        } else {
            _payments.postValue(mapOf("NEGATIVE_NUMBER" to (negativeNumber)))
        }

        return _payments
    }




}