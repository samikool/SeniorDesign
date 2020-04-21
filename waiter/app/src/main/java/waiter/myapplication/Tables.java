package waiter.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class Tables extends AppCompatActivity {
    public static final String Table_Need = "waiter.myapplication.Table_Need";

    private Button backbutton;
    private Button claimButton;
    private Button orderButton;
    private Button SendCheckbutton;
    private Button MarkTablebutton;
    private Button HelpTableButton;
    private Button OptOutButton;
    private TextView Tablenumtext;

    int Send = 0;
    int Help = 0;
    int Mark =0;
    int OptOut = 0;
    int tableneed = 0;
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
        SendCheckbutton = (Button)findViewById(R.id.SendChecktoTable);
        MarkTablebutton = (Button)findViewById(R.id.MarkTable);
        HelpTableButton = (Button)findViewById(R.id.HelpedTable);
        OptOutButton = (Button)findViewById(R.id.OptOutTable);
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

        SendCheckbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Intent intent = new Intent(Tables.this, DisplayReceipt.class);
                startActivity(intent);
            }
        });

        HelpTableButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                if(Help%2 ==0) {
                    HelpTableButton.setBackgroundColor(Color.GREEN);

                }
                else{

                    HelpTableButton.setBackgroundColor(Color.LTGRAY);
                }
                Help =Help+1;
            }
        });

        MarkTablebutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                if(Mark%2 == 0){
                    MarkTablebutton.setBackgroundColor(Color.RED);
                    tableneed = 1;
                }
                else{
                    MarkTablebutton.setBackgroundColor(Color.LTGRAY);
                    tableneed = 0;

                }
                Mark = Mark+1;
            }
        });

        OptOutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                if(OptOut%2 ==0) {
                    OptOutButton.setBackgroundColor(Color.RED);

                }
                else{

                    OptOutButton.setBackgroundColor(Color.LTGRAY);
                }
                OptOut =OptOut+1;
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
