package com.example.expensemanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.Model.Data;
import com.example.expensemanager.Model.Data1;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;


public class DashboardFragment extends Fragment {
    private TextView mDisplayDate;
    //for expenses
    private ArrayList<Data> data;
    CustomAdapter customAdapter;
    //for incomes
    private ArrayList<Data1> data1;
    CustomAdapter1 customAdapter1;
    String date;


    private DatePickerDialog.OnDateSetListener mDateSetListener;


    //Floating Button
    private FloatingActionButton fab_main;
    private FloatingActionButton fab_income;
    private FloatingActionButton fab_expense;

    //Floating Button TextView
    private TextView fab_income_text;
    private TextView fab_expense_text;

    private boolean isOpen = false;

    // animation class objects
    private Animation fadeOpen, fadeClose;

    //Dashboard income and expense result
    private TextView totalIncomeResult;
    private TextView totalExpenseResult;


    //Recycler view
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dashboard, container, false);


        fab_main = myview.findViewById(R.id.fb_main_plus_btn);
        fab_income = myview.findViewById(R.id.income_ft_btn);
        fab_expense = myview.findViewById(R.id.expense_ft_btn);

        // Connect floating text
        fab_income_text = myview.findViewById(R.id.income_ft_text);
        fab_expense_text = myview.findViewById(R.id.expense_ft_text);

        //Total income and expense
        totalIncomeResult = myview.findViewById(R.id.income_set_result);
        totalExpenseResult = myview.findViewById(R.id.expense_set_result);

        //Recycler
        mRecyclerIncome = myview.findViewById(R.id.recycler_income);
        mRecyclerExpense = myview.findViewById(R.id.recycler_expense);

        //Animations
        fadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        fadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();
                floatingButtonAnimation();
            }
        });

        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);

        data = new ArrayList<>();
        DBHelper db = new DBHelper(getActivity());
        DBHelper1 db1 = new DBHelper1(getActivity());

        totalExpenseResult.setText(String.valueOf(db.getExpenses()));
        totalIncomeResult.setText(String.valueOf(db1.getIncomes()));

        data = db.retrieveData();
        data1 = db1.retrieveData1();
        db.getExpenses();
        db1.getIncomes();
        customAdapter = new CustomAdapter(data, getActivity());
        customAdapter1 = new CustomAdapter1(data1, getActivity());


       mRecyclerExpense.setAdapter(new CustomAdapter(data, getActivity()));
       mRecyclerIncome.setAdapter(new CustomAdapter1(data1, getActivity()));
        return myview;

    }
    //Floating button animation
    private void floatingButtonAnimation() {
        if (isOpen) {
            fab_income.startAnimation(fadeClose);
            fab_expense.startAnimation(fadeClose);
            fab_income.setClickable(false);
            fab_expense.setClickable(false);
            fab_income_text.startAnimation(fadeClose);
            fab_expense_text.startAnimation(fadeClose);
            fab_income_text.setClickable(false);
            fab_expense_text.setClickable(false);
        } else {
            fab_income.startAnimation(fadeOpen);
            fab_expense.startAnimation(fadeOpen);
            fab_income.setClickable(true);
            fab_expense.setClickable(true);
            fab_expense_text.startAnimation(fadeOpen);
            fab_income_text.startAnimation(fadeOpen);
            fab_income_text.setClickable(true);
            fab_expense_text.setClickable(true);
        }
        isOpen = !isOpen;

    }

    private void addData() {
        //Fab Button Income
        fab_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createIncomeData();
            }
        });

        fab_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExpenseData();
            }

        });
    }
    //for expense
    private void createExpenseData() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);
        EditText edtamount = myview.findViewById(R.id.amount);
        EditText edttype = myview.findViewById(R.id.type_edt);

        mDisplayDate = myview.findViewById(R.id.tvDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d("TAG","onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
               date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);

            }
        } ;
        Button saveBtn = myview.findViewById(R.id.btnSave);
        Button cancelBtn = myview.findViewById(R.id.btnCancel);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                Data newData = new Data();
                newData.setAmount(Integer.parseInt(edtamount.getText().toString()));
                newData.setType(edttype.getText().toString());
                newData.setDate(date);
                insertData(newData);
                dialog.show();
                startActivity(new Intent(getActivity(),HomeActivity.class));
                floatingButtonAnimation();
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //for income
    private void createIncomeData() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);
        EditText edtamount = myview.findViewById(R.id.amount);
        EditText edttype = myview.findViewById(R.id.type_edt);

        mDisplayDate = myview.findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d("TAG","onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);

            }
        } ;

        Button saveBtn = myview.findViewById(R.id.btnSave);
        Button cancelBtn = myview.findViewById(R.id.btnCancel);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data newData = new Data();
                newData.setAmount(Integer.parseInt(edtamount.getText().toString()));
                newData.setType(edttype.getText().toString());
                newData.setDate(date);
                insertData1(newData);
                dialog.show();
                startActivity(new Intent(getActivity(),HomeActivity.class));
                floatingButtonAnimation();
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    //insert data for expense
    private void insertData(Data data) {
        if (dataValid(data)) {
            DBHelper db = new DBHelper(getActivity());
            db.insertDataToDb(data);
            this.data = db.retrieveData();
            Toast.makeText(getActivity(), "Expense Added Successfully", Toast.LENGTH_SHORT).show();
            CustomAdapter c = new CustomAdapter(this.data,getActivity());
            this.mRecyclerExpense.setAdapter(c);
            c.notifyDataSetChanged();
        } else {

        }

    }
    //insert data for income
    private void insertData1(Data data) {
        if (dataValid(data)) {
            DBHelper1 db1 = new DBHelper1(getActivity());
            db1.insertDataToDb1(data);
            Toast.makeText(getActivity(), "Income Added Successfully", Toast.LENGTH_SHORT).show();
            CustomAdapter1 c = new CustomAdapter1(this.data1,getActivity());
            this.mRecyclerIncome.setAdapter(c);
            c.notifyDataSetChanged();
        } else {

        }
    }

    private boolean dataValid(Data data){
        boolean valid = true;
        if (TextUtils.isEmpty(data.getType())){
            valid = false;
        }
        return valid;
    }

}
