// JS port of Joy's NotificationService.java (Observer pattern).
// Stores unfollow notifications keyed by the target user's ID,
// ready to be served to the dashboard via the API.

class NotificationService {
  constructor() {
    // Map<userId, DashboardNotification[]>
    this.notificationsByUserId = new Map();
  }

  // Seed notifications from the activity log at startup.
  load(activityService) {
    activityService.getEventsForUser === undefined; // no-op guard
    const unfollows = activityService.events.filter(e => e.type === 'UNFOLLOW');
    unfollows.forEach(event => this._store(event));
    console.log(`Loaded ${unfollows.length} unfollow notifications`);
  }

  // Add a single notification at runtime (used by the /simulate endpoint).
  addUnfollowEvent(event) {
    this._store(event);
  }

  // Dashboard-friendly notification objects for a given user.
  getNotifications(userId) {
    const list = this.notificationsByUserId.get(userId) || [];
    return list.slice().sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
  }

  getUnreadCount(userId) {
    return (this.notificationsByUserId.get(userId) || []).filter(n => !n.read).length;
  }

  _store(event) {
    const list = this.notificationsByUserId.get(event.toUserId) || [];
    list.push({
      id: `${event.toUserId}-unfollow-${list.length}`,
      targetUserId: event.toUserId,
      eventType: 'UNFOLLOW',
      title: 'Follower Update',
      message: `@${event.fromUsername} unfollowed you`,
      timestamp: event.timestamp,
      read: false
    });
    this.notificationsByUserId.set(event.toUserId, list);
  }
}

module.exports = new NotificationService();
