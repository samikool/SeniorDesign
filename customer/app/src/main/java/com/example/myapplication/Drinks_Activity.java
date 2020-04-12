package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Drinks_Activity extends MainActivity {

    private RecyclerView recyclerView;
    private Drinks_CustomAdapter customAdapter;
    private Button btnnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        recyclerView = (RecyclerView) findViewById(R.id.drinkRecycler);
        btnnext = (Button) findViewById(R.id.nextb);

        if(Menu.Drinks == null) {
            Menu.Drinks_Submenu();
        }

        customAdapter = new Drinks_CustomAdapter(this);

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Drinks_Activity.this,NextActivity.class);
                startActivity(intent);
            }
        });
    }
}