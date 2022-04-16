package com.example.expensemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.Model.Data;
import com.example.expensemanager.Model.Data1;

import java.util.ArrayList;

public class CustomAdapter1 extends RecyclerView.Adapter<CustomAdapter1.MyViewHolder> {
    Context c;
    private ArrayList<Data1> data1;
    AlertDialog.Builder alert;
    DBHelper1 dbHelper1;

    public CustomAdapter1(ArrayList<Data1> l,Context c) {
        data1 = l;
        this.c = c;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_income, null);
        MyViewHolder viewHolder = new MyViewHolder(convertView);
        dbHelper1 = new DBHelper1(c);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.amount.setText("Amount: "+ String.valueOf(data1.get(position).getAmount()));;
        holder.type.setText("Type: "+ data1.get(position).getType());
        holder.date.setText("Date: " +data1.get(position).getDate());

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int x = position;
                CharSequence[] delete = {
                        "Delete"
                };

                alert = new AlertDialog.Builder(c);
                alert.setItems(delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            if(dbHelper1.delete(data1.get(position).getType())>0){
                                Toast.makeText(c, "Deleted success", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            };
                            data1.remove(x);

                            notifyItemRemoved(x);

                        }
                    }
                });
                alert.create().show();

                return false;
            }
        });
    }

    @Override

    public int getItemCount() {
        return data1.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount, type, date;
        View view;

        public MyViewHolder(View convertView) {
            super(convertView);
            type = convertView.findViewById(R.id.type_itxt);
            amount = convertView.findViewById(R.id.amt_itxt);
            date = convertView.findViewById(R.id.date_itxt);
            view = convertView;
        }
    }

}
