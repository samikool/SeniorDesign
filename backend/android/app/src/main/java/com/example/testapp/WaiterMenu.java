package com.example.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class WaiterMenu extends AppCompatActivity {
    private Button claimButton;
    private Button orderButton;
    private Button checkButton;
    private Button closeButton;
    private TextView claimNumberView;
    Receipt receipt;
    private static HashMap<Integer, Receipt> receiptMap = new HashMap<Integer, Receipt>();

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_waiter_menu);

        claimButton = findViewById(R.id.claimButton);
        orderButton = findViewById(R.id.orderButton);
        checkButton = findViewById(R.id.checkButton);
        closeButton = findViewById(R.id.closeButton);
        claimNumberView = findViewById(R.id.claimNumberView);

        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int claimTid = Integer.parseInt(claimNumberView.getText().toString());
                Linker.claim(claimTid);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int randomNum = random.nextInt(4);
                if(randomNum == 0){
                    Linker.orderBBQ(1,2, 2);
                }
                else if(randomNum == 1){
                    Linker.orderBBQ(2, 1, 2);
                }
                else if(randomNum == 2){
                    Linker.orderDrink(1, 2, 2);
                }
                else if(randomNum == 3){
                    Linker.orderSide(0, 1, 2);
                }
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linker.printReceipt();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
