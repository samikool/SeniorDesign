package com.example.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.LinkedBlockingQueue;

public class TableMenu extends AppCompatActivity {
    private Button callButton;
    private Button drinkButton;
    private Button bbqButton;
    private Button checkButton;
    private Button sideButton;
    private Button utensilButton;
    private Receipt receipt;
    private TextView idView;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_table_menu);
        Linker.currentView = findViewById(R.id.tableLayout);
        callButton = findViewById(R.id.callButton);
        drinkButton = findViewById(R.id.drinkButton);
        bbqButton = findViewById(R.id.bbqButton);
        checkButton = findViewById(R.id.checkButton);
        sideButton = findViewById(R.id.sideButton);
        utensilButton = findViewById(R.id.utensilButton);
        idView = findViewById(R.id.idView);

        receipt = new Receipt(2);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linker.call();
            }
        });

        drinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linker.orderDrink(1,2);
            }
        });

        bbqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linker.orderBBQ(1,2);
            }
        });

        sideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linker.orderSide(1,2);
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linker.requestCheck();
                Linker.printReceipt();
            }
        });

        utensilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linker.requestUtensil("fork", 2);
            }
        });
        String claimView = "Table: " + Linker.getId();
        idView.setText(claimView);
    }
}
