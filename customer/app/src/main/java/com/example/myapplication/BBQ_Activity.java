package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class BBQ_Activity extends MainActivity {

    private RecyclerView recyclerView;
    public static ArrayList<Model> modelArrayList;
    public static ArrayList<Model> modelDescriptions;
    private CustomAdapter customAdapter;
    private Button btnnext;
    private String[] foodlist = new String[]{BBQ_Menu.menuItem1, BBQ_Menu.menuItem2, BBQ_Menu.menuItem3, BBQ_Menu.menuItem4, BBQ_Menu.menuItem5};
    private String[] foodDescription = new String[]{BBQ_Menu.menuDescription1, BBQ_Menu.menuDescription2, BBQ_Menu.menuDescription3, BBQ_Menu.menuDescription4, BBQ_Menu.menuDescription5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbq);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        btnnext = (Button) findViewById(R.id.next);

        modelArrayList = getModel();
        modelDescriptions = getModelDescriptions();
        customAdapter = new CustomAdapter(this);

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BBQ_Activity.this,NextActivity.class);
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