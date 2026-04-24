const userService = require('./UserService');

class LookupService {
  constructor() {
    // Generate phone numbers for users if not present
    this.phoneMap = new Map();
    this.generatePhones();
  }

  generatePhones() {
    const users = userService.getAllUsers();
    users.forEach(user => {
      // Generate a fake phone number based on id
      const phone = `+1-${String(user.id).padStart(3, '0')}-${String(user.id * 10 % 1000).padStart(3, '0')}-${String(user.id * 100 % 10000).padStart(4, '0')}`;
      this.phoneMap.set(user.id, phone);
    });
  }

  lookupFollower(loggedInUserId, queryUsername) {
    const loggedInUser = userService.getUserById(loggedInUserId);
    if (!loggedInUser) return null;

    // Find if the queryUsername is a follower
    const followers = Array.from(loggedInUser.followers);
    for (const fid of followers) {
      const follower = userService.getUserById(fid);
      if (follower && follower.username === queryUsername) {
        return {
          username: follower.username,
          email: follower.email,
          phone: this.phoneMap.get(follower.id)
        };
      }
    }
    return null; // Not a follower or not found
  }

  getUserContact(loggedInUserId) {
    const user = userService.getUserById(loggedInUserId);
    if (!user) return null;
    return {
      username: user.username,
      email: user.email,
      phone: this.phoneMap.get(user.id)
    };
  }
}

module.exports = new LookupService();