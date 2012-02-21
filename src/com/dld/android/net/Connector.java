package com.dld.android.net;

public abstract class Connector implements Runnable {
    public void start() {
        new Thread(this).start();
    }
}