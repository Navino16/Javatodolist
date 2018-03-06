package com.java_todolist.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jaunet_n on 30/01/2018.
 * Custom adapter
 */

class taskAdapter extends ArrayAdapter<String> {
    private final DbHelper dbHelper;
    private final ManageNotify mNotify;
    private final Locale locale;

    public taskAdapter(Context context, @SuppressWarnings("SameParameterValue") int resource, List<String> items) {
        super(context, resource, items);
        dbHelper = new DbHelper(context);
        mNotify = new ManageNotify(context);
        locale = context.getResources().getConfiguration().locale;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull final ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row, parent, false);
        }

        final String taskName = getItem(position);

        if (taskName != null) {
            final TextView tt1 = v.findViewById(R.id.task_Title);
            final CheckBox btn = v.findViewById(R.id.TaskDone);

            if (btn != null && tt1 != null) {
                tt1.setText(taskName);
                btn.setChecked(dbHelper.getDone(taskName));
                final int id = dbHelper.getId(taskName);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dbHelper.getDone(taskName)) {
                            btn.setChecked(false);
                            dbHelper.changeDone(taskName, false);
                            String datetime = dbHelper.getDatetime(taskName);
                            try {
                                Date when = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale).parse(datetime);
                                mNotify.addNotification(when.getTime(), taskName, parent.getResources().getString(R.string.EndDueDateTask), id);
                            } catch (ParseException e) {
                                Context context = parent.getContext();
                                String text = parent.getResources().getString(R.string.ErrorAddNotification);
                                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                        else {
                            btn.setChecked(true);
                            dbHelper.changeDone(taskName, true);
                            mNotify.StopNotification(id);
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        }

        return v;
    }
}
