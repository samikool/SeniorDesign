package waiter.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import waiter.myapplication.BackendClasses.Linker;
import waiter.myapplication.DisplayMenu.Order;
import waiter.myapplication.DisplayReceipt.DisplayReceipt;

public class Tables extends AppCompatActivity {
    public static final String Table_Need = "waiter.myapplication.Table_Need";

    private Button backbutton;
    private Button claimButton;
    private Button orderButton;
    private Button seeCheckButton;
    private Button voidItemsButton;
    private Button MarkTablebutton;
    private Button HelpTableButton;
    private Button closeButton;
    private TextView Tablenumtext;

    private static int Tablenumber;
    private static final int ORDERED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        Intent intent1 = getIntent();
        Tablenumber = intent1.getIntExtra(MainActivity.MAIN_NUMBER, 0);

        backbutton = (Button)findViewById(R.id.TablestoMain);
        claimButton = (Button)findViewById(R.id.ClaimTable);
        orderButton = findViewById(R.id.TakeOrderforTable);
        seeCheckButton = (Button)findViewById(R.id.SendChecktoTable);
        voidItemsButton = findViewById(R.id.voidItemsButton);
        MarkTablebutton = (Button)findViewById(R.id.markTable);
        HelpTableButton = (Button)findViewById(R.id.HelpedTable);
        closeButton = (Button)findViewById(R.id.closeTable);
        Tablenumtext = (TextView)findViewById(R.id.Tablenumbertext);

        Tablenumtext.setText(""+Tablenumber);

        backbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                finish();
            }
        });

        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Linker.claim(Tablenumber);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tables.this, Order.class);
                intent.putExtra("Tablenumber", Tablenumber);
                startActivityForResult(intent, ORDERED);
            }
        });

        seeCheckButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(Tables.this, DisplayReceipt.class);
                intent.putExtra("void", false);
                startActivity(intent);
            }
        });

        voidItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tables.this, DisplayReceipt.class);
                intent.putExtra("void", true);
                startActivity(intent);
            }
        });

        MarkTablebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Linker.markTable(Tablenumber);
                Snackbar.make(v, (String) "Table has been marked for visit", Snackbar.LENGTH_LONG).show();
            }
        });

        HelpTableButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Linker.checkedTable(Tablenumber);
                Snackbar.make(v, (String) "Table has been marked as checked", Snackbar.LENGTH_LONG).show();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Linker.closeTable(Tablenumber);
            }
        });




    }

    public static int getTablenumber() {
        return Tablenumber;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ORDERED && resultCode == Activity.RESULT_OK){
            Linker.setCurrentView(this.findViewById(R.id.activity_tables));
            boolean ordered = data.getBooleanExtra("ordered", false);
            int tableNumber = data.getIntExtra("Tablenumber", 0);

            if(tableNumber != 0){
                Tablenumber = tableNumber;
            }

            if(ordered){
                Snackbar.make(this.findViewById(R.id.activity_tables), "Items ordered for Table: " + Tablenumber, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            Tablenumtext.setText(""+Tablenumber);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Linker.setCurrentView(this.findViewById(R.id.activity_tables));
    }

}
