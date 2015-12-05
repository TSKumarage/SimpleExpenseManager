package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Sandaruwan on 12/3/2015.
 */
public class DBManager extends SQLiteOpenHelper {

    /*Constants */
    public static final String DATABASE_NAME = "130306x.db";
    public static final int DATABASE_VERSION = 2;
    public static final String ACCOUNT_TABLE_NAME = "account";
    public static final String ACCOUNT_NO = "acnt_number";
    public static final String ACCOUNT_BANK_NO = "bank_number";
    public static final String ACCOUNT_HOLDER = "account_holder";
    public static final String ACCOUNT_BALANCE = "balance";
    public static final String TRANSACTION_TABLE_NAME = "transact";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_TYPE = "type";
    /*...... */

    //Database Creation DDL statements....
    // Accounts table
    private static final String ACCOUNT_TABLE =
    "CREATE TABLE IF NOT EXISTS "+ACCOUNT_TABLE_NAME+ "("+
            ACCOUNT_NO +" TEXT PRIMARY KEY,"+
            ACCOUNT_BANK_NO+" TEXT NOT NULL,"+
            ACCOUNT_HOLDER+" TEXT NOT NULL,"+
            ACCOUNT_BALANCE+ " NUMERIC(12,2) NOT NULL);";

    // Transaction table
    private static final String TRANSACTION_TABLE =
            "CREATE TABLE IF NOT EXISTS "+TRANSACTION_TABLE_NAME+ "("+
                    TRANSACTION_ID +" INTEGER NOT NULL AUTO_INCREMENT,"+
                    ACCOUNT_NO+" VARCHAR(10) NOT NULL,"+
                    TRANSACTION_DATE+" DATE NOT NULL,"+
                    TRANSACTION_TYPE+" INTEGER NOT NULL,"+
                    TRANSACTION_AMOUNT+ " DECIMAL(12,2) NOT NULL,PRIMARY KEY (" +ACCOUNT_NO+","+TRANSACTION_ID+")," +
                    " FOREIGN KEY ("+ACCOUNT_NO+")" +
                    " REFERENCES"+TRANSACTION_TABLE_NAME+"("+ACCOUNT_NO+")" +
                    " ON DELETE CASCADE ON UPDATE CASCADE )";


    /*...............................................*/
    private static final String CREATE_TABLE_Account = "CREATE TABLE " + ACCOUNT_TABLE_NAME+ "(" + ACCOUNT_NO
            + " TEXT PRIMARY KEY," + ACCOUNT_BANK_NO + " TEXT NOT NULL,"+ ACCOUNT_HOLDER + " TEXT NOT NULL," + ACCOUNT_BALANCE + " NUMERIC(10,2) NOT NULL);";

