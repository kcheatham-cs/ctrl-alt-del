import java.util.List;

/* 
* INTERFACE: RewindRankStrategy
* Any concrete strategy class that implements this interface must provide an implemenation of the rank() method.
* Used by Rewind.java
* Created: Kiersten
*/

/* 
* Ranks users by an engagement metric then returns the top N users
* @params events - Full activity log 
* @params mainUser - The logged in user's username
* @params topN - How many users to return
* @return - Ordered list of username, with the first being the most engaged
) */
public interface RewindRankStrategy {
    List<User> rank(List<ActivityEvent> events, String mainUser, int topN);
}

// go over what the activity event class looks like!!
