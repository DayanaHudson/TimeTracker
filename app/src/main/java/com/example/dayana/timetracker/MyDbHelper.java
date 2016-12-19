package com.example.dayana.timetracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "emails";
    public static final String COL_ACCOUNT = "Account";
    public static final String COL_EMAIL = "Email";
    protected static final String STRING_CREATE =
            "CREATE TABLE " +TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_ACCOUNT + " TEXT, " + COL_EMAIL + " TEXT);";
    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL(STRING_CREATE);
    }
    public static void drop(){
        MainActivity.mydb.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        MainActivity.mydb.execSQL(STRING_CREATE);
        MainActivity.mydb.execSQL("INSERT INTO emails(Account, Email) VALUES('"+MainActivity.id+"','"+MainActivity.email+"');");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}