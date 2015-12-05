package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHandler.TRANSACTION_DATE, String.valueOf(transaction.getDate()));
        values.put(dbHandler.ACCOUNT_NO, transaction.getAccountNo());
        values.put(dbHandler.TRANSACTION_AMOUNT, transaction.getAmount());
        switch (transaction.getExpenseType()) {
            case EXPENSE:
                values.put(dbHandler.TRANSACTION_TYPE, -1);
                break;
            case INCOME:
                values.put(dbHandler.TRANSACTION_TYPE, 1);
                break;
        }
        // Inserting Row
        db.insert(dbHandler.TRANSACTION_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        // Select All from account table Query
        String selectQuery = "SELECT  * FROM " + dbHandler.TRANSACTION_TABLE_NAME;

        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Transaction tran;
        String acntNo;
        String date;
        int type;
        ExpenseType tranType;
        Double amount;
        Date transDate=new Date();
        // looping through all rows and adding to list
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    acntNo = cursor.getString(cursor.getColumnIndex(dbHandler.ACCOUNT_NO));
                    date = cursor.getString(cursor.getColumnIndex(dbHandler.TRANSACTION_DATE));
                    type = cursor.getInt(cursor.getColumnIndex(dbHandler.TRANSACTION_TYPE));
                    if (type == 1) {
                        tranType = ExpenseType.INCOME;
                    } else {
                        tranType = ExpenseType.EXPENSE;
                    }
                    amount = cursor.getDouble(cursor.getColumnIndex(dbHandler.TRANSACTION_AMOUNT));

                    SimpleDateFormat fort = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        transDate = fort.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tran = new Transaction(transDate, acntNo, tranType, amount);
                    // Adding transaction to the list
                    transactionList.add(tran);
                } while (cursor.moveToNext());
            }
        }
        // return account list
        return transactionList;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        //Used below code in order to stop unnecessary looping of the cursor

        List<Transaction> transactionList = new ArrayList<Transaction>();
        // Select All from account table Query
        String selectQuery = "SELECT  * FROM " + dbHandler.TRANSACTION_TABLE_NAME;

        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count=0;
        Transaction tran;
        String acntNo;
        String date;
        int type;
        ExpenseType tranType;
        Double amount;
        Date transDate=new Date();
        // looping through all rows and adding to list
        if (cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    acntNo = cursor.getString(cursor.getColumnIndex(dbHandler.ACCOUNT_NO));
                    date = cursor.getString(cursor.getColumnIndex(dbHandler.TRANSACTION_DATE));
                    type = cursor.getInt(cursor.getColumnIndex(dbHandler.TRANSACTION_TYPE));
                    if (type == 1) {
                        tranType = ExpenseType.INCOME;
                    } else {
                        tranType = ExpenseType.EXPENSE;
                    }
                    amount = cursor.getDouble(cursor.getColumnIndex(dbHandler.TRANSACTION_AMOUNT));

                    SimpleDateFormat fort = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        transDate = fort.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tran = new Transaction(transDate, acntNo, tranType, amount);
                    // Adding transaction to the list
                    transactionList.add(tran);
                    count++;
                } while (cursor.moveToNext() && count <= limit); //check weather limit has exceeded or cursor come to the last row
            }
        }
        // return account list
        return transactionList;
    }
}
