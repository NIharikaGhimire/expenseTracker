package com.example.expensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.expensemanager.Model.Data;
import com.example.expensemanager.Model.Data1;

import java.util.ArrayList;

public class DBHelper1 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "incomes.db";
    private SQLiteDatabase db1;

    public DBHelper1(Context c) {
        super(c, DATABASE_NAME, null, 1);
        db1 = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create tables
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                "incomes(amount INTEGER," +
                "type VARCHAR(255)," +
                "date VARCHAR(255))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //drop existing tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS incomes");
        onCreate(sqLiteDatabase);
    }

    public int getIncomes(){
        Cursor cursor = db1.rawQuery("SELECT SUM(amount) as Total FROM incomes", null);

        int total =0;
        if(cursor.moveToFirst()){
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }
        Log.e("TAG", "getIncomes: "+total );
        // call this method from where you want to show and set text

        return  total;
    }
    public long delete(String  title) {
        return db1.delete("incomes","type=?",new String[]{title});

    }

    public void insertDataToDb1(Data data) {
        ContentValues cv = new ContentValues();
        cv.put("amount", data.getAmount());
        cv.put("type", data.getType());
        cv.put("date", data.getDate());
        db1.insert("incomes", null, cv);

    }

    public ArrayList<Data1> retrieveData1() {
        //aauta index ma 5 wata value save
        ArrayList<Data1> data1 = new ArrayList<>();
        String query = "Select * from incomes";
        //pointer
        Cursor cursor = db1.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Data1 st = new Data1();
                st.setAmount(cursor.getInt(0));
                st.setType(cursor.getString(1));
                st.setDate(cursor.getString(2));
                data1.add(st);

            } while (cursor.moveToNext());
        }
        return data1;
    }


}
