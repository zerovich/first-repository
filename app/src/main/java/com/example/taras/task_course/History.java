package com.example.taras.task_course;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class History extends Activity implements PopupMenu.OnMenuItemClickListener {

    private DBHelper usersDB;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


         listView = (ListView) findViewById(R.id.listView);


        usersDB = new DBHelper(this);

        SQLiteDatabase database = usersDB.getWritableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_PAY, null, null, null, null, null, null);

        List<String> payment = new ArrayList<>();


        while (cursor.moveToNext()) {
            String userName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_USER_NAME));
            String year = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_YEAR));
            String month = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_MONTH));
            String lightVol = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_LIGHT));
            String lightPay = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_LIGHT_MONEY));
            String gasVol = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GAS));
            String gasPay = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GAS_MONEY));
            String waterVol = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_WATER));
            String waterPay = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_WATER_MONEY));
            String sumUp = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SUM_UP));

            if (MainProgram.getActiveUser().equals(userName)) {
                payment.add(year + " " + month + ": " + "\nсвітло(" + lightVol + " кВт/год, " + lightPay + " грн." +
                        "), " + "\nгаз(" + gasVol + " " + " куб." + ", " + gasPay + " грн." +
                        "), " + "\nвода(" + waterVol + " " + " куб." + ", " + waterPay + " грн." +
                        "), " + "\nсума: " + sumUp + " грн.");
            }
        }


        cursor.close();
        database.close();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, payment);
        listView.setAdapter(adapter);


        //Очистити базу
        ImageView image = (ImageView) findViewById(R.id.btn_clear_db);
        image.setOnClickListener(onClickListener);
    }


    //Popup
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopup(v);
        }
    };

    public void showPopup(View v) {
        Context wrapper = new ContextThemeWrapper(this, R.style.MyPopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, v);

        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_clear);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                SQLiteDatabase database = usersDB.getWritableDatabase();
                listView.setAdapter(null);
                database.delete(DBHelper.TABLE_PAY, DBHelper.KEY_USER_NAME + " = ?", new String[]{MainProgram.getActiveUser()});
                database.close();
            default:
                return false;
        }
    }
}

