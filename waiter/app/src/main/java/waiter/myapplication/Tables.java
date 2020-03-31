package waiter.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Tables extends AppCompatActivity {

    private Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        Intent intent1 = getIntent();
        int Tablenumber = intent1.getIntExtra(MainActivity.MAIN_NUMBER, 0);


        TextView textView1 = (TextView) findViewById(R.id.Tablenumbers);
        textView1.setText(""+ Tablenumber);
        backbutton = (Button)findViewById(R.id.TablestoMain);
        backbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToActivity1();
            }
        });
    }

    private void moveToActivity1(){

        Intent intent = new Intent(Tables.this, MainActivity.class);
        startActivity(intent);
    }
}
