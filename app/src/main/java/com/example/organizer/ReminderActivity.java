package com.example.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//этот класс должен добавить напоминания от пользователя и вставки в базу данных
public class ReminderActivity extends AppCompatActivity {

    Button SubmitBtn, DateBtn, TimeBtn;
    EditText TitleEdit, DescriptionEdit;
    String TimeToNotify;
    ImageView mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        //назначены все ссылки на материалы для получения и установки данных
        TitleEdit = (EditText) findViewById(R.id.editTitle);
        DescriptionEdit = (EditText) findViewById(R.id.editDescript);
        DateBtn = (Button) findViewById(R.id.btnDate);
        TimeBtn = (Button) findViewById(R.id.btnTime);
        SubmitBtn = (Button) findViewById(R.id.btnSbumit);
        mCancel = (ImageView) findViewById(R.id.mCancel);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cancel();
            }
        });

        TimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();              //когда мы нажимаем кнопку выбора времени, он вызывает метод выбора времени
            }
        });

        DateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }                     //когда мы нажимаем кнопку выбора даты, он вызывает метод выбора даты
        });

        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = TitleEdit.getText().toString().trim();             //получить доступ к данным из поля ввода
                String description = DescriptionEdit.getText().toString().trim();
                String date = DateBtn.getText().toString().trim();     //получить доступ к дате с помощью кнопки выбора даты
                String time = TimeBtn.getText().toString().trim();    //доступ ко времени с помощью кнопки выбора времени

                if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Поле с названием пустое", Toast.LENGTH_SHORT).show();
                    //показывает Toast, если поле ввода пусто
                } else if (time.equals("time") || date.equals("date")) {
                    //показывает Toast, если дата и время не выбраны
                        Toast.makeText(getApplicationContext(), "Поле дата и время не выбрано",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ProcessInsert(title, description, date, time);

                    }
                }

        });
    }

    private void Cancel(){
        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentBack);
    }
    private void ProcessInsert(String title, String description, String date, String time) {
        //вставляет заголовок, дату и время в базу данных sqlite
        String result = new DatabaseHelper(this).addReminder(title, description, date, time);
        setAlarm(title, description, date, time);          //вызывает метод установки будильника для установки будильника
        TitleEdit.setText("");
        DescriptionEdit.setText("");
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }


    private void selectTime() {                                           //этот метод выполняет задачу выбора времени
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                TimeToNotify = i + ":" + i1;                  //переменная temp для хранения времени установки будильника
                TimeBtn.setText(FormatTime(i, i1));             //устанавливает текст кнопки как выбранное время
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void selectDate() {                          //этот метод выполняет задачу выбора даты
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                DateBtn.setText(day + "-" + (month + 1) + "-" + year);
            } //устанавливает выбранную дату в качестве теста для кнопки
        }, year, month, day);
        datePickerDialog.show();
    }

    public String FormatTime(int hour, int minute) {      //этот метод преобразует время в 24-часовой формат

        String time;
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "00" + ":" + formattedMinute ;
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute ;
        } else{
            time = hour + ":" + formattedMinute ;
        }

        return time;
    }


    private void setAlarm(String title, String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //назначение объекта alarm manager для установки тревоги

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("title", title);
        intent.putExtra("event", text);        //отправка данных в класс тревоги для создания канала и уведомления
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intent, PendingIntent.FLAG_ONE_SHOT);
        String Date_Time = date + " " + TimeToNotify;
        @SuppressLint("SimpleDateFormat")
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy HH:mm");
        try {
            Date date1 = formatter.parse(Date_Time);
            assert date1 != null;
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getApplicationContext(), "Alaram", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //это намерение будет вызвано после завершения настройки тревоги
        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);           //переходит от добавления действия напоминания к основному действию

    }
}