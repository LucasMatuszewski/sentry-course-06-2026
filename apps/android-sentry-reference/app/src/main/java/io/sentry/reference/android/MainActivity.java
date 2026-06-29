// Adapted from Sentry's sentry-samples-android MainActivity.
// Copyright (c) 2019 Sentry.
// Licensed under the MIT License.
// https://github.com/getsentry/sentry-java/blob/d8b6ce11cabd05be9a3f03a1d20fe247956d091d/sentry-samples/sentry-samples-android/src/main/java/io/sentry/samples/android/MainActivity.kt

package io.sentry.reference.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import io.sentry.Breadcrumb;
import io.sentry.ISpan;
import io.sentry.ITransaction;
import io.sentry.Sentry;
import io.sentry.SentryLogLevel;
import io.sentry.SpanStatus;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.User;

public final class MainActivity extends Activity {
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.status);

        bind(R.id.capture_message, () -> {
            SentryId id = Sentry.captureMessage("Android reference message");
            showCaptured("Message", id);
        });

        bind(R.id.capture_exception, () -> {
            SentryId id =
                    Sentry.captureException(
                            new IllegalStateException("Handled exception from Android reference"));
            showCaptured("Handled exception", id);
        });

        bind(R.id.capture_context, () -> {
            Breadcrumb breadcrumb = new Breadcrumb();
            breadcrumb.setCategory("reference.ui");
            breadcrumb.setMessage("Context example button tapped");
            Sentry.addBreadcrumb(breadcrumb);

            User user = new User();
            user.setId("pseudonymous-training-user-123");
            Sentry.setUser(user);
            Sentry.setTag("reference", "android");
            Sentry.setExtra("lesson", "scope-context");

            SentryId id =
                    Sentry.captureException(
                            new IllegalArgumentException("Exception enriched with scope context"));
            showCaptured("Context-rich exception", id);
        });

        bind(R.id.capture_log, () -> {
            Sentry.logger()
                    .log(
                            SentryLogLevel.INFO,
                            "Privacy-safe Android training log: checkout flow opened");
            status.setText(R.string.log_sent);
        });

        bind(R.id.capture_feedback, () -> {
            Sentry.feedback()
                    .show(
                            options -> {
                                options.setFormTitle("Privacy-safe training feedback");
                                options.setShowName(false);
                                options.setShowEmail(false);
                                options.setUseSentryUser(false);
                                options.setMessageLabel("Training feedback");
                                options.setMessagePlaceholder(
                                        "Describe the exercise. Do not enter personal, secret, or production data.");
                            });
            status.setText(R.string.feedback_opened);
        });

        bind(R.id.capture_transaction, () -> {
            ITransaction transaction =
                    Sentry.startTransaction("android-reference-transaction", "ui.action");
            ISpan child = transaction.startChild("reference.work", "Record a small child span");
            child.setData("source", "manual-transaction-button");
            child.setStatus(SpanStatus.OK);
            child.finish();
            transaction.setStatus(SpanStatus.OK);
            transaction.finish();
            status.setText(R.string.transaction_finished);
        });

        bind(R.id.trigger_crash, () -> {
            throw new IllegalStateException("Intentional uncaught crash from Android reference");
        });
    }

    private void bind(int buttonId, Runnable action) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(ignored -> action.run());
    }

    private void showCaptured(String kind, SentryId id) {
        status.setText(getString(R.string.captured_event, kind, id));
    }
}
