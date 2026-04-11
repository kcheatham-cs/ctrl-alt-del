package com.ctrlaltdelete.notification;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This receives unfollow events and sends them to two places:
 * 1. the terminal so I can test the backend quickly
 * 2. a dashboard-ready list Brandon can expose to the website later
 */
public class NotificationService implements FollowerObserver {
    private static final DateTimeFormatter DASHBOARD_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // This acts as my lightweight account lookup without depending on User.java.
    private final Map<String, AccountRecord> accountsByUserId = new LinkedHashMap<>();

    // This keeps the raw unfollow events in case the backend needs the original event objects.
    private final Map<String, List<UnfollowEvent>> unfollowEventsByUserId = new LinkedHashMap<>();

    // This keeps the dashboard-friendly version Brandon can send to the frontend later.
    private final Map<String, List<DashboardNotification>> dashboardNotificationsByUserId = new LinkedHashMap<>();

    public void registerAccount(String userId, String username, String email) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(username, "username cannot be null");
        Objects.requireNonNull(email, "email cannot be null");

        // I register the target account first so the service knows where notifications belong.
        AccountRecord accountRecord = new AccountRecord(userId, username, email);
        accountsByUserId.put(userId, accountRecord);
        unfollowEventsByUserId.putIfAbsent(userId, new ArrayList<>());
        dashboardNotificationsByUserId.putIfAbsent(userId, new ArrayList<>());
    }

    public void notifyUnfollow(UnfollowEvent event) {
        Objects.requireNonNull(event, "event cannot be null");
        requireRegisteredAccount(event.getTargetUserId());

        // This stores the raw backend event exactly as it happened.
        unfollowEventsByUserId
                .computeIfAbsent(event.getTargetUserId(), ignored -> new ArrayList<>())
                .add(event);

        // This builds the cleaner dashboard record from the raw event.
        DashboardNotification dashboardNotification = buildDashboardNotification(event);
        dashboardNotificationsByUserId
                .computeIfAbsent(event.getTargetUserId(), ignored -> new ArrayList<>())
                .add(dashboardNotification);
    }

    public List<UnfollowEvent> getNotifications(String userId) {
        List<UnfollowEvent> notifications = unfollowEventsByUserId.get(userId);
        if (notifications == null) {
            return List.of();
        }

        // I return a read-only list so other code cannot accidentally change my stored history.
        return Collections.unmodifiableList(notifications);
    }

    public List<DashboardNotification> getDashboardNotifications(String userId) {
        List<DashboardNotification> notifications = dashboardNotificationsByUserId.get(userId);
        if (notifications == null) {
            return List.of();
        }

        // Brandon can read this list, but this protects the stored backend copy from outside edits.
        return Collections.unmodifiableList(notifications);
    }

    public int getNotificationCount(String userId) {
        return getNotifications(userId).size();
    }

    @Override
    public void onUnfollow(UnfollowEvent event) {
        Objects.requireNonNull(event, "event cannot be null");

        // This stores the unfollow in both backend collections first.
        notifyUnfollow(event);

        // This prints the alert to the terminal for backend testing.
        System.out.println("[Terminal Alert] " + formatTerminalMessage(event));

        // This prints the dashboard message so Brandon can compare the backend output with the site later.
        DashboardNotification latestDashboardNotification = getLatestDashboardNotification(event.getTargetUserId());
        System.out.println("[Dashboard Alert] " + latestDashboardNotification.getMessage());
    }

    public String formatTerminalMessage(UnfollowEvent event) {
        // This keeps the terminal message simple and easy to verify during demos.
        return "@" + event.getActorUsername()
                + " unfollowed @"
                + event.getTargetUsername()
                + " at "
                + event.getTimestamp()
                + ".";
    }

    private DashboardNotification buildDashboardNotification(UnfollowEvent event) {
        // I build a predictable id so each alert can be tracked in order.
        String notificationId = event.getTargetUserId() + "-unfollow-" + getNotificationCount(event.getTargetUserId());
        String title = "Follower Update";

        // This is the text the dashboard can display directly.
        String message = "@"
                + event.getActorUsername()
                + " unfollowed you on "
                + DASHBOARD_TIME_FORMAT.format(event.getTimestamp())
                + ".";

        return new DashboardNotification(
                notificationId,
                event.getTargetUserId(),
                "UNFOLLOW",
                title,
                message,
                event.getTimestamp()
        );
    }

    private DashboardNotification getLatestDashboardNotification(String userId) {
        List<DashboardNotification> notifications = dashboardNotificationsByUserId.get(userId);
        if (notifications == null || notifications.isEmpty()) {
            throw new IllegalStateException("No dashboard notifications found for user: " + userId);
        }

        // I use the most recent item for the live dashboard printout.
        return notifications.get(notifications.size() - 1);
    }

    private void requireRegisteredAccount(String userId) {
        // This stops the service from storing notifications for an unknown account.
        if (!accountsByUserId.containsKey(userId)) {
            throw new IllegalArgumentException("Notification target is not registered: " + userId);
        }
    }

    /**
     * This keeps the account details I need without using a separate User.java file.
     */
    private static class AccountRecord {
        private final String userId;
        private final String username;
        private final String email;

        private AccountRecord(String userId, String username, String email) {
            this.userId = userId;
            this.username = username;
            this.email = email;
        }
    }
}
