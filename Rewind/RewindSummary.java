import java.util.List;
/*
* CLASS: RewindSummary
* A plain data container that holds all of the rewind stat results 
* Created: Kiersten
*/
public class RewindSummary {
    // Fields
    // Logged in User
    private String mainUser;
    // Lists of the usernames with the top N specified engagment event
    private List<User> topCommenters;
    private List<User> topPostLikers;
    private List<User> topStoryLikers;
    private List<User> topViewers;
    // Raw count of how many users followed mainUser 
    private long totalFollowers;
    // Raw count of how many users viewed the mainUser's story
    private long totalStoryViews;
    // Raw count of how many users commented on the mainUser's posts
    private long totalComments;

    // Constructor
    public RewindSummary(String mainUser, List<User> topCommenters, List<User> topPostLikers, List<User> topStoryLikers,
                         List<User> topViewers, long totalFollowers, long totalStoryViews, long totalComments){
        this.mainUser = mainUser;
        this.topCommenters = topCommenters;
        this.topPostLikers = topPostLikers;
        this.topStoryLikers = topStoryLikers;
        this.topViewers = topViewers;
        this.totalFollowers = totalFollowers;
        this.totalStoryViews = totalStoryViews;
        this.totalComments = totalComments;
    }

    // Getter Methods
    public String getMainUsername(){
        return mainUser;
    }
    
    public List<User> getTopCommenters(){
        return topCommenters;
    }

    public List<User> getTopPostLikers(){
        return topPostLikers;
    }

    public List<User> getTopStoryLikers(){
        return topStoryLikers;
    }

    public List<User> getTopViewers(){
        return topViewers;
    }

    public long getTotalFollowers(){
        return totalFollowers;
    }

    public long getTotalStoryViews(){
        return totalStoryViews;
    }

    public long getTotalComments(){
        return totalComments;
    }
}
