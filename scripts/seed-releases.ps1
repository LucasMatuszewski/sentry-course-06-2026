#!/usr/bin/env pwsh
# Seeds Sentry with two releases — a buggy one and a fix — so Exercise 04
# (release trend before/after fix) has live trend data when the course starts.
#
# Prerequisites:
#   1. Coolify app is live and pulling images from GHCR.
#   2. GitHub `training` environment has SENTRY_AUTH_TOKEN set.
#   3. The Release workflow has run on `main` at least once.
#
# Usage (from repo root):
#   ./scripts/seed-releases.ps1
#
# What it does:
#   1. Introduces a regression in apps/api-spring/.../QuoteService.java
#      (off-by-one in driverFactor) and commits with message "demo: regression".
#   2. Waits for the Release workflow to finish and the Coolify deploy.
#   3. Polls the live PolicyLab URL 12 times to generate buggy events.
#   4. Reverts the bug, commits with message "demo: fix regression".
#   5. Waits for the second release and polls again to seed the fixed trend.
#
# Total runtime: ~6–10 minutes depending on Docker build cache.

param(
  [string]$RepoSlug = "LucasMatuszewski/sentry-course-06-2026",
  [string]$PublicUrl = $(gh variable get PUBLIC_URL --env training --repo $RepoSlug 2>$null),
  [int]$EventsPerRelease = 12
)

if (-not $PublicUrl) {
  Write-Error "PUBLIC_URL variable is not set on the training environment. Run Coolify bootstrap first."
  exit 1
}

function Wait-LatestRun {
  Write-Host "Waiting for the latest Release run to finish..." -ForegroundColor Cyan
  do {
    Start-Sleep -Seconds 15
    $status = gh run list --repo $RepoSlug --workflow=Release --limit 1 --json status,conclusion | ConvertFrom-Json
  } while ($status.status -ne "completed")
  if ($status.conclusion -ne "success") {
    Write-Error "Release workflow failed: $($status.conclusion)"
    exit 1
  }
  Write-Host "Release finished. Waiting 30s for Coolify pull..." -ForegroundColor Cyan
  Start-Sleep -Seconds 30
}

function Hit-LiveStack($scenario) {
  for ($i = 0; $i -lt $EventsPerRelease; $i++) {
    try {
      Invoke-RestMethod -Uri "$PublicUrl/api/quotes" -Method Post `
        -ContentType 'application/json' `
        -Headers @{ 'X-Demo-Scenario' = $scenario } `
        -Body (@{
          vehicleType = 'car'; manufactureYear = 2018; coverage = 'comprehensive';
          postalCode = '00-001'; driverAge = 35
        } | ConvertTo-Json) -ErrorAction SilentlyContinue | Out-Null
    } catch {
      # 500s are expected for the buggy release; just keep firing.
    }
    Start-Sleep -Milliseconds 500
  }
}

# Step 1 — introduce the bug
$svc = "apps/api-spring/src/main/java/com/policylab/api/quote/QuoteService.java"
$content = Get-Content $svc -Raw
$buggy = $content -replace 'new BigDecimal\("1.50"\)', 'new BigDecimal("15.00")'
Set-Content -Path $svc -Value $buggy -NoNewline
git commit -am "demo: regression - young driver premium 10x too high"
git push

Wait-LatestRun
Hit-LiveStack -scenario 'server-error'

# Step 2 — fix
Set-Content -Path $svc -Value $content -NoNewline
git commit -am "demo: fix regression - restore young driver factor to 1.50"
git push

Wait-LatestRun
Hit-LiveStack -scenario 'normal'

Write-Host "Seeded. Open https://devpowers.sentry.io/releases/?project=4511644672983120 to see two releases." -ForegroundColor Green
