package com.example.taras.task_course;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.Double2;
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

public class Gas extends Activity {
    private static EditText etGas;
    private static TextView tvGasRes;
    private static Double finalGasRes = 0.0;
    private static boolean checkEt = false;

    public static String getEtGas() {
        return etGas.getText().toString();
    }
    public static boolean getCheckEt() { return checkEt;}
    public static EditText getForClearEt() { return etGas;}
    public static Double getFinalGasRes() {
        return finalGasRes;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas);

        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
               try {
                   String text = etGas.getText().toString();
                   Double temp = Double.parseDouble(text) * 6.879;
                   finalGasRes = Math.round(temp * 100.0) / 100.0;
                   String result = finalGasRes.toString();
                   tvGasRes.setText(result + " грн.");
               } catch (NumberFormatException e) {
                   tvGasRes.setText("");
               }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        etGas = (EditText) findViewById(R.id.etGas);
        //etGas.setText("");
        etGas.addTextChangedListener(inputTextWatcher);

        tvGasRes = (TextView) findViewById(R.id.tvGasRes);
        //tvGasRes.setText("");
        tvGasRes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etGas.getText().toString().equals("") && !Light.getCheckEt() && !Water.getCheckEt()) {
                    checkEt = false;
                    finalGasRes = 0.0;
                    MainProgram.tvSumUp.setText("");
                    MainProgram.textView2.setText("");
                }   else if(etGas.getText().toString().equals("")) {
                    checkEt = false;
                    finalGasRes = 0.0;
                    Double sumUp = Math.round((Light.getFinalLightRes() + Water.getFinalWaterRes())*100.0)/100.0;
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
        etGas.setHint(Html.fromHtml("м<sup>3</sup>"));
        etGas.setGravity(Gravity.CENTER);
    }
}
