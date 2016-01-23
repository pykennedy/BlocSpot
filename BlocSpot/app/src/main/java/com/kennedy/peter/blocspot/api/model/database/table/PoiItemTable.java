package com.kennedy.peter.blocspot.api.model.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PoiItemTable extends Table {

    public static class Builder implements Table.Builder {

        ContentValues values = new ContentValues();

        public Builder setTitleID(String titleID) {
            values.put(COLUMN_TITLEID, titleID);
            return this;
        }

        public Builder setName(String name) {
            values.put(COLUMN_NAME, name);
            return this;
        }

        public Builder setCategory(String category) {
            values.put(COLUMN_CATEGORY, category);
            return this;
        }

        public Builder setNotes(String notes) {
            values.put(COLUMN_NOTES, notes);
            return this;
        }

        public Builder setLongitude(double longitude) {
            values.put(COLUMN_LONGITUDE, Double.toString(longitude));
            return this;
        }

        public Builder setLatitude(double latitude) {
            values.put(COLUMN_LATITUDE, Double.toString(latitude));
            return this;
        }

        public Builder setViewed(boolean viewed) {
            values.put(COLUMN_VIEWED, viewed ? 1 : 0);
            return this;
        }

        @Override
        public long insert(SQLiteDatabase writeableDB) {
            return writeableDB.insert(NAME, null, values);
        }

        public void update(SQLiteDatabase writeableDB, long id) {
            writeableDB.update(NAME, values, "ID = ?", new String[]{Long.toString(id)});
        }

        public void remove(SQLiteDatabase writeableDB, long id) {
            writeableDB.delete(NAME, "ID = ?", new String[]{Long.toString(id)});
        }
    }

    private static final String NAME = "poi_items";

    private static final String COLUMN_TITLEID = "titleID";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_VIEWED = "viewed";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getCreateStatement() {
        return "CREATE TABLE " + getName() + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITLEID + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_NOTES + " TEXT,"
                + COLUMN_LONGITUDE + " TEXT,"
                + COLUMN_LATITUDE + " TEXT,"
                + COLUMN_VIEWED + " INTEGER DEFAULT 0)";
    }
    public static String getTitleID(Cursor cursor) { return getString(cursor, COLUMN_TITLEID); }
    public static String getName(Cursor cursor) { return getString(cursor, COLUMN_NAME); }
    public static String getCategory(Cursor cursor) { return getString(cursor, COLUMN_CATEGORY); }
    public static String getNotes(Cursor cursor) { return getString(cursor, COLUMN_NOTES); }
    public static long getID(Cursor cursor) { return getLong(cursor, COLUMN_ID); }
    public static String getLongitude(Cursor cursor) { return getString(cursor, COLUMN_LONGITUDE); }
    public static String getLatitude(Cursor cursor) { return getString(cursor, COLUMN_LATITUDE); }
    public static boolean getViewed(Cursor cursor) { return getBoolean(cursor, COLUMN_VIEWED); }

    public Cursor fetchAllItems(SQLiteDatabase readableDB) {
        return readableDB.rawQuery("SELECT * FROM " + NAME, null);
    }

    public Cursor fetchAllPoiWithCategory(SQLiteDatabase readableDB, String category) {
        return readableDB.query(NAME, null, COLUMN_CATEGORY + " = ?", new String[]{category},
                null, null, null);
    }
}
