const fs = require('fs');

const NUM_USERS = 100;

function randomInt(max) {
  return Math.floor(Math.random() * max);
}

function generateUsers() {
  const users = [];

  for (let i = 1; i <= NUM_USERS; i++) {
    const user = {
      id: i,
      username: `user${i}`,
      email: `user${i}@test.com`,
      followers: [],
      following: [],
      posts: []
    };

    users.push(user);
  }

  // Create random follow relationships
  users.forEach(user => {
    const numFollows = randomInt(10);

    for (let i = 0; i < numFollows; i++) {
      const target = users[randomInt(NUM_USERS)];

      if (target.id !== user.id && !user.following.includes(target.id)) {
        user.following.push(target.id);
        target.followers.push(user.id);
      }
    }
  });

  fs.writeFileSync('users.json', JSON.stringify(users, null, 2));
  console.log("users.json generated!");
}

generateUsers();
