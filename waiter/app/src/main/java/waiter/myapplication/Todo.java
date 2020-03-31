package waiter.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Todo extends AppCompatActivity {

    private Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        backbutton = (Button)findViewById(R.id.TodotoMenu);

        backbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToActivity2();
            }
        });
    }

    private void moveToActivity2(){

        Intent intent = new Intent(Todo.this, Activity2.class);
        startActivity(intent);
    }
}
