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

## How the source-map part works

The build tool creates source maps; the Sentry SDK does not. In these course examples, `npm run build -- --configuration production` is expected to write the web build output into `dist/policylab-web`, including compiled JavaScript files and matching `.map` files when source maps are enabled for that build configuration.

`dist/policylab-web` is not a Sentry standard directory. It is just the example application's build-output folder. Sentry CLI only processes the directory path passed on the command line; it does not automatically search a universal default location for source maps.

That means the examples assume all of the following are true:

- the production build really generates source maps;
- the generated `.map` files are stored under `dist/policylab-web`;
- the upload step runs against the exact same build output, without rebuilding in between.

If your real project writes source maps somewhere else, update the path passed to `sourcemaps inject` and `sourcemaps upload` to match your actual build output.

## Files

- `bitbucket-pipelines.example.yml`: illustrative Bitbucket Cloud build and Angular source-map upload.
- `Jenkinsfile.example`: illustrative Declarative Pipeline with scoped Jenkins credential binding.
- `release-contract.md`: provider-neutral release, source-artifact, deploy, and verification checklist.

The examples intentionally stop before deployment. The deploy-record command appears only as a commented reference because recording a deploy that did not happen corrupts release evidence.

## Adaptation checklist

- [ ] Confirm current Sentry CLI and SDK/bundler-plugin versions.
- [ ] Replace fictional organization/project names.
- [ ] Match output directory and component release convention.
- [ ] Ensure the production build generates source maps when your release process requires them.
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
