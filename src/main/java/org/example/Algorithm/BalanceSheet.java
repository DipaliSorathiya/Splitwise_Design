package org.example.Algorithm;

import org.example.ObserverPattenImp.Interfaces.ExpenseObserver;
import org.example.UtilityClasses.Expense;
import org.example.UtilityClasses.Transaction;
import org.example.UtilityClasses.UserPair;
import org.example.UtilityClasses.User;

import java.util.*;


public class BalanceSheet  implements ExpenseObserver {

    // store the net balance between pairs of users
    private Map<UserPair,Double> balances = new HashMap<>();
    @Override
    public void onExpenseAdded(Expense expense) {

    }

    @Override
    public void onExpenseUpdated(Expense expense) {

    }

    /**
     * updates the balances based on a new or updated expense
     * - each participant's share is added to their balance with the payer.
     * - @param expense the expense to process.
     *
     */
    private void updateBalances(Expense expense) {
        User payer = expense.getPayer();  // user who paid for the expense
        Map<User, Double> shares = expense.getShares();
        for (Map.Entry<User, Double> entry : shares.entrySet()) {
            User participant = entry.getKey();
            Double amount = entry.getValue();
            if (!participant.equals(payer)) {
                UserPair userPair = new UserPair(participant, payer);
                Double currentBalance = balances.getOrDefault(userPair, 0.0);
                balances.put(userPair, currentBalance + amount);
            }
        }
    }

        /**
         * gets the net balance between two users
         *   @return the amount user1 owns user2 - -ne if user2 owns user1.
         */

        public double getBalance(User user1, User user2){

            UserPair pair1 = new UserPair(user1,user2);
            UserPair pair2 = new UserPair(user2,user1);

            double balance1 = balances.getOrDefault(pair1,0.0);
            double balance2 = balances.getOrDefault(pair2,0.0);
            return balance1 - balance2;

        }

    /**
     * calculates the total balance for a single user.
     * the balance is -ne if the user owns money and positive if they are owned money.
     * @return - total balance for the user.
     */
    public double getTotalBalance(User user){
        double total = 0.0;
        for(Map.Entry<UserPair,Double> entry:balances.entrySet()){
            UserPair pair = entry.getKey();
            double amount = entry.getValue();

            if(pair.getUser1().equals(user)){
                total -= amount;
            }else if(pair.getUser2().equals(user)){
                total += amount;
            }
        }
        return total;
    }

    /**
     * simplifies the balances into a list of transaction of settle all debts.
     */

    public List<Transaction> getSimplifiedSettlement(){
        // step : 1 calculate net balances for each user
       Map<User,Double> netBalances = new HashMap<>();
       for(Map.Entry<UserPair,Double> entry:balances.entrySet()){
           UserPair pair = entry.getKey();
           double amount = entry.getValue();

           User debtor = pair.getUser1(); // user who ownes money
           User creditor = pair.getUser2();

           netBalances.put(debtor,netBalances.getOrDefault(debtor,0.0)-amount);
           netBalances.put(creditor,netBalances.getOrDefault(creditor,0.0)+amount);
       }
       // step : 2 seperate users into debtors and creditors
        List<User> debtors = new ArrayList<>();
       List<User> creditors = new ArrayList<>();
       for(Map.Entry<User,Double> entry: netBalances.entrySet()){
           User user = entry.getKey();
           double balance = entry.getValue();
           if(balance < 0){
               debtors.add(user);
           }
           else if(balance > 0){
               creditors.add(user);
           }
       }

       // step : 3 match debtors and creditors to create transactions
        List<Transaction> transactions = new ArrayList<>();
       int debtorIndex =0;
       int creditorIndex =0;

       while(debtorIndex < debtors.size() && creditorIndex < creditors.size()){
           User debtor = debtors.get(debtorIndex);
           User creditor = creditors.get(creditorIndex);

           double debtorBalance = netBalances.get(debtor);
           double creditBalance = netBalances.get(creditor);

           // determine the transfer amount as the smaller of the balances
           double trasferAmount = Math.min(Math.abs(debtorBalance),creditBalance);
           // create a trasaction for the trasfer amount.
           transactions.add(new Transaction(debtor,creditor,trasferAmount));
           // update net balances after the trasfer amount
           netBalances.put(debtor,debtorBalance + trasferAmount);
           netBalances.put(creditor,creditBalance - trasferAmount);

           // move to the next debtor or creditor if their balance is settled.
           if(Math.abs(netBalances.get(debtor)) < 0.001){
               debtorIndex++;
           }
           if(Math.abs(netBalances.get(creditor)) < 0.001){
               creditorIndex++;
           }


       }
       return transactions;

    }

    /**
     * calculates the minimum number of transactions
     */



}
