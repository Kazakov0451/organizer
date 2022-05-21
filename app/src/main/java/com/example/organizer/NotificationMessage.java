package com.example.organizer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//этот класс создает сообщение уведомления о напоминании

public class NotificationMessage extends AppCompatActivity {
    TextView textView, textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
        textView = findViewById(R.id.tv_message);
        textView1 = findViewById(R.id.tv_message2);
        Bundle bundle = getIntent().getExtras();              //вызвать данные, которые передаются другим намерением
        textView.setText(bundle.getString("message"));
        textView1.setText(bundle.getString("message2"));

    }
}
