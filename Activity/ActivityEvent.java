import java.time.LocalDateTime;

public class ActivityEvent {
    private final User fromUser;
    private final User toUser;
    private final ActivityType type;
    private final String content;
    private final LocalDateTime timestamp;

    public ActivityEvent(User fromUser, User toUser, ActivityType type) {
        this(fromUser, toUser, type, "");
    }

    public ActivityEvent(User fromUser, User toUser, ActivityType type, String content) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.type = type;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public User getToUser() { return toUser; }
    public User getFromUser() { return fromUser; }
    public ActivityType getType() { return type; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

}
