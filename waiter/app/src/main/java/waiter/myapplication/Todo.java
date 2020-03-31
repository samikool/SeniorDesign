package waiter.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Todo extends AppCompatActivity {

    private Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);


        Intent MENUpage = getIntent();
        int pagenumber = MENUpage.getIntExtra(MainActivity.MAIN_NUMBER, 0);
        TextView textView1 = (TextView) findViewById(R.id.number);
        textView1.setText(""+ pagenumber);

        Intent MAINpage = getIntent();
        int pagenumber1 = MAINpage.getIntExtra(Activity2.MENU_NUMBER, 0);
        TextView textView2 = (TextView) findViewById(R.id.number2);
        textView2.setText(""+ pagenumber1);


        backbutton = (Button)findViewById(R.id.TodotoMenu);

        backbutton.setOnClickListener(new View.OnClickListener(){


                @Override
                public void onClick (View v){
                moveToActivity2();

            }

        });
    }

    private void moveToActivity2(){

        Intent intent = new Intent(Todo.this, Activity2.class);
        startActivity(intent);
    }

    private void moveToActivity1(){

        Intent intent2 = new Intent(Todo.this, MainActivity.class);
        startActivity(intent2);
    }
}
