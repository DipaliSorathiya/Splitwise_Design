package org.example.UtilityClasses;

public class Transaction {
    private User from;
    private User to;
    private double amount;

    public Transaction(User from,User to,double amount){
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public User getFrom() { return from;}
    public User getTo() { return to;}
    public double getAmount() { return amount;}
}
