package com.example.myapplication.Check;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.BackendClasses.Item;
import com.example.myapplication.BackendClasses.Linker;
import com.example.myapplication.BackendClasses.Receipt;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class Check extends AppCompatActivity {

    private LinearLayout itemContainer;
    private static Receipt receipt;
    private int Tablenumber;
    private static boolean orderServer = false;
    private Button btnnext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalized_check);
        itemContainer = findViewById(R.id.itemContainer);

        btnnext = (Button) findViewById(R.id.GenericButton);
        orderServer = getIntent().getBooleanExtra("void", false);



        if(isVoiding() == false){
            btnnext.setText("Send Order to Server");

            btnnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast myToast = Toast.makeText(getApplicationContext(), "Order Called!", Toast.LENGTH_SHORT);
                    myToast.show();
                    Linker.orderItems(Check.getReceipt());
                    MainActivity.receipt = new Receipt(Linker.getId());
                    finish();
                }
            });
        } else {
            btnnext.setText("Ask for Check");
            btnnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast myToast = Toast.makeText(getApplicationContext(), "Check Requested!", Toast.LENGTH_SHORT);
                    myToast.show();
                    Linker.requestCheck();
                }
            });
        }
    }

    public boolean isVoiding(){

        return orderServer;}

    @Override
    protected void onResume() {
        super.onResume();
        receipt = MainActivity.receipt;
        int i=0;
        for(Item item : receipt.getItems()){
            int quant = receipt.getItemCount(item);
            getSupportFragmentManager().beginTransaction().add(itemContainer.getId(), ItemTile.newInstance(item, quant), "item"+i++).commit();
        }
    }

    public static Receipt getReceipt() {
        return receipt;
    }



}
