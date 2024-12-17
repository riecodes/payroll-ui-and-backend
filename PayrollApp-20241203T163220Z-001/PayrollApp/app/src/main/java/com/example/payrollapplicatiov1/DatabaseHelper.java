package com.example.payrollapplicatiov1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserManager.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_USER = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHOTO = "photo";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FULL_NAME + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_PHOTO + " TEXT,"
                + "working_hours INTEGER DEFAULT 0," // New column for working hours
                + "salary REAL DEFAULT 0.0" + ")";   // New column for salary
        db.execSQL(CREATE_USER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USER + " ADD COLUMN working_hours INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_USER + " ADD COLUMN salary REAL DEFAULT 0.0");
        }
    }


    public long addUser(String fullName, String username, String password, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHOTO, photo);
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result;
    }

    public boolean checkUser(String username, String password) {
        String[] columns = {COLUMN_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    public void updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("working_hours", employee.getWorkingHours());
        values.put("salary", employee.getSalary());

        db.update(TABLE_USER, values, COLUMN_USERNAME + " = ?", new String[]{employee.getUsername()});
        db.close();
    }

    public void deleteEmployee(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Use TABLE_USER instead of "employees"
        db.delete(TABLE_USER, COLUMN_USERNAME + " = ?", new String[]{username});
        db.close();
    }



    public Employee getEmployee(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, password});

        if (cursor != null && cursor.moveToFirst()) {
            Employee employee = new Employee();
            employee.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_FULL_NAME)));
            employee.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))); // Ensure username is set
            employee.setPhoto(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO))); // Ensure username is set
            employee.setWorkingHours(cursor.getInt(cursor.getColumnIndex("working_hours")));
            employee.setSalary(cursor.getDouble(cursor.getColumnIndex("salary")));
            cursor.close();
            return employee;
        }

        return null; // Login failed
    }



    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee(
                        cursor.getString(cursor.getColumnIndex(COLUMN_FULL_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO)),
                        cursor.getInt(cursor.getColumnIndex("working_hours")),  // Updated to fetch working hours
                        cursor.getDouble(cursor.getColumnIndex("salary"))      // Updated to fetch salary
                );
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return employeeList;
    }

}