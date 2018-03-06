package com.java_todolist.todolist;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailView extends AppCompatActivity {
    private DbHelper dbHelper;
    private ManageNotify mNotify;
    private static final String EXTRA_MESSAGE = "com.java_todolist.todolist.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        dbHelper = new DbHelper(this);
        mNotify = new ManageNotify(this);

        Intent intent = getIntent();
        String taskName = intent.getStringExtra(EXTRA_MESSAGE);

        TextView textView = findViewById(R.id.ViewDetailTitle);
        textView.setText(taskName);
        textView = findViewById(R.id.ViewDetailDesc);
        textView.setText(dbHelper.getDesc(taskName));
        textView = findViewById(R.id.ViewDetailDate);
        textView.setText(dbHelper.getDatetime(taskName));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editdelete, menu);

        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_task:
                Intent intent = new Intent(this, EditView.class);
                TextView taskTextView = findViewById(R.id.ViewDetailTitle);
                String taskName = taskTextView.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, taskName);
                startActivity(intent);
                return true;
            case R.id.action_delete_task:
                deleteTaskMain();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTaskMain() {
        TextView taskTextView = findViewById(R.id.ViewDetailTitle);
        String task = String.valueOf(taskTextView.getText());
        mNotify.StopNotification(dbHelper.getId(task));
        dbHelper.deleteTask(task);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
