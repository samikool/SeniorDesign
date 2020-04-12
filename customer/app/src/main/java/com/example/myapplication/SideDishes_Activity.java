package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SideDishes_Activity extends MainActivity {

    private RecyclerView recyclerView;
    public static ArrayList<Model> modelArrayList;
    public static ArrayList<Model> modelDescriptions;
    private SideDishes_CustomAdapter customAdapter;
    private Button btnnext;
    private String[] foodlist = new String[]{"mk", "j", "hj", "jk", "gy"};
    private String[] foodDescription = new String[]{"Asdf","asdf","asdf","asdf","adsf"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidedishes);

        recyclerView = (RecyclerView) findViewById(R.id.sideDishRecycler);
        btnnext = (Button) findViewById(R.id.nextbutton);

        modelArrayList = getModel();
        modelDescriptions = getModelDescriptions();
        customAdapter = new SideDishes_CustomAdapter(this);

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SideDishes_Activity.this, NextActivity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Model> getModel(){
        ArrayList<Model> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){

            Model model = new Model();
            model.setNumber(0);
            model.setFood(foodlist[i]);
            list.add(model);
        }
        return list;
    }

    private ArrayList<Model> getModelDescriptions(){
        ArrayList<Model> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){

            Model model = new Model();
            model.setNumber(0);
            model.setDescription(foodDescription[i]);
            list.add(model);
        }
        return list;
    }


}
