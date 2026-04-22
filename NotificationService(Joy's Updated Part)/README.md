# Joy's Backend Notification System

Observer pattern backend for the unfollow notification feature.

## Files
- `DashboardNotification`
- `UnfollowEvent`
- `FollowerObservable`
- `FollowerObserver`
- `NotificationService`
- `DemoActivitySimulator`
- `NotificationDemo`

## Flow
1. A follower change happens in `DemoActivitySimulator`.
2. An `UnfollowEvent` is created with ids and usernames.
3. `NotificationService` receives the event.
4. The alert is printed to the terminal.
5. A dashboard-ready notification object is stored for the target user.

## Notes for Brandon
- Joy's code does not depend on `User.java` anymore.
- `getDashboardNotifications(userId)` is the main method to expose to the dashboard.
- `registerAccount(...)` is the current placeholder for real backend account data.

## Run
```powershell
javac -d out src\main\java\com\ctrlaltdelete\notification\*.java
java -cp out com.ctrlaltdelete.notification.NotificationDemo
```
