package com.example.owes.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debtors")
data class Debtor(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "is_owned")
    val isOwned: Boolean,

    @ColumnInfo(name = "debtor_name")
    val personName: String,

    @ColumnInfo(name = "money_amount")
    val amountMoney: Int,

    @ColumnInfo(name = "reference")
    val reference: String,

    @ColumnInfo(name = "due_date")
    val dueDate: String,

    @ColumnInfo(name = "is_payed")
    var isPayed: Boolean
)