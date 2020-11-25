package com.example.jodygryanapp.news;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDatabaseFile";
    public static final int VERSION_NUM = 2;
    public static final String TABLE_NAME = "Articles";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_AUTHOR = "AUTHOR";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_URL = "URL";
    public static final String COL_URLTOIMAGE = "URLTOIMAGE";


    public DataBaseHelper(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    public void onCreate(SQLiteDatabase db)
    {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT,"
                + COL_AUTHOR + " TEXT,"
                + COL_DESCRIPTION + " TEXT,"
                + COL_URL + " TEXT,"
                + COL_URLTOIMAGE + " TEXT" + ")" );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}

