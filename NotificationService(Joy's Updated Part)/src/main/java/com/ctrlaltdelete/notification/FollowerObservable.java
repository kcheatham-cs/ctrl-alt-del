package com.ctrlaltdelete.notification;

public interface FollowerObservable {
    // This lets me attach the notification service to any class that creates follower events.
    void addObserver(FollowerObserver observer);

    void removeObserver(FollowerObserver observer);

    void notifyObservers(UnfollowEvent event);
}
