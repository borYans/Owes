package com.example.owes.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DebtorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDebtor(debtor: Debtor)


    @Query("SELECT * FROM debtors")
    fun getAllDebtors(): LiveData<List<Debtor>>

    @Query("SELECT money_amount FROM debtors WHERE is_owned = 1")
    suspend fun getIncomeAmount(): List<Int>

    @Query("SELECT money_amount FROM debtors WHERE is_owned = 0")
    suspend fun getOutcomeAmount(): List<Int>


    @Delete
    fun deleteDebtor(debtor: Debtor)

}