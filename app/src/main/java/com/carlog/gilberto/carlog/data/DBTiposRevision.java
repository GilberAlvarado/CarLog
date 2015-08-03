package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class dbTiposRevision {
    public static final String TABLE_NAME = "tiposrevision";

    public static final String CN_TIPO = "tipo";
    public static final String CN_IMG = "img";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_TIPO + " text primary key,"
            + CN_IMG + " text not null);";

    private dbHelper helper;
    private SQLiteDatabase db;

    public dbTiposRevision(Context context) {
        helper = new dbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(String mytiporevision, String img) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, mytiporevision);
        valores.put(CN_IMG, img);
        return valores;
    }

    public void insertar(String mytiporevision, String img) {
        db.insert(TABLE_NAME, null, generarContentValues(mytiporevision, img));
    }

    public void eliminar(String mytiporevision) {
        db.delete(TABLE_NAME, CN_TIPO + "=?", new String[]{mytiporevision});
    }

    public Cursor cargarCursorTiposRevision() {
        String[] columnas = new String[]{CN_TIPO, CN_IMG};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }



    public Cursor buscarTipo(String mytiporevision) {
        String[] columnas = new String[]{CN_TIPO, CN_IMG};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{mytiporevision}, null, null, null);

    }
}
