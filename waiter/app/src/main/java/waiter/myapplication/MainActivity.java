package waiter.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String MAIN_NUMBER = "waiter.myapplication.MAIN_NUMBER";

    private Button button;
    private Button table1;
    private Button table2;
    private Button table3;
    private Button table4;
    private Button table5;
    private Button table6;
    private Button table7;
    private Button table8;
    private Button table9;
    private Button TodoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.MaintoMenu);
        table1 = (Button)findViewById(R.id.MaintoTable1);
        table2 = (Button)findViewById(R.id.MaintoTable2);
        table3 = (Button)findViewById(R.id.MaintoTable3);
        table4 = (Button)findViewById(R.id.MaintoTable4);
        table5 = (Button)findViewById(R.id.MaintoTable5);
        table6 = (Button)findViewById(R.id.MaintoTable6);
        table7 = (Button)findViewById(R.id.MaintoTable7);
        table8 = (Button)findViewById(R.id.MaintoTable8);
        table9 = (Button)findViewById(R.id.MaintoTable9);
        TodoButton = (Button)findViewById(R.id.MaintoTodo);


        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToActivity2();
            }
        });

        TodoButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                int num = 1;
                moveToTodo(num);
            }
        });

        table1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                int num = 1;
                moveToTables(num);
            }
        });

        table2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                int num = 2;
                moveToTables(num);
            }
        });

        table3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                int num = 3;
                moveToTables(num);
            }
        });

        table4.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View v){
                int num = 4;
                moveToTables(num);
            }
        });

        table5.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View v){
                int num = 5;
                moveToTables(num);
            }
        });

        table6.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View v){
                int num = 6;
                moveToTables(num);
            }
        });

        table7.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View v){
                int num = 7;
                moveToTables(num);
            }
        });

        table8.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View v){
                int num = 8;
                moveToTables(num);
            }
        });

        table9.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View v){
                int num = 9;
                moveToTables(num);
            }
        });



    }

    private void moveToActivity2(){

        Intent intent = new Intent(MainActivity.this, Activity2.class);
        startActivity(intent);
    }

    private void moveToTables(int z){

        Intent intent1= new Intent(MainActivity.this, Tables.class);
        intent1.putExtra(MAIN_NUMBER, z);
        startActivity(intent1);
    }

    private void moveToTodo(int z){

        Intent intent2= new Intent(MainActivity.this, Todo.class);
        intent2.putExtra(MAIN_NUMBER, z);
        startActivity(intent2);
    }




}
