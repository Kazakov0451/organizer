package com.example.organizer;

import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Toast;


//этот класс должен удалять напоминания от пользователя и вставки в базу данных
public class DeleteReminder extends AppCompatActivity {

    Button  mDelete;
    ImageView mCancel;
    long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_reminder);
        mDelete = (Button) findViewById(R.id.btnDel);
        mCancel = (ImageView) findViewById(R.id.mCancel);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cancel();
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DelReminder();                   //когда мы нажимаем кнопку удалить, он вызывает метод удалить из БД
            }
        });

    }
    private void Cancel(){
        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentBack);
    }
    private void DelReminder() {

        Bundle extras = getIntent().getExtras();
        // Получение ID
        if (extras != null) {
            id = extras.getInt("id");
        }

        String result = new DatabaseHelper(this).DeleteReminder(id);
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);

    }


}