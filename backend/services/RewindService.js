class RewindService {
  constructor() {
    this.activityService = null;
    this.userService = null;
  }

  init(activityService, userService) {
    this.activityService = activityService;
    this.userService = userService;
  }

  _rankByType(userId, type, topN) {
    const events = this.activityService
      .getEventsForUser(userId)
      .filter(e => e.type === type);

    const counts = {};
    events.forEach(e => {
      counts[e.fromUserId] = (counts[e.fromUserId] || 0) + 1;
    });

    return Object.entries(counts)
      .sort((a, b) => b[1] - a[1])
      .slice(0, topN)
      .map(([uid, count]) => {
        const user = this.userService.getUserById(parseInt(uid));
        return { username: user ? user.username : `user${uid}`, count };
      });
  }

  getTopPostLikers(userId, topN = 8)    { return this._rankByType(userId, 'LIKE_POST',   topN); }
  getTopStoryLikers(userId, topN = 8)   { return this._rankByType(userId, 'LIKE_STORY',  topN); }
  getTopCommenters(userId, topN = 8)    { return this._rankByType(userId, 'COMMENT',     topN); }
  getTopStoryViewers(userId, topN = 8)  { return this._rankByType(userId, 'VIEW_STORY',  topN); }

  generateRewind(userId) {
    const user = this.userService.getUserById(userId);
    if (!user) return null;

    const events = this.activityService.getEventsForUser(userId);

    return {
      username: user.username,
      topPostLikers:   this.getTopPostLikers(userId),
      topStoryLikers:  this.getTopStoryLikers(userId),
      topCommenters:   this.getTopCommenters(userId),
      topStoryViewers: this.getTopStoryViewers(userId),
      totalFollowers:  user.followers.size,
      totalPostLikes:  events.filter(e => e.type === 'LIKE_POST').length,
      totalStoryLikes: events.filter(e => e.type === 'LIKE_STORY').length,
      totalStoryViews: events.filter(e => e.type === 'VIEW_STORY').length,
      totalComments:   events.filter(e => e.type === 'COMMENT').length
    };
  }
}

module.exports = new RewindService();
