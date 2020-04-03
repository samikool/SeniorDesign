package waiter.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SyncTable extends AppCompatActivity {

    private Button backbutton;
    private EditText TableInput;
    private TextView Output;
    private Button test1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_table);

        backbutton = (Button)findViewById(R.id.SyncTabletoMenu);
        test1 = (Button)findViewById(R.id.SetSync);
        TableInput = (EditText) findViewById(R.id.SyncTableNumber);
        Output = (TextView)findViewById(R.id.Sync1);




        backbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToActivity2();
            }
        });

        test1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToMain();
            }
        });
    }
    private void moveToActivity2(){

        Intent intent = new Intent(SyncTable.this, Activity2.class);
        startActivity(intent);
    }

    private void moveToMain(){

        Intent intent1 = new Intent(SyncTable.this, MainActivity.class);
        startActivity(intent1);
    }
}
