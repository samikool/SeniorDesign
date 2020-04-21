package waiter.myapplication.DisplayMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import waiter.myapplication.BackendClasses.Linker;
import waiter.myapplication.R;
import waiter.myapplication.BackendClasses.Receipt;
import waiter.myapplication.Tables;
import waiter.myapplication.ui.main.SectionsPagerAdapter;

public class Order extends AppCompatActivity {
    public static int tableNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        tableNumber = getIntent().getIntExtra("Tablenumber", -1);
        System.out.println(tableNumber);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Receipt receipt = MenuList.getReceipt();
                Linker.orderItems(receipt);
                receipt.removeAllItems();

                Intent intent = new Intent(Order.this, Tables.class);
                intent.putExtra("ordered", true);
                intent.putExtra("Tablenumber", tableNumber);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tableNumber = Tables.getTablenumber();
        System.out.println();;
    }

    public static int getTablenumber(){
        return tableNumber;
    }
}