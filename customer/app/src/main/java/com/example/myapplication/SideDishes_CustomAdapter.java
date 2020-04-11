package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class SideDishes_CustomAdapter extends RecyclerView.Adapter<SideDishes_CustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context ctx;

    public SideDishes_CustomAdapter(Context ctx) {

        inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }


    /*only creates a new view holder when there are no existing view holders which the RecyclerView can reuse*/
    @Override
    public SideDishes_CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    //Called to Reuse CardViews
    /*Initially you will get new unused view holders and you have to fill them with data you want to display.
    But as you scroll you'll start getting view holders that were used for rows that went off screen
    and you have to replace old data that they held with new data.*/
    @Override
    public void onBindViewHolder(final SideDishes_CustomAdapter.MyViewHolder holder, int position) {

        holder.tvFood.setText(SideDishes_Activity.modelArrayList.get(position).getFood());
        holder.tvnumber.setText(String.valueOf(SideDishes_Activity.modelArrayList.get(position).getNumber()));
        holder.tvDescription.setText(SideDishes_Activity.modelDescriptions.get(position).getDescription());
    }

    @Override
    public int getItemCount() {

        return SideDishes_Activity.modelArrayList.size();
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
                SideDishes_Activity.modelArrayList.get(getAdapterPosition()).setNumber(number);

            } else if(v.getId() == btn_minus.getId()) {

                View tempview = (View) btn_minus.getTag(R.integer.btn_minus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.number);
                int number = Integer.parseInt(tv.getText().toString()) - 1;
                tv.setText(String.valueOf(number));
                SideDishes_Activity.modelArrayList.get(getAdapterPosition()).setNumber(number);
            }
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
