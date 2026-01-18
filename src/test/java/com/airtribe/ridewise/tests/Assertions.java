package com.airtribe.ridewise.tests;

public final class Assertions {
    private Assertions() {
        // utility
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError(message);
        }
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError(message + " expected=" + expected + " actual=" + actual);
    }

    public static void assertEqualsDouble(double expected, double actual, double eps, String message) {
        if (Math.abs(expected - actual) <= eps) return;
        throw new AssertionError(message + " expected=" + expected + " actual=" + actual);
    }

    public static void assertThrows(Class<? extends Throwable> expectedType, Runnable r, String message) {
        try {
            r.run();
        } catch (Throwable t) {
            if (expectedType.isInstance(t)) return;
            throw new AssertionError(message + " expectedException=" + expectedType.getName() + " actual=" + t.getClass().getName(), t);
        }
        throw new AssertionError(message + " expectedException=" + expectedType.getName() + " but no exception was thrown");
    }
}


