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

    public List<Transaction> getSimplifiedSettlements(){
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
     * calculates the minimum number of transactions needed to settle all balances
     * uses backtracking approach to find the solution
     *  - @return : minimum count needed to settle all debts.
     */
    public int getSubOptimalMinimumSettlements(){
        // Step : 1 calculate net balances for each user
        Map<User,Double> netBalances = new HashMap<>();
        for(Map.Entry<UserPair,Double> entry:balances.entrySet()){
            UserPair pair = entry.getKey();
            double amount = entry.getValue();

            User debtor = pair.getUser1();  // the user who owns money
            User creditor = pair.getUser2(); // the user who is owned money

            // update the net balance of each user
            netBalances.put(debtor,netBalances.getOrDefault(debtor,0.0)-amount);
            netBalances.put(creditor,netBalances.getOrDefault(creditor,0.0)+amount);
        }
        List<Double> creditList = new ArrayList<>();
        for(Map.Entry<User,Double> entry:netBalances.entrySet()){
            if(Math.abs(entry.getValue()) > 0.001){
                creditList.add(entry.getValue()); // store the net balance.
            }
        }
        int n = creditList.size(); // total number of users with non zero balance
        return subOptimalDfs(0,creditList,n); // call dfs to compute the minimum transactions.
    }

    /**
     *  - recursivly finds the minimum number of transactions required to settle debts.
     *  - uses a greedy approach by settling the current user's balance with future users.
     *  - currentUserIndex : index of the user whose balance needs to be settled.
     *  - creditList list of net balances for all users.
     *  - n number of users with non-zero balances.
     *  - minimum transactions required to settle all debts.
     */
     private int subOptimalDfs(int currentUserIndex,List<Double> creditList,int n){
         // skip already settled users( those with zero balance)
         while(currentUserIndex < n && creditList.get(currentUserIndex)==0){
             currentUserIndex++;
         }
         // base case : if all users have zero balance, no further transactions are needed.
         if(currentUserIndex == n)
             return 0;
         int cost = Integer.MAX_VALUE;
         for(int nextIndex =currentUserIndex+1;nextIndex <n;nextIndex++) {
             // ensure we only settle debts between users with opposite balances.
             if(creditList.get(nextIndex) * creditList.get(currentUserIndex) <0){
                 creditList.set(nextIndex, creditList.get(nextIndex)+creditList.get(currentUserIndex));
                 cost = Math.min(cost,1 + subOptimalDfs(currentUserIndex+1,creditList,n));

                 // backtrack : undo the transaction to explore other possibilities
                 creditList.set(nextIndex,creditList.get(nextIndex)-creditList.get(currentUserIndex));
             }
         }
         return cost;
     }

     public int getOptimalMinimumSettlements() {
         // Step : 1 calculate net balances for each user
         Map<User,Double> netBalances = new HashMap<>();
         for(Map.Entry<UserPair,Double> entry:balances.entrySet()){
             UserPair pair = entry.getKey();
             double amount = entry.getValue();
             User debtor = pair.getUser1();
             User creditor = pair.getUser2();
             // update the net balances of each user
             netBalances.put(debtor,netBalances.getOrDefault(debtor,0.0)-amount);
             netBalances.put(creditor,netBalances.getOrDefault(creditor,0.0)+amount);
         }
         List<Double> creditList = new ArrayList<>();
         for(Map.Entry<User,Double> entry:netBalances.entrySet()){
             if(Math.abs(entry.getValue()) > 0.001){
                 creditList.add(entry.getValue());
             }
         }
         // step : 3 apply dynamic prograaming to find the minimum transactions required.
         int n = creditList.size();
         int[] dp = new int[1 << n];
         Arrays.fill(dp,-1);
         dp[0] =0;
         int maxSubGroups = dfs((1 << n)-1,dp,creditList);
         return  n - maxSubGroups;
     }

     /*
     helper method to calculate the sum of balances in a subset , given by a bitmask.

      */

    private double sumOfMask(List<Double> values,int mask){
        double sum =0;
        for(int i=0;i<values.size();i++){
            if((mask & (1 << i)) !=0){ // check if the ith bit is set in the mask
                sum += values.get(i); // add the corresponding balance to the sum.
            }
        }
        return  sum;
    }

    /*
    dfs with memoization to determine the maximum number of balanced subgroups
     */
    private int dfs(int mask,int[] dp,List<Double> creditList){
        if(mask ==0){
            return 0;
        }
        if(dp[mask]!=-1){
            return dp[mask];
        }
        int maxSubGroups =0;
        int n = creditList.size();
        for(int submask =1;submask < (1 << n);submask++){
            if((submask & mask) == submask && Math.abs(sumOfMask(creditList,submask)) < 0.001){
                maxSubGroups = Math.max(maxSubGroups,1+dfs(mask^submask,dp,creditList));
            }
        }
        dp[mask] = maxSubGroups;
        return maxSubGroups;
    }





}
