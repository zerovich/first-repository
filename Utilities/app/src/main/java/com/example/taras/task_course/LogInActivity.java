package com.example.taras.task_course;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends Activity implements View.OnClickListener {

    private Button btn_enter;

    private EditText logInUser;
    private EditText logInPassword;

    private DBHelper usersDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        btn_enter = (Button) findViewById(R.id.btn_enter);
        btn_enter.setTransformationMethod(null);
        btn_enter.setOnClickListener(this);

        logInUser     = (EditText) findViewById(R.id.logInUser);
        logInPassword = (EditText) findViewById(R.id.logInPassword);

        logInUser.setGravity(Gravity.CENTER);
        logInPassword.setGravity(Gravity.CENTER);

        logInUser.setHint("Ім'я користувача");
        logInPassword.setHint("Пароль");

        usersDB = new DBHelper(this);

    }

    @Override
    public void onClick(View v) {

        String textLogInUser= logInUser.getText().toString();
        String textLogInPassword = logInPassword.getText().toString();

        SQLiteDatabase database = usersDB.getWritableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);


        while (cursor.moveToNext()) {

            String userName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_USER_NAME));
            String userPassword = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PASSWORD));


            if (userName.equals(textLogInUser) && userPassword.equals(textLogInPassword)){
                finish();
                Intent intent = new Intent(this, MainProgram.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("userName", userName);
                startActivity(intent);
                break;
            }
            if (cursor.isLast())
                Toast.makeText(LogInActivity.this, "Введіть правильні дані!", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        database.close();

    }
}
