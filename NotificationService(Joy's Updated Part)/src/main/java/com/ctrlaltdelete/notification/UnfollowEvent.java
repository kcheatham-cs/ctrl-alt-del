package com.ctrlaltdelete.notification;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This stores one unfollow event.
 * I keep both ids and usernames here so the next layer does not need another lookup.
 */
public class UnfollowEvent {
    private final String actorUserId;
    private final String actorUsername;
    private final String targetUserId;
    private final String targetUsername;
    private final LocalDateTime timestamp;

    public UnfollowEvent(
            String actorUserId,
            String actorUsername,
            String targetUserId,
            String targetUsername,
            LocalDateTime timestamp
    ) {
        this.actorUserId = Objects.requireNonNull(actorUserId, "actorUserId cannot be null");
        this.actorUsername = Objects.requireNonNull(actorUsername, "actorUsername cannot be null");
        this.targetUserId = Objects.requireNonNull(targetUserId, "targetUserId cannot be null");
        this.targetUsername = Objects.requireNonNull(targetUsername, "targetUsername cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    // This identifies which account performed the unfollow.
    public String getActorUserId() {
        return actorUserId;
    }

    // This gives me the readable username for messages and alerts.
    public String getActorUsername() {
        return actorUsername;
    }

    // This identifies the account that should receive the notification.
    public String getTargetUserId() {
        return targetUserId;
    }

    // This gives me the readable target username for logging and demos.
    public String getTargetUsername() {
        return targetUsername;
    }

    // This keeps the exact event time.
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
