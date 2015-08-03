package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Gilberto on 22/07/2015.
 */
public class dbFiltroAceite {
    public static final String TABLE_NAME = "fil_aceite";

    public static final String CN_ID = "_id";
    public static final String CN_TIPO = "tipo";
    public static final String CN_VECES = "veces";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null, "
            + CN_VECES + " int not null);";


    private dbHelper helper;
    private static SQLiteDatabase db;

    public dbFiltroAceite(Context context) {
        helper = new dbHelper(context);
        db = helper.getWritableDatabase();
    }


    public ContentValues generarContentValues(String mytipoFiltroAceite, int num_kms) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, mytipoFiltroAceite);
        valores.put(CN_VECES, num_kms);
        return valores;
    }

    public void insertar(String mytipoFiltroAceite, int num_kms) {
        db.insert(TABLE_NAME, null, generarContentValues(mytipoFiltroAceite, num_kms));
    }


    public Cursor buscarTiposFiltroAceite() {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_VECES};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }



    public static Cursor buscarTiposFiltroAceite(String mytipoFiltroAceite) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_VECES};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{mytipoFiltroAceite}, null, null, null);

    }

    public static Cursor buscarTiposFiltroAceite(int id) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_VECES};
        return db.query(TABLE_NAME, columnas, CN_ID + "=?", new String[]{Integer.toString(id)}, null, null, null);

    }
}
