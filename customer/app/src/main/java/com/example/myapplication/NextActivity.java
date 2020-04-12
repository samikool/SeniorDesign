package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;


public class NextActivity extends MainActivity {

    private TextView tv;
    private static String outputTextFinal;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        tv = (TextView) findViewById(R.id.tv);

        if (Menu.BBQFoods != null){
            for (int i = 0; i < Menu.BBQFoods.size(); i++) {
                if (Menu.BBQFoods.get(i).getNumber() > 0) {
                    String text = tv.getText().toString();
                    String text2 = Menu.BBQFoods.get(i).getFood();
                    int text3 = Menu.BBQFoods.get(i).getNumber();
                    String outputTextAppend = text + text2 + "->" + text3 + "\n";
                    outputTextFinal = outputTextFinal + outputTextAppend;
                }
            }
        }

        if(Menu.SideDishFoods != null){
            for (int i=0; i < Menu.SideDishFoods.size(); i++) {
                if (Menu.SideDishFoods.get(i).getNumber() > 0) {
                    String text = tv.getText().toString();
                    String text2 = Menu.SideDishFoods.get(i).getFood();
                    int text3 = Menu.SideDishFoods.get(i).getNumber();
                    String outputTextAppend = text + text2 + "->" + text3 + "\n";
                    outputTextFinal = outputTextFinal + outputTextAppend;
                }
            }
        }


        tv.setText(outputTextFinal);


    }
}