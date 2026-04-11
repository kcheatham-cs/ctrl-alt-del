const express = require('express');
const path = require('path');
const app = express();

const routes = require('./api/routes');
const userService = require('./services/UserService');
const activityService = require('./services/ActivityService');
const notificationService = require('./services/NotificationService');
const rewindService = require('./services/RewindService');

app.use(express.json());

// Serve frontend files statically from the frontend/ folder
app.use(express.static(path.join(__dirname, '../frontend')));

// Boot sequence: load users → generate activity → seed notifications → init rewind
userService.loadUsers();
activityService.generate(userService.users);
notificationService.load(activityService);
rewindService.init(activityService, userService);

// API routes
app.use('/api', routes);

// Root redirect → login page
app.get('/', (req, res) => {
  res.redirect('/site/login.html');
});

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});
