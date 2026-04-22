package com.ctrlaltdelete.notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class fakes follow and unfollow activity so I can test my notification
 * system before Brandon connects the real backend and frontend pieces.
 */
public class DemoActivitySimulator implements FollowerObservable {
    private final List<FollowerObserver> observers = new ArrayList<>();

    // This maps each user id to a username so the demo can build readable alerts.
    private final Map<String, String> usernamesByUserId = new LinkedHashMap<>();

    // This stores who follows each target user during the simulation.
    private final Map<String, Set<String>> followersByTargetUserId = new LinkedHashMap<>();

    /**
     * This registers a user profile for the simulation.
     * I only keep the fields I need for the notification flow.
     */
    public void registerProfile(String userId, String username) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(username, "username cannot be null");
        usernamesByUserId.put(userId, username);
        followersByTargetUserId.putIfAbsent(userId, new LinkedHashSet<>());
    }

    @Override
    public void addObserver(FollowerObserver observer) {
        Objects.requireNonNull(observer, "observer cannot be null");

        // This prevents me from adding the same observer twice.
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(FollowerObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(UnfollowEvent event) {
        // This sends the same event to every observer that subscribed.
        for (FollowerObserver observer : observers) {
            observer.onUnfollow(event);
        }
    }

    /**
     * This adds starter follower data so I can run the demo with a known setup.
     */
    public void seedFollow(String followerUserId, String targetUserId) {
        requireRegisteredUser(followerUserId);
        requireRegisteredUser(targetUserId);
        followersByTargetUserId.get(targetUserId).add(followerUserId);
    }

    /**
     * This removes one follower and creates the event that drives the notification system.
     * It returns false when there is no follow relationship to remove.
     */
    public boolean simulateUnfollow(String followerUserId, String targetUserId) {
        requireRegisteredUser(followerUserId);
        requireRegisteredUser(targetUserId);

        Set<String> followerIds = followersByTargetUserId.get(targetUserId);

        // If the follower was never attached to the target, there is nothing to notify.
        if (!followerIds.remove(followerUserId)) {
            return false;
        }

        // I create the event with both ids and usernames so the next layer has everything it needs.
        UnfollowEvent event = new UnfollowEvent(
                followerUserId,
                usernamesByUserId.get(followerUserId),
                targetUserId,
                usernamesByUserId.get(targetUserId),
                LocalDateTime.now()
        );
        notifyObservers(event);
        return true;
    }

    public int getFollowerCount(String targetUserId) {
        requireRegisteredUser(targetUserId);
        return followersByTargetUserId.get(targetUserId).size();
    }

    private void requireRegisteredUser(String userId) {
        // This fails early if the demo tries to use a profile I never registered.
        if (!usernamesByUserId.containsKey(userId)) {
            throw new IllegalArgumentException("Unknown user id: " + userId);
        }
    }
}
