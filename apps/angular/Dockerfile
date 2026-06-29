# syntax=docker/dockerfile:1.7
FROM node:24.15.0-alpine AS build

WORKDIR /workspace
RUN corepack enable && corepack prepare pnpm@9 --activate

COPY package.json pnpm-lock.yaml pnpm-workspace.yaml ./
RUN pnpm install --frozen-lockfile

COPY angular.json tsconfig.json tsconfig.app.json tsconfig.spec.json ./
COPY public public
COPY src src

ARG SENTRY_RELEASE=angular@local
ARG SENTRY_AUTH_TOKEN=""
ENV SENTRY_RELEASE=${SENTRY_RELEASE} \
    SENTRY_AUTH_TOKEN=${SENTRY_AUTH_TOKEN}

# Production build with source maps. Maps stay inside the build context so the
# release workflow can upload them to Sentry before we strip them in the final
# image stage.
RUN pnpm exec ng build --configuration production --source-map

# Optional: upload source maps directly from the build container when a CI
# auth token is provided. Skipped silently when SENTRY_AUTH_TOKEN is empty.
RUN if [ -n "$SENTRY_AUTH_TOKEN" ]; then \
      pnpm dlx @sentry/cli@latest sourcemaps inject dist || true ; \
      pnpm dlx @sentry/cli@latest sourcemaps upload \
        --org devpowers --project javascript-angular \
        --release "$SENTRY_RELEASE" dist || true ; \
    fi && \
    find dist -name '*.map' -delete

# ---- Runtime stage: nginx + Angular dist + /api reverse proxy ----
FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/dist/angular/browser /usr/share/nginx/html

EXPOSE 80
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget -q -O - http://127.0.0.1/healthz || exit 1
