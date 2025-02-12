package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.BackendClasses.Receipt;

import java.util.List;



public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context ctx;

    public CustomAdapter(Context ctx) {
        inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }


    /*only creates a new view holder when there are no existing view holders which the RecyclerView can reuse*/
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    //Called to Reuse CardViews
    /*Initially you will get new unused view holders and you have to fill them with data you want to display.
    But as you scroll you'll start getting view holders that were used for rows that went off screen
    and you have to replace old data that they held with new data.*/
    @Override
    public void onBindViewHolder(final CustomAdapter.MyViewHolder holder, int position) {
        holder.tvFood.setText(BBQ_Activity.BBQFoods.get(position).getName());
        holder.tvnumber.setText(String.valueOf(MainActivity.receipt.getItemCount(BBQ_Activity.BBQFoods.get(position))));
        
        //holder.tvnumber.setText(String.valueOf(BBQ_Activity.BBQFoods.get(position)));
        //holder.tvDescription.setText(Menu.BBQDescriptions.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return BBQ_Activity.BBQFoods.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        protected Button btn_plus, btn_minus;
        private TextView tvFood, tvnumber, tvDescription;


        public MyViewHolder(View itemView) {
            super(itemView);

                cv = (CardView) itemView.findViewById(R.id.card_view);

                tvFood = (TextView) itemView.findViewById(R.id.food);
                tvnumber = (TextView) itemView.findViewById(R.id.number);
                tvDescription = (TextView) itemView.findViewById(R.id.TextDescription);
                btn_plus = (Button) itemView.findViewById(R.id.plus);
                btn_minus = (Button) itemView.findViewById(R.id.minus);

                btn_plus.setTag(R.integer.btn_plus_view, itemView);
                btn_minus.setTag(R.integer.btn_minus_view, itemView);
                btn_plus.setOnClickListener(this);
                btn_minus.setOnClickListener(this);

        }

        // onClick Listener for view
        @Override
        public void onClick(View v) {

            if (v.getId() == btn_plus.getId()){

                View tempview = (View) btn_plus.getTag(R.integer.btn_plus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.number);
                int number = Integer.parseInt(tv.getText().toString()) + 1;
                tv.setText(String.valueOf(number));
                //Send over to the Receipt this value
                 //BBQ_Activity.BBQFoods.get(getAdapterPosition()).setnumber());
               // System.out.println(BBQ_Activity.BBQFoods.get(getAdapterPosition()));
                //System.out.println(MainActivity.receipt);
                 MainActivity.receipt.addItem(BBQ_Activity.BBQFoods.get(getAdapterPosition()), 1);

            } else if(v.getId() == btn_minus.getId()) {

                View tempview = (View) btn_minus.getTag(R.integer.btn_minus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.number);
                int number = Integer.parseInt(tv.getText().toString()) - 1;

                //Send over to the receipt this value
                //BBQ_Activity.BBQFoods.get(getAdapterPosition());
                if(number>-1){
                    tv.setText(String.valueOf(number));
                    MainActivity.receipt.removeItem(BBQ_Activity.BBQFoods.get(getAdapterPosition()), 1);
                }

            }
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}