package com.example.easyexcel.thread;

public class SynchronizedInteger {

    private int value = 0;

    public synchronized int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }
}
