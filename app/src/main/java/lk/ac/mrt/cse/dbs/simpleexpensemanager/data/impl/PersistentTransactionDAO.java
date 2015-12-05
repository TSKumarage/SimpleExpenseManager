package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DBManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Sandaruwan on 12/3/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private DBManager dbHandler;

    public PersistentTransactionDAO(Context context){

        dbHandler=new DBManager(context);
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        dbHandler.addTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        return dbHandler.getTransactions();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return dbHandler.getTransactions(limit);
    }
}
