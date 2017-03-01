package com.example.taras.task_course;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;

/**
 * Created by Taras on 05.11.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "usersDB";

    public static final String TABLE_USERS = "users";
    public static final String TABLE_PAY = "pay";


    public static final String KEY_ID = "_id";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_STREET = "street";
    public static final String KEY_HOUSE = "house";
    public static final String KEY_APARTMENT = "apartment";
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_YEAR = "year";
    public static final String KEY_MONTH = "month";
    public static final String KEY_LIGHT = "light";
    public static final String KEY_LIGHT_MONEY = "light_money";
    public static final String KEY_GAS = "gas";
    public static final String KEY_GAS_MONEY = "gas_money";
    public static final String KEY_WATER = "water";
    public static final String KEY_WATER_MONEY = "water_money";
    public static final String KEY_SUM_UP = "sum_up";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Таблиця для історії оплат
        db.execSQL("create table " + TABLE_PAY + "(" + KEY_ID + " integer primary key," + KEY_USER_NAME + " text," +
                KEY_YEAR + " integer," + KEY_MONTH + " text," + KEY_LIGHT + " real," + KEY_LIGHT_MONEY + " real," +
                KEY_GAS + " real," + KEY_GAS_MONEY + " real," + KEY_WATER + " real," + KEY_WATER_MONEY + " real," +
                KEY_SUM_UP + " real" + ")");

       //Таблиця для логування
        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID + " integer primary key," + KEY_USER_NAME +
                " text," + KEY_STREET + " text," + KEY_HOUSE + " integer," + KEY_APARTMENT + " integer," +
                KEY_PASSWORD + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_USERS);
        db.execSQL("drop table if exists " + TABLE_PAY);
        onCreate(db);
    }
}
