package com.java_todolist.todolist;

import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Created by jaunet_n on 31/01/2018.
 * Test Bdd
 */

public class DbHelperTest {
    private DbHelper dbHelper;

    @Before
    public void setUp() {
        dbHelper = new DbHelper(InstrumentationRegistry.getTargetContext());
        cleanDb();
    }

    private void cleanDb() {
        while (!dbHelper.getTaskList().isEmpty()) {
            dbHelper.deleteTask(dbHelper.getTaskList().get(0));
        }
    }

    @Test
    public void testPreConditions() {
        assertNotNull(dbHelper);
    }

    @Test
    public void insertNewTaskTest1() {
        dbHelper.insertNewTask("Test", "Desc", "Date");
        assertNotNull(dbHelper.getDesc("Test"));
        assertNotNull(dbHelper.getDatetime("Test"));
        cleanDb();
    }

    @Test
    public void insertNewTaskTest2() throws SQLiteConstraintException {
        dbHelper.insertNewTask(null, null, null);
        cleanDb();
    }

    @Test
    public void DeleteTaskTest1() {
        dbHelper.insertNewTask("Test", "Desc", "Date");
        dbHelper.deleteTask("Test");
        assertSame(dbHelper.getDesc("Test"), "");
        cleanDb();
    }

    @Test
    public void DeleteTaskTest2() {
        dbHelper.insertNewTask("Test", "Desc", "Date");
        dbHelper.deleteTask(null);
        assertSame(dbHelper.getDesc(""), "");
        cleanDb();
    }

    @Test
    public void updateTest1(){
        String oldName = "Test";
        String newName = "Update";
        String oldDesc = "Desc";
        String newDesc = "DescUpdate";
        String oldDate = "01/01/2000 00:00";
        String newDate = "31/01/2018 21:47";

        dbHelper.insertNewTask(oldName, oldDesc, oldDate);
        dbHelper.update(oldName, newName, newDesc, newDate);

        assertEquals(true, dbHelper.dontExist(oldName));
        assertEquals(false, dbHelper.dontExist(newName));

        assertNotEquals(oldDesc, dbHelper.getDesc(newName));
        assertEquals(newDesc, dbHelper.getDesc(newName));

        assertNotEquals(oldDate, dbHelper.getDatetime(newName));
        assertEquals(newDate, dbHelper.getDatetime(newName));
        cleanDb();
    }

    @Test
    public void updateTest2(){
        String oldName = "Test";
        String oldDesc = "Desc";
        String oldDate = "01/01/2000 00:00";

        dbHelper.insertNewTask(oldName, oldDesc, oldDate);
        dbHelper.update(oldName, null, null, null);

        assertEquals(false, dbHelper.dontExist(oldName));
        assertEquals(oldDesc, dbHelper.getDesc(oldName));
        assertEquals(oldDate, dbHelper.getDatetime(oldName));
        cleanDb();
    }

    @Test
    public void getTaskListTest() {
        dbHelper.insertNewTask("Test", "Desc", "Date");
        assertNotNull(dbHelper.getTaskList());
        cleanDb();
    }

    @Test
    public void getDescTest() {
        dbHelper.insertNewTask("Test", "Desc", "Date");
        assertNotNull(dbHelper.getDesc("Test"));
        cleanDb();
    }
}
