package com.example.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.expensemanager.Model.Data;
import com.example.expensemanager.Model.Data1;

import java.util.ArrayList;

public class IncomeActivity extends AppCompatActivity {
    private RecyclerView mIncome;
    private ArrayList<Data1> data1;
    CustomAdapter1 customAdapter1;
    DBHelper1 db;
    TextView income_total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        db = new DBHelper1(IncomeActivity.this);


        mIncome = findViewById(R.id.income_rv);
        income_total = findViewById(R.id.income_total);
        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(IncomeActivity.this, LinearLayoutManager.HORIZONTAL, false);

        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        mIncome.setHasFixedSize(true);
        mIncome.setLayoutManager(layoutManagerExpense);

        data1 = new ArrayList<>();
        data1 = db.retrieveData1();
        income_total.setText(String.valueOf(db.getIncomes()));
        customAdapter1 = new CustomAdapter1(data1, IncomeActivity.this);
        mIncome.setAdapter(customAdapter1);

    }

}