package com.example.owes.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DebtorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDebtor(debtor: Debtor)


    @Query("SELECT * FROM debtors WHERE is_payed = 0")
    fun getAllUnpaidDebtors(): LiveData<List<Debtor>>

    @Query("SELECT * FROM debtors WHERE is_payed = 1")
    fun getAllPaidDebtors(): LiveData<List<Debtor>>

    @Query("SELECT money_amount FROM debtors WHERE is_owned = 1 AND  is_payed = 0")
    suspend fun getIncomeAmount(): List<Int>

    @Query("SELECT money_amount FROM debtors WHERE is_owned = 0 AND is_payed = 0")
    suspend fun getOutcomeAmount(): List<Int>

    @Update
    fun updateDebtor(debtor:Debtor)


    @Delete
    fun deleteDebtor(debtor: Debtor)

}