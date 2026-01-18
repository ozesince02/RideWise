package com.airtribe.ridewise.tests;

public final class TestRunner {
    public static void main(String[] args) {
        run("StrategyTests", StrategyTests::runAll);
        run("RideServiceTests", RideServiceTests::runAll);
        System.out.println("ALL TESTS PASSED");
    }

    private static void run(String name, Runnable suite) {
        try {
            suite.run();
            System.out.println("[PASS] " + name);
        } catch (Throwable t) {
            System.err.println("[FAIL] " + name + ": " + t.getMessage());
            t.printStackTrace(System.err);
            System.exit(1);
        }
    }
}


