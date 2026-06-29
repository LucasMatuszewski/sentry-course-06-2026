package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "claims")
data class Claim(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val policyId: Int,
    val claimNumber: String,
    val incidentType: String, // e.g., "Szkoda komunikacyjna (OC/AC)", "Zalanie mieszkania", "Nieszczęśliwy wypadek"
    val reportedDate: Long = System.currentTimeMillis(),
    val estimatedCostPln: Double,
    val description: String,
    val status: String // "Weryfikacja", "Zaakceptowana", "Do wypłaty", "Odrzucona"
)
