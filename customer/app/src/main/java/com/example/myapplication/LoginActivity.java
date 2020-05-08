package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.BackendClasses.Linker;


public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText idNumberView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        idNumberView = findViewById(R.id.idNumberView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(idNumberView.getText().toString());
                new Linker(id, false);
                try{
                    Thread.sleep(2000);
                }catch (Exception e){
                    System.out.println(e);
                }

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}