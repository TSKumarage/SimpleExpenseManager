package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DBManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Sandaruwan on 12/3/2015.
 */
public class PersistentAccountDAO implements AccountDAO {
    private DBManager dbHandler;

    public PersistentAccountDAO(Context context){
        dbHandler=new DBManager(context);
    }
    @Override
    public List<String> getAccountNumbersList() {

        return dbHandler.getAccountNumberList();
    }

    @Override
    public List<Account> getAccountsList() {

        return dbHandler.getAccountList();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        Account account=dbHandler.getAccount(accountNo);
        if (account!=null)
        {
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);

    }

    @Override
    public void addAccount(Account account) {
        dbHandler.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbHandler.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        dbHandler.updateBlanace(accountNo,expenseType, amount);
    }
}
