package com.example.owes.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partial_payment")
data class PartialPayment(
    @PrimaryKey(autoGenerate = true)
    val paymentId: Int?,
    val date: String,
    val amount: Double,
    val debtor_name: String

)
