package com.example.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.expensemanager.Model.Data;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {

    private RecyclerView mExpense;
    private ArrayList<Data> data;
    CustomAdapter customAdapter;
    TextView expenseResult;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        mExpense = findViewById(R.id.expense_rv);
        db = new DBHelper(this);
        expenseResult = findViewById(R.id.expenseResult);
        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(ExpenseActivity.this, LinearLayoutManager.HORIZONTAL, false);

        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        mExpense.setHasFixedSize(true);
        mExpense.setLayoutManager(layoutManagerExpense
        );

        data = new ArrayList<>();


        data = db.retrieveData();
        expenseResult.setText(String.valueOf(db.getExpenses()));
        customAdapter = new CustomAdapter(data, ExpenseActivity.this);


        mExpense.setAdapter(new CustomAdapter(data, ExpenseActivity.this));
    }

//    public long delete(String  title) {
//        return db.delete("expenses","type=?",new String[]{title});
//
//    }

}