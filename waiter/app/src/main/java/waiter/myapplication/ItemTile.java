package waiter.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemTile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemTile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Item item;
    private int quantity;
    private double total;
    private TextView nameView;
    private TextView quantityView;
    private TextView priceView;
    private ConstraintLayout layout;

    private static DecimalFormat priceFormat = new DecimalFormat("$0.00");

    public ItemTile() {
        // Required empty public constructor
    }

    public void setItem(Item item, int quantity){
        this.item = item;
        this.quantity = quantity;
        this.total = item.getPrice() * quantity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ItemTile.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemTile newInstance(Item item, int quantity) {
        ItemTile fragment = new ItemTile();
        fragment.setItem(item, quantity);
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onStart() {
        super.onStart();
        nameView = getView().findViewById(R.id.nameView);
        quantityView = getView().findViewById(R.id.quantityView);
        priceView = getView().findViewById(R.id.priceView);
        layout = getView().findViewById(R.id.itemBackground);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                System.out.println("Hello");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Void " + item.getName() + "?");

                builder.setMessage("Select the correct amount of the item then click done.");

                final VoidItemTile frag = VoidItemTile.newInstance(item, quantity);
                getChildFragmentManager().beginTransaction().add(frag, "frag1").commitNow();

                try{
                    Thread.sleep(250);
                }catch (Exception e){
                    e.printStackTrace();
                }

                builder.setView(frag.getView());

                builder.setPositiveButton(R.string.void_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int quantity = frag.getQuantity();
                        int currentQuantity = frag.getStartQuant();
                        total = item.getPrice() * quantity;

                        quantityView.setText(Integer.toString(quantity));
                        priceView.setText(priceFormat.format(total));

                        Linker.voidItem(item, currentQuantity-quantity, Tables.getTablenumber());
                    }
                });

                builder.setNegativeButton(R.string.void_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        quantity = frag.getQuantity();
                        int correctQuantity = frag.getStartQuant();
                        DisplayReceipt.getReceipt().addItem(item, correctQuantity-quantity);

                        quantity = correctQuantity;
                        System.out.println(DisplayReceipt.getReceipt());

                        //user clicked no
                    }
                });



                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        nameView.setText(item.getName());
        quantityView.setText(Integer.toString(quantity));
        priceView.setText(priceFormat.format(total));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_tile, container, false);
    }
}
