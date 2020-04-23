package waiter.myapplication.TodoList;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import waiter.myapplication.BackendClasses.Linker;
import waiter.myapplication.DisplayReceipt.ItemTile;
import waiter.myapplication.R;

public class TodoListActivity extends AppCompatActivity {
    private LinearLayout todoItemContainer;
    private static ArrayList<String>  todoList;
    private static HashMap<String, Long> todoTimes;
    private static ArrayList<TodoItem> todoItemTiles;
    private static boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        todoList = Linker.getTodoList();
        todoTimes = Linker.getTodoTimes();
        todoItemContainer = findViewById(R.id.todoItemContainer);
        todoItemTiles = new ArrayList<TodoItem>();
    }

    public static ArrayList<TodoItem> getTodoItemTiles(){return todoItemTiles;}

    public  void addTodo(String request){
        TodoItem todoItemTile = TodoItem.newInstance(request, todoTimes.get(request));
        todoItemTiles.add(todoItemTile);
        getSupportFragmentManager().beginTransaction().add(todoItemContainer.getId(), todoItemTile, "request"+todoList.size()).commit();
    }

    public static boolean isActive(){return active;}

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Linker.setTodoListActivity(this);
        int i=0;
        for(String request : todoList){
            TodoItem todoItemTile = TodoItem.newInstance(request, todoTimes.get(request));
            todoItemTiles.add(todoItemTile);
            getSupportFragmentManager().beginTransaction().add(todoItemContainer.getId(), todoItemTile, "request"+i).commit();
            i++;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        todoItemTiles.clear();
    }
}
