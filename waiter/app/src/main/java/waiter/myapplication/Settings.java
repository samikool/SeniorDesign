package waiter.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    private Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backbutton = (Button)findViewById(R.id.Settings2Menu);

        backbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                moveToActivity2();
            }
        });
    }

    private void moveToActivity2(){

        Intent intent = new Intent(Settings.this, AppMenu.class);
        startActivity(intent);
    }
}
