package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    //Variables for this activity
    private Button sendButton;
    private Button nextButton;
    private TextView messageBox;
    private TextView responseBox;
    private Connection connection;

    int i = 0;

    //Variables for other activities
    private Linker linker;
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


        messageBox = findViewById(R.id.messageBox);
        responseBox = findViewById(R.id.responseBox);

        todoList = new ArrayList<>(16);
        linker = new Linker(todoList);

        try{
            //emulator
            connection = new Connection("10.0.2.2", 4044, linker);
            //phone at home
            //connection = new Connection("192.168.0.20", 4044);
            connection.start();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    public void sendButton(){
        connection.sendData("hello " + i);
        i++;
    }

    public void launchRecycle(){
        Intent intent = new Intent(this, RecycleActivity.class);
        startActivity(intent);
    }


}
