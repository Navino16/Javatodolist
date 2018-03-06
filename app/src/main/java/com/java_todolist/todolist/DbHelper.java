package com.java_todolist.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by jaunet_n on 29/01/2018.
 * Database helper file
 */

class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Todolist";
    private static final int DB_VER = 1;
    private static final String DB_TABLE = "Task";
    private static final String DB_COLUMN1 = "Name";
    private static final String DB_COLUMN2 = "Desc";
    private static final String DB_COLUMN3 = "Date";
    private static final String DB_COLUMN4 = "Done";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (_ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER DEFAULT 0);", DB_TABLE, DB_COLUMN1, DB_COLUMN2, DB_COLUMN3, DB_COLUMN4);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = String.format("DELETE TABLE IF EXIST %s", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewTask(String taskName, String taskDesc, String taskDate) {
        if (taskName == null || taskDesc == null || taskDate == null)
            return;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN1, taskName);
        values.put(DB_COLUMN2, taskDesc);
        values.put(DB_COLUMN3, taskDate);
        values.put(DB_COLUMN4, 0);
        db.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteTask(String task) {
        if (task == null)
            return;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN1 + " = ?", new String[]{task});
        db.close();
    }

    public void update(String oldTaskName, String taskName, String desc, String date) {
        if (oldTaskName == null || taskName == null || desc == null || date == null)
            return;
        int id = getId(oldTaskName);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN1, taskName);
        values.put(DB_COLUMN2, desc);
        values.put(DB_COLUMN3, date);
        db.update(DB_TABLE, values, "_ID=" + id, null);
        db.close();
    }

    public void changeDone(String taskName, boolean done) {
        int id = getId(taskName);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (done)
            values.put(DB_COLUMN4, 1);
        else
            values.put(DB_COLUMN4, 0);
        db.update(DB_TABLE, values, "_ID=" + id, null);
        db.close();
    }

    public boolean dontExist(String taskName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE %s = '%s'", DB_COLUMN1, DB_TABLE, DB_COLUMN1, taskName);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public ArrayList<String> getTaskList() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN1}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DB_COLUMN1);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public int getId(String taskName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT _ID FROM %s WHERE %s = '%s'", DB_TABLE, DB_COLUMN1, taskName);
        Cursor cursor = db.rawQuery(query, null);
        int id = -1;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String tmp = cursor.getString(cursor.getColumnIndex("_ID"));
            id = Integer.valueOf(tmp);
        }
        cursor.close();
        db.close();
        return id;
    }

    public String getDesc(String taskName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE %s = '%s'", DB_COLUMN2, DB_TABLE, DB_COLUMN1, taskName);
        Cursor cursor = db.rawQuery(query, null);
        String desc = "";
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            desc = cursor.getString(cursor.getColumnIndex(DB_COLUMN2));
        }
        cursor.close();
        db.close();
        return desc;
    }

    public String getDatetime(String taskName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE %s = '%s'", DB_COLUMN3, DB_TABLE, DB_COLUMN1, taskName);
        Cursor cursor = db.rawQuery(query, null);
        String date = "";
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            date = cursor.getString(cursor.getColumnIndex(DB_COLUMN3));
        }
        cursor.close();
        db.close();
        return date;
    }

    public Boolean getDone(String taskName) {
        if (dontExist(taskName))
            return false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE %s = '%s'", DB_COLUMN4, DB_TABLE, DB_COLUMN1, taskName);
        Cursor cursor = db.rawQuery(query, null);
        Boolean done = false;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
             String tmp = cursor.getString(cursor.getColumnIndex(DB_COLUMN4));
             int v = Integer.valueOf(tmp);
             if (v != 0)
                 done = true;
        }
        cursor.close();
        db.close();
        return done;
    }
}
