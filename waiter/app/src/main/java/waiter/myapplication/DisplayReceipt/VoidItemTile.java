package waiter.myapplication.DisplayReceipt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import waiter.myapplication.BackendClasses.Item;
import waiter.myapplication.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoidItemTile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoidItemTile extends Fragment {
    private Item item;
    private int quant;
    private int startQuant;
    TextView itemName;
    TextView quantity;
    Button plusButton;
    Button minusButton;

    public VoidItemTile() {
        // Required empty public constructor
    }

    public void setItem(Item item, int quant){
        this.item = item;
        this.quant = quant;
        this.startQuant = quant;
    }

    public int getQuantity(){return quant;}

    public int getStartQuant(){return startQuant;}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment VoidItemTile.
     */
    // TODO: Rename and change types and number of parameters
    public static VoidItemTile newInstance(Item item, int quant) {
        VoidItemTile fragment = new VoidItemTile();
        fragment.setItem(item, quant);

        return fragment;
    }

    public void updateQuantity(){

        quantity.setText("Quantity: "+quant);
        System.out.println(DisplayReceipt.getReceipt());
    }

    @Override
    public void onStart() {
        super.onStart();
        itemName = getView().findViewById(R.id.itemName);
        quantity = getView().findViewById(R.id.quantityLabel);
        plusButton = getView().findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quant++;
                if(quant == startQuant+1)
                    quant--;
                else{
                    DisplayReceipt.getReceipt().addItem(item, 1);
                    updateQuantity();
                }
            }
        });
        minusButton = getView().findViewById(R.id.minusButton);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quant--;
                if(quant == -1)
                    quant++;
                else{
                    DisplayReceipt.getReceipt().removeItem(item, 1);
                    updateQuantity();
                }
            }
        });

        itemName.setText(item.getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateQuantity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_void_item_tile, container, false);
    }
}
