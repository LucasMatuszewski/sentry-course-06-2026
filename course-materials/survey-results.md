# Participant survey results

**Audience:** five developers from a large Polish insurance company  
**Known responses:** response counts differ by question; that limits interpretation.

## Experience

- Java/Angular developer: 10 years
- Android developer: 10 years
- Frontend developer: 15 years; architect for approximately six months

<details>
<summary>Original responses</summary>

> programista java/angular 10 lat

> Android od 10 lat

> Programista frontend od 15lat. Architekt od pół roku

</details>

## Particularly important topics

One response identified:

- Recovering complete Android logs without minification
- Filtering by a specific device
- Observing issue trends after introducing a fix
- Attaching custom fields and information to logs

<details>
<summary>Original response</summary>

> - Odzyskiwanie pełnych logów z Android bez minify  
> - filtrowanie po konkretnych urządzeniach  
> - obserwowanie trendów zgłoszeń (czy maleje po wprowadzeniu poprawki)  
> - jak dołączać customowe pola (informacje) do logów

</details>

## Expectations

One response:

- General introduction to Sentry
- Understanding configuration suitable for the participant's enterprise context

<details>
<summary>Original response</summary>

> ogólne zapoznanie się z sentry  
> poznanie konfiguracji dla PZU

</details>

## Current Sentry knowledge

- Two responses: **1/5** — never used Sentry
- One response: **2/5** — very low/basic knowledge

## Current incident workflow

Two responses:

1. Jira ticket → notification → ownership → analysis → development when required → bugfix release → test deployment → pull request → merge to bugfix branch.
2. Issues reported in Jira → analysis based on the report → reproduction attempt in a test environment.

<details>
<summary>Original responses</summary>

> ticket w jira -> notyfikacja do mnie -> podjęcie błędu -> analiza -> development jeśli wymagany na wydanie bugfixowe -> deploy na środowisko testowe -> jak ok pull request + merge do brancha bugfixowego

> Błędy zgłaszane w jira. Analiza na podstawie zgłoszenia, próba odtworzenia na środowisku testowym

</details>

## Greatest current challenges

- Tracing and performance analysis: one response
- Data Scrubbing and data privacy: one response

## Languages

- JavaScript/TypeScript: two
- Java: two
- Swift: one
- Kotlin: one

## Requested integrations

- Angular: one
- Spring Boot: one

## Work environment

- Operating systems: Windows (two), macOS (one)
- IDE: JetBrains/IntelliJ IDEA (three)
- Tools: Bitbucket (three), Jira (three), GitHub (one), Jenkins (one)
- Projects: internal applications (two), mobile (one), eCommerce (one)

## AI-assisted development

- Generated code 1–25%: two responses
- Generated code 26–50%: one response
- GitHub Copilot: two
- Claude Code: two
- Cursor: one
- OpenAI Codex: one
- Sentry AI interest: two responses at 3/5 and one at 4/5; average approximately 3.3/5

AI is useful as a short supplementary topic, not the primary training path.

## Training implications

- Start with concepts and manual Sentry UI workflows.
- Use Angular and Spring Boot for the common hands-on path.
- Give Android a focused mobile observability demonstration.
- Prioritize custom context, device filtering, release trends, tracing, and privacy.
- Connect the workflow to Jira/Bitbucket concepts without spending the day configuring Atlassian infrastructure.
- Provide zero-install alternatives because corporate installation rights are unknown.
- Use synthetic enterprise/insurance scenarios without client branding or data.
