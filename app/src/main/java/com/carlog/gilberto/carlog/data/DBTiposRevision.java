package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carlog.gilberto.carlog.TipoLog;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class DBTiposRevision {
    public static final String TABLE_NAME = "tiposrevision";

    public static final String CN_ID = "_id";
    public static final String CN_TIPO = "tipo";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null);";

    private DbHelper helper;
    private SQLiteDatabase db;

    public DBTiposRevision(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(String mytiporevision) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, mytiporevision);
        return valores;
    }

    public void insertar(String mytiporevision) {
        db.insert(TABLE_NAME, null, generarContentValues(mytiporevision));
    }

    public void eliminar(String mytiporevision) {
        db.delete(TABLE_NAME, CN_TIPO + "=?", new String[]{mytiporevision});
    }

    public Cursor cargarCursorTiposRevision() {
        String[] columnas = new String[]{CN_ID, CN_TIPO};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }

    public Cursor buscarTipo(String mytiporevision) {
        String[] columnas = new String[]{CN_ID, CN_TIPO};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{mytiporevision}, null, null, null);

    }
}
