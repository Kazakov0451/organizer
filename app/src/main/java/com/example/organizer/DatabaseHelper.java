package com.example.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reminder";            //Имя таблицы для хранения напоминаний в sqllite
    static final String TABLE = "reminder";                                // название таблицы в бд
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {                    //SQL-запрос для вставки данных в sqllite
        db.execSQL("CREATE TABLE reminder (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE
                + " TEXT, " + COLUMN_DESCRIPTION + " TEXT , " + COLUMN_DATE
                + " TEXT, " + COLUMN_TIME + " TEXT);");
        // Создание таблицы для базы данных

    }

    @Override
    //SQL-запрос для проверки таблицы с тем же именем или нет выполняет команду sql
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public String addReminder(String title, String description, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);                                    //Вставляет данные в базу данных sqlite
        contentValues.put("description", description);
        contentValues.put("date", date);
        contentValues.put("time", time);

        float result = db.insert("reminder", null, contentValues);
        //возвращает -1, если данные успешно вставлены в базу данных

        if (result == -1) {
            return "Ошибка";
        } else {
            return "Успешно";
        }

    }

    public Cursor ReadAllReminders() {
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor =   db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        //SQL-запрос для получения данных из базы данных
        return cursor;
    }

    public String DeleteReminder(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {String.valueOf(id)};
        float result = db.delete(DatabaseHelper.TABLE, "id = ?",  args);

        if (result == 0) {
            return "Ошибка";
        } else {
            return "Успешно";
        }
    }

}
