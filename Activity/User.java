import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User {

/* Not fully correct just a reference
    private final String userId;
    private final String username;
    private final String email;
    private final List<String> followers;
    private final List<String> following;

    public User(String userId, String username, String email) {
        this(userId, username, email, List.of(), List.of());
    }

    public User(String userId, String username, String email,
                List<String> followers, List<String> following) {
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.username = Objects.requireNonNull(username, "username cannot be null");
        this.email = Objects.requireNonNull(email, "email cannot be null");
        this.followers = new ArrayList<>(Objects.requireNonNull(followers, "followers cannot be null"));
        this.following = new ArrayList<>(Objects.requireNonNull(following, "following cannot be null"));
    }

    //getter methods 
     public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getFollowers() {
        return Collections.unmodifiableList(followers);
    }

    public List<String> getFollowing() {
        return Collections.unmodifiableList(following);
    }
 */
//ceionna portion
    public void follow(User target, NotificationService notificationService) {
    if (target == null || target == this) return;

    if (following.add(target)) {
        target.followers.add(this);

        ActivityEvent event = new ActivityEvent(this, target, ActivityType.FOLLOW);

        recordActivity(event);
        target.recordActivity(event);

        if (notificationService != null) {
            notificationService.notify(event);
        }
    }
}

public void unfollow(User target, NotificationService notificationService) {
    if (target == null || target == this) return;

    if (following.remove(target)) {
        target.followers.remove(this);

        ActivityEvent event = new ActivityEvent(this, target, ActivityType.UNFOLLOW);

        recordActivity(event);
        target.recordActivity(event);

        if (notificationService != null) {
            notificationService.notify(event);
        }
    }
}

public void likePost(Post post, NotificationService notificationService) {
    if (post == null) return;

    if (post.addLike(this)) {
        ActivityEvent event = new ActivityEvent(this, post.getOwner(), ActivityType.LIKE_POST);

        recordActivity(event);
        post.getOwner().recordActivity(event);

        if (notificationService != null) {
            notificationService.notify(event);
        }
    }
}

public void likeStory(Story story, NotificationService notificationService) {
    if (story == null) return;

    if (story.addLike(this)) {
        ActivityEvent event = new ActivityEvent(this, story.getOwner(), ActivityType.LIKE_STORY);

        recordActivity(event);
        story.getOwner().recordActivity(event);

        if (notificationService != null) {
            notificationService.notify(event);
        }
    }
}

public void viewStory(Story story, NotificationService notificationService) {
    if (story == null) return;

    if (story.addView(this)) {
        ActivityEvent event = new ActivityEvent(this, story.getOwner(), ActivityType.VIEW_STORY);

        recordActivity(event);
        story.getOwner().recordActivity(event);

        if (notificationService != null) {
            notificationService.notify(event);
        }
    }
}

public void commentOnPost(Post post, String comment, NotificationService notificationService) {
    if (post == null || comment == null || comment.isBlank()) return;

    post.addComment(username + ": " + comment);

    ActivityEvent event = new ActivityEvent(this, post.getOwner(), ActivityType.COMMENT, comment);

    recordActivity(event);
    post.getOwner().recordActivity(event);

    if (notificationService != null) {
        notificationService.notify(event);
    }
}

}
