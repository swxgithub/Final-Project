package com.example.youci;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Word.db";
    public static String TABLE_NAME = "R_word";
    public String COL_1 = "id";
    public String COL_2 = "word";


    //Simplified constructor
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create the database
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "id integer primary key autoincrement," +
                "word text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertData(String word) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, word);
        //Table name, null and the content values are needed as param

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            //Insert has failed
            return false;
        } else {
            //Successful insertion
            return true;
        }
    }

    public boolean Delete(String word){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] args = {String.valueOf(word)};
        //sqLiteDatabase.delete(TABLE_NAME,null,args);
        long result = sqLiteDatabase.delete(TABLE_NAME,"word=?",args);
        if (result == -1) {
            //Insert has failed
            return false;
        } else {
            //Successful insertion
            return true;
        }
    }

    public Cursor readData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }
}
