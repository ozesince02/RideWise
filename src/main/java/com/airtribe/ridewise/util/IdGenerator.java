package com.airtribe.ridewise.util;

import java.util.UUID;

public final class IdGenerator {
    private IdGenerator() {
        // utility
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }
}


