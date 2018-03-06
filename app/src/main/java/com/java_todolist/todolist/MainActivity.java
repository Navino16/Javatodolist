package com.java_todolist.todolist;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.java_todolist.todolist.MESSAGE";
    private DbHelper dbHelper;
    private taskAdapter mAdapter;
    private ListView listTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        listTask = findViewById(R.id.listTask);
        loadTaskList();
    }

    private void loadTaskList() {
        ArrayList<String> taskList = dbHelper.getTaskList();
        if (mAdapter == null) {
            mAdapter = new taskAdapter(this, R.id.task_Title, taskList);
            listTask.setAdapter(mAdapter);
        }
        else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent intent = new Intent(this, NewTask.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDetail(View view) {
        View parent = (View)view.getParent();
        Intent intent = new Intent(this, DetailView.class);
        TextView taskTextView = parent.findViewById(R.id.task_Title);
        String taskName = taskTextView.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, taskName);
        startActivity(intent);
    }
}