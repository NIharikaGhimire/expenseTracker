package com.example.expensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.expensemanager.Model.Data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expenses.db";
    private SQLiteDatabase db;

    public DBHelper (Context c){
        super(c, DATABASE_NAME, null,1);
        db = getWritableDatabase();

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create tables
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                "expenses(amount INTEGER,"+
                "type VARCHAR(255)," +
                "date VARCHAR(255))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //drop existing tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS expenses");
        onCreate(sqLiteDatabase);

    }

    public int getExpenses(){
        Cursor cursor = db.rawQuery("SELECT SUM(amount) as Total FROM expenses", null);

        int total =0;
        if(cursor.moveToFirst()){
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }
        Log.e("TAG", "getExpenses: "+total );
        // call this method from where you want to show and set text

        return  total;
    }
        public long delete(String  title) {
            return db.delete("expenses","type=?",new String[]{title});

        }

    public void insertDataToDb(Data data){
        ContentValues cv = new ContentValues();
        cv.put("amount",data.getAmount());
        cv.put("type",data.getType());
        cv.put("date",data.getDate());
        db.insert("expenses",null,cv);
    }

    public ArrayList<Data> retrieveData() {
        //aauta index ma 5 wata value save
        ArrayList<Data> data = new ArrayList<>();
        String query = "Select * from expenses";
        //pointer
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Data st = new Data();
                st.setAmount(cursor.getInt(0));
                st.setType(cursor.getString(1));
                st.setDate(cursor.getString(2));
                data.add(st);

            }while(cursor.moveToNext());
        }
        return data;
    }



}
