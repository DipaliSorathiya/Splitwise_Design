package org.example.UtilityClasses;

import java.util.List;
import java.util.Map;

public class Expense {

    private String id;
    private String description;
    private double amount;
    private User payer;
    private List<User> participants;
    private Map<User,Double> shares;

    public Expense(String id,String description,double amount, User payer, List<User> participants,Map<User,Double> shares){
        this.id = id;
        this.description = description;
        this.amount  = amount;
        this.payer = payer;
        this.participants = participants;
        this.shares = shares;
    }

    public String getId() {return id;}
    public String getDescription() { return description;}

    public double getAmount() { return amount;}

    public User getPayer() { return payer;}

    public List<User> getParticipants() { return participants;}

    public Map<User,Double> getShares() { return shares;}


}
