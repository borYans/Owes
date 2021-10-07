package com.boryans.tefter.repository

import com.boryans.tefter.data.model.entities.Debtor

interface DebtorRepositoryImpl {
    fun getAllDebtors(): List<Debtor>
}