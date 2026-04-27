const express = require('express');
const path = require('path');
const app = express();

const routes = require('./api/routes');
const userService = require('./services/UserService');
const activityService = require('./services/ActivityService');
const notificationService = require('./services/NotificationService');
const rewindService = require('./services/RewindService');

app.use(express.json());
app.use(express.static(path.join(__dirname, '../frontend')));
app.use('/assets', express.static(path.join(__dirname, '../assets')));

userService.loadUsers();
activityService.generate(userService.users);
notificationService.load(activityService);
rewindService.init(activityService, userService);

app.use('/api', routes);

app.get('/', (req, res) => {
  res.redirect('/site/about.html');
});

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});
