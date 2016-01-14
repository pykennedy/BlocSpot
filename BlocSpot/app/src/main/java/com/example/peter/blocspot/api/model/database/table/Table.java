package com.example.peter.blocspot.api.model.database.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.peter.blocspot.BlocSpotApplication;
import com.example.peter.blocspot.api.model.database.DatabaseOpenHelper;

public abstract class Table {

    public static interface Builder {
        public long insert();
    }

    protected static final String COLUMN_ID = "id";
    protected static final String COLUMN_TITLEID = "titleID";
    public abstract String getName();
    public abstract String getCreateStatement();

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor fetchRow(long rowId) {
        DatabaseOpenHelper databaseOpenHelper = BlocSpotApplication.getSharedDataSource().getPoiItemTable().getDatabaseOpenHelper();
        return databaseOpenHelper.getReadableDatabase().query(true, getName(), null, COLUMN_ID + " = ?",
                new String[]{Long.toString(rowId)}, null, null, null, null);
    }

    public Cursor fetchRowFromMarkerID(String titleID) {
        DatabaseOpenHelper databaseOpenHelper = BlocSpotApplication.getSharedDataSource().getPoiItemTable().getDatabaseOpenHelper();
        return databaseOpenHelper.getReadableDatabase().query(true, getName(), null, COLUMN_TITLEID + " = ?",
                new String[] {titleID}, null, null, null, null);
    }

    protected static String getString(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        if (columnIndex == -1) {
            return "";
        }
        return cursor.getString(columnIndex);
    }

    protected static long getLong(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        if (columnIndex == -1) {
            return -1l;
        }
        return cursor.getLong(columnIndex);
    }

    protected static boolean getBoolean(Cursor cursor, String column) {
        return getLong(cursor, column) == 1l;
    }
}
