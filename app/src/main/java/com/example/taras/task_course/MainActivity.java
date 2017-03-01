package com.example.taras.task_course;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btn_log_in;
    private Button btn_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btn_log_in = (Button) findViewById(R.id.btn_log_in);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);

        btn_log_in.setTransformationMethod(null);
        btn_sign_up.setTransformationMethod(null);

        btn_log_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_log_in:
                intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sign_up:
                intent = new Intent(this, SingUpActivity.class);
                startActivity(intent);
                break;
        }
    }


}
