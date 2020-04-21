package waiter.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuTile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuTile extends Fragment {
    private Item item;
    private int quant;
    TextView itemName;
    TextView quantity;
    Button plusButton;
    Button minusButton;

    public MenuTile() {
        // Required empty public constructor
    }

    public MenuTile(Item item){
        this.item = item;
        this.quant = 0;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuTile.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuTile newInstance(Item item) {

        MenuTile fragment = new MenuTile(item);
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateQuantity(){

        quantity.setText("Quantity: "+quant);
        System.out.println(MenuList.getReceipt());
    }



    @Override
    public void onStart() {
        super.onStart();
        itemName = getView().findViewById(R.id.itemName);
        quantity = getView().findViewById(R.id.quantity);
        plusButton = getView().findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quant++;
                MenuList.getReceipt().addItem(item, 1);
                updateQuantity();
            }
        });
        minusButton = getView().findViewById(R.id.minusButton);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quant--;
                if(quant == -1)
                    quant++;
                MenuList.getReceipt().removeItem(item, 1);
                updateQuantity();
            }
        });

        itemName.setText(item.getName());
        updateQuantity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_tile, container, false);
    }
}
