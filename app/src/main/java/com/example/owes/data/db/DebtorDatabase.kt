package com.example.owes.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Debtor::class], version = 1, exportSchema = false)
abstract class DebtorDatabase: RoomDatabase() {
    abstract fun debtorDao(): DebtorDao

    companion object {

        @Volatile
        private var INSTANCE: DebtorDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) =
            INSTANCE ?: synchronized(LOCK) {
                INSTANCE ?: createDatabase(context).also { instanceOfDb ->
                    INSTANCE = instanceOfDb
                }
            }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DebtorDatabase::class.java,
                "debtor_database"
            ).build()

    }

}