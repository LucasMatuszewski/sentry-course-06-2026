package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "policies")
data class Policy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val policyNumber: String,
    val type: String, // e.g., "Auto OC/AC", "Mieszkanie i Dom", "Życie i Zdrowie", "Podróż zagraniczna"
    val premiumAmountPln: Double,
    val coverageDetails: String,
    val holderName: String,
    val isActive: Boolean = true
)
