const fs = require('fs');
const path = require('path');
const User = require('../models/User');

class UserService {
  constructor() {
    this.users = new Map();
  }

  loadUsers() {
    const filePath = path.join(
      __dirname,
      '../../simulated_users/data/users.json'
    );

    const rawData = JSON.parse(fs.readFileSync(filePath));

    // Create user objects
    rawData.forEach(u => {
      const user = new User(u.id, u.username, u.email);
      this.users.set(user.id, user);
    });

    // Connect relationships
    rawData.forEach(u => {
      const user = this.users.get(u.id);

      u.followers.forEach(fid => user.followers.add(fid));
      u.following.forEach(fid => user.following.add(fid));
    });

    console.log(`Loaded ${this.users.size} users`);
  }

  getUserById(id) {
    return this.users.get(id);
  }

  getAllUsers() {
    return Array.from(this.users.values());
  }

  login(userId) {
    return this.getUserById(userId);
  }
}

module.exports = new UserService();
