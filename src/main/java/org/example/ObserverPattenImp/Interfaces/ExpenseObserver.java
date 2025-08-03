package org.example.ObserverPattenImp.Interfaces;

import org.example.UtilityClasses.Expense;

public interface ExpenseObserver {

    // called when a new expense is added to the system
    void onExpenseAdded(Expense expense);

    // called when an expense is updated in the system
    void onExpenseUpdated(Expense expense);
}
