package org.jboss.pnc.scheduler;

public class Request {
    String callback;

    String payload;

    public Request(String callback, String payload) {
        this.callback = callback;
        this.payload = payload;
    }

    public String getCallback() {
        return callback;
    }

    public String getPayload() {
        return payload;
    }
}
