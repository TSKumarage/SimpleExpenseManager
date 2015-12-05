package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    // DDL statements..........
    // Accounts table
    private static final String ACCOUNT_TABLE =
    "CREATE TABLE IF NOT EXISTS "+ACCOUNT_TABLE_NAME+ "("+
            ACCOUNT_NO +" VARCHAR(10) PRIMARY KEY,"+
            ACCOUNT_BANK_NO+" VARCHAR(20) NOT NULL,"+
            ACCOUNT_HOLDER+" VARCHAR(45) NOT NULL,"+
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


    public DBManager(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ACCOUNT_TABLE);
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

}
