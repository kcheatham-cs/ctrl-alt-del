package Rewind;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/*
* CLASS: BaseRankStrategy
* IMPLEMENTS: RewindRankStrategy
* Serves as a way to abstract the protected method getTopN() for all strategies and connect them to their interface 
*/

import RewindRankStrategy;

public abstract class BaseRankStrategy implements RewindRankStrategy{
    /*
    * Shared helper — implemented here once instead of in all four strategies
    * @Params counts - a map whos keys (String ) are users and values(Integers) are the amount of events enacted
    * @Params n -  an int value representing the amount of users that should be returned 
    * @Returns a List of the Top N Users 
    */
    protected List<User> getTopN(Map<User, Integer> counts, int n) {
        return counts.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .limit(n)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    // may want it to return the values as well??

}
