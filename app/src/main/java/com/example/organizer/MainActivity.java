package com.example.organizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ImageView mCreateRem;
    RecyclerView mRecyclerview;
    ArrayList<Model> db = new ArrayList<Model>();    //Список массивов для добавления напоминаний и отображения в recyclerview


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCreateRem = (ImageView) findViewById(R.id.create);           //Плавающая кнопка действия для изменения активности
        mCreateRem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
                startActivity(intent);                              //Запускает новое действие для добавления напоминаний
            }

        });

        Cursor cursor = new DatabaseHelper(getApplicationContext()).ReadAllReminders();
        //Курсор для загрузки данных из базы данных

        while (cursor.moveToNext()) {
            Model task = new Model(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4));
            db.add(task);
        }


        myAdapter.OnTaskClickListener taskClickListener = new myAdapter.OnTaskClickListener() {
            //Связывает адаптер с recyclerview
            @Override
                public void onTaskClick(Model task, int position) {
                Intent intent = new Intent(getApplicationContext(), DeleteReminder.class);
                intent.putExtra("id", task.getId());
                startActivity(intent);
            }
        };
        myAdapter adapter = new myAdapter(this,db, taskClickListener);
        mRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();                      //Заставляет пользователя выйти из приложения
        super.onBackPressed();

    }
}