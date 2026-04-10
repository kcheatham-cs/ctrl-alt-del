const express = require('express');
const router = express.Router();

const userService = require('../services/UserService');
const SearchService = require('../services/SearchService');

const searchService = new SearchService(userService);




router.get('/status', (req, res) => {
  res.json({
    message: "Backend is working",
    time: new Date(),
    totalUsers: userService.getAllUsers().length
  });
});
// Get all users
router.get('/users', (req, res) => {
  res.json(userService.getAllUsers());
});

// Get user by ID
router.get('/users/:id', (req, res) => {
  const user = userService.getUserById(parseInt(req.params.id));
  res.json(user);
});

// Login simulation
router.get('/login/:id', (req, res) => {
  const user = userService.login(parseInt(req.params.id));
  res.json(user);
});

// Search by email or ID
router.get('/search', (req, res) => {
  const result = searchService.search(req.query.q);
  res.json(result);
});

module.exports = router;
