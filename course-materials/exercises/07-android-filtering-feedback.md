# Exercise 7: Android filtering and feedback

**Timebox:** 20 minutes  
**Decision:** Is the Android report build-specific, OS/device-specific, or general?

## Zero-install path

Use the trainer's mirrored Galaxy S24 and prepared Android issue/feedback screenshots. Participants do not need Android Studio, ADB, scrcpy, or a personal phone.

## Task

1. Filter the Android project to `production`, the seeded release/build, Android OS version, and Galaxy S24 device family/model.
2. Compare affected users/events with the same release on other devices.
3. Confirm the stack is deobfuscated and mapping corresponds to the build.
4. Read the linked synthetic user feedback and correlate it to event/time/trace.
5. Decide whether the evidence supports a device-specific, OS-specific, release-specific, or general hypothesis.
6. State one safe next test.

## Success criteria

- Release/build and symbolication are checked before code conclusions.
- Device/OS filters are compared with a broader baseline.
- Feedback is treated as user evidence, not root-cause proof.
- No personal notifications or device data appear during sharing.

## Hints

1. “Galaxy S24” alone does not distinguish hardware from OS/build.
2. An obfuscated frame can point to the wrong owner.
3. Compare the same build across devices, then the same device across builds.

<details>
<summary>Solution</summary>

The seed should indicate a release-specific regression visible across more than one device, with the Galaxy feedback describing the symptom. Mapping must match before assigning. Safe next test: reproduce the synthetic quote on the previous and current builds on the workshop S24 while collecting the same governed trace/context—no personal account or production data.

</details>
