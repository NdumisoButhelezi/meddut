package com.example.newdutmed;

public class Conversation {
    private String message;
    private long timestamp;

    /**
     * No-argument constructor for Firebase Firestore.
     */
    public Conversation() {
    }

    /**
     * Constructor for creating a new Conversation instance.
     * @param message The message content of the conversation.
     * @param timestamp The timestamp when the conversation occurred.
     */
    public Conversation(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Gets the message content of the conversation.
     * @return The message content.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content of the conversation.
     * @param message The message content to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp when the conversation occurred.
     * @return The timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the conversation occurred.
     * @param timestamp The timestamp to set.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
