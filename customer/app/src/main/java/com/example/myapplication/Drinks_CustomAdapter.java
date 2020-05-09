package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class Drinks_CustomAdapter extends RecyclerView.Adapter<Drinks_CustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context ctx;

    public Drinks_CustomAdapter(Context ctx) {

        inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }


    /*only creates a new view holder when there are no existing view holders which the RecyclerView can reuse*/
    @Override
    public Drinks_CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    //Called to Reuse CardViews
    /*Initially you will get new unused view holders and you have to fill them with data you want to display.
    But as you scroll you'll start getting view holders that were used for rows that went off screen
    and you have to replace old data that they held with new data.*/
    @Override
    public void onBindViewHolder(final Drinks_CustomAdapter.MyViewHolder holder, int position) {

        holder.tvFood.setText(Drinks_Activity.DrinkList.get(position).getName());
        holder.tvnumber.setText(String.valueOf(MainActivity.receipt.getItemCount(Drinks_Activity.DrinkList.get(position))));
      //  holder.tvDescription.setText(Menu.DrinkDescriptions.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return Drinks_Activity.DrinkList.size();
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
                //Menu.Drinks.get(getAdapterPosition()).setNumber(number);
                MainActivity.receipt.addItem(Drinks_Activity.DrinkList.get(getAdapterPosition()), 1);


            } else if(v.getId() == btn_minus.getId()) {

                View tempview = (View) btn_minus.getTag(R.integer.btn_minus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.number);
                int number = Integer.parseInt(tv.getText().toString()) - 1;

                //Menu.Drinks.get(getAdapterPosition()).setNumber(number);
                if(number>-1){
                    tv.setText(String.valueOf(number));
                    MainActivity.receipt.removeItem(Drinks_Activity.DrinkList.get(getAdapterPosition()), 1);
                }


            }
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}