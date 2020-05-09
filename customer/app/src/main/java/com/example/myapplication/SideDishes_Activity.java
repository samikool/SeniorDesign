package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.BackendClasses.Item;
import com.example.myapplication.BackendClasses.Linker;

import java.util.ArrayList;

public class SideDishes_Activity extends MainActivity {
    public static ArrayList<Item> SideFoods;

    private RecyclerView recyclerView;
    private SideDishes_CustomAdapter customAdapter;
    private Button btnnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidedishes);

        recyclerView = (RecyclerView) findViewById(R.id.sideDishRecycler);
        btnnext = (Button) findViewById(R.id.nextbutton);

        SideFoods = new ArrayList<Item>();
        for(int i=0; i<Linker.getSideItems().size(); i++){
            SideFoods.add(Linker.getSideItems().get(i));
        }

        for(int i=0; i<SideFoods.size(); i++){
            if(SideFoods.get(i).getName() == null){
                SideFoods.remove(i);
            }
        }

        /*if(Menu.SideDishFoods == null) {
            Menu.SideDish_Submenu();
        }*/

        customAdapter = new SideDishes_CustomAdapter(this);

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SideDishes_Activity.this, com.example.myapplication.Check.Check.class);
                intent.putExtra("void", false);
                startActivity(intent);
                finish();
            }
        });
    }
}
