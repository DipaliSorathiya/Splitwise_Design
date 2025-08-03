package org.example.SplitTypeFactoryPattern.ConcreteSplitClasses;

import org.example.SplitTypeFactoryPattern.Split;
import org.example.UtilityClasses.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PercentageSplit implements Split {
    @Override
    public Map<User, Double> calculateSplit(double amount, List<User> participants, Map<String, Object> splitDetails) {
        // retrieve the percentage allocation for each participant from the split details.
        Map<User,Double> percentages = (Map<User, Double>) splitDetails.get("percentages");
        Map<User,Double> splits = new HashMap<>(); // map to hold the calculated split

        for(User user: participants){
            double percentage = percentages.getOrDefault(user,0.0); // get the percentage for the user
            splits.put(user,amount*percentage/100.0);
        }
        return splits;
    }
}
