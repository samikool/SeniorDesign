package waiter.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
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
        final int p2 = pagenumber;

        Intent MAINpage = getIntent();
        int pagenumber1 = MAINpage.getIntExtra(Activity2.MENU_NUMBER, 0);



        backbutton = (Button)findViewById(R.id.TodotoMenu);

        backbutton.setOnClickListener(new View.OnClickListener(){



                @Override
                public void onClick (View v){

                    if(p2 == 0) {
                        moveToActivity2();
                    }
                    else{
                        moveToActivity1();
                    }
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
