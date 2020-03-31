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
    private Button SyncTableButton;
    private Button TodoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        backbutton = (Button)findViewById(R.id.button2);
        MyTablesButton = (Button)findViewById(R.id.MyTablesMENU);
        SettingsButton = (Button)findViewById(R.id.MenutoSettings);
        SyncTableButton = (Button)findViewById(R.id.MenutoSyncTable);
        TodoButton = (Button)findViewById(R.id.MenutoTodo);

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

        SyncTableButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToSyncTable();
            }
        });

        TodoButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToTodo();
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

    private void moveToSyncTable(){

        Intent intent3 = new Intent(Activity2.this, SyncTable.class);
        startActivity(intent3);
    }

    private void moveToTodo(){

        Intent intent4 = new Intent(Activity2.this, Todo.class);
        startActivity(intent4);
    }
}
