package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Variables for this activity
    private Button sendButton;
    private Button nextButton;
    private Button waiterButton;
    private Button tableButton;
    private TextView messageBox;
    private TextView responseBox;

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

        messageBox = findViewById(R.id.messageBox);

        todoList = new ArrayList<>(16);



    }

    public void sendButton(){
        linker.sendMessage(messageBox.getText().toString());
    }

    public void launchRecycle(){
        Intent intent = new Intent(this, RecycleActivity.class);
        startActivity(intent);
    }

    public void waiterMenu(){
        linker = new Linker(004, true, todoList);
        Intent intent = new Intent(this, WaiterMenu.class);
        startActivity(intent);
    }

    public void tableMenu(){
        linker = new Linker(2, false, todoList);
        Intent intent = new Intent(this, TableMenu.class);
        startActivity(intent);
    }


}
