package com.rodriguez.leavingthenest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ToDoItemHelper extends SQLiteOpenHelper {

    static final String TAG = "HelperTag";

    static final String DATABASE_NAME = "toDoItemsDatabase.db";
    static final int DATABASE_VERSION = 1;

    static final String TODOITEMS_TABLE = "tableVideos";
    static final String ID = "_id";
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String IMPORTANCE = "importance";

    public ToDoItemHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreate = "CREATE TABLE " + TODOITEMS_TABLE +
                "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " + DESCRIPTION + " TEXT, " +
                IMPORTANCE + " INTEGER)";
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void insertToDoItem(ToDoItem toDoItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, toDoItem.getTitle());
        contentValues.put(DESCRIPTION, toDoItem.getDescription());
        contentValues.put(IMPORTANCE, toDoItem.getImportance());
        //Write to Database
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TODOITEMS_TABLE, null, contentValues);
        db.close();
    }

    public List<ToDoItem> getAllToDoItems() {
        List<ToDoItem> toDoItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TODOITEMS_TABLE, new String[]{ID, TITLE, DESCRIPTION, IMPORTANCE},
                null, null, null, null, null);

        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            int importance = cursor.getInt(3);
            ToDoItem toDoItem = new ToDoItem(id, title, description, importance);
            toDoItems.add(toDoItem);
        }
        return toDoItems;
    }

    public ToDoItem getSelectToDoItem(int idArg) {
        //Log.d(TAG, "ID Argument: " + idArg);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TODOITEMS_TABLE, new String[]{ID, TITLE, DESCRIPTION, IMPORTANCE},
                ID + "=?", new String[]{"" + idArg}, null, null, null);
        ToDoItem toDoItem = null;
        if(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            int importance = cursor.getInt(3);
            toDoItem = new ToDoItem(id, title, description, importance);
        }
        //Log.d(TAG, "Title: "+ toDoItem.getTitle() + "ID: " + toDoItem.getId());
        return toDoItem;
    }

    public void updateToDoItemById(ToDoItem toDoItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, toDoItem.getTitle());
        contentValues.put(DESCRIPTION, toDoItem.getDescription());
        contentValues.put(IMPORTANCE, toDoItem.getImportance());
        //Update database
        SQLiteDatabase db = getWritableDatabase();
        db.update(TODOITEMS_TABLE, contentValues, ID + "=?", new String[]{"" + toDoItem.getId()});
        db.close();
    }

    public void deleteToDoItem( int idArg) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TODOITEMS_TABLE, ID + "=?", new String[]{"" + idArg});
        db.close();
    }

    public void deleteAllToDoItems() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TODOITEMS_TABLE, null, null);
        db.close();
    }
}
