package com.example.taras.task_course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;

public class Light extends Activity {
    private static EditText etLight;
    private TextView tvLightRes;
    private static Double finalLightRes = 0.0;
    private static boolean checkEt = false;

    public static String getEtLight() { return etLight.getText().toString();}
    public static EditText getForClearEt() { return etLight;}
    public static boolean getCheckEt() { return checkEt;}
    public static Double getFinalLightRes() {
        return finalLightRes;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
               try {
                   String text = etLight.getText().toString();
                   Double temp = Double.parseDouble(text) * 1.29;
                   finalLightRes = Math.round(temp * 100.0) / 100.0;
                   String result = finalLightRes.toString();
                   tvLightRes.setText(result + " грн.");
               } catch (NumberFormatException e) {
                   tvLightRes.setText("");
               }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };



        etLight = (EditText) findViewById(R.id.etLight);

        etLight.addTextChangedListener(inputTextWatcher);

        tvLightRes = (TextView) findViewById(R.id.tvLightRes);
        //tvLightRes.setText("");
        tvLightRes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etLight.getText().toString().equals("") && !Gas.getCheckEt() && !Water.getCheckEt()) {
                    checkEt = false;
                    finalLightRes = 0.0;
                    MainProgram.tvSumUp.setText("");
                    MainProgram.textView2.setText("");
                } else if(etLight.getText().toString().equals("")) {
                    checkEt = false;
                    finalLightRes = 0.0;
                    Double sumUp = Math.round((Water.getFinalWaterRes() + Gas.getFinalGasRes())*100.0)/100.0;
                    MainProgram.textView2.setText("До сплати:");
                    MainProgram.tvSumUp.setText(sumUp.toString() + " грн.");
                }
                else {
                    checkEt = true;
                    Double sumUp = Math.round((Light.getFinalLightRes() + Gas.getFinalGasRes() + Water.getFinalWaterRes())*100.0)/100.0;
                    MainProgram.textView2.setText("До сплати:");
                    MainProgram.tvSumUp.setText(sumUp.toString() + " грн.");
                }
            }
        });

        //Поле
        etLight.setHint("кВт/год");
        etLight.setGravity(Gravity.CENTER);
    }


}
