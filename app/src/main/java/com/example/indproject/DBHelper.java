package com.example.indproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USER = "users";
    private static final String TABLE_TASKS = "tasks";

    // Common Column Names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";

    // User Table - column names
    private static final String COLUMN_PASSWORD = "password";

    // Tasks Table - column names
    private static final String COLUMN_TASK = "task";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_TASK + " TEXT" + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Method to add a new user
    public boolean addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USER, null, values);
        db.close();

        return result != -1; // Return true if insertion is successful, otherwise false
    }

    // Method to check user credentials
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0; // Return true if user exists, otherwise false
    }

    // Method to add a new task
    public boolean insertTask(String email, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_TASK, task);

        long result = db.insert(TABLE_TASKS, null, contentValues);
        db.close();

        return result != -1; // Return true if insertion is successful, otherwise false
    }

    // Method to retrieve tasks for a specific user
    public Cursor getTasks(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
    }

    // Method to delete a specific task
    public boolean deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        return result != -1; // Return true if deletion is successful, otherwise false
    }
}
