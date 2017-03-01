package com.example.taras.task_course;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SingUpActivity extends Activity {

    private EditText etUserName;
    private EditText etStreet;
    private EditText etHouse;
    private EditText etApartment;
    private EditText etPassword;

    private Button btn_save;

    private DBHelper usersDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        etUserName      = (EditText) findViewById(R.id.etUserName);
        etStreet        = (EditText) findViewById(R.id.etStreet);
        etHouse         = (EditText) findViewById(R.id.etHouse);
        etApartment     = (EditText) findViewById(R.id.etApartment);
        etPassword      = (EditText) findViewById(R.id.etPassword);

        btn_save    = (Button) findViewById(R.id.btn_save);

        etUserName.setGravity(Gravity.CENTER);
        etStreet.setGravity(Gravity.CENTER);
        etHouse.setGravity(Gravity.CENTER);
        etApartment.setGravity(Gravity.CENTER);
        etPassword.setGravity(Gravity.CENTER);

        etUserName.setHint("Ім'я користувача");
        etStreet.setHint("Вулиця");
        etHouse.setHint("Будинок");
        etApartment.setHint("Квартира");
        etPassword.setHint("Пароль");

        usersDB = new DBHelper(this);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textUserName = etUserName.getText().toString();
                String textStreet = etStreet.getText().toString();
                String textHouse = etHouse.getText().toString();
                String textApartment = etApartment.getText().toString();
                String textPassword = etPassword.getText().toString();

                SQLiteDatabase database = usersDB.getWritableDatabase();

                Cursor cursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

                while (cursor.moveToNext()) {

                    String userName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_USER_NAME));
                    String userStreet = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_STREET));
                    String userHouse = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_HOUSE));
                    String userApartment = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_APARTMENT));

                    if (userName.equals(textUserName)) {
                        Toast.makeText(SingUpActivity.this, "Такий користувач вже зареєстрований!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (userStreet.equals(textStreet) && userHouse.equals(textHouse) && userApartment.equals(textApartment)) {
                        Toast.makeText(SingUpActivity.this, "Така квартира вже зареєстрована!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                cursor.close();


                if (textUserName.matches("") || textStreet.matches("")
                        || textHouse.matches("") || textApartment.matches("") || textPassword.matches("")) {
                    Toast.makeText(SingUpActivity.this, "Заповніть всі поля!", Toast.LENGTH_SHORT).show();
                } else {

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DBHelper.KEY_USER_NAME, textUserName);
                    contentValues.put(DBHelper.KEY_STREET, textStreet);
                    contentValues.put(DBHelper.KEY_HOUSE, textHouse);
                    contentValues.put(DBHelper.KEY_APARTMENT, textApartment);
                    contentValues.put(DBHelper.KEY_PASSWORD, textPassword);

                    database.insert(DBHelper.TABLE_USERS, null, contentValues);

                    database.close();

                    Toast.makeText(SingUpActivity.this, "Збережено", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
