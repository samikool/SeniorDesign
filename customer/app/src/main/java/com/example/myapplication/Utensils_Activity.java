package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Utensils_Activity extends MainActivity {

    private RecyclerView recyclerView;
    private Utensils_CustomAdapter customAdapter;
    private Button btnnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utensils);

        recyclerView = (RecyclerView) findViewById(R.id.UtensilsRecycler);
        btnnext = (Button) findViewById(R.id.nextbb);

        if(Menu.Utensils == null) {
            Menu.Utensils_Submenu();
        }
        customAdapter = new Utensils_CustomAdapter(this);

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Utensils_Activity.this,NextActivity.class);
                startActivity(intent);
            }
        });
    }
}