package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by Gilberto on 24/07/2015.
 */
public class DBLogin {

    public static String Table = "login";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    private DbHelper helper;
    private static SQLiteDatabase db;

    public DBLogin(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
    }

    public static final String SQLCreateLogin = "CREATE TABLE login("
            + " id INTEGER PRIMARY KEY,"
            + " name TEXT,"
            + " email TEXT UNIQUE,"
            + " uid TEXT,"
            + " created_at TEXT" + ")";



    public void addUser(String name, String email, String uid, String created_at) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at);

        db.insert(Table, null, values);
        db.close();
    }

    /**
     * Obtener usuario de la base de datos
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + Table;

        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();

        return user;
    }


    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + Table;
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        return rowCount;
    }

    public Cursor getCursorUsuario(){
        return db.rawQuery("SELECT name, email, uid " +
                " FROM "+Table

                , null);
    }


    public void resetTables(){
        db.delete(Table, null, null);
        db.close();
    }



}