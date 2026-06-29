package com.example.data

import io.sentry.Sentry
import io.sentry.SpanStatus
import kotlinx.coroutines.flow.Flow

class InsuranceRepository(private val dao: InsuranceDao) {

    // Observe active policies and claims reactively
    val allPolicies: Flow<List<Policy>> = dao.getAllPolicies()
    val allClaims: Flow<List<Claim>> = dao.getAllClaims()

    suspend fun insertPolicy(policy: Policy) {
        // Teach senior developers how to create custom programmatic Sentry child spans
        val activeSpan = Sentry.getSpan()
        val span = activeSpan?.startChild("db.query", "insert_policy")
        span?.description = "Inserting policy ${policy.policyNumber} into Room DB"
        
        try {
            dao.insertPolicy(policy)
            span?.status = SpanStatus.OK
        } catch (e: Exception) {
            span?.throwable = e
            span?.status = SpanStatus.INTERNAL_ERROR
            // Log to Sentry with context
            Sentry.captureException(e) { scope ->
                scope.setTag("db_operation", "insert_policy")
                scope.setContexts("policy_data", mapOf(
                    "policyNumber" to policy.policyNumber,
                    "premium" to policy.premiumAmountPln
                ))
            }
            throw e
        } finally {
            span?.finish()
        }
    }

    suspend fun insertClaim(claim: Claim) {
        val activeSpan = Sentry.getSpan()
        val span = activeSpan?.startChild("db.query", "insert_claim")
        span?.description = "Inserting claim ${claim.claimNumber} into Room DB"
        
        // Enrich trace context with custom metadata
        span?.setData("claim_number", claim.claimNumber)
        span?.setData("estimated_cost_pln", claim.estimatedCostPln)
        span?.setData("incident_type", claim.incidentType)

        try {
            dao.insertClaim(claim)
            span?.status = SpanStatus.OK
        } catch (e: Exception) {
            span?.throwable = e
            span?.status = SpanStatus.INTERNAL_ERROR
            Sentry.captureException(e) { scope ->
                scope.setTag("db_operation", "insert_claim")
                scope.setContexts("claim_data", mapOf(
                    "claimNumber" to claim.claimNumber,
                    "estimatedCostPln" to claim.estimatedCostPln,
                    "incidentType" to claim.incidentType
                ))
            }
            throw e
        } finally {
            span?.finish()
        }
    }

    suspend fun deleteClaim(claim: Claim) {
        val activeSpan = Sentry.getSpan()
        val span = activeSpan?.startChild("db.query", "delete_claim")
        span?.description = "Deleting claim ${claim.claimNumber} from Room DB"

        try {
            dao.deleteClaim(claim)
            span?.status = SpanStatus.OK
        } catch (e: Exception) {
            span?.throwable = e
            span?.status = SpanStatus.INTERNAL_ERROR
            throw e
        } finally {
            span?.finish()
        }
    }

    suspend fun resetDatabase() {
        val activeSpan = Sentry.getSpan()
        val span = activeSpan?.startChild("db.query", "reset_database")
        span?.description = "Clearing policies and claims for simulation reset"

        try {
            dao.clearPolicies()
            dao.clearClaims()
            span?.status = SpanStatus.OK
        } catch (e: Exception) {
            span?.throwable = e
            span?.status = SpanStatus.INTERNAL_ERROR
            throw e
        } finally {
            span?.finish()
        }
    }

    suspend fun reseedDatabase(initialPolicies: List<Policy>) {
        val activeSpan = Sentry.getSpan()
        val span = activeSpan?.startChild("db.query", "reseed_database")
        span?.description = "Reseeding database with initial Poland Insurance values"

        try {
            dao.insertPolicies(initialPolicies)
            span?.status = SpanStatus.OK
        } catch (e: Exception) {
            span?.throwable = e
            span?.status = SpanStatus.INTERNAL_ERROR
            throw e
        } finally {
            span?.finish()
        }
    }
}
