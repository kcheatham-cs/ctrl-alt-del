// JS port of the Activity system (ActivityEvent.java + ActivityType.java).
// Generates simulated engagement events at startup and stores them in memory.
// Other services (NotificationService, RewindService) consume this data.

class ActivityService {
  constructor() {
    this.events = []; // Array of plain event objects
  }

  // Called once at startup. Populates events and back-fills stats onto User objects.
  generate(userMap) {
    const users = Array.from(userMap.values());
    const engagementTypes = ['LIKE_POST', 'LIKE_STORY', 'COMMENT', 'VIEW_STORY'];

    users.forEach(actor => {
      // Each user performs 5–25 random engagement actions against other users
      const count = 5 + Math.floor(Math.random() * 20);
      for (let i = 0; i < count; i++) {
        const target = users[Math.floor(Math.random() * users.length)];
        if (target.id === actor.id) continue;
        const type = engagementTypes[Math.floor(Math.random() * engagementTypes.length)];
        this.events.push({
          fromUserId: actor.id,
          fromUsername: actor.username,
          toUserId: target.id,
          toUsername: target.username,
          type,
          timestamp: new Date(Date.now() - Math.floor(Math.random() * 30) * 24 * 60 * 60 * 1000)
        });
      }

      // Simulate unfollows: ~20% chance a user unfollows each person they currently follow
      Array.from(actor.following).forEach(targetId => {
        if (Math.random() < 0.2) {
          const target = userMap.get(targetId);
          if (target) {
            this.events.push({
              fromUserId: actor.id,
              fromUsername: actor.username,
              toUserId: target.id,
              toUsername: target.username,
              type: 'UNFOLLOW',
              timestamp: new Date(Date.now() - Math.floor(Math.random() * 7) * 24 * 60 * 60 * 1000)
            });
          }
        }
      });
    });

    // Randomly boost follower counts so every user starts with a realistic number.
    // Each user gains between 10 and 60 additional random followers.
    users.forEach(actor => {
      const newFollows = 10 + Math.floor(Math.random() * 51);
      for (let i = 0; i < newFollows; i++) {
        const target = users[Math.floor(Math.random() * users.length)];
        if (target.id !== actor.id && !actor.following.has(target.id)) {
          actor.following.add(target.id);
          target.followers.add(actor.id);
        }
      }
    });

    // Back-fill engagement counters and snapshot follower count onto User model objects
    users.forEach(user => {
      user.postLikes     = this.events.filter(e => e.toUserId === user.id && e.type === 'LIKE_POST').length;
      user.storyLikes    = this.events.filter(e => e.toUserId === user.id && e.type === 'LIKE_STORY').length;
      user.commentsCount = this.events.filter(e => e.toUserId === user.id && e.type === 'COMMENT').length;
      user.followerCount = user.followers.size; // snapshot after all follows are applied
    });

    console.log(`Generated ${this.events.length} activity events`);
  }

  // Adds a single new event at runtime (used by the /simulate endpoint).
  // Mutates the User objects in-place so toJSON() reflects the change immediately.
  addEvent(fromUser, toUser, type) {
    const event = {
      fromUserId: fromUser.id,
      fromUsername: fromUser.username,
      toUserId: toUser.id,
      toUsername: toUser.username,
      type,
      timestamp: new Date()
    };
    this.events.push(event);

    if (type === 'LIKE_POST')  toUser.postLikes++;
    if (type === 'LIKE_STORY') toUser.storyLikes++;
    if (type === 'COMMENT')    toUser.commentsCount++;
    if (type === 'UNFOLLOW') {
      if (toUser.followerCount > 0) toUser.followerCount--;
    }
    if (type === 'FOLLOW') {
      toUser.followerCount++;
    }

    return event;
  }

  // Returns all events where the given user is the target (received events).
  getEventsForUser(userId) {
    return this.events.filter(e => e.toUserId === userId);
  }

  // Returns only UNFOLLOW events targeting a user.
  getUnfollowsForUser(userId) {
    return this.events.filter(e => e.toUserId === userId && e.type === 'UNFOLLOW');
  }
}

module.exports = new ActivityService();
