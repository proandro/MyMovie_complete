package com.example.lucky.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ToDoListDBAdapter {
    public static final String DB_NAME = "movie.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_TODO = "favourite";
    public static final String COLUMN_TODO_ID = "task_id";
    public static final String COLUMN_Title = "todo";
    public static final String COLUMN_Releasedate = "releasedate";
    public static final String COLUMN_imageurl = "imageurl";
    public static final String COLUMN_backimageurl = "backimagerurl";
    public static final String COLUMN_desc = "description";
    public static final String COLUMN_rating = "rating";
    private static final String TAG = ToDoListDBAdapter.class.getSimpleName();
    public static String CREATE_TABLE_TODO = "CREATE TABLE " + TABLE_TODO + "(" + COLUMN_TODO_ID + " INTEGER PRIMARY KEY, " + COLUMN_Title + " TEXT NOT NULL, " +
            COLUMN_Releasedate + " TEXT, " + COLUMN_imageurl + " TEXT," + COLUMN_backimageurl + " TEXT," + COLUMN_desc + " TEXT," + COLUMN_rating + " TEXT )";
    private static ToDoListDBAdapter toDoListDBAdapterInstance;
    private Context context;
    private SQLiteDatabase sqLliteDatabase;


    ToDoListDBAdapter(Context context) {
        this.context = context;
        sqLliteDatabase = new ToDoListDBHelper(this.context, DB_NAME, null, DB_VERSION).getWritableDatabase();
    }

    public static ToDoListDBAdapter getToDoListDBAdapterInstance(Context context) {
        if (toDoListDBAdapterInstance == null) {
            toDoListDBAdapterInstance = new ToDoListDBAdapter(context);
        }
        return toDoListDBAdapterInstance;
    }

    public Cursor getCursorsForAllToDos() {
        Cursor cursor = sqLliteDatabase.query(TABLE_TODO, new String[]{COLUMN_TODO_ID, COLUMN_Title, COLUMN_Releasedate, COLUMN_imageurl, COLUMN_backimageurl, COLUMN_desc, COLUMN_rating}, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor getCursorForSpecificPlace(String place) {

        Cursor cursor = sqLliteDatabase.query(TABLE_TODO, new String[]{COLUMN_TODO_ID, COLUMN_Title, COLUMN_Releasedate, COLUMN_imageurl, COLUMN_backimageurl, COLUMN_desc, COLUMN_rating}, COLUMN_Title + " LIKE '%" + place + "%'", null, null, null, null, null);
        return cursor;
    }

    public Cursor getCount() {
        Cursor cursor = sqLliteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_TODO, null);
        return cursor;
    }

    public boolean insert(String toDoItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_Title, toDoItem);
        return sqLliteDatabase.insert(TABLE_TODO, null, contentValues) > 0;
    }

    public boolean insert(String id, String title, String releasedate, String imageurl, String backimgage, String desc, String rating) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TODO_ID, id);
        contentValues.put(COLUMN_Title, title);
        contentValues.put(COLUMN_Releasedate, releasedate);
        contentValues.put(COLUMN_imageurl, imageurl);
        contentValues.put(COLUMN_backimageurl, backimgage);
        contentValues.put(COLUMN_desc, desc);
        contentValues.put(COLUMN_rating, rating);
        return sqLliteDatabase.insert(TABLE_TODO, null, contentValues) > 0;
    }


    public long insert(ContentValues contentValues) {
        return sqLliteDatabase.insert(TABLE_TODO, null, contentValues);
    }

    public boolean delete(int taskId) {
        return sqLliteDatabase.delete(TABLE_TODO, COLUMN_TODO_ID + " = " + taskId, null) > 0;
    }

    public int delete(String whereClause, String[] whereValues) {
        return sqLliteDatabase.delete(TABLE_TODO, whereClause, whereValues);
    }

    public boolean modify(int taskId, String newToDoItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_Title, newToDoItem);
        return sqLliteDatabase.update(TABLE_TODO, contentValues, COLUMN_TODO_ID + " = " + taskId, null) > 0;
    }

    public int update(ContentValues contentValues, String s, String[] strings) {
        return sqLliteDatabase.update(TABLE_TODO, contentValues, s, strings);
    }

    public List<ListItem> getAllToDos() {
        List<ListItem> toDoList = new ArrayList<ListItem>();
        Cursor cursor = sqLliteDatabase.query(TABLE_TODO, new String[]{COLUMN_TODO_ID, COLUMN_Title, COLUMN_Releasedate, COLUMN_imageurl, COLUMN_backimageurl, COLUMN_desc, COLUMN_rating}, null, null, null, null, null, null);

        if (cursor != null & cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ListItem toDo = new ListItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                toDoList.add(toDo);

            }
        }
        cursor.close();
        return toDoList;
    }

    private static class ToDoListDBHelper extends SQLiteOpenHelper {

        public ToDoListDBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int dbVersion) {
            super(context, databaseName, factory, dbVersion);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            Log.i(TAG, "Inside onConfigure");
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_TODO);
            Log.i(TAG, "Inside onCreate");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                              int oldVersion, int newVersion) {
            switch (oldVersion) {
                case 1:
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_TODO + " ADD COLUMN " + COLUMN_Title + " TEXT");
                    break;
                default:
                    break;
            }
            Log.i(TAG, "Inside onUpgrade");
        }
    }

}
