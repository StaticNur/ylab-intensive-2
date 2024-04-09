package com.ylab.intensive.security;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final Map<String, Object> sessionData;

    public Session() {
        this.sessionData = new HashMap<>();
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        sessionData.put(attributeName, attributeValue);
    }

    public Object getAttribute(String attributeName) {
        return sessionData.get(attributeName);
    }

    public void removeAttribute(String attributeName) {
        sessionData.remove(attributeName);
    }
    public void clearSession() {
        sessionData.clear();
    }

}

