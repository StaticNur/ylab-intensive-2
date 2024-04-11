package com.ylab.intensive.model.security;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a session that stores session data.
 */
public class Session {
    /**
     * The map to store session data
     */
    private final Map<String, Object> sessionData;

    /**
     * Constructs a new session with an empty session data map.
     */
    public Session() {
        this.sessionData = new HashMap<>();
    }

    /**
     * Sets an attribute in the session with the specified name and value.
     *
     * @param attributeName  The name of the attribute
     * @param attributeValue The value of the attribute
     */
    public void setAttribute(String attributeName, Object attributeValue) {
        sessionData.put(attributeName, attributeValue);
    }

    /**
     * Retrieves the value of the attribute with the specified name from the session.
     *
     * @param attributeName The name of the attribute
     * @return The value of the attribute, or null if the attribute does not exist
     */
    public Object getAttribute(String attributeName) {
        return sessionData.get(attributeName);
    }

    /**
     * Removes the attribute with the specified name from the session.
     *
     * @param attributeName The name of the attribute to be removed
     */
    public void removeAttribute(String attributeName) {
        sessionData.remove(attributeName);
    }

    /**
     * Clears all attributes in the session, effectively resetting the session.
     */
    public void clearSession() {
        sessionData.clear();
    }

}

