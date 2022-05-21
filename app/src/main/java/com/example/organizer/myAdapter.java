package com.example.organizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {
    interface OnTaskClickListener{
        void onTaskClick(Model task, int position);
    }
    private final OnTaskClickListener onClickListener;
    private final LayoutInflater inflater;
    ArrayList<Model> db = new ArrayList<Model>();            //список массивов для хранения напоминаний
    myAdapter(Context context, ArrayList<Model> DataHolder, OnTaskClickListener onClickListener) {
        this.db = DataHolder;
        this.onClickListener = onClickListener;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_reminder_file, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
//Привязывает одиночные объекты напоминания к представлению переработчика
        Model task = db.get(position);
        holder.mTitle.setText(db.get(position).getTitle());
        holder.mDiscription.setText(db.get(position).getDescription());
        holder.mDate.setText(db.get(position).getDate());
        holder.mTime.setText(db.get(position).getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View View)
            {
                onClickListener.onTaskClick(task, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return db.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

       final TextView mTitle, mDiscription, mDate, mTime;

        MyViewHolder(View itemView) {
            super(itemView);
            //содержит ссылку на материалы для отображения данных в recyclerview
            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mDiscription = (TextView) itemView.findViewById(R.id.txtDescription);
            mDate = (TextView) itemView.findViewById(R.id.txtDate);
            mTime = (TextView) itemView.findViewById(R.id.txtTime);
        }
    }
}

