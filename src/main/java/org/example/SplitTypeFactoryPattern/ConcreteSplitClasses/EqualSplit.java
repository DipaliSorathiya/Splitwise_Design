package org.example.SplitTypeFactoryPattern.ConcreteSplitClasses;

import org.example.SplitTypeFactoryPattern.Split;
import org.example.UtilityClasses.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqualSplit implements Split {


    @Override
    public Map<User, Double> calculateSplit(double amount, List<User> participants, Map<String, Object> splitDetails) {
            double amountPerPerson = amount/participants.size(); //divide the amount equally among all participants.
            Map<User,Double> splits = new HashMap<>(); // map to hold the calculated split.
            for(User user: participants){
                splits.put(user,amountPerPerson);  // assign each participant the equal amount.
            }
            return splits;
    }
}
