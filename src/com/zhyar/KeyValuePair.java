package com.zhyar;

public class KeyValuePair {
    private final String key;
    private final int value;

    public KeyValuePair(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key;
    }
}
