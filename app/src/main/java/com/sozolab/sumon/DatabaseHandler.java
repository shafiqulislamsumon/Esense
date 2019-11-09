package com.sozolab.sumon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "esensedb";
    private static final String TABLE_ACTIVITY = "history";
    private static final String KEY_ID = "id";
    private static final String KEY_ACTIVITY = "activity";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_STOP_TIME = "stop_time";
    private static final String KEY_DURATION = "duration";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACTIVITY_TABLE = "CREATE TABLE " + TABLE_ACTIVITY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ACTIVITY + " TEXT,"
                + KEY_START_TIME + " TEXT," + KEY_STOP_TIME + " TEXT," + KEY_DURATION + " TEXT" + ")";
        db.execSQL(CREATE_ACTIVITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        onCreate(db); // Create tables again
    }

    public void addActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ACTIVITY, activity.getActivityName());
        contentValues.put(KEY_START_TIME, activity.getStartTime());
        contentValues.put(KEY_STOP_TIME, activity.getStopTime());
        contentValues.put(KEY_DURATION, activity.getDuration());

        db.insert(TABLE_ACTIVITY, null, contentValues);
        db.close();
    }

    public ArrayList<Activity> getAllActivities() {

        ArrayList<Activity> activityList = new ArrayList<Activity>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACTIVITY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity.setActivityName(cursor.getString(1));
                activity.setStartTime(cursor.getString(2));
                activity.setStopTime(cursor.getString(3));
                activity.setDuration(cursor.getString(4));
                activityList.add(activity);
            } while (cursor.moveToNext());
        }

        Collections.reverse(activityList);
        return activityList;
    }
}
