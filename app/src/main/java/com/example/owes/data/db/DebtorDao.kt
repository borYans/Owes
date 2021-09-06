package com.example.owes.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DebtorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDebtor(debtor: Debtor)


    @Query("SELECT * FROM debtors")
    fun getAllDebtors(): LiveData<List<Debtor>>


    @Delete
    fun deleteDebtor(debtor: Debtor)

}