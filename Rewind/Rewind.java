import java.util.List;

import Rewind.CommentsRankStrategy;
import Rewind.PostLikesRankStrategy;

/*
* CLASS: Rewind
* Context class for the strategy design pattern
* Utilizes the events from the activity log to create and compile stats for the RewindSummary to store 
* Created: Kiersten
*/

public class Rewind {
    // Fields
    // Logged in User
    private String mainUser;
    // Full list of every action that occured
    private List<ActivityEvent> activityLog;
    // Four independent strategies for ranking 
    private RewindRankStrategy commentsStr;
    private RewindRankStrategy postLikesStr;
    private RewindRankStrategy storyLikesStr;
    private RewindRankStrategy storyViewsStr;

    // Constructor
    public Rewind(String mainUser, List<ActivityEvent> activityLog){
        this.mainUser = mainUser;
        this.activityLog = activityLog;
        this.commentsStr = new CommentsRankStrategy();
        this.postLikesStr = new PostLikesRankStrategy();
        this.storyLikesStr = new StoryLikesRankStrategy();
        this.storyViewsStr = new StoryViewsRankStrategy();

    }

    // Ranked list using strategy methods 

    // Returns top N users who liked the most of the main user's POSTS.
    public List<User> getTopPostLikers(int topN){
        return postLikesStr.rank(activityLog, mainUser, topN);
    }
    // Returns top N users who liked the most of the main main user's STORIES.
     public List<User> getTopStoryLikers(int topN){
        return storyLikesStr.rank(activityLog, mainUser, topN);
    }
    // Returns top N users who commented the most on the  main user's POSTS.
     public List<User> getTopCommenters(int topN){
        return commentsStr.rank(activityLog, mainUser, topN);
    }
    // Returns top N users who viewed the most of the main user's STORIES.
    public List<User> getTopViewers(int topN){
        return storyViewsStr.rank(activityLog, mainUser, topN);
    }

    // count engagement events methods 

    //Returns the total number of follow events received by the mainUser
    public long getTotalFollowers(){
        return activityLog.stream()
                        .filter(e -> e.getToUser().equals(mainUser))
                        .filter(e -> e.getType().equals("FOLLOW"))
                        .count();
    }

     //Returns the total number of story views events received by the mainUser
    public long getTotalStoryViews(){
       return activityLog.stream()
                        .filter(e -> e.getToUser().equals(mainUser))
                        .filter(e -> e.getType().equals("VIEW_STORY"))
                        .count();
    }

    //Returns the total number of comment events received by the mainUser
    public long getTotalComments(){
       return activityLog.stream()
                        .filter(e -> e.getToUser().equals(mainUser))
                        .filter(e -> e.getType().equals("COMMENTS"))
                        .count();
    }

    // Possibly add : total likes received (story & post or seperate), or total engagement events the user enacted instead of received 
        // if so wuld have to update rewindSummary

    // Compilng summary
    // Generates a RewindSummary Object that contains all the stats in one location
    public RewindSummary generateRewind(){
        return new RewindSummary(
            mainUser,
            getTopCommenters(8), // can always change this numbers
            getTopPostLikers(8),
            getTopStoryLikers(8),
            getTopViewers(8),
            getTotalFollowers(),
            getTotalStoryViews(),
            getTotalComments()
        );
    }



}
