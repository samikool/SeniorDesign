package waiter.myapplication.DisplayMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import waiter.myapplication.BackendClasses.Item;
import waiter.myapplication.BackendClasses.Linker;
import waiter.myapplication.R;
import waiter.myapplication.BackendClasses.Receipt;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuList extends Fragment {
    private ArrayList<Item> itemList;
    private static Receipt receipt = new Receipt(Order.getTablenumber(), Linker.getId());;
    private LinearLayout layout;
    private ArrayList<MenuTile> frags;
    private boolean created;
    private boolean destroyed;

    public MenuList() {
        // Required empty public constructor
//        layout = getView().findViewById(R.id.drinkLayout);
//        System.out.println("helo");
    }

    public void initializeList(ArrayList<Item> itemList){
        this.itemList = itemList;
        this.frags = new ArrayList<MenuTile>();

    }

    public static Receipt getReceipt(){
        return receipt;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DrinkList.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuList newInstance(ArrayList<Item> list) {
        MenuList fragment = new MenuList();
        fragment.initializeList(list);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        layout = getView().findViewById(R.id.menuLayout);
        if(!created){
            for(int i = 1; i< itemList.size(); i++){
                getChildFragmentManager().beginTransaction().add(layout.getId(), MenuTile.newInstance(itemList.get(i)), "item"+i).commit();
            }
        }
        created = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        receipt.setTid(Order.getTablenumber());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(!destroyed){
            for(Fragment tile : getChildFragmentManager().getFragments()){
                frags.add((MenuTile) tile);
            }
        }
        destroyed = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_list, container, false);
    }
}
