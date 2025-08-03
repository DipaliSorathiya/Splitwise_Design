package org.example.ObserverPattenImp.Interfaces;

import org.example.UtilityClasses.Expense;

public interface ExpenseSubject {
    // adds on observer to the notification list.
    void addObserver(ExpenseObserver observer);

    // removes an observer from the notification list.
    void removeObserver(ExpenseObserver observer);

    // notifies all observers about a new expense.
    void notifyExpenseAdded(Expense expense);

    // notifies all observes about an updated expense.
    void notifyExpenseUpdated(Expense expense);


}
