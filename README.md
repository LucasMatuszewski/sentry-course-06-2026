# Sentry dla developerów — materiały szkoleniowe

Jednodniowy warsztat praktyczny prowadzony **dla [Sages](https://sages.com.pl/)** przez Łukasza Matuszewskiego ([devpowers.com](https://devpowers.com)). Data: **2026-06-29**, 09:00–17:00 Europe/Warsaw.

Repozytorium zawiera materiały kursu (program, ćwiczenia, slajdy interaktywne) oraz w pełni uruchomialne aplikacje demonstracyjne: Angular + Spring Boot 4 + Android, z propagacją śladów (distributed tracing) i pipeline'em release na GitHub Actions + Coolify.

Wszystkie dane są syntetyczne — fikcyjna domena ubezpieczeniowa **PolicyLab**. Żadnych nazw, znaków towarowych ani danych klientów nie wniesiono do repo.

---

## Najszybsze ścieżki

| Cel | Plik |
|---|---|
| Pełna lista materiałów kursu | [`course-materials/README.md`](course-materials/README.md) |
| Slajdy interaktywne (offline HTML, 58 slajdów) | [`course-materials/slides/index.html`](course-materials/slides/index.html) |
| Program szkolenia (Polski) | [`course-materials/Course-agenda-program-szkolenia-Sentry_2026.md`](course-materials/Course-agenda-program-szkolenia-Sentry_2026.md) |
| Skrócona agenda 1-dniowa | [`course-materials/course-agenda-1-day.md`](course-materials/course-agenda-1-day.md) |
| Onboarding w Sentry (z zrzutami ekranu) | [`course-materials/sentry-onboarding-first-steps/sentry-onboarding.md`](course-materials/sentry-onboarding-first-steps/sentry-onboarding.md) |
| Issues tour — zwiedzanie panelu Issues | [`course-materials/sentry-onboarding-issues-tour/issues-tour.md`](course-materials/sentry-onboarding-issues-tour/issues-tour.md) |
| Wyniki ankiety przed kursem | [`course-materials/survey-results.md`](course-materials/survey-results.md) |
| Słownik pojęć Sentry | [`course-materials/glossary.md`](course-materials/glossary.md) |
| Tokeny + deploy (Coolify, GitHub Actions) | [`course-materials/guides/setup-tokens-and-deploy.md`](course-materials/guides/setup-tokens-and-deploy.md) |
| Lista zadań przed kursem (trainer-only) | [`NEXT-STEPS.md`](NEXT-STEPS.md) |

---

## Czego się nauczysz

Uczestnicy ankiety wskazali sześć priorytetów. Każdy ma dedykowane slajdy i ćwiczenia:

1. **Odzyskanie pełnych logów z Androida bez minifikacji** — R8/ProGuard mapping upload (slajdy 28, 46; ćwiczenie 04 i 07)
2. **Filtrowanie po konkretnym urządzeniu** — Sentry auto-tagi `device.model`, `os.version` (slajd 47)
3. **Obserwacja trendu zgłoszeń po wprowadzeniu poprawki** — release trends (slajdy 30, 33; ćwiczenie 04)
4. **Dołączanie customowych pól do logów** — tags vs context vs extra (slajdy 14, 33; ćwiczenia 03, 07)
5. **Distributed tracing, performance analysis** — Angular → Spring → DB w jednym waterfall (slajdy 32–35; ćwiczenie 05)
6. **Data scrubbing & prywatność** — `send-default-pii=false`, `beforeSend`, server-side scrubbers (slajdy 13–15; ćwiczenie 03)

---

## Mapa repozytorium

| Ścieżka | Co zawiera | Rola w szkoleniu |
|---|---|---|
| `apps/angular` | Klient PolicyLab w Angular 20 (TypeScript, pnpm) | Główna ścieżka uczestnika |
| `apps/api-spring` | API PolicyLab w Spring Boot 4 (Gradle 9.6) | Główna ścieżka uczestnika |
| `apps/android` | Polskie demo Androidowe z AI Studio | Demo trenera / opcjonalna ścieżka |
| `apps/android-sentry-reference` | Oficjalny sample Sentry Android | Materiał referencyjny |
| `apps/ios-sentry-reference` | Oficjalny sample Sentry iOS (SwiftUI) | Referencja konceptualna |
| `course-materials/` | Program, slajdy, ćwiczenia, słownik, screenshots | Materiały szkoleniowe |
| `course-materials/ci-cd/` | Przykłady Bitbucket Pipelines + Jenkinsfile | Wzorce do replikacji |
| `course-materials/guides/` | 14 przewodników modułowych + setup per platforma | Pogłębione referencje |
| `.github/workflows/` | CI dla PR-ów + Release na main (image build, Sentry release, deploy) | Żywy przykład CI/CD |
| `compose.yaml` | Stack produkcyjny (nginx + Spring API) dla Coolify | Demo deploymentu |

---

## Wymagania

Do podstawowych ćwiczeń wystarczy przeglądarka i dostęp do organizacji Sentry **DevPowers** (slug: `devpowers` — to nazwa portfolio trenera, hostująca projekty kursowe; nie jest to firma uczestników).

Każde ćwiczenie ma alternatywę zero-install opartą o przygotowane wcześniej eventy w Sentry.

Do uruchamiania lokalnego potrzeba dodatkowo:

- **Node.js 24.15.0 LTS** dla Angulara
- **JDK 17+** dla Spring Boot
- **Git**, IDE (rekomendowane JetBrains lub VS Code)
- Opcjonalnie: Docker / Docker Desktop, Android Studio, Xcode (tylko macOS dla iOS)

### Instalacja Node.js — wybierz jedną opcję

Repo pinuje wersję w `mise.toml`. Możesz użyć dowolnego managera lub po prostu pobrać Node bezpośrednio.

```powershell
# Opcja A — Mise (recommended; cross-platform, pinned per repo)
# https://mise.jdx.dev/
mise install
mise exec -- node --version

# Opcja B — Volta (Windows/macOS, szybki, lekki)
# https://volta.sh/
volta install node@24.15.0

# Opcja C — fnm (najszybszy, zwykle wystarcza)
# https://github.com/Schniz/fnm
fnm install 24.15.0
fnm use 24.15.0

# Opcja D — nvm-windows (Windows klasyk)
# https://github.com/coreybutler/nvm-windows
nvm install 24.15.0
nvm use 24.15.0

# Opcja E — bezpośrednia instalacja
# https://nodejs.org/en/download — pobierz 24.15.0 LTS
```

---

## Mapa projektów Sentry

| Aplikacja | Projekt w Sentry (org `devpowers`) |
|---|---|
| Angular | `javascript-angular` |
| Spring Boot | `java-spring-boot` |
| Android (primary) | `android` |
| iOS (reference) | `apple-ios` |

Kurs używa środowiska `training`. Konfiguracje tworzone przez uczestników powinny mieć prefix `[TRAINING participant-N]` żeby było łatwo posprzątać po kursie.

---

## Bezpieczeństwo

- **`SENTRY_AUTH_TOKEN`** — sekret. Nigdy nie commituj. Trzymaj tylko w protected CI secrets (GitHub Actions environment secret).
- **DSN** — publiczny endpoint przyjmujący eventy. Nie jest tajny, ale można go ratować i rate-limitować. Treningowe DSN-y są ograniczone i rotowane po kursie jeśli zostaną nadużyte.
- **Dane** — tylko syntetyczne PolicyLab. Żadnych nazw, brandu, URL-i ani danych klientów uczestników.

Pełna macierz "który token gdzie": [`course-materials/guides/setup-tokens-and-deploy.md`](course-materials/guides/setup-tokens-and-deploy.md).

---

## CI/CD i deployment

- **GitHub Actions** — żywy przykład w [`.github/workflows/release.yml`](.github/workflows/release.yml). Buduje obrazy Docker, uploaduje source maps + source bundle do Sentry, pushuje na ghcr.io, deploy na Coolify.
- **Bitbucket Pipelines** — annotated starting point w [`course-materials/ci-cd/bitbucket-pipelines.example.yml`](course-materials/ci-cd/bitbucket-pipelines.example.yml). Pokazuje wzorzec; wymaga ~30 min dostosowania do realnego repo.
- **Jenkins** — starting point w [`course-materials/ci-cd/Jenkinsfile.example`](course-materials/ci-cd/Jenkinsfile.example).
- **Coolify** — bootstrap UI w 3 minuty: [`course-materials/guides/coolify-bootstrap.md`](course-materials/guides/coolify-bootstrap.md).

---

## Licencja i atrybucja

Materiały Sentry SDK i sample'y zachowują swoje oryginalne licencje (zobacz `apps/*/LICENSE*` i `apps/*/UPSTREAM.md`). Materiały kursowe (slajdy, przewodniki, ćwiczenia) — © 2026 Łukasz Matuszewski / DevPowers, do użytku przez Sages w ramach tego szkolenia. Reuse poza tym kontekstem wymaga zgody.
