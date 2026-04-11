package com.ctrlaltdelete.notification;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This stores the notification data in a format the dashboard can use later.
 * I keep this separate from the raw event so Brandon has a cleaner object to send to the site.
 */
public class DashboardNotification {
    private final String notificationId;
    private final String targetUserId;
    private final String eventType;
    private final String title;
    private final String message;
    private final LocalDateTime timestamp;
    private boolean read;

    public DashboardNotification(
            String notificationId,
            String targetUserId,
            String eventType,
            String title,
            String message,
            LocalDateTime timestamp
    ) {
        this.notificationId = Objects.requireNonNull(notificationId, "notificationId cannot be null");
        this.targetUserId = Objects.requireNonNull(targetUserId, "targetUserId cannot be null");
        this.eventType = Objects.requireNonNull(eventType, "eventType cannot be null");
        this.title = Objects.requireNonNull(title, "title cannot be null");
        this.message = Objects.requireNonNull(message, "message cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
        this.read = false;
    }

    // Brandon can use this id if he needs a stable key on the dashboard side.
    public String getNotificationId() {
        return notificationId;
    }

    // This tells me which account should receive the notification.
    public String getTargetUserId() {
        return targetUserId;
    }

    // This keeps the dashboard open for more event types later.
    public String getEventType() {
        return eventType;
    }

    // This is the short label for the dashboard card or alert section.
    public String getTitle() {
        return title;
    }

    // This is the actual text the website can display to the user.
    public String getMessage() {
        return message;
    }

    // This keeps the original event time for sorting or display.
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // This tells the frontend whether the user has already seen the alert.
    public boolean isRead() {
        return read;
    }

    // This flips the notification to read after the dashboard opens it.
    public void markAsRead() {
        read = true;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + title + " - " + message;
    }
}
