const express = require('express');
const router = express.Router();

const userService        = require('../services/UserService');
const SearchService      = require('../services/SearchService');
const activityService    = require('../services/ActivityService');
const notificationService = require('../services/NotificationService');
const rewindService      = require('../services/RewindService');

router.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', 'Content-Type');
  next();
});

const searchService = new SearchService(userService);

router.get('/status', (req, res) => {
  res.json({
    message: 'Backend is working',
    time: new Date(),
    totalUsers: userService.getAllUsers().length
  });
});

router.get('/users', (req, res) => {
  res.json(userService.getAllUsers());
});

router.get('/users/:id', (req, res) => {
  const user = userService.getUserById(parseInt(req.params.id));
  if (!user) return res.status(404).json({ error: 'User not found' });
  res.json(user);
});

//Login
router.get('/login/:id', (req, res) => {
  const user = userService.login(parseInt(req.params.id));
  if (!user) return res.status(404).json({ error: 'User not found' });
  res.json(user);
});

router.post('/login', (req, res) => {
  const { username, password } = req.body;
  if (username === 'admin' && password === '1234') {
    const user = userService.getUserById(1);
    return res.json({ success: true, user });
  }
  res.status(401).json({ success: false, error: 'Invalid credentials' });
});

//Search
router.get('/search', (req, res) => {
  const result = searchService.search(req.query.q);
  res.json(result);
});

//Dashboard
router.get('/dashboard/:id', (req, res) => {
  const user = userService.getUserById(parseInt(req.params.id));
  if (!user) return res.status(404).json({ error: 'User not found' });

  const notifications = notificationService.getNotifications(user.id);
  res.json({
    ...user.toJSON(),
    notifications: notifications.slice(0, 5),
    notificationCount: notifications.length
  });
});

//Notifications
router.get('/notifications/:id', (req, res) => {
  const userId = parseInt(req.params.id);
  const user = userService.getUserById(userId);
  if (!user) return res.status(404).json({ error: 'User not found' });
  res.json(notificationService.getNotifications(userId));
});

//Rewind
router.get('/rewind/:id', (req, res) => {
  const userId = parseInt(req.params.id);
  const summary = rewindService.generateRewind(userId);
  if (!summary) return res.status(404).json({ error: 'User not found' });
  res.json(summary);
});

//Simulation
router.post('/simulate/:id', (req, res) => {
  const userId = parseInt(req.params.id);
  const user = userService.getUserById(userId);
  if (!user) return res.status(404).json({ error: 'User not found' });

  const others = userService.getAllUsers().filter(u => u.id !== userId);
  const actor  = others[Math.floor(Math.random() * others.length)];

  const pool = [
    'LIKE_POST', 'LIKE_POST', 'LIKE_POST',
    'LIKE_STORY', 'LIKE_STORY',
    'COMMENT', 'COMMENT',
    'VIEW_STORY', 'VIEW_STORY',
    'FOLLOW', 'FOLLOW',
    'UNFOLLOW'
  ];
  const type = pool[Math.floor(Math.random() * pool.length)];

  const event = activityService.addEvent(actor, user, type);

  //unfollow events 
  if (type === 'UNFOLLOW') {
    notificationService.addUnfollowEvent(event);
  }

  const labels = {
    LIKE_POST:  `@${actor.username} liked your post`,
    LIKE_STORY: `@${actor.username} liked your story`,
    COMMENT:    `@${actor.username} commented on your post`,
    VIEW_STORY: `@${actor.username} viewed your story`,
    FOLLOW:     `@${actor.username} followed you`,
    UNFOLLOW:   `@${actor.username} unfollowed you`
  };

  const notifications = notificationService.getNotifications(userId);
  res.json({
    ...user.toJSON(),
    notifications: notifications.slice(0, 5),
    notificationCount: notifications.length,
    simulatedEvent: labels[type]
  });
});

module.exports = router;
