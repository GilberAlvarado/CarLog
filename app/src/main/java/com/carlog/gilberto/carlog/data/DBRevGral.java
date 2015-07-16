package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Gilberto on 15/07/2015.
 */
public class DBRevGral {
    public static final String TABLE_NAME = "revgral";

    public static final String CN_ID = "_id";
    public static final String CN_TIPO = "tipo";
    public static final String CN_KMS = "kms";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null, "
            + CN_KMS + " int not null);";


    private DbHelper helper;
    private static SQLiteDatabase db;

    public DBRevGral(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(String mytipoRevGral, int num_kms) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, mytipoRevGral);
        valores.put(CN_KMS, num_kms);
        return valores;
    }

    public void insertar(String mytipoRevGral, int num_kms) {
        db.insert(TABLE_NAME, null, generarContentValues(mytipoRevGral, num_kms));
    }


    public Cursor buscarTiposRevGral() {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }



    public static Cursor buscarTiposRevGral(String mytipoRevGral) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{mytipoRevGral}, null, null, null);

    }

    public static Cursor buscarTiposRevGral(int id) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_ID + "=?", new String[]{Integer.toString(id)}, null, null, null);

    }
}
