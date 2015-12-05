package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
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
        List<String> acntNumList = new ArrayList<String>();
        // Select Accounts from account table Query
        String selectQuery = "SELECT "+ dbHandler.ACCOUNT_NO +" FROM " + dbHandler.ACCOUNT_TABLE_NAME;

        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        Cursor cursor=null;
        try{cursor = db.rawQuery(selectQuery, null);}
        catch (NullPointerException e)
        {
            System.out.println("Error");
        }
        String acntNo;
        // looping through all rows and adding to the list
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    acntNo = cursor.getString(cursor.getColumnIndex(dbHandler.ACCOUNT_NO));
                    // Adding account number to the list
                    acntNumList.add(acntNo);
                } while (cursor.moveToNext());
            }
        }
        // return account numbers list
        return acntNumList;
    }

    @Override
    public List<Account> getAccountsList() { //returning all the accounts in database
        List<Account> acntList = new ArrayList<Account>();
        // Select All from account table Query
        String selectQuery = "SELECT  * FROM " + dbHandler.ACCOUNT_TABLE_NAME;

        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Account acnt;
        String acntNo;
        String bankNo;
        String acntHolder;
        Double balance;
        if (cursor!=null) {// looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    acntNo = cursor.getString(cursor.getColumnIndex(dbHandler.ACCOUNT_NO));
                    bankNo = cursor.getString(cursor.getColumnIndex(dbHandler.ACCOUNT_BANK_NO));
                    acntHolder = cursor.getString(cursor.getColumnIndex(dbHandler.ACCOUNT_HOLDER));
                    balance = cursor.getDouble(cursor.getColumnIndex(dbHandler.ACCOUNT_BALANCE));
                    acnt = new Account(acntNo, bankNo, acntHolder, balance);
                    // Adding account to the list
                    acntList.add(acnt);
                } while (cursor.moveToNext());
            }
        }
        // return account list
        return acntList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account=null;
        // Select account from account table Query
        String selectQuery = "SELECT  * FROM " + dbHandler.ACCOUNT_TABLE_NAME+ " WHERE "+dbHandler.ACCOUNT_NO+" = '"+accountNo+"'" ;

        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor!=null)
        {
            cursor.moveToFirst();
            String bankNo=cursor.getString(cursor.getColumnIndex(dbHandler.ACCOUNT_BANK_NO));
            String acntHolder=cursor.getString(cursor.getColumnIndex(dbHandler.ACCOUNT_HOLDER));
            Double balance=cursor.getDouble(cursor.getColumnIndex(dbHandler.ACCOUNT_BALANCE));
            account=new Account(accountNo,bankNo,acntHolder,balance);
        }

        if (account!=null)
        {
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);

    }

    @Override
    public void addAccount(Account account) { //Adding a new Account to the database
        SQLiteDatabase db = this.dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(dbHandler.ACCOUNT_NO, account.getAccountNo());
        values.put(dbHandler.ACCOUNT_BANK_NO, account.getBankName());
        values.put(dbHandler.ACCOUNT_HOLDER, account.getAccountHolderName());
        values.put(dbHandler.ACCOUNT_BALANCE, account.getBalance());

        // Inserting Row
        db.insert(dbHandler.ACCOUNT_TABLE_NAME, null, values);
        db.close(); // Closing database connection

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        db.delete(dbHandler.ACCOUNT_TABLE_NAME, dbHandler.ACCOUNT_NO + " = ?",
                new String[]{accountNo});
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        double acnt_balanace=0;
        switch (expenseType) {
            case EXPENSE:
                acnt_balanace=getCurrentBalance(accountNo) - amount;
                break;
            case INCOME:
                acnt_balanace=getCurrentBalance(accountNo) + amount;
                break;
        }
        ContentValues values = new ContentValues();
        values.put(dbHandler.ACCOUNT_BALANCE, acnt_balanace);

        // updating balance
        db.update(dbHandler.ACCOUNT_TABLE_NAME, values, dbHandler.ACCOUNT_NO + " = ?",
                new String[]{accountNo});
    }

    //For the use of update balance method
    private double getCurrentBalance(String acntNumber){
        SQLiteDatabase db = this.dbHandler.getReadableDatabase();
        String selectQuery = "SELECT "+ dbHandler.ACCOUNT_BALANCE +" FROM " + dbHandler.ACCOUNT_TABLE_NAME +" WHERE "+ dbHandler.ACCOUNT_NO +" = '"+ acntNumber+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            return cursor.getDouble(0);
        }
        return 0.0;
    }
}
