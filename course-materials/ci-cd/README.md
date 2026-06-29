# Sentry CI/CD reference (non-live)

> **Training reference only.** These files are deliberately non-live. They use fictional PolicyLab names, placeholder images/credentials, and no deploy stage. Do not run them unchanged or connect them to a client repository, tenant, or production environment.

## Contract

CI should:

1. derive one immutable `SENTRY_RELEASE`;
2. build production artifacts with that value embedded;
3. create the Sentry release and associate commits;
4. upload source maps/mappings/symbols from the exact build;
5. finalize the release;
6. deploy the unchanged artifact in a separately approved pipeline;
7. record the deploy only after deployment succeeds;
8. verify a synthetic event.

Use an organization-owned integration token restricted to the required projects. Current Sentry API guidance defines `org:ci` for CI workflows including source maps, releases, and code mappings. Store it as:

- Bitbucket: secured repository/deployment variable `SENTRY_AUTH_TOKEN`;
- Jenkins: Secret text credential referenced by credential ID.

Never store a token in YAML/Jenkinsfile, echo it, include it in an artifact, or expose it to untrusted pull-request builds.

## Files

- `bitbucket-pipelines.example.yml`: illustrative Bitbucket Cloud build and Angular source-map upload.
- `Jenkinsfile.example`: illustrative Declarative Pipeline with scoped Jenkins credential binding.
- `release-contract.md`: provider-neutral release, source-artifact, deploy, and verification checklist.

The examples intentionally stop before deployment. The deploy-record command appears only as a commented reference because recording a deploy that did not happen corrupts release evidence.

## Adaptation checklist

- [ ] Confirm current Sentry CLI and SDK/bundler-plugin versions.
- [ ] Replace fictional organization/project names.
- [ ] Match output directory and component release convention.
- [ ] Restrict execution to trusted branches/tags.
- [ ] Use the real build once; never rebuild after source-artifact upload.
- [ ] Add approval and deployment using the organization's platform controls.
- [ ] Record deploy after success.
- [ ] Validate symbolication, release/environment, trace, and privacy with synthetic data.

## Sources and validation

- [Sentry: API Permissions and Scopes](https://docs.sentry.io/api/permissions/)
- [Sentry: Create a Release API](https://docs.sentry.io/api/releases/create-a-new-release-for-an-organization/)
- [Sentry: Angular Source Maps](https://docs.sentry.io/platforms/javascript/guides/angular/sourcemaps/)
- [Atlassian: Bitbucket Pipelines](https://support.atlassian.com/bitbucket-cloud/docs/build-test-and-deploy-with-pipelines/)
- [Atlassian: Variables and secrets](https://support.atlassian.com/bitbucket-cloud/docs/variables-and-secrets/)
- [Jenkins: Using credentials](https://www.jenkins.io/doc/book/using/using-credentials/)
- [Jenkins: Credentials Binding](https://www.jenkins.io/doc/pipeline/steps/credentials-binding/)

Validation: official Sentry, Bitbucket Cloud, and Jenkins sources checked 2026-06-29.
