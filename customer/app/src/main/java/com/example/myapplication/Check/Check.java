package com.example.myapplication.Check;

import android.os.Bundle;
import android.widget.LinearLayout;

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
    private static boolean voiding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalized_check);
        itemContainer = findViewById(R.id.itemContainer);
        voiding = getIntent().getBooleanExtra("void", false);
    }

    public static boolean isVoiding(){return voiding;}

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
