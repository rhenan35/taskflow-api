package com.rhenan.taskflow.domain.valueObjects;

public record Description(String value) {
    public Description {
        if (value != null) {
            value = value.trim();
        }
    }

    public static Description empty() {
        return new Description("");
    }
}
