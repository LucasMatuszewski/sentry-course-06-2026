package com.example

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import io.sentry.*
import io.sentry.protocol.SentryId
import io.sentry.android.okhttp.SentryOkHttpInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.UUID

// Custom Polish insurance business exceptions to make debugging data look real in Sentry
class InsuranceClaimException(message: String) : Exception(message)
class PremiumCalculationException(message: String, val inputValues: Map<String, Any>) : Exception(message)

class SentryDemoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = InsuranceDatabase.getDatabase(application, viewModelScope)
    private val repository = InsuranceRepository(database.insuranceDao())

    // UI state streams from Room Database
    val policies: StateFlow<List<Policy>> = repository.allPolicies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val claims: StateFlow<List<Claim>> = repository.allClaims
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Sandbox Logs and Status Indicators
    private val _sandboxLogs = MutableStateFlow<List<String>>(listOf("System gotowy. Wybierz akcję Sentry poniżej..."))
    val sandboxLogs: StateFlow<List<String>> = _sandboxLogs.asStateFlow()

    private val _lastCapturedEventId = MutableStateFlow<String?>(null)
    val lastCapturedEventId: StateFlow<String?> = _lastCapturedEventId.asStateFlow()

    private val _isNetworkLoading = MutableStateFlow(false)
    val isNetworkLoading: StateFlow<Boolean> = _isNetworkLoading.asStateFlow()

    init {
        // Log a Sentry breadcrumb on ViewModel initialization
        val initBreadcrumb = Breadcrumb().apply {
            message = "SentryDemoViewModel initialized"
            category = "lifecycle"
            type = "navigation"
            level = SentryLevel.INFO
            setData("locale", "pl_PL")
        }
        Sentry.addBreadcrumb(initBreadcrumb)
    }

    private fun addLog(message: String) {
        _sandboxLogs.update { list -> (listOf("[${System.currentTimeMillis() % 100000}] $message") + list).take(50) }
    }

    // --- SENTRY EXPERIMENT 1: CRASHES & EXCEPTIONS ---

    /**
     * Simulates an uncaught exception that will crash the application.
     * Instructors can teach how Sentry captures fatal exceptions automatically
     * and transmits them on the next app startup.
     */
    fun triggerUncaughtCrash() {
        val crashBreadcrumb = Breadcrumb().apply {
            message = "User triggered deliberate crash in Sandbox"
            category = "action"
            level = SentryLevel.FATAL
        }
        Sentry.addBreadcrumb(crashBreadcrumb)
        
        addLog("💥 Wyzwalanie krytycznego błędu (NPE)... Aplikacja zostanie zamknięta!")
        
        // Simulating standard Kotlin NullPointerException for training
        val nullableString: String? = null
        nullableString!!.trim()
    }

    /**
     * Captures a caught exception manually using Sentry.captureException.
     * Demonstrates how non-fatal exceptions appear in the Sentry issues feed.
     */
    fun triggerCaughtException(peselNumber: String, claimType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val transaction = Sentry.startTransaction("Sandbox-Caught-Exception", "simulation")

            addLog("⚠️ Rozpoczęto walidację zgłoszenia szkody dla PESEL: $peselNumber")
            
            // Add custom local breadcrumb
            Sentry.addBreadcrumb(Breadcrumb().apply {
                message = "Validation check started for claim type: $claimType"
                category = "validation"
                setData("pesel_input", peselNumber)
            })

            try {
                // If the PESEL format is invalid, throw custom domain exception
                if (peselNumber.length != 11 || !peselNumber.all { it.isDigit() }) {
                    throw InsuranceClaimException(
                        "Błąd walidacji PESEL '$peselNumber'. Odrzucono weryfikację zgłoszenia ubezpieczeniowego ($claimType)."
                    )
                }
                
                addLog("✅ PESEL ubezpieczonego jest poprawny.")
                transaction.status = SpanStatus.OK
            } catch (e: Exception) {
                transaction.status = SpanStatus.INTERNAL_ERROR
                transaction.throwable = e

                // Capture exception on Sentry with contextual scopes (tags, extra data)
                val sentryId = Sentry.captureException(e) { scope ->
                    scope.setTag("polsecure_error_type", "validation_failed")
                    scope.setTag("pesel_checked", peselNumber)
                    scope.setTag("claim_type", claimType)
                    
                    scope.setContexts("Claim Validation Context", mapOf(
                        "pesel" to peselNumber,
                        "claim_type" to claimType,
                        "timestamp" to System.currentTimeMillis(),
                        "suggested_action" to "Poproś klienta o poprawne dane PESEL"
                    ))
                }

                _lastCapturedEventId.value = sentryId.toString()
                addLog("❌ Przechwycono błąd! Wysłano do Sentry. ID zdarzenia: $sentryId")
            } finally {
                transaction.finish()
            }
        }
    }

    /**
     * Deliberately captures a Sentry text message with warning level.
     */
    fun captureSentryMessage(message: String, severity: SentryLevel) {
        viewModelScope.launch {
            Sentry.addBreadcrumb(Breadcrumb().apply {
                this.message = "User typed custom Sentry message"
                category = "user_input"
                setData("raw_text", message)
            })

            val sentryId = Sentry.captureMessage("Ostrzeżenie z portalu ubezpieczeniowego: $message", severity)
            _lastCapturedEventId.value = sentryId.toString()
            addLog("✉️ Wysłano wiadomość logu ($severity). Sentry Event ID: $sentryId")
        }
    }

    /**
     * Correlation of captured exception with direct User Feedback.
     * Shows senior developers how to connect an Event ID with user descriptions.
     */
    fun submitUserFeedback(eventIdStr: String, name: String, email: String, comments: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Actually call the Sentry Feedback API
            val feedback = UserFeedback(SentryId(eventIdStr)).apply {
                this.name = name
                this.email = email
                this.comments = comments
            }
            Sentry.captureUserFeedback(feedback)
            
            addLog("💬 Wysłano opinię użytkownika powiązaną z błędem $eventIdStr!")
        }
    }

    // --- SENTRY EXPERIMENT 2: PERFORMANCE & CUSTOM TRACING ---

    /**
     * Custom transaction measuring a dynamic insurance calculation algorithm.
     * Highlights manual span creation, setting status, and profiling.
     */
    fun runPolicyValuationPerformanceSimulation(propertyValuePln: Double, ageOfHolder: Int, riskClass: String) {
        viewModelScope.launch(Dispatchers.Default) {
            addLog("📊 Rozpoczynamy kalkulację składki ubezpieczeniowej (Transakcja Sentry)...")

            // Start a high-level Transaction
            val transaction = Sentry.startTransaction(
                "Calculate-Premium-Valuation", // Transaction name
                "premium_valuation"          // Operation category
            )
            transaction.setTag("risk_class", riskClass)
            transaction.setData("property_value_pln", propertyValuePln)
            transaction.setData("holder_age", ageOfHolder)

            try {
                // Span 1: Fetch Risk Matrix Data (Simulated)
                val spanFetch = transaction.startChild("algo.fetch", "fetch_risk_matrix")
                spanFetch.description = "Retrieving risk factors for region 'Mazowieckie' and class '$riskClass'"
                delaySimulated(150)
                if (propertyValuePln > 10000000) {
                    spanFetch.setData("warning", "High value asset risk")
                }
                spanFetch.finish(SpanStatus.OK)

                // Span 2: Calculate Age & Demographics Factor
                val spanAge = transaction.startChild("algo.calc", "calculate_age_factor")
                spanAge.description = "Mathematical assessment of holder age ($ageOfHolder years)"
                delaySimulated(80)
                if (ageOfHolder < 18) {
                    spanAge.finish(SpanStatus.INVALID_ARGUMENT)
                    throw PremiumCalculationException("Ubezpieczony musi być pełnoletni!", mapOf("age" to ageOfHolder))
                }
                val ageFactor = if (ageOfHolder < 25) 1.4 else if (ageOfHolder > 65) 1.2 else 1.0
                spanAge.setData("age_factor_applied", ageFactor)
                spanAge.finish(SpanStatus.OK)

                // Span 3: Calculate Premium algorithm with deliberate delay and sub-computations
                val spanMath = transaction.startChild("algo.calc", "compute_final_premium")
                spanMath.description = "Running complex risk assessment and VAT computations"
                delaySimulated(300)
                
                val basePremium = propertyValuePln * 0.005
                val calculatedPremiumPln = basePremium * ageFactor * (if (riskClass == "A") 0.8 else if (riskClass == "C") 1.5 else 1.1)
                
                spanMath.setData("calculated_amount_pln", calculatedPremiumPln)
                spanMath.finish(SpanStatus.OK)

                addLog("✅ Wyznaczono składkę roczną: ${String.format("%.2f", calculatedPremiumPln)} PLN")
                transaction.status = SpanStatus.OK
            } catch (e: Exception) {
                transaction.status = SpanStatus.INTERNAL_ERROR
                transaction.throwable = e
                Sentry.captureException(e) { scope ->
                    scope.setTag("performance_error", "calculation_failed")
                }
                addLog("❌ Błąd w kalkulacji: ${e.message}")
            } finally {
                // Finish the transaction to send it to Sentry performance dashboards
                transaction.finish()
            }
        }
    }

    /**
     * Performs an HTTP request intercepted by the SentryOkHttpInterceptor.
     * Demonstrates automated trace linking (headers matching) and HTTP span tracing.
     */
    fun fetchPolandExchangeRates() {
        if (_isNetworkLoading.value) return
        _isNetworkLoading.value = true
        addLog("🌐 Wysyłanie zapytania HTTP do NBP (Narodowy Bank Polski)...")

        viewModelScope.launch(Dispatchers.IO) {
            // Start custom transaction so this network call has a parent transaction
            val transaction = Sentry.startTransaction("NBP-Currency-Sync", "network_sync")

            // Set up OkHttp client with the Sentry Interceptor
            val client = OkHttpClient.Builder()
                .addInterceptor(SentryOkHttpInterceptor())
                .build()

            val request = Request.Builder()
                .url("https://api.nbp.pl/api/exchangerates/tables/A/?format=json")
                .header("Accept", "application/json")
                .build()

            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("Niespodziewany kod odpowiedzi: $response")
                    }
                    val body = response.body?.string() ?: ""
                    
                    withContext(Dispatchers.Main) {
                        addLog("✅ Odebrano kursy walut z NBP! Rozmiar danych: ${body.length} znaków.")
                        // Log a snippet in Sentry breadcrumb
                        Sentry.addBreadcrumb(Breadcrumb().apply {
                            message = "NBP exchange rates loaded successfully"
                            category = "network"
                            setData("response_preview", body.take(100))
                        })
                    }
                }
                transaction.status = SpanStatus.OK
            } catch (e: Exception) {
                transaction.status = SpanStatus.INTERNAL_ERROR
                transaction.throwable = e
                Sentry.captureException(e) { scope ->
                    scope.setTag("sync_error", "nbp_api_failed")
                }
                withContext(Dispatchers.Main) {
                    addLog("❌ Błąd połączenia sieciowego: ${e.message}")
                }
            } finally {
                transaction.finish()
                withContext(Dispatchers.Main) {
                    _isNetworkLoading.value = false
                }
            }
        }
    }

    // --- DATABASE ACTIONS (ROOM OPERATIONS) ---

    fun submitNewClaim(policyId: Int, incidentType: String, estimatedCost: Double, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Start a Sentry transaction to wrap this Room Database insert flow
            val transaction = Sentry.startTransaction("Submit-Claim-Transaction", "claim_lifecycle")

            try {
                // Sentry Breadcrumb
                Sentry.addBreadcrumb(Breadcrumb().apply {
                    message = "Submitting a claim in Poland Insurance Portal"
                    category = "claim_flow"
                    setData("policy_id", policyId.toString())
                    setData("incident_type", incidentType)
                    setData("estimated_cost", estimatedCost.toString())
                })

                if (estimatedCost < 0) {
                    throw IllegalArgumentException("Koszt szkody nie może być ujemny: $estimatedCost PLN")
                }

                val generatedClaimNumber = "SZK-${UUID.randomUUID().toString().take(8).uppercase()}-2026"
                val newClaim = Claim(
                    policyId = policyId,
                    claimNumber = generatedClaimNumber,
                    incidentType = incidentType,
                    estimatedCostPln = estimatedCost,
                    description = description,
                    status = "Weryfikacja (Verification)"
                )

                // Insert through repo which executes within customized Sentry child spans
                repository.insertClaim(newClaim)

                withContext(Dispatchers.Main) {
                    addLog("✅ Zgłoszono szkodę: $generatedClaimNumber. Zapisano w bazie Room.")
                }
                transaction.status = SpanStatus.OK
            } catch (e: Exception) {
                transaction.status = SpanStatus.INTERNAL_ERROR
                transaction.throwable = e
                Sentry.captureException(e) { scope ->
                    scope.setTag("insurance_portal_action", "claim_submission_failed")
                    scope.setContexts("claim_inputs", mapOf(
                        "policy_id" to policyId,
                        "incident_type" to incidentType,
                        "cost" to estimatedCost,
                        "description" to description
                    ))
                }
                withContext(Dispatchers.Main) {
                    addLog("❌ Nie udało się zgłosić szkody: ${e.message}")
                }
            } finally {
                transaction.finish()
            }
        }
    }

    fun deleteClaimsReport(claim: Claim) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteClaim(claim)
                withContext(Dispatchers.Main) {
                    addLog("🗑️ Usunięto zgłoszenie szkody: ${claim.claimNumber}.")
                }
            } catch (e: Exception) {
                Sentry.captureException(e)
            }
        }
    }

    fun resetAndReseedDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            addLog("🧹 Czyszczenie i ponowne zasiedlanie bazy danych...")
            try {
                repository.resetDatabase()
                
                val initialPolicies = listOf(
                    Policy(
                        policyNumber = "PL-M-90218-2026",
                        type = "Mieszkanie i Dom (Home)",
                        premiumAmountPln = 450.0,
                        coverageDetails = "Ubezpieczenie mieszkania od ognia, zalania, kradzieży. Suma ubezpieczenia: 500,000 PLN.",
                        holderName = "Jan Kowalski"
                    ),
                    Policy(
                        policyNumber = "PL-A-10293-2026",
                        type = "Auto OC/AC (Car Insurance)",
                        premiumAmountPln = 1850.0,
                        coverageDetails = "Pakiet ubezpieczenia komunikacyjnego z Assistance Premium na Europę. Suma: 120,000 PLN.",
                        holderName = "Anna Nowak"
                    ),
                    Policy(
                        policyNumber = "PL-Z-54321-2026",
                        type = "Życie i Zdrowie (Life & Health)",
                        premiumAmountPln = 2100.0,
                        coverageDetails = "Ubezpieczenie na życie z pakietem onkologicznym i pobytem w szpitalu.",
                        holderName = "Jan Kowalski"
                    ),
                    Policy(
                        policyNumber = "PL-T-77889-2026",
                        type = "Podróż zagraniczna (Travel)",
                        premiumAmountPln = 120.0,
                        coverageDetails = "Koszty leczenia za granicą do 100,000 EUR + NNW i ubezpieczenie bagażu.",
                        holderName = "Piotr Wiśniewski"
                    )
                )
                repository.reseedDatabase(initialPolicies)
                withContext(Dispatchers.Main) {
                    addLog("✅ Baza danych ubezpieczeń została przywrócona.")
                }
            } catch (e: Exception) {
                Sentry.captureException(e)
            }
        }
    }

    // --- HELPER METRICS ---

    private suspend fun delaySimulated(ms: Long) {
        withContext(Dispatchers.Default) {
            try {
                Thread.sleep(ms) // Standard blocking sleep inside Default dispatcher to ensure exact thread profiling data for Sentry
            } catch (e: Exception) {
                // ignore
            }
        }
    }
}
