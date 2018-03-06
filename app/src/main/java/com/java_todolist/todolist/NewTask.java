package com.java_todolist.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTask extends AppCompatActivity {
    private DbHelper dbHelper;
    private ManageNotify mNotify;
    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        locale = getResources().getConfiguration().locale;

        dbHelper = new DbHelper(this);
        mNotify = new ManageNotify(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.valide, menu);

        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                EditText title = findViewById(R.id.Title);
                if (dbHelper.dontExist(title.getText().toString())) {
                    EditText desc = findViewById(R.id.Description);
                    TextView date = findViewById(R.id.Date);
                    TextView time = findViewById(R.id.Time);
                    if (title.getText().toString().length() > 0
                            && desc.getText().toString().length() > 0
                            && date.getText().toString().length() > 0
                            && time.getText().toString().length() > 0) {
                        String datetime = String.format("%s %s", date.getText(), time.getText());
                        dbHelper.insertNewTask(title.getText().toString(), desc.getText().toString(), datetime);
                        try {
                            Date when = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale).parse(datetime);
                            mNotify.addNotification(when.getTime(), title.getText().toString(), getResources().getString(R.string.EndDueDateTask), dbHelper.getId(title.getText().toString()));
                        } catch (ParseException e) {
                            Context context = getApplicationContext();
                            String text = getResources().getString(R.string.ErrorAddNotification);
                            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                            toast.show();
                        }
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Context context = getApplicationContext();
                        String text = getResources().getString(R.string.FieldEmpty);
                        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else {
                    Context context = getApplicationContext();
                    String text = getResources().getString(R.string.AlreadyExist);
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                    toast.show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickDate(View view) {
        final View parent = (View)view.getParent();
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        TextView txtDate = parent.findViewById(R.id.Date);
                        txtDate.setText(String.format(locale, "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 1000);
        datePickerDialog.show();
    }

    public void onClickTime(View view) {
        final View parent = (View)view.getParent();
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        TextView txtTime = parent.findViewById(R.id.Time);
                        txtTime.setText(String.format(locale,"%02d:%02d",hourOfDay, minute));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
}