    private static final String CREATE_TABLE_Transaction = "CREATE TABLE transact(transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,acnt_number TEXT,date TEXT NOT NULL,type TEXT NOT NULL,amount NUMERIC(10,2) NOT NULL);";


    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ACCOUNT_TABLE);
        Log.d("my_error", "database Created");
        db.execSQL(TRANSACTION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    //Adding a new Account to the database
    public void addAccount(Account new_acnt){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NO, new_acnt.getAccountNo()); // Contact Name
        values.put(ACCOUNT_BANK_NO, new_acnt.getBankName()); // Contact Phone Number
        values.put(ACCOUNT_HOLDER, new_acnt.getAccountHolderName());
        values.put(ACCOUNT_BALANCE, new_acnt.getBalance());

        // Inserting Row
        db.insert(ACCOUNT_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public Account getAccount(String acntNo){
            Account acnt=null;
        // Select account from account table Query
        String selectQuery = "SELECT  * FROM " + ACCOUNT_TABLE_NAME+ " WHERE "+ACCOUNT_NO+" = '"+acntNo+"'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor!=null)
        {
            cursor.moveToFirst();
            String bankNo=cursor.getString(cursor.getColumnIndex(ACCOUNT_BANK_NO));
            String acntHolder=cursor.getString(cursor.getColumnIndex(ACCOUNT_HOLDER));
            Double balance=cursor.getDouble(cursor.getColumnIndex(ACCOUNT_BALANCE));
            acnt=new Account(acntNo,bankNo,acntHolder,balance);
        }
        return acnt;
    }
    //returning all the accounts in database
    public List<Account> getAccountList()
    {
        List<Account> acntList = new ArrayList<Account>();
        // Select All from account table Query
        String selectQuery = "SELECT  * FROM " + ACCOUNT_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Account acnt;
        String acntNo;
        String bankNo;
        String acntHolder;
        Double balance;
        if (cursor!=null) {// looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    acntNo = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
                    bankNo = cursor.getString(cursor.getColumnIndex(ACCOUNT_BANK_NO));
                    acntHolder = cursor.getString(cursor.getColumnIndex(ACCOUNT_HOLDER));
                    balance = cursor.getDouble(cursor.getColumnIndex(ACCOUNT_BALANCE));
                    acnt = new Account(acntNo, bankNo, acntHolder, balance);
                    // Adding account to the list
                    acntList.add(acnt);
                } while (cursor.moveToNext());
            }
        }
        // return account list
        return acntList;
    }

    public List<String> getAccountNumberList()
    {
        List<String> acntNumList = new ArrayList<String>();
        // Select Accounts from account table Query
        String selectQuery = "SELECT "+ ACCOUNT_NO +" FROM " + ACCOUNT_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
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
                    acntNo = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
                    // Adding account number to the list
                    acntNumList.add(acntNo);
                } while (cursor.moveToNext());
            }
        }
        // return account numbers list
        return acntNumList;
    }

    public void removeAccount(String acnt_number){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ACCOUNT_TABLE_NAME, ACCOUNT_NO + " = ?",
                new String[]{acnt_number});
        db.close();
    }

    public void updateBlanace(String accountNo, ExpenseType expenseType, double amount){
        SQLiteDatabase db = this.getWritableDatabase();
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
        values.put(ACCOUNT_BALANCE, acnt_balanace);

        // updating balance
        db.update(ACCOUNT_TABLE_NAME, values, ACCOUNT_NO + " = ?",
                new String[] { accountNo });
    }

    private double getCurrentBalance(String acntNumber){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+ ACCOUNT_BALANCE +" FROM " + ACCOUNT_TABLE_NAME +" WHERE "+ ACCOUNT_NO +" = '"+ acntNumber+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            return cursor.getDouble(0);
        }
        return 0.0;
    }

    public void addTransaction(Transaction new_Trans){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_DATE, String.valueOf(new_Trans.getDate()));
        values.put(ACCOUNT_NO, new_Trans.getAccountNo());
        values.put(TRANSACTION_AMOUNT, new_Trans.getAmount());
        switch (new_Trans.getExpenseType()) {
            case EXPENSE:
                values.put(TRANSACTION_TYPE, -1);
                break;
            case INCOME:
                values.put(TRANSACTION_TYPE, 1);
                break;
        }


        // Inserting Row
        db.insert(TRANSACTION_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public List<Transaction> getTransactions(){

        List<Transaction> transactionList = new ArrayList<Transaction>();
        // Select All from account table Query
        String selectQuery = "SELECT  * FROM " + TRANSACTION_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
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
                    acntNo = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
                    date = cursor.getString(cursor.getColumnIndex(TRANSACTION_DATE));
                    type = cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE));
                    if (type == 1) {
                        tranType = ExpenseType.INCOME;
                    } else {
                        tranType = ExpenseType.EXPENSE;
                    }
                    amount = cursor.getDouble(cursor.getColumnIndex(TRANSACTION_AMOUNT));

                    SimpleDateFormat fort = new SimpleDateFormat("YYYY MM DD");
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

    public List<Transaction> getTransactions(int limit)
    {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        // Select All from account table Query
        String selectQuery = "SELECT  * FROM " + TRANSACTION_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
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
                    acntNo = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
                    date = cursor.getString(cursor.getColumnIndex(TRANSACTION_DATE));
                    type = cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE));
                    if (type == 1) {
                        tranType = ExpenseType.INCOME;
                    } else {
                        tranType = ExpenseType.EXPENSE;
                    }
                    amount = cursor.getDouble(cursor.getColumnIndex(TRANSACTION_AMOUNT));

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
                } while (cursor.moveToNext() && count <= limit);
            }
        }
        // return account list
        return transactionList;

    }
}
