import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Rewind.BaseRankStrategy;
/*
* CLASS: StoryViewsRankStrategy
* EXTENDS: BaseRankStrategy
* STRATEGY: Ranks users by how many times they VIEWED the main user's STORIES
* Created: Kiersten
 */
public class StoryViewsRankStrategy extends BaseRankStrategy{
    @Override
    public List<User> rank(List<ActivityEvent> events, String mainUser, int topN){
        Map<User, Integer> counts = new HashMap<>();

        for (ActivityEvent e : events) {
            if (e.getToUser().equals(mainUser) && e.getType().equals("VIEW_STORY")) {   // check if .equals right or should i user a comparator
                counts.put(e.getFromUser(), counts.getOrDefault(e.getFromUser(), 0) + 1);
            }
        }

        return getTopN(counts, topN);
    }

}
