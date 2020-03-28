package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    //Variables for this activity
    private Button sendButton;
    private Button nextButton;
    private Button waiterButton;
    private Button tableButton;
    private TextView messageBox;
    private TextView responseBox;
    private TextView tableNumberBox;
    private TextView waiterNumberBox;

    //Variables for other activities
    private static Linker linker;
    private static ArrayList<String> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButton();

            }
        });
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRecycle();
            }
        });
        waiterButton = findViewById(R.id.waiterButton);
        waiterButton.setOnClickListener(new View.OnClickListener() {
            @Override public void
            onClick(View v) {
                waiterMenu();
            }
        });
        tableButton = findViewById(R.id.tableButton);
        tableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableMenu();
            }
        });

        tableNumberBox = findViewById(R.id.tableNumberBox);
        waiterNumberBox = findViewById(R.id.waiterNumberBox);

        messageBox = findViewById(R.id.messageBox);

        todoList = new ArrayList<>(16);
    }

    public void sendButton(){
        Linker.sendMessage(messageBox.getText().toString());
    }

    public void launchRecycle(){
        Intent intent = new Intent(this, RecycleActivity.class);
        startActivity(intent);
    }

    public void waiterMenu(){
        Intent intent = new Intent(this, WaiterMenu.class);
        startActivity(intent);
        int id = Integer.parseInt(waiterNumberBox.getText().toString());
        new Linker(id, true, todoList);
    }



    public void tableMenu(){
        Intent intent = new Intent(this, TableMenu.class);
        startActivity(intent);
        int id = Integer.parseInt(tableNumberBox.getText().toString());
        new Linker(id, false, todoList);
    }
}
