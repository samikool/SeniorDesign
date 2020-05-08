package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.BackendClasses.Item;
import com.example.myapplication.BackendClasses.Linker;
import com.example.myapplication.Check.Check;

import java.util.ArrayList;

public class BBQ_Activity extends MainActivity {
    public static ArrayList<Item> BBQFoods;

    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private Button btnnext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbq);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        btnnext = (Button) findViewById(R.id.next);

        BBQFoods = Linker.getBBQItems();

        customAdapter = new CustomAdapter(this);

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


        for (int i=0; i<BBQFoods.size(); i++){
            System.out.println(BBQFoods.get(i).getName());
        }

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BBQ_Activity.this, com.example.myapplication.Check.Check.class);
                //intent.setFlags(Intent.);
                //intent.putExtra(fromMain)
                startActivity(intent);
            }
        });
    }
}