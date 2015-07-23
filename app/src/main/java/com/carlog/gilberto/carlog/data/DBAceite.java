package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Gilberto on 19/05/2015.
 */
public class DBAceite {
    public static final String TABLE_NAME = "aceite";

    public static final String CN_ID = "_id";
    public static final String CN_TIPO = "tipo";
    public static final String CN_KMS = "kms";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null, "
            + CN_KMS + " int not null);";


    private DbHelper helper;
    private static SQLiteDatabase db;

    public DBAceite(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
    }


    public ContentValues generarContentValues(String mytipoAceite, int num_kms) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, mytipoAceite);
        valores.put(CN_KMS, num_kms);
        return valores;
    }

    public void insertar(String mytipoAceite, int num_kms) {
        db.insert(TABLE_NAME, null, generarContentValues(mytipoAceite, num_kms));
    }


    public Cursor buscarTiposAceite() {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }



    public static Cursor buscarTiposAceite(String mytipoAceite) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{mytipoAceite}, null, null, null);

    }

    public static Cursor buscarTiposAceite(int id) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_ID + "=?", new String[]{Integer.toString(id)}, null, null, null);

    }
}
