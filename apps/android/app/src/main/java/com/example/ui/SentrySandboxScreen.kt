package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.SentryDemoViewModel
import com.example.data.Claim
import com.example.data.Policy
import io.sentry.SentryLevel

// Professional Polish Color Palette
val ProfessionalBackground = Color(0xFFF7F9FC)
val ProfessionalSurface = Color(0xFFFFFFFF)
val ProfessionalBorder = Color(0xFFE2EAF5)
val ProfessionalPrimary = Color(0xFF0061A4)
val ProfessionalOnPrimary = Color(0xFFFFFFFF)
val ProfessionalSecondaryContainer = Color(0xFFD3E4FF)
val ProfessionalOnSecondaryContainer = Color(0xFF001C3B)

val AlertOrange = Color(0xFFE65100)  // Darker orange for readable text
val NeonGreen = Color(0xFF10B981)    // Success Emerald
val MutedText = Color(0xFF64748B)
val DarkText = Color(0xFF1E293B)

@Composable
fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedLabelColor = Color(0xFF334155), // Slate 700 - high-contrast dark gray for labels
    focusedLabelColor = ProfessionalPrimary,
    unfocusedBorderColor = Color(0xFF64748B), // Slate 500 - high-contrast border
    focusedBorderColor = ProfessionalPrimary,
    unfocusedTextColor = DarkText,
    focusedTextColor = DarkText,
    unfocusedPlaceholderColor = Color(0xFF475569),
    focusedPlaceholderColor = Color(0xFF475569)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentrySandboxScreen(
    viewModel: SentryDemoViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        TabInfo("Insurance Portal", Icons.Default.Shield, "Polisy"),
        TabInfo("Sentry Error Lab", Icons.Default.BugReport, "Error Lab"),
        TabInfo("Tracing & Performance", Icons.Default.Speed, "Tracing"),
        TabInfo("Developer Reference", Icons.Default.Code, "Baza Wiedzy")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = DarkText,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "DevPowers Insurance",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    ),
                                    color = DarkText
                                )
                                Text(
                                    text = "PolSecure S.A. • Sentry SDK active",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MutedText
                                )
                            }
                        }
                        
                        // "DP" Avatar badge from Design HTML
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(ProfessionalSecondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "DP",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = ProfessionalOnSecondaryContainer
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ProfessionalBackground,
                    titleContentColor = DarkText
                )
            )
        },
        bottomBar = {
            // Material 3 Bottom Navigation styled after the Design HTML theme
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(Color(0xFFF3F4F9))
                    .drawBehind {
                        drawLine(
                            color = ProfessionalBorder,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .navigationBarsPadding()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTab == index
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedTab = index }
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(76.dp)
                                .height(42.dp)
                                .clip(RoundedCornerShape(21.dp))
                                .background(if (isSelected) ProfessionalSecondaryContainer else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (isSelected) ProfessionalOnSecondaryContainer else MutedText,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = tab.label,
                            color = if (isSelected) DarkText else MutedText,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        containerColor = ProfessionalBackground,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Centralized Sentry Console output
            LogConsole(viewModel = viewModel)

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable Active Tab Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                when (selectedTab) {
                    0 -> TabInsurancePortal(viewModel)
                    1 -> TabSentryErrorLab(viewModel)
                    2 -> TabPerformanceTracing(viewModel)
                    3 -> TabDeveloperReference()
                }
            }
        }
    }
}

data class TabInfo(
    val title: String,
    val icon: ImageVector,
    val label: String
)

// --- CONSOLE VIEW FOR EVENTS LOGGING ---

