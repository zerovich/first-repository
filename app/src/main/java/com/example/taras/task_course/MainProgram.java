package com.example.taras.task_course;

import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainProgram extends TabActivity implements PopupMenu.OnMenuItemClickListener, View.OnClickListener {


    private Button btn_pay;
    private Button btn_history;

    private DBHelper usersDB;


    private NumberPicker pick_year;
    private NumberPicker pick_month;

    public static TextView tvSumUp;
    public static TextView textView2;

    private static String activeUser;

    public static String getActiveUser() {
        return activeUser;
    }

    final String[] months = {"січень", "лютий", "березень", "квітень", "травень", "червень", "липень",
            "серпень", "вересень", "жовтень", "листопад", "грудень"};


    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_program);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("Світло");
        tabSpec.setIndicator("Світло");
        tabSpec.setContent(new Intent(this, Light.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Газ");
        tabSpec.setIndicator("Газ");
        tabSpec.setContent(new Intent(this, Gas.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Вода");
        tabSpec.setIndicator("Вода");
        tabSpec.setContent(new Intent(this, Water.class));
        tabHost.addTab(tabSpec);

        //Міняю колір тексту вкладок
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }

        Intent intent = getIntent();
        activeUser = intent.getStringExtra("userName");
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText("Вітаю, " + activeUser + "!");


        ImageView image = (ImageView) findViewById(R.id.btn_popup);
        image.setOnClickListener(onClickListener);


        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_history = (Button) findViewById(R.id.btn_history);

        pick_year = (NumberPicker) findViewById(R.id.pick_year);
        pick_year.setMinValue(2010);
        pick_year.setMaxValue(2100);
        pick_year.setWrapSelectorWheel(true);
        pick_year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        pick_month = (NumberPicker) findViewById(R.id.pick_month);
        pick_month.setMinValue(0);
        pick_month.setMaxValue(months.length - 1);
        pick_month.setWrapSelectorWheel(true);
        pick_month.setDisplayedValues(months);
        pick_month.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        btn_pay.setOnClickListener(this);
        btn_history.setOnClickListener(this);

        usersDB = new DBHelper(this);



        tvSumUp = (TextView) findViewById(R.id.tvSumUp);
        textView2 = (TextView) findViewById(R.id.textView2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:


                String strSum = tvSumUp.getText().toString();
                String newStrSum = "";
                for (int i = 0; i < strSum.length(); i++) {
                    if (strSum.charAt(i) == ' ') {
                        newStrSum = strSum.substring(0,i);
                        break;
                    }
                }

                //Об'єм
                Double lightVol;
                Double gasVol;
                Double waterVol;

                try {
                    lightVol = Double.parseDouble(Light.getEtLight());
                } catch (NumberFormatException e) {
                    lightVol = 0.0;
                }
                try {
                    gasVol = Double.parseDouble(Gas.getEtGas());
                } catch (NumberFormatException e) {
                    gasVol   = 0.0;
                }
                try {
                    waterVol = Double.parseDouble(Water.getEtWater());
                } catch (NumberFormatException e) {
                    waterVol = 0.0;
                }

                //Вартість
                Double lightPay = Light.getFinalLightRes();
                Double gasPay = Gas.getFinalGasRes();
                Double waterPay = Water.getFinalWaterRes();

                //Загальна сума
                Double sumUp;
                try {
                    sumUp = Double.parseDouble(newStrSum);
                } catch (NumberFormatException e) {
                    sumUp = 0.0;
                }

                //Рік
                Integer year = pick_year.getValue();

                //Місяць
                Integer tempMonth = pick_month.getValue();
                String month = months[tempMonth];

                SQLiteDatabase database = usersDB.getWritableDatabase();

                Cursor cursor = database.query(DBHelper.TABLE_PAY, null, null, null, null, null, null);

                ContentValues contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_USER_NAME, activeUser);
                contentValues.put(DBHelper.KEY_YEAR, year);
                contentValues.put(DBHelper.KEY_MONTH, month);
                contentValues.put(DBHelper.KEY_LIGHT, lightVol);
                contentValues.put(DBHelper.KEY_LIGHT_MONEY, lightPay);
                contentValues.put(DBHelper.KEY_GAS, gasVol);
                contentValues.put(DBHelper.KEY_GAS_MONEY, gasPay);
                contentValues.put(DBHelper.KEY_WATER, waterVol);
                contentValues.put(DBHelper.KEY_WATER_MONEY, waterPay);
                contentValues.put(DBHelper.KEY_SUM_UP, sumUp);



                boolean checkUpdateRow = false;
                while (cursor.moveToNext()) {
                    String userName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_USER_NAME));
                    String pickedYear = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_YEAR));
                    String pickedMonth = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_MONTH));
                    if (userName.equals(activeUser) && pickedYear.equals(year.toString()) && pickedMonth.equals(month)){
                        checkUpdateRow = true;
                        database.update(DBHelper.TABLE_PAY, contentValues, DBHelper.KEY_YEAR + " = ? AND " +
                                DBHelper.KEY_MONTH + " = ?", new String[] {year.toString(),month});
                        break;
                    }
                }

                if (!checkUpdateRow) {
                    database.insert(DBHelper.TABLE_PAY, null, contentValues);
                }
                cursor.close();
                database.close();

                clearET(Light.getForClearEt());
                clearET(Gas.getForClearEt());
                clearET(Water.getForClearEt());





                Toast toast = Toast.makeText(MainProgram.this, "Оплачено!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,160);
                toast.show();
                break;
            case R.id.btn_history:
                Intent intent = new Intent(this, History.class);
                startActivity(intent);

                break;
        }
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
        popup.inflate(R.menu.menu_exit);
        popup.inflate(R.menu.menu_exit_program);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
               Intent intent = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
          //  break;
            case R.id.menu_exit_program:
                finish();
                System.exit(0);

         //       break;
            default:return false;
        }
    }

    public static void clearET(EditText e) {
        e.setText("");
    }



}



