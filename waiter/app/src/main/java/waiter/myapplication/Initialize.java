package waiter.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Initialize extends AppCompatActivity {
    public static final String TABLE_NEED = "waiter.myapplication.TABLE_NEED";
    public static final String TABLE_NUMBER = "waiter.myapplication.TABLE_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);


    }
}
