package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

/**
 * Created by Sandaruwan on 12/4/2015.
 */
public class PersistentDemoExpenseManager extends ExpenseManager {
    private static Context context;     //Context of MainActivity
    public PersistentDemoExpenseManager(Context context)
    {
        this.context=context;
        setup();

    }
    @Override
    public void setup() {

        TransactionDAO inMemoryTransactionDAO = new PersistentTransactionDAO(this.context);
        setTransactionsDAO(inMemoryTransactionDAO);

        AccountDAO inMemoryAccountDAO = new PersistentAccountDAO(this.context);
        setAccountsDAO(inMemoryAccountDAO);
    }
}
