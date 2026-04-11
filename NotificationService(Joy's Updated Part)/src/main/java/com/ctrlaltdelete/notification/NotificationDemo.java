package com.ctrlaltdelete.notification;

import java.util.List;

/**
 * This runs a quick backend-only test of my notification flow.
 */
public class NotificationDemo {
    public static void main(String[] args) {
        NotificationService notificationService = new NotificationService();

        // I register the accounts the notification service is allowed to notify.
        notificationService.registerAccount("u100", "joy_demo", "joy@ctrlaltdelete.demo");
        notificationService.registerAccount("u101", "brandon_cad", "brandon@ctrlaltdelete.demo");
        notificationService.registerAccount("u102", "kiersten_ui", "kiersten@ctrlaltdelete.demo");

        DemoActivitySimulator simulator = new DemoActivitySimulator();

        // I register the same users in the simulator so it can build the demo event.
        simulator.registerProfile("u100", "joy_demo");
        simulator.registerProfile("u101", "brandon_cad");
        simulator.registerProfile("u102", "kiersten_ui");
        simulator.addObserver(notificationService);

        // This sets up two followers before I simulate the unfollow.
        simulator.seedFollow("u101", "u100");
        simulator.seedFollow("u102", "u100");

        System.out.println("Before unfollow: " + simulator.getFollowerCount("u100") + " followers");
        boolean eventTriggered = simulator.simulateUnfollow("u101", "u100");
        System.out.println("Event triggered: " + eventTriggered);
        System.out.println("After unfollow: " + simulator.getFollowerCount("u100") + " followers");
        System.out.println("Notifications stored: " + notificationService.getNotificationCount("u100"));

        // This shows the dashboard data Brandon can expose in an API response later.
        List<DashboardNotification> dashboardNotifications = notificationService.getDashboardNotifications("u100");
        System.out.println("Dashboard notifications:");
        for (DashboardNotification notification : dashboardNotifications) {
            System.out.println(" - " + notification);
        }
    }
}
