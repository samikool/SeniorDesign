package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ExecutorService executor = Executors.newCachedThreadPool();
    Client client = new Client();
    Button sendButton;
    TextView messageBox;
    TextView responseBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence message = messageBox.getText();
                String response = client.send(messageBox.getText().toString());
                responseBox.setText(response);
            }
        });
        messageBox = findViewById(R.id.messageBox);
        responseBox = findViewById(R.id.responseBox);
    }

    public void sendButton(){

    }


}
