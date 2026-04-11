package com.ctrlaltdelete.notification;

public interface FollowerObserver {
    // This receives the unfollow event after the observable publishes it.
    void onUnfollow(UnfollowEvent event);
}
