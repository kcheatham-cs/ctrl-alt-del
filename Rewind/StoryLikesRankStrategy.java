import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Rewind.BaseRankStrategy;
/*
* CLASS: StoryLikesRankStrategy
* EXTENDS: BaseRankStrategy
* STRATEGY: Ranks users by how many times they LIKED the main user's STORIES
* Created: Kiersten
 */
public class StoryLikesRankStrategy extends BaseRankStrategy{
    @Override
    public List<User> rank(List<ActivityEvent> events, String mainUser, int topN){
        Map<User, Integer> counts = new HashMap<>();

        for (ActivityEvent e : events) {
            if (e.getToUser().equals(mainUser) && e.getType().equals("LIKE_STORY")) {   /// check if .equals right or should i user a comparator
                counts.put(e.getFromUser(), counts.getOrDefault(e.getFromUser(), 0) + 1);
            }
        }

        return getTopN(counts, topN);

    }

}
