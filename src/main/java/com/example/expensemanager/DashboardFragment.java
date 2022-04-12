package com.example.expensemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.expensemanager.Model.Data;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;


public class DashboardFragment extends Fragment {
    private TextView mDisplayDate;
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
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);

            }
        } ;

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

    private void createExpenseData() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);
        EditText edtamount = myview.findViewById(R.id.amount);
        EditText edttype = myview.findViewById(R.id.type_edt);
        EditText edtnote = myview.findViewById(R.id.note_edt);

        Button saveBtn = myview.findViewById(R.id.btnSave);
        Button cancelBtn = myview.findViewById(R.id.btnCancel);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data data = new Data();
                data.setAmount(Integer.parseInt(edtamount.getText().toString()));
                data.setType(edttype.getText().toString());
                data.setNote(edtnote.getText().toString());
                insertData(data);
                dialog.show();

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

    private void createIncomeData() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);
        EditText edtamount = myview.findViewById(R.id.amount);
        EditText edtType = myview.findViewById(R.id.type_edt);
        EditText edtNote = myview.findViewById(R.id.note_edt);

        Button saveBtn = myview.findViewById(R.id.btnSave);
        Button cancelBtn = myview.findViewById(R.id.btnCancel);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void insertData(Data data) {
        if (dataValid(data)) {
            DBHelper db = new DBHelper(getActivity());
            db.insertDataToDb(data);
            Toast.makeText(getActivity(), "Record Added Successfully", Toast.LENGTH_SHORT).show();
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
