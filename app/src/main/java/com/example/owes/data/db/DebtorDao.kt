package com.example.owes.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.owes.data.model.Debtor

@Dao
interface DebtorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDebtor(debtor: Debtor)


    @Query("SELECT * FROM debtors WHERE is_payed = 0")
     fun getAllUnpaidDebtors(): LiveData<List<Debtor>>

    @Query("SELECT * FROM debtors WHERE is_payed = 1")
     fun getAllPaidDebtors(): LiveData<List<Debtor>>

    @Query("SELECT * FROM debtors WHERE id = :debtorName LIMIT 1")
     fun getSingleDebtor(debtorName: String): Debtor

    @Query("SELECT money_amount FROM debtors WHERE is_owned = 1 AND  is_payed = 0")
    suspend fun getIncomeAmount(): List<Int>

    @Query("SELECT money_amount FROM debtors WHERE is_owned = 0 AND is_payed = 0")
    suspend fun getOutcomeAmount(): List<Int>

    @Update
    suspend fun updateDebtor(debtor: Debtor)


    @Delete
    suspend fun deleteDebtor(debtor: Debtor)

}