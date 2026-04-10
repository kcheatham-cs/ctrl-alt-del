const express = require('express');
const app = express();

const routes = require('./api/routes');
const userService = require('./services/UserService');

app.use(express.json());

// Load your simulated users
userService.loadUsers();

// Routes
app.use('/api', routes);

const PORT = 3000;

app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});
