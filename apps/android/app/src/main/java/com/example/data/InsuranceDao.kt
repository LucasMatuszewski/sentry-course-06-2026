package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InsuranceDao {

    @Query("SELECT * FROM policies ORDER BY premiumAmountPln DESC")
    fun getAllPolicies(): Flow<List<Policy>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolicy(policy: Policy)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolicies(policies: List<Policy>)

    @Query("SELECT * FROM claims ORDER BY reportedDate DESC")
    fun getAllClaims(): Flow<List<Claim>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(claim: Claim)

    @Delete
    suspend fun deleteClaim(claim: Claim)

    @Query("SELECT * FROM claims WHERE policyId = :policyId")
    fun getClaimsForPolicy(policyId: Int): Flow<List<Claim>>

    @Query("DELETE FROM policies")
    suspend fun clearPolicies()

    @Query("DELETE FROM claims")
    suspend fun clearClaims()
}
