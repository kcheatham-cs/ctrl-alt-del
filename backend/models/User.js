class User {
  constructor(id, username, email) {
    this.id = id;
    this.username = username;
    this.email = email;

    this.followers = new Set();
    this.following = new Set();
    this.followerCount = 0; // kept in sync by ActivityService; used instead of followers.size

    this.postLikes = 0;
    this.storyLikes = 0;
    this.commentsCount = 0;
  }

  follow(user) {
    this.following.add(user.id);
    user.followers.add(this.id);
  }

  unfollow(user) {
    this.following.delete(user.id);
    user.followers.delete(this.id);
  }

  toJSON() {
    return {
      id: this.id,
      username: this.username,
      email: this.email,
      followers: this.followerCount,
      following: this.following.size,
      postLikes: this.postLikes,
      storyLikes: this.storyLikes,
      commentsCount: this.commentsCount
    };
  }
}

module.exports = User;
