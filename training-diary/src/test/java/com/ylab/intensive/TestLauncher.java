/*
package com.ylab.intensive;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;

public class TestLauncher {

    public static void main(String[] args) {
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        AnsiColor ansiColor = new AnsiColor();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage("com.ylab.intensive"))
                .build();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();

        try (PrintWriter writer = new PrintWriter(System.out)) {
            summary.printTo(writer);
            writer.println(ansiColor.green("Successful Tests: " + summary.getTestsSucceededCount()));
            writer.println(ansiColor.red("Failed Tests: " + summary.getTestsFailedCount()));
            writer.println("Total Tests: " + summary.getTestsFoundCount());
        }
    }
}


*/
