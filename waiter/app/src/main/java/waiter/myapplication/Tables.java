package waiter.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Tables extends AppCompatActivity {
    public static final String TABLE_NEED = "waiter.myapplication.TABLE_NEED";
    public static final String TABLE_NUMBER = "waiter.myapplication.TABLE_NUMBER";

    private Button backbutton;
    private Button SendCheckbutton;
    private Button MarkTablebutton;
    int tableneed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        Intent intent1 = getIntent();
        final int Tablenumber = intent1.getIntExtra(MainActivity.MAIN_NUMBER, 0);

        backbutton = (Button)findViewById(R.id.TablestoMain);
        SendCheckbutton = (Button)findViewById(R.id.SendChecktoTable);
        MarkTablebutton = (Button)findViewById(R.id.MarkTable);

        backbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToActivity1(Tablenumber, tableneed);
            }
        });

        SendCheckbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                SendCheckbutton.setBackgroundColor(Color.GREEN);
            }
        });

        MarkTablebutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                tableneed = 1;
                MarkTablebutton.setBackgroundColor(Color.RED);
            }
        });




    }

    private void moveToActivity1(int y, int z){

        Intent intent = new Intent(Tables.this, MainActivity.class);
        intent.putExtra(TABLE_NEED, z);
        intent.putExtra(TABLE_NUMBER, y);
        startActivity(intent);
    }
}
