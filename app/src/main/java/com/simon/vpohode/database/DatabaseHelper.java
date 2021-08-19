package com.simon.vpohode.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "vpohode.db"; //name of DB
    private static final int SCHEMA = 1;  // Version of DB
    public static final String TABLE = "items";// Name of Table
    public static final String TABLE_LOOKS = "looks";// Name of Table Looks
    private static final String RAW_QUERY_PART = "SELECT * FROM " + TABLE + " WHERE ";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL ("CREATE TABLE " + TABLE + " ("
                + DBFields.ID.toFieldName()
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBFields.NAME.toFieldName() + " " + DBFields.NAME.toType() + ", "
                + DBFields.STYLE.toFieldName() + " " + DBFields.STYLE.toType() + ", "
                + DBFields.ISTOP.toFieldName() + " " + DBFields.ISTOP.toType() + ", "
                + DBFields.TERMID.toFieldName() + " " + DBFields.TERMID.toType() + ", "
                + DBFields.LAYER.toFieldName() + " " + DBFields.LAYER.toType() + ", "
                + DBFields.COLOR.toFieldName() + " " + DBFields.COLOR.toType() + ", "
                + DBFields.FOTO.toFieldName() + " " + DBFields.FOTO.toType() + ", "
                + DBFields.USED.toFieldName() + " " + DBFields.USED.toType() + ", "
                + DBFields.CREATED.toFieldName() + " " + DBFields.CREATED.toType() + ", "
                + DBFields.INWASH.toFieldName() + " " + DBFields.INWASH.toType() + ", "
                + DBFields.BRAND.toFieldName() + " " + DBFields.BRAND.toType() + ");");

        db.execSQL("CREATE TABLE " + TABLE_LOOKS + " ("
                + DBLooksFields.ID.toFieldName() + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBLooksFields.NAME.toFieldName() + " " + DBLooksFields.NAME.toType() + ", "
                + DBLooksFields.TERMMAX.toFieldName() + " " + DBLooksFields.TERMMAX.toType() + ", "
                + DBLooksFields.TERMMIN.toFieldName() + " " + DBLooksFields.TERMMIN.toType() + ", "
                + DBLooksFields.ITEMS.toFieldName() + " " + DBLooksFields.ITEMS.toType() + ");"
        );

        addPrefilledLooks(db);
    }

    private void addPrefilledLooks(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DBLooksFields.NAME.toFieldName(), "First look");
        cv.put(DBLooksFields.TERMMAX.toFieldName(), 30d);
        cv.put(DBLooksFields.TERMMIN.toFieldName(), 25d);
        cv.put(DBLooksFields.ITEMS.toFieldName(), "1,2,3,4");
        db.insert(TABLE_LOOKS,null,cv);
        cv.clear();

        cv.put(DBLooksFields.NAME.toFieldName(), "Second look");
        cv.put(DBLooksFields.TERMMAX.toFieldName(), 25d);
        cv.put(DBLooksFields.TERMMIN.toFieldName(), 20d);
        cv.put(DBLooksFields.ITEMS.toFieldName(), "1,3,4,5,7");
        db.insert(TABLE_LOOKS,null,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKS);
        onCreate(db);
    }

    public static Cursor getCursorWardrobe(SQLiteDatabase db, int sortBy){
        return db.rawQuery(RAW_QUERY_PART + DBFields.INWASH.toFieldName() + " = 0 " + getOrderString(sortBy), null);
    }

    public static Cursor getCursorInWash(SQLiteDatabase db){
        return db.rawQuery(RAW_QUERY_PART + DBFields.INWASH.toFieldName() + " = 1", null);
    }

    public static Cursor getCursoreByIsTop (SQLiteDatabase db, final int istop, int layer){
        return db.rawQuery(RAW_QUERY_PART + DBFields.ISTOP.toFieldName() + " = " + istop + " AND " + DBFields.LAYER.toFieldName() + "=" + layer + " AND " + DBFields.INWASH.toFieldName() + " = 0", null);
    }

    public static String getOrderString(int sortBy){
        String result = "ORDER BY ";
        switch (sortBy){
            case 1:
                result += "name";
                break;
            case 2:
                result += "name DESC";
                break;
            case 3:
                result += "brand";
                break;
            case 4:
                result += "termindex";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

}
