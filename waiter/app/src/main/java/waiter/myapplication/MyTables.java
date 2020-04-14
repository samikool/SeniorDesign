package waiter.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyTables extends AppCompatActivity {

    private Button backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tables);

        backbutton = (Button)findViewById(R.id.button7);

        backbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToActivity2();
            }
        });
    }

    private void moveToActivity2(){

        Intent intent = new Intent(MyTables.this, Activity2.class);
        startActivity(intent);
    }
}
