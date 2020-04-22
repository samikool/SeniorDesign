package waiter.myapplication;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import waiter.myapplication.BackendClasses.Linker;


public class MainActivity extends AppCompatActivity {
    public static final String MAIN_NUMBER = "waiter.myapplication.MAIN_NUMBER";

    private static Button button;
    private static Button table1;
    private static Button table2;
    private static Button table3;
    private static Button table4;
    private static Button table5;
    private static Button table6;
    private static Button table7;
    private static Button table8;
    private static Button table9;
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

        //Thread analyzes times of checking to set colors
        new Thread(){
            public void run(){
                try{
                    while(true){
                        HashMap<Integer, Date> checkedTables = Linker.getCheckedMap();
                        for(Integer tid : checkedTables.keySet()){
                            Date checkedDate = checkedTables.get(tid);
                            Date currentDate = Calendar.getInstance().getTime();
                            long currentTime = currentDate.getTime();
                            long checkedTime = checkedDate.getTime();

                            double difference = currentTime - checkedTime;
                            difference /= 1000.;
                            difference /= 60.; //now in minutes
                            System.out.println();
                            if(difference < 1)
                                updateTableColor(tid, getColor(R.color.lightGreen));
                            else if(difference < 2)
                                updateTableColor(tid, getColor(R.color.yellow));
                            else if(difference < 5)
                                updateTableColor(tid, getColor(R.color.lightOrange));
                            else if(difference < 12)
                                updateTableColor(tid, getColor(R.color.orange));
                            else if(difference < 15)
                                updateTableColor(tid, getColor(R.color.darkOrange));
                            else if(difference >= 15)
                                updateTableColor(tid, getColor(R.color.red));

                            System.out.println();
                        }

                        Thread.sleep(5000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Linker.setCurrentView(findViewById(R.id.activity_main));
    }


    public static void updateTableColor(int tid, int color){
        switch(tid){
            case 1:
                table1.setBackgroundColor(color);
                break;
            case 2:
                table2.setBackgroundColor(color);
                break;
            case 3:
                table3.setBackgroundColor(color);
                break;
            case 4:
                table4.setBackgroundColor(color);
                break;
            case 5:
                table5.setBackgroundColor(color);
                break;
            case 6:
                table6.setBackgroundColor(color);
                break;
            case 7:
                table7.setBackgroundColor(color);
                break;
            case 8:
                table8.setBackgroundColor(color);
                break;
            case 9:
                table9.setBackgroundColor(color);
                break;
        }

    }

    private void moveToActivity2(){
        Intent intent = new Intent(MainActivity.this, AppMenu.class);
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
