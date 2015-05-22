package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carlog.gilberto.carlog.AddLog;
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
    public static final String CN_ACEITE = "aceite";
    public static final String CN_CAR = "matricula";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null,"
            + CN_FECHA + " int not null,"
            + CN_ACEITE + " int,"
            + CN_CAR + " text not null,"
            + " FOREIGN KEY("+CN_ACEITE+") REFERENCES "+ DBAceite.TABLE_NAME+"("+DBAceite.CN_ID+"), "
            + " FOREIGN KEY("+CN_CAR+") REFERENCES "+ DBCar.TABLE_NAME+"("+DBCar.CN_MATRICULA+"));";

    private DbHelper helper;
    private SQLiteDatabase db;

    public DBLogs(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(TipoLog milog) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, milog.getTipo(milog));
        valores.put(CN_FECHA, milog.getFechaint(milog));
        if (milog.getAceite(milog) != AddLog.NO_KMS)
            valores.put(CN_ACEITE, milog.getFechaint(milog));
        valores.put(CN_CAR, milog.getCoche(milog));
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
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA, CN_ACEITE, CN_CAR};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }

    public Cursor LogsOrderByFecha() {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA, CN_ACEITE, CN_CAR};
        return db.query(TABLE_NAME, columnas, null, null, null, null, CN_FECHA + " ASC");

    }

    public Cursor LogsOrderByFechaString(int int_now) {
        String sql = "SELECT _id, tipo, strftime('%d-%m-%Y',"+CN_FECHA+",'unixepoch') as fecha_string, aceite, matricula FROM "+TABLE_NAME + " WHERE " + CN_FECHA + " > " + int_now + " ORDER BY "+ CN_FECHA;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor buscarTipo(String tipo) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA, CN_ACEITE, CN_CAR};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{tipo}, null, null, null);

    }
}
