package com.example.owes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "partial_payment")
data class PartialPayment(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val date: String,
    val amount: Double
)
