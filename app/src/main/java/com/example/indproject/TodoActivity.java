package com.example.indproject;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class TodoActivity extends AppCompatActivity {

    EditText editTask;
    Button btnAdd;
    ListView listViewTasks;
    DBHelper DB;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        editTask = findViewById(R.id.editTask);
        btnAdd = findViewById(R.id.buttonAdd);
        listViewTasks = findViewById(R.id.listViewTasks);
        DB = new DBHelper(this);

        email = getIntent().getStringExtra("email");
        loadTasks(email);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = editTask.getText().toString();
                if (!task.isEmpty()) {
                    Boolean insertTask = DB.insertTask(email, task);
                    if (insertTask) {
                        Toast.makeText(TodoActivity.this, "Task Added Successfully", Toast.LENGTH_SHORT).show();
                        loadTasks(email);
                        editTask.setText("");
                    } else {
                        Toast.makeText(TodoActivity.this, "Failed to Add Task", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TodoActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            String task = (String) parent.getItemAtPosition(position);
            Toast.makeText(TodoActivity.this, "Task: " + task, Toast.LENGTH_SHORT).show();
        });

        listViewTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            int taskId = (int) id;
            Boolean deleteTask = DB.deleteTask(taskId);
            if (deleteTask) {
                Toast.makeText(TodoActivity.this, "Task Deleted Successfully", Toast.LENGTH_SHORT).show();
                loadTasks(email);
            } else {
                Toast.makeText(TodoActivity.this, "Failed to Delete Task", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void loadTasks(String email) {
        ArrayList<String> tasksList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasksList);
        listViewTasks.setAdapter(adapter);

        Cursor cursor = DB.getTasks(email);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No tasks found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String task = cursor.getString(2);
                tasksList.add(task);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
