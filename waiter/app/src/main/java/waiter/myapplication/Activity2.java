package waiter.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity2 extends AppCompatActivity {

    private Button backbutton;
    private Button MyTablesButton;
    private Button SettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        backbutton = (Button)findViewById(R.id.button2);
        MyTablesButton = (Button)findViewById(R.id.MyTablesMENU);
        SettingsButton = (Button)findViewById(R.id.MenutoSettings);
        backbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToActivity1();
            }
        });

        MyTablesButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToMyTables();
            }
        });

        SettingsButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToSettings();
            }
        });
    }

    private void moveToActivity1(){

        Intent intent = new Intent(Activity2.this, MainActivity.class);
        startActivity(intent);
    }

    private void moveToMyTables(){

        Intent intent1 = new Intent(Activity2.this, MyTables.class);
        startActivity(intent1);
    }

    private void moveToSettings(){

        Intent intent2 = new Intent(Activity2.this, MyTables.class);
        startActivity(intent2);
    }
}