@Composable
fun LogConsole(viewModel: SentryDemoViewModel) {
    val logs by viewModel.sandboxLogs.collectAsStateWithLifecycle()
    val lastEventId by viewModel.lastCapturedEventId.collectAsStateWithLifecycle()
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isExpanded) 260.dp else 120.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ProfessionalBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(NeonGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SENTRY DEBUG CONSOLE",
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Toggle Console Height",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(20.dp)
                    )
                }
                if (lastEventId != null) {
                    Text(
                        text = "Last Event ID: ${lastEventId?.take(8)}...",
                        color = NeonGreen,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            // Copy to clipboard or visual alert
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = false
            ) {
                items(logs) { log ->
                    Text(
                        text = log,
                        color = Color(0xFF38BDF8),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// --- TAB 0: INSURANCE PORTAL (CRUD ON ROOM + OKHTTP RATE SYNC) ---

@Composable
fun TabInsurancePortal(viewModel: SentryDemoViewModel) {
    val policies by viewModel.policies.collectAsStateWithLifecycle()
    val claims by viewModel.claims.collectAsStateWithLifecycle()
    val isNetworkLoading by viewModel.isNetworkLoading.collectAsStateWithLifecycle()

    var showClaimForm by remember { mutableStateOf(false) }
    var selectedPolicyForClaim by remember { mutableStateOf<Policy?>(null) }
    var showExplanationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Welcome Card - Clean Light Blue Executive Style from Design HTML
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = ProfessionalSecondaryContainer),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AKTUALNA POLISA",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 12.sp
                        ),
                        color = ProfessionalOnSecondaryContainer.copy(alpha = 0.7f)
                    )
                    Box(
                        modifier = Modifier
                            .background(ProfessionalPrimary, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "PREMIUM",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Witaj, Kursancie!",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                    color = ProfessionalOnSecondaryContainer
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Aplikacja demonstracyjna dla seniorów. Lokalna baza danych oraz zapytania sieciowe są monitorowane pod kątem błędów przez Sentry SDK.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    color = ProfessionalOnSecondaryContainer.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { showExplanationDialog = true }
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = ProfessionalPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Co to oznacza? Dowiedz się więcej",
                        color = ProfessionalPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Actions Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.fetchPolandExchangeRates() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProfessionalPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f).height(54.dp),
                enabled = !isNetworkLoading,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isNetworkLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Sync, contentDescription = "Sync", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pobierz kursy NBP", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Button(
                onClick = { viewModel.resetAndReseedDatabase() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64748B),
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Restore, contentDescription = "Reset", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Resetuj lokalną bazę", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // section: Policies
        Text(
            text = "Twoje Aktywne Polisy (${policies.size})",
            color = DarkText,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp
        )
        Text(
            text = "Kliknij polisę, aby zgłosić szkodę i zapisać ją w bazie danych.",
            color = MutedText,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Policy List
        if (policies.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = ProfessionalSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, ProfessionalBorder)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Info, contentDescription = "Empty", tint = MutedText, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Brak aktywnych polis. Kliknij Resetuj lokalną bazę.", color = MutedText, fontSize = 15.sp)
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                policies.forEach { policy ->
                    PolicyCard(policy = policy, onClick = {
                        selectedPolicyForClaim = policy
                        showClaimForm = true
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Claims
        Text(
            text = "Zgłoszone Szkody (${claims.size})",
            color = DarkText,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (claims.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = ProfessionalSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, ProfessionalBorder)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CloudQueue, contentDescription = "Brak szkód", tint = MutedText, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Brak zgłoszonych szkód.", color = DarkText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Kliknij na jedną z polis powyżej, aby zgłosić nową.", color = MutedText, fontSize = 13.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                claims.forEach { claim ->
                    ClaimCard(claim = claim, onDelete = { viewModel.deleteClaimsReport(claim) })
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp)) // Padding for scrolling
    }

    // Modal dialog to submit claim
    if (showClaimForm && selectedPolicyForClaim != null) {
        ClaimSubmissionDialog(
            policy = selectedPolicyForClaim!!,
            onDismiss = { showClaimForm = false },
            onSubmit = { incidentType, cost, description ->
                viewModel.submitNewClaim(
                    policyId = selectedPolicyForClaim!!.id,
                    incidentType = incidentType,
                    estimatedCost = cost,
                    description = description
                )
                showClaimForm = false
            }
        )
    }

    if (showExplanationDialog) {
        AlertDialog(
            onDismissRequest = { showExplanationDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Help",
                        tint = ProfessionalPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Zrozumieć technologię",
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column {
                        Text(
                            text = "Czym jest „baza danych Room”?",
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "To lokalna baza danych zapisana bezpośrednio w pamięci Twojego telefonu (dostarczana przez Google jako biblioteka Room). Pozwala aplikacji działać szybko i poprawnie w trybie offline (bez dostępu do internetu), bezpiecznie przechowując Twoje polisy i zgłoszone szkody lokalnie.",
                            color = MutedText,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                    
                    Column {
                        Text(
                            text = "Jak chroni ją Sentry SDK?",
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Sentry działa jak diagnostyczna skrzynka bezpieczeństwa. Automatycznie monitoruje zapytania do bazy danych oraz połączenia sieciowe. Jeśli wystąpi błąd (np. brak pamięci lub uszkodzenie pliku), Sentry natychmiast wysyła raport techniczny do programisty, co umożliwia szybką naprawę usterki.",
                            color = MutedText,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showExplanationDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProfessionalPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Rozumiem", fontSize = 14.sp)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun PolicyCard(policy: Policy, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(containerColor = ProfessionalSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, ProfessionalBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ProfessionalSecondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        policy.type.contains("Home") || policy.type.contains("Mieszkanie") -> Icons.Default.Home
                        policy.type.contains("Car") || policy.type.contains("Auto") -> Icons.Default.DirectionsCar
                        policy.type.contains("Life") || policy.type.contains("Życie") -> Icons.Default.Favorite
                        else -> Icons.Default.FlightTakeoff
                    },
                    contentDescription = "Policy Type",
                    tint = ProfessionalOnSecondaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = policy.type,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF0F4FA), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Room", color = ProfessionalPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Text(text = "Nr polisy: ${policy.policyNumber}", color = MutedText, fontSize = 13.sp)
                Text(text = "Ubezpieczony: ${policy.holderName}", color = MutedText, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = policy.coverageDetails, color = MutedText.copy(alpha = 0.8f), fontSize = 12.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${policy.premiumAmountPln.toInt()} PLN",
                    fontWeight = FontWeight.Bold,
                    color = ProfessionalPrimary,
                    fontSize = 18.sp
                )
                Text(
                    text = "Składka",
                    color = MutedText,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ClaimCard(claim: Claim, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(containerColor = ProfessionalSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, ProfessionalBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFFECE0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Claim",
                            tint = AlertOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = claim.claimNumber, fontWeight = FontWeight.Bold, color = DarkText, fontSize = 16.sp)
                        Text(text = claim.incidentType, color = ProfessionalPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFECE0), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(text = claim.status, color = AlertOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = claim.description, color = MutedText, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MonetizationOn, contentDescription = "Cost", tint = AlertOrange, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Wycena: ${claim.estimatedCostPln} PLN",
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        fontSize = 15.sp
                    )
                }
                
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828), modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}

@Composable
fun ClaimSubmissionDialog(
    policy: Policy,
    onDismiss: () -> Unit,
    onSubmit: (String, Double, String) -> Unit
) {
    var incidentType by remember { mutableStateOf("Szkoda komunikacyjna (OC/AC)") }
    var estimatedCostStr by remember { mutableStateOf("2500") }
    var description by remember { mutableStateOf("Stłuczka parkingowa, uszkodzony zderzak przedni.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Zgłoś szkodę do polisy ${policy.policyNumber}", color = DarkText, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Polisa: ${policy.type}", color = ProfessionalPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                
                Text("Typ zdarzenia:", fontSize = 14.sp, color = DarkText, fontWeight = FontWeight.SemiBold)
                val options = listOf("Szkoda komunikacyjna (OC/AC)", "Zalanie mieszkania", "Kradzież z włamaniem", "Złamanie nogi / Trzcina", "Ujemne koszty (Wyzwalacz błędu!)")
                var expanded by remember { mutableStateOf(false) }
                Box {
                    Button(
                        onClick = { expanded = true }, 
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F4FA)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(incidentType, color = ProfessionalPrimary, fontSize = 14.sp)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        options.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt, fontSize = 14.sp) },
                                onClick = {
                                    incidentType = opt
                                    if (opt == "Ujemne koszty (Wyzwalacz błędu!)") {
                                        estimatedCostStr = "-1500"
                                    }
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = estimatedCostStr,
                    onValueChange = { estimatedCostStr = it },
                    label = { Text("Szacowany koszt szkody (PLN)", fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Opis zdarzenia", fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = textFieldColors()
                )
                
                Text(
                    text = "💡 Wskazówka Sentry: Wybranie opcji 'Ujemne koszty...' lub wpisanie ujemnego kosztu wywoła błąd IllegalArgumentException, który zostanie przechwycony przez Sentry!",
                    fontSize = 13.sp,
                    color = AlertOrange,
                    lineHeight = 16.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cost = estimatedCostStr.toDoubleOrNull() ?: 0.0
                    onSubmit(incidentType, cost, description)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProfessionalPrimary,
                    contentColor = Color.White
                )
            ) {
                Text("Wyślij Zgłoszenie")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj", color = MutedText)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

// --- TAB 1: SENTRY ERROR LAB (DELIBERATE ISSUE GENERATOR) ---

@Composable
fun TabSentryErrorLab(viewModel: SentryDemoViewModel) {
    var peselInput by remember { mutableStateOf("8801021234") } // Default invalid length
    var claimTypeInput by remember { mutableStateOf("Autocasco") }
    
    var customLogMsg by remember { mutableStateOf("Nieznany stan połączenia terminala płatniczego") }

    // Feedback dialog fields
    val lastEventId by viewModel.lastCapturedEventId.collectAsStateWithLifecycle()
    var showFeedbackForm by remember { mutableStateOf(false) }
    var fbName by remember { mutableStateOf("Anna Nowak") }
    var fbEmail by remember { mutableStateOf("anna.nowak@polska.pl") }
    var fbComment by remember { mutableStateOf("Aplikacja zgłosiła błąd weryfikacji PESEL. Wpisywałam poprawny PESEL mojego męża.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Lab Section Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = ProfessionalSecondaryContainer),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ProfessionalPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.BugReport, contentDescription = "Lab", tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Sentry Error Laboratory",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                        color = ProfessionalOnSecondaryContainer
                    )
                    Text(
                        text = "Wywołuj i analizuj wyjątki ubezpieczeniowe bezpośrednio powiązane ze śledzeniem platformy Sentry.",
                        color = ProfessionalOnSecondaryContainer.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Card 1: Uncaught Fatal Crash
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = ProfessionalSurface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, ProfessionalBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Dangerous, contentDescription = "Crash", tint = Color(0xFFC62828), modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("1. Fatal Crash (Błąd Krytyczny)", fontWeight = FontWeight.Bold, color = DarkText, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Wywołaj NullPointerException na wątku głównym. Aplikacja ulegnie awarii. Sentry zarejestruje błąd automatycznie i prześle go po restarcie.",
                    color = MutedText,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                
                Button(
                    onClick = { viewModel.triggerUncaughtCrash() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFCDD2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✖", color = Color(0xFFC62828), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Force Fatal Crash", color = Color(0xFFC62828), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Runtime ArithmeticException", color = Color(0xFFC62828).copy(alpha = 0.8f), fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Card 2: Caught Business Exception
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = ProfessionalSurface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, ProfessionalBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = "Exception", tint = AlertOrange, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("2. Caught Exception (Przechwycony Wyjątek)", fontWeight = FontWeight.Bold, color = DarkText, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Wpisz niepoprawny PESEL (za krótki), aby wywołać błąd walidacji. Wyjątek ubezpieczeniowy zostanie programowo przechwycony i wysłany do Sentry.",
                    color = MutedText,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = peselInput,
                    onValueChange = { peselInput = it },
                    label = { Text("PESEL Ubezpieczonego (11 cyfr)", fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = claimTypeInput,
                    onValueChange = { claimTypeInput = it },
                    label = { Text("Rodzaj zgłoszenia", fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors()
                )

                Spacer(modifier = Modifier.height(14.dp))
                
                Button(
                    onClick = { viewModel.triggerCaughtException(peselInput, claimTypeInput) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF3E0)),
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFE0B2))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFE0B2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("!", color = Color(0xFFE65100), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Capture Exception programmatically", color = Color(0xFFE65100), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("sentry.captureException(e)", color = Color(0xFFE65100).copy(alpha = 0.8f), fontSize = 13.sp)
                        }
                    }
                }

                // If error logged, trigger Feedback manual popup
                if (lastEventId != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE0F2FE), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "Wyjątek wysłany do Sentry!",
                                color = Color(0xFF0369A1),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Event ID: $lastEventId",
                                color = DarkText,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { showFeedbackForm = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ProfessionalPrimary,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Feedback, contentDescription = "Feedback", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Dołącz opinie (User Feedback)", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Card 3: Custom Logs
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = ProfessionalSurface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, ProfessionalBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Message, contentDescription = "Log Message", tint = ProfessionalPrimary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("3. Capture Message (Sentry Log)", fontWeight = FontWeight.Bold, color = DarkText, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Zaloguj dowolny niestandardowy tekst bezpośrednio do panelu Sentry z określonym poziomem ważności (Severity Level). Zdarzenie to pojawi się jako Message.",
                    color = MutedText,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = customLogMsg,
                    onValueChange = { customLogMsg = it },
                    label = { Text("Treść wiadomości logu", fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.captureSentryMessage(customLogMsg, SentryLevel.WARNING) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Text("⚠️ WARNING", color = DarkText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.captureSentryMessage(customLogMsg, SentryLevel.ERROR) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFFFCDD2))
                    ) {
                        Text("❌ ERROR", color = Color(0xFFC62828), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }

    // Modal dialogue for User Feedback Correlation
    if (showFeedbackForm && lastEventId != null) {
        AlertDialog(
            onDismissRequest = { showFeedbackForm = false },
            title = { Text("Formularz opinii Sentry User Feedback", color = DarkText, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "To okno zbiera feedback powiązany bezpośrednio z identyfikatorem błędu Sentry: $lastEventId.",
                        fontSize = 14.sp,
                        color = MutedText
                    )
                    OutlinedTextField(
                        value = fbName,
                        onValueChange = { fbName = it },
                        label = { Text("Twoje imię i nazwisko", fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors()
                    )
                    OutlinedTextField(
                        value = fbEmail,
                        onValueChange = { fbEmail = it },
                        label = { Text("Adres e-mail", fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors()
                    )
                    OutlinedTextField(
                        value = fbComment,
                        onValueChange = { fbComment = it },
                        label = { Text("Co poszło nie tak? Opis błędu", fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        colors = textFieldColors()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitUserFeedback(lastEventId!!, fbName, fbEmail, fbComment)
                        showFeedbackForm = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProfessionalPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Wyślij do Sentry")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFeedbackForm = false }) {
                    Text("Anuluj", color = MutedText)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

// --- TAB 2: PERFORMANCE TRACING HUB (SPAN ANALYSIS) ---

@Composable
fun TabPerformanceTracing(viewModel: SentryDemoViewModel) {
    var propValue by remember { mutableStateOf("1500000") } // PLN
    var holderAge by remember { mutableStateOf("24") }      // Deliberate age factor trigger
    var riskCategory by remember { mutableStateOf("B") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Section Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = ProfessionalSecondaryContainer),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ProfessionalPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Speed, contentDescription = "Performance", tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Performance Tracing Lab",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                        color = ProfessionalOnSecondaryContainer
                    )
                    Text(
                        text = "Analizuj rozproszone transakcje i mierz czas wykonania kodu podzielony na spany.",
                        color = ProfessionalOnSecondaryContainer.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Card 1: Custom Algorithmic Spans
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = ProfessionalSurface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, ProfessionalBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timeline, contentDescription = "Spans", tint = ProfessionalPrimary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("1. Programmatic Spans (Kalkulator Składki)", fontWeight = FontWeight.Bold, color = DarkText, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Kalkulator składki majątkowej uruchamia transakcję Sentry i sekwencyjne pod-operacje (spany): pobieranie macierzy ryzyka, analiza demographics i kalkulacja końcowa.",
                    color = MutedText,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = propValue,
                    onValueChange = { propValue = it },
                    label = { Text("Wartość nieruchomości (PLN)", fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = holderAge,
                    onValueChange = { holderAge = it },
                    label = { Text("Wiek ubezpieczającego (lata)", fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Kategoria ryzyka szkody:", fontSize = 14.sp, color = DarkText, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("A (Niskie)", "B (Średnie)", "C (Wysokie)").forEach { opt ->
                        val letter = opt.take(1)
                        val isSelected = riskCategory == letter
                        Button(
                            onClick = { riskCategory = letter },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) ProfessionalPrimary else Color(0xFFF1F5F9)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(48.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                        ) {
                            Text(opt, fontSize = 13.sp, color = if (isSelected) Color.White else DarkText, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        val pVal = propValue.toDoubleOrNull() ?: 100000.0
                        val age = holderAge.toIntOrNull() ?: 30
                        viewModel.runPolicyValuationPerformanceSimulation(pVal, age, riskCategory)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F4FA)),
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2EAF5))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE2EAF5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚡", color = Color(0xFF0061A4), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Start Performance Trace", color = Color(0xFF0061A4), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Sentry.startTransaction()", color = Color(0xFF0061A4).copy(alpha = 0.8f), fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Card 2: Visual Representation of Sentry Transaction Waterfall
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, ProfessionalBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "TRANSACTION GANTT WATERFALL CHART",
                    color = Color(0xFFE65100),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(12.dp))

                WaterfallBar("Calculate-Premium-Valuation (Transaction)", 0.0f, 1.0f, Color(0xFF818CF8))
                WaterfallBar(" ↳ algo.fetch: fetch_risk_matrix", 0.0f, 0.28f, Color(0xFFF472B6))
                WaterfallBar(" ↳ algo.calc: calculate_age_factor", 0.28f, 0.43f, Color(0xFFFB923C))
                WaterfallBar(" ↳ algo.calc: compute_final_premium", 0.43f, 1.0f, Color(0xFF34D399))
                
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "💡 Każdy powyższy pasek reprezentuje oddzielną mierzalną jednostkę kodu (Span). W panelu Sentry, kliknięcie w pasek wyświetli metadane (np. risk_class, cost) oraz logi.",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8),
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun WaterfallBar(label: String, startPct: Float, endPct: Float, color: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color.White, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            Text("${((endPct - startPct) * 530).toInt()}ms", color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFF334155), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(endPct - startPct)
                    .offset(x = (startPct * 200).dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

// --- TAB 4: DEVELOPER REFERENCE MANUAL (BEST PRACTICES & CODES) ---

@Composable
fun TabDeveloperReference() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Reference Section Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = ProfessionalSecondaryContainer),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ProfessionalPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Code, contentDescription = "Manual", tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Sentry SDK Setup Guide",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = ProfessionalOnSecondaryContainer
                    )
                    Text(
                        text = "Gotowe kody źródłowe w języku Kotlin pokazujące jak zaimplementować Sentry w aplikacjach ubezpieczeniowych.",
                        color = ProfessionalOnSecondaryContainer.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CodeSection(
            title = "1. Inicjalizacja SDK (SentryDemoApplication.kt)",
            code = """
// W pliku Application onCreate():
SentryAndroid.init(this) { options ->
    options.dsn = "https://60c377bf4862a6a8f0073462e41464e6@..."
    options.environment = "production-pl"
    options.release = "polsecure-app@1.0.0"
    
    // Konfiguracja próbkowania (100% dla testów)
    options.tracesSampleRate = 1.0
    options.profilesSampleRate = 1.0
    
    // Tagowanie globalne ułatwiające filtrowanie
    options.setTag("company", "PolSecure-Insurance")
}
"""
        )

        Spacer(modifier = Modifier.height(12.dp))

        CodeSection(
            title = "2. Raportowanie Wyjątków (Try-Catch)",
            code = """
try {
    validatePeselCode(clientPesel)
} catch (e: InsuranceClaimException) {
    // Sentry przechwyci błąd z dodatkowym kontekstem
    SentryId id = Sentry.captureException(e) { scope ->
        scope.setTag("verification_step", "pesel_validation")
        scope.setContexts("Client Data", mapOf(
            "pesel" to clientPesel,
            "broker_id" to "PL-90218"
        ))
    }
}
"""
        )

        Spacer(modifier = Modifier.height(12.dp))

        CodeSection(
            title = "3. Ręczne Tworzenie Transakcji i Spanów",
            code = """
// Rozpoczęcie transakcji wydajnościowej
val tx = Sentry.startTransaction("PremiumCalculation", "calculation")
try {
    // Pod-operacja 1 (Span 1)
    val span1 = tx.startChild("db.query", "fetch_matrix")
    val matrix = database.fetchMatrix()
    span1.finish(SpanStatus.OK)
    
    // Pod-operacja 2 (Span 2)
    val span2 = tx.startChild("math.calc", "evaluate_formula")
    val quote = runFormula(matrix)
    span2.finish(SpanStatus.OK)
    
    tx.status = SpanStatus.OK
} catch (e: Exception) {
    tx.status = SpanStatus.INTERNAL_ERROR
    tx.throwable = e
    Sentry.captureException(e)
} finally {
    tx.finish() // Wysyła dane do Sentry
}
"""
        )

        Spacer(modifier = Modifier.height(12.dp))

        CodeSection(
            title = "4. Integracja z OkHttp (Automatyczny Tracing API)",
            code = """
// Przygotowanie klienta HTTP z interceptorem Sentry
val client = OkHttpClient.Builder()
    .addInterceptor(SentryOkHttpInterceptor())
    .build()

// Wszystkie żądania wykonywane tym klientem 
// automatycznie wygenerują spany sieciowe w Sentry!
"""
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun CodeSection(title: String, code: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = ProfessionalSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, ProfessionalBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = DarkText, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F172A))
                    .clip(RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = code.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                )
            }
        }
    }
}
