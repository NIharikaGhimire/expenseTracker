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

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context c;
    private ArrayList<Data> data;
    AlertDialog.Builder alert;
    DBHelper dbHelper;

    public CustomAdapter(ArrayList<Data> l, Context c) {
        data = l;
        this.c = c;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_expense, null);
        MyViewHolder viewHolder = new MyViewHolder(convertView);
        dbHelper = new DBHelper(c);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.amount.setText("Amount: "+ String.valueOf(data.get(holder.getAdapterPosition()).getAmount()));;
        holder.type.setText("Type: "+ data.get(holder.getAdapterPosition()).getType());
        holder.date.setText("Date: " +data.get(position).getDate());

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
                                if(dbHelper.delete(data.get(position).getType())>0){
                                    Toast.makeText(c, "Deleted success", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                };
                                data.remove(x);

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
        return data.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount, type, date;
        View view;

        public MyViewHolder(View convertView) {
            super(convertView);
            type = convertView.findViewById(R.id.type_txt);
            amount = convertView.findViewById(R.id.amt_txt);
            date = convertView.findViewById(R.id.date_txt);
            view = convertView;
        }
    }

}
