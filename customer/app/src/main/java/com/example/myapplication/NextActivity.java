package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NextActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        tv = (TextView) findViewById(R.id.tv);

        for (int i = 0; i < 5; i++) {
            String text = tv.getText().toString();
            String text2 = BBQ_Activity.modelArrayList.get(i).getFood();
            int text3 = BBQ_Activity.modelArrayList.get(i).getNumber();
            String outputText = text + text2 + "->" + text3 + "\n";
            tv.setText(outputText);
        }
    }
}