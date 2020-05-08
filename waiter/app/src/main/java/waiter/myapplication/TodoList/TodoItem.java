package waiter.myapplication.TodoList;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import waiter.myapplication.BackendClasses.Linker;
import waiter.myapplication.MainActivity;
import waiter.myapplication.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoItem extends Fragment {
    private String request;
    private String tableNumber;
    private String requestDescription;
    private String timeString;
    private Long requestTime;
    private TextView tableNumberView;
    private TextView requestView;
    private TextView timeView;
    private ConstraintLayout layout;
    private boolean done;

    public TodoItem() {
        // Required empty public constructor
    }

    public void init(String request, Long requestTime) {
        this.request = request;
        this.requestTime = requestTime;
        String[] parts = request.split(",");
        this.tableNumber = parts[0];
        String command = parts[1];

        switch (command){
            case "call":
                requestDescription = "Check on table "+tableNumber;
                break;
            case "check":
                requestDescription="Give check to table "+tableNumber;
                break;
            case "chop":
                int quant = Integer.parseInt(parts[3]);
                if(quant > 1)
                    parts[2] += "s";
                requestDescription="Give "+parts[3]+" "+parts[2];
                break;
            case  "order":
                requestDescription="Table "+tableNumber+" ordered food";
        }
        timeString = "calculating..";
        done = false;
    }

    public void updateViewColor(int hour, int minutes){
        if(minutes > 28 || hour > 1)
            this.getView().setBackgroundColor(getView().getContext().getColor(R.color.red));
        else if(minutes < 3)
            this.getView().setBackgroundColor(getView().getContext().getColor(R.color.white));
        else if(minutes < 8)
            this.getView().setBackgroundColor(getView().getContext().getColor(R.color.lightGreen));
        else if(minutes < 13)
            this.getView().setBackgroundColor(getView().getContext().getColor(R.color.yellow));
        else if(minutes < 18)
            this.getView().setBackgroundColor(getView().getContext().getColor(R.color.lightOrange));
        else if(minutes < 23)
            this.getView().setBackgroundColor(getView().getContext().getColor(R.color.orange));
        else if(minutes <= 28)
            this.getView().setBackgroundColor(getView().getContext().getColor(R.color.darkOrange));
    }

    public static TodoItem newInstance(String request, Long time) {
        TodoItem fragment = new TodoItem();
        fragment.init(request,time);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        tableNumberView = getView().findViewById(R.id.tableNumberView);
        requestView = getView().findViewById(R.id.requestView);
        timeView = getView().findViewById(R.id.timeView);
        layout = getView().findViewById(R.id.backgroundLayout);


        tableNumberView.setText(tableNumber);
        requestView.setText(requestDescription);
        timeView.setText(timeString);


        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().remove(TodoItem.this).commitNow();
                Linker.getTodoList().remove(request);
                Linker.getTodoTimes().remove(request);
                done = true;

                for(String request : Linker.getTodoList()){
                    if(request.startsWith(tableNumber)){
                        return;
                    }
                }
                Linker.checkedTable(Integer.parseInt(tableNumber));
            }
        });

        new Thread(){
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                super.run();
                try{
                    while(!done) {
                        //update time
                        Long currentTime = Calendar.getInstance().getTime().getTime();
                        double difference = currentTime - requestTime;

                        difference /= 1000.;
                        String tempTimeString = "";
                        int seconds = (int) difference % 60;
                        final int minutes = (int) difference / 60 % 60;
                        final int hours = (int) difference / 60 / 60;
                        if(hours > 0)
                            tempTimeString += String.format("%02dh ", hours);
                        if(minutes > 0)
                            tempTimeString += String.format("%02dm ", minutes);
                        tempTimeString += String.format("%02ds ", seconds);
                        timeString = tempTimeString;
                        timeView.setText(timeString);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateViewColor(hours, minutes);
                            }
                        });

                        Thread.sleep(1000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }.start();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_item, container, false);
    }
}
