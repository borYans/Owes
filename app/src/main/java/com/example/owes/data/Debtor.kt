package com.example.owes.data

data class Debtor(
    val personName: String,
    val amountMoney: Int,
    val reference: String,
    val isRecurringPayment: Boolean,
    val dueDate: String
)