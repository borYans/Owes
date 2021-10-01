package com.example.owes.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.owes.data.model.entities.Debtor
import com.example.owes.data.model.entities.PartialPayment

data class DebtorWithPPayments(
    @Embedded val debtor: Debtor,
    @Relation(
        parentColumn = "debtor_name",
        entityColumn = "debtor_name"
    )
    val pPayments: List<PartialPayment>
)
