package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Policy::class, Claim::class], version = 1, exportSchema = false)
abstract class InsuranceDatabase : RoomDatabase() {

    abstract fun insuranceDao(): InsuranceDao

    companion object {
        @Volatile
        private var INSTANCE: InsuranceDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): InsuranceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InsuranceDatabase::class.java,
                    "polsecure_insurance_db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.insuranceDao())
                }
            }
        }

        suspend fun populateDatabase(dao: InsuranceDao) {
            // Pre-populate with typical Polish insurance policies for demonstration
            val initialPolicies = listOf(
                Policy(
                    policyNumber = "PL-M-90218-2026",
                    type = "Mieszkanie i Dom (Home)",
                    premiumAmountPln = 450.0,
                    coverageDetails = "Ubezpieczenie mieszkania od ognia, zalania, kradzieży. Suma ubezpieczenia: 500,000 PLN.",
                    holderName = "Jan Kowalski",
                    isActive = true
                ),
                Policy(
                    policyNumber = "PL-A-10293-2026",
                    type = "Auto OC/AC (Car Insurance)",
                    premiumAmountPln = 1850.0,
                    coverageDetails = "Pakiet ubezpieczenia komunikacyjnego z Assistance Premium na Europę. Suma: 120,000 PLN.",
                    holderName = "Anna Nowak",
                    isActive = true
                ),
                Policy(
                    policyNumber = "PL-Z-54321-2026",
                    type = "Życie i Zdrowie (Life & Health)",
                    premiumAmountPln = 2100.0,
                    coverageDetails = "Ubezpieczenie na życie z pakietem onkologicznym i pobytem w szpitalu.",
                    holderName = "Jan Kowalski",
                    isActive = true
                ),
                Policy(
                    policyNumber = "PL-T-77889-2026",
                    type = "Podróż zagraniczna (Travel)",
                    premiumAmountPln = 120.0,
                    coverageDetails = "Koszty leczenia za granicą do 100,000 EUR + NNW i ubezpieczenie bagażu.",
                    holderName = "Piotr Wiśniewski",
                    isActive = true
                )
            )
            dao.insertPolicies(initialPolicies)
        }
    }
}
