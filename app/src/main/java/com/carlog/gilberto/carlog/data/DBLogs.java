package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carlog.gilberto.carlog.TipoLog;

import java.net.ContentHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class DBLogs {
    public static final String TABLE_NAME = "logs";

    public static final String CN_ID = "_id";
    public static final String CN_TIPO = "tipo";
    public static final String CN_FECHA = "fecha";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null,"
            + CN_FECHA + " text not null);";

    private DbHelper helper;
    private SQLiteDatabase db;

    public DBLogs(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(TipoLog milog) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, milog.getTipo(milog));
        valores.put(CN_FECHA, milog.getFechatxt(milog));
        return valores;
    }

    public void insertar(TipoLog milog) {
        db.insert(TABLE_NAME, null, generarContentValues(milog));
    }


    public void eliminar_por_id(int id) {
        db.delete(TABLE_NAME, CN_ID + "=?", new String[]{Integer.toString(id)});
    }

    public void eliminar(TipoLog milog) {
        db.delete(TABLE_NAME, CN_TIPO + "=?", new String[]{milog.getTipo(milog)});
    }


    public Cursor cargarCursorLogs() {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }

    public Cursor buscarTipo(String tipo) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{tipo}, null, null, null);

    }
}
