
class ActivityService {
  constructor() {
    this.events = []; // Array of plain event objects
  }

  generate(userMap) {
    const users = Array.from(userMap.values());
    const engagementTypes = ['LIKE_POST', 'LIKE_STORY', 'COMMENT', 'VIEW_STORY'];

    users.forEach(actor => {
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

    users.forEach(user => {
      user.postLikes     = this.events.filter(e => e.toUserId === user.id && e.type === 'LIKE_POST').length;
      user.storyLikes    = this.events.filter(e => e.toUserId === user.id && e.type === 'LIKE_STORY').length;
      user.commentsCount = this.events.filter(e => e.toUserId === user.id && e.type === 'COMMENT').length;
      user.followerCount = user.followers.size; // snapshot after all follows are applied
    });

    console.log(`Generated ${this.events.length} activity events`);
  }

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

  getEventsForUser(userId) {
    return this.events.filter(e => e.toUserId === userId);
  }

  getUnfollowsForUser(userId) {
    return this.events.filter(e => e.toUserId === userId && e.type === 'UNFOLLOW');
  }
}

module.exports = new ActivityService();
