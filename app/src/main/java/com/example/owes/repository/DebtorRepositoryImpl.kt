package com.example.owes.repository

import com.example.owes.data.model.entities.Debtor

interface DebtorRepositoryImpl {
    fun getAllDebtors(): List<Debtor>
}