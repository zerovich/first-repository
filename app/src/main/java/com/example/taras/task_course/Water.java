package com.example.taras.task_course;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Water extends Activity {
    private static EditText etWater;
    private static TextView tvWaterRes;
    private static Double finalWaterRes = 0.0;
    private static boolean checkEt = false;

    public static String getEtWater() { return etWater.getText().toString();}
    public static EditText getForClearEt() { return etWater;}

    public static boolean getCheckEt() { return checkEt;}
    public static Double getFinalWaterRes() {
        return finalWaterRes;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    String text = etWater.getText().toString();
                    Double temp = Double.parseDouble(text) * 5.04;
                    finalWaterRes = Math.round(temp * 100.0) / 100.0;
                    String result = finalWaterRes.toString();
                    tvWaterRes.setText(result + " грн.");
                } catch (NumberFormatException e) {
                    tvWaterRes.setText("");
                }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        etWater = (EditText) findViewById(R.id.etWater);
        //etWater.setText("");
        etWater.addTextChangedListener(inputTextWatcher);

        tvWaterRes = (TextView) findViewById(R.id.tvWaterRes);
        //tvWaterRes.setText("");
        tvWaterRes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etWater.getText().toString().equals("") && !Light.getCheckEt() && !Gas.getCheckEt()) {
                    checkEt = false;
                    finalWaterRes = 0.0;
                    MainProgram.tvSumUp.setText("");
                    MainProgram.textView2.setText("");
                } else if(etWater.getText().toString().equals("")) {
                    checkEt = false;
                    finalWaterRes = 0.0;
                    Double sumUp = Math.round((Light.getFinalLightRes() + Gas.getFinalGasRes())*100.0)/100.0;
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
        etWater.setHint(Html.fromHtml("м<sup>3</sup>"));
        etWater.setGravity(Gravity.CENTER);
    }
}
