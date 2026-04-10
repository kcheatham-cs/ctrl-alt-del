class SearchService {
  constructor(userService) {
    this.userService = userService;
  }

  search(query) {
    return this.userService.getAllUsers().filter(user =>
      user.email === query || user.id.toString() === query
    );
  }
}

module.exports = SearchService;
