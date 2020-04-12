package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button CallServer, BBQOrders, SideDishes, Drinks, Utensils, Check;
    private LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);


        CallServer = (Button) findViewById(R.id.Server_Call);
        BBQOrders = (Button) findViewById(R.id.BBQ_Orders);
        SideDishes = (Button) findViewById(R.id.Side_Dishes);
        Drinks = (Button) findViewById(R.id.Drinks);
        Utensils = (Button) findViewById(R.id.Utensils);
        Check = (Button) findViewById(R.id.Check);



        CallServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(getApplicationContext(), "Server Called!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

        BBQOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BBQ_Activity.class);
                startActivity(intent);
            }
        });

        SideDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SideDishes_Activity.class);
                startActivity(intent);
            }
        });

    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
