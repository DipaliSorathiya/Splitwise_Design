package org.example.ObserverPattenImp.Classes;

import org.example.ObserverPattenImp.Interfaces.ExpenseObserver;
import org.example.ObserverPattenImp.Interfaces.ExpenseSubject;
import org.example.UtilityClasses.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseManager implements ExpenseSubject {

    private List<ExpenseObserver> observers = new ArrayList<>();
    private List<Expense> expenses= new ArrayList<>();
    @Override
    public void addObserver(ExpenseObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ExpenseObserver observer) {
            observers.remove(observer);
    }

    @Override
    public void notifyExpenseAdded(Expense expense) {
        for(ExpenseObserver observer:observers){
            observer.onExpenseAdded(expense);
        }
    }

    @Override
    public void notifyExpenseUpdated(Expense expense) {
            for(ExpenseObserver observer:observers){
                observer.onExpenseUpdated(expense);
            }
    }

    // adds a new expense to the system and notifies observes
    public void addExpense(Expense expense){
        expenses.add(expense);
        notifyExpenseAdded(expense);
    }

    // updates an existing expense and notifies observes.
    public void updateExpense(Expense expense){
        // find and replace the expense with same id in the list.
        for(int i=0;i<expenses.size();i++){
            if(expenses.get(i).getId().equals(expense.getId())){
                expenses.set(i,expense);
                notifyExpenseUpdated(expense);
                return;
            }
        }
        throw new IllegalArgumentException("Expense with ID"+expense.getId() + "not found.");
    }

    // retrieves all expenses in the system
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }
}
