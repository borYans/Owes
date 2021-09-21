package com.example.owes.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.owes.data.model.Debtor

@Database(entities = [Debtor::class], version = 1, exportSchema = false)
abstract class DebtorDatabase: RoomDatabase() {

    abstract fun debtorDao(): DebtorDao


}