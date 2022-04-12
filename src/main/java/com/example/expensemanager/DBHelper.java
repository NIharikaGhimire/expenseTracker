package com.example.expensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.expensemanager.Model.Data;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expenses.db";
    private SQLiteDatabase db;

    public DBHelper (Context c){
        super(c, DATABASE_NAME, null,1);
        db = getWritableDatabase();

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                "expense(amount INTEGER,"+
                "type VARCHAR(255)," +
                "note VARCHAR(255)," +
                "date VARCHAR(255))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS expense");
        onCreate(sqLiteDatabase);

    }
    public void insertDataToDb(Data data){
        ContentValues cv = new ContentValues();
        cv.put("amount",data.getAmount());
        cv.put("type",data.getType());
        cv.put("note",data.getNote());
//        cv.put("date",data.getDate());
        db.insert("expense",null,cv);
    }
}
