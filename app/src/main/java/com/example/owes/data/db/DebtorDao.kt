package com.example.owes.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.owes.data.model.entities.Debtor
import com.example.owes.data.model.entities.PartialPayment

@Dao
interface DebtorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDebtor(debtor: Debtor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPPayment(partialPayment: PartialPayment)

    @Query("SELECT * FROM debtors")
    suspend fun getAllDebtors(): List<Debtor>

    @Query("SELECT * FROM debtors WHERE is_payed = 0")
     fun getAllUnpaidDebtors(): LiveData<List<Debtor>>

    @Query("SELECT * FROM debtors WHERE is_payed = 1")
     fun getAllPaidDebtors(): LiveData<List<Debtor>>

    @Query("SELECT * FROM debtors WHERE debtor_name = :debtorName LIMIT 1")
     fun getSingleDebtor(debtorName: String): LiveData<Debtor>

    @Query("SELECT remaining_amount FROM debtors WHERE is_owned = 1 AND  is_payed = 0")
    suspend fun getIncomeAmount(): List<Double>

    @Query("SELECT remaining_amount FROM debtors WHERE is_owned = 0 AND is_payed = 0")
    suspend fun getOutcomeAmount(): List<Double>

    @Transaction
    @Query("SELECT * FROM debtors d INNER JOIN partial_payment p ON d.debtor_name = p.debtor_name WHERE d.debtor_name = :debtorName")
     fun getPPaymentsForDebtor(debtorName: String): LiveData<List<PartialPayment>>


    @Update
    suspend fun updateDebtor(debtor: Debtor)


    @Delete
    suspend fun deleteDebtor(debtor: Debtor)

    @Delete
    suspend fun deletePartialPayment(partialPay: PartialPayment)

}