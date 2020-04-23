package waiter.myapplication.DisplayReceipt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import waiter.myapplication.BackendClasses.Item;
import waiter.myapplication.BackendClasses.Linker;
import waiter.myapplication.R;
import waiter.myapplication.BackendClasses.Receipt;
import waiter.myapplication.Tables;

public class DisplayReceipt extends AppCompatActivity {

    private LinearLayout itemContainer;
    private static Receipt receipt;
    private int Tablenumber;
    private static boolean voiding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_receipt);
        itemContainer = findViewById(R.id.itemContainer);
        voiding = getIntent().getBooleanExtra("void", false);
    }

    public static boolean isVoiding(){return voiding;}

    @Override
    protected void onResume() {
        super.onResume();
        Tablenumber = Tables.getTablenumber();
        receipt = Linker.getTableReceipt(Tablenumber);
        int i=0;
        for(Item item : receipt.getItems()){
            int quant = receipt.getItemCount(item);
            getSupportFragmentManager().beginTransaction().add(itemContainer.getId(), ItemTile.newInstance(item, quant), "item"+i++).commit();
        }
    }

    public static Receipt getReceipt() {
        return receipt;
    }
}