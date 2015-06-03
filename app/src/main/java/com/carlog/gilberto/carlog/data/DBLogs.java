package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.carlog.gilberto.carlog.AddLog;
import com.carlog.gilberto.carlog.TipoCoche;
import com.carlog.gilberto.carlog.TipoLog;

import java.net.ContentHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class DBLogs {
    public static final int NO_REALIZADO = 0;
    public static final int REALIZADO = 1;

    public static final String TABLE_NAME = "logs";

    public static final String CN_ID = "_id";
    public static final String CN_TIPO = "tipo";
    public static final String CN_FECHA = "fecha";
    public static final String CN_ACEITE = "aceite";
    public static final String CN_CAR = "matricula";
    public static final String CN_REALIZADO = "realizado";
    public static final String CN_KMS = "kms";  // kms que tenía el coche cuando se añadió el log

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null,"
            + CN_FECHA + " integer not null,"
            + CN_ACEITE + " int,"
            + CN_CAR + " text not null,"
            + CN_REALIZADO + " integer not null,"
            + CN_KMS + " integer not null,"
            + " FOREIGN KEY("+CN_ACEITE+") REFERENCES "+ DBAceite.TABLE_NAME+"("+DBAceite.CN_ID+"), "
            + " FOREIGN KEY("+CN_CAR+") REFERENCES "+ DBCar.TABLE_NAME+"("+DBCar.CN_MATRICULA+") ON DELETE CASCADE);";

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
        if (milog.getAceite(milog) != AddLog.NO_ACEITE) {
            valores.put(CN_ACEITE, milog.getAceite(milog));
        }
        valores.put(CN_CAR, milog.getCoche(milog));
        valores.put(CN_REALIZADO, milog.getRealizado(milog));
        valores.put(CN_KMS, milog.getKms(milog));
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
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA, CN_ACEITE, CN_CAR, CN_REALIZADO, CN_KMS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }

    public Cursor LogsOrderByFecha() { // Todos los tipos y historicos o no
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA, CN_ACEITE, CN_CAR, CN_REALIZADO, CN_KMS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, CN_FECHA + " ASC");

    }

    public Cursor LogsTodosOrderByFechaString(int int_now, String matricula) {  // todos los tipos pero solo los logs futuros y no realizados
        String sql = "SELECT _id, tipo, strftime('%d-%m-%Y',"+CN_FECHA+",'unixepoch') as fecha_string, aceite, matricula, realizado, kms FROM "+TABLE_NAME + " WHERE "
              /*  + CN_FECHA + " > " + int_now + " AND " */+ CN_REALIZADO + " = " + NO_REALIZADO + " AND " + CN_CAR + " = '" + matricula + "' ORDER BY "+ CN_FECHA;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor LogsHistoricoAceiteOrderByFechaString(int int_now, String matricula) { // Los de tipo aceite del histórico para elegir el más reciente
        String sql = "SELECT _id, tipo, strftime('%d-%m-%Y',"+CN_FECHA+",'unixepoch') as fecha_string, aceite, matricula, realizado, kms FROM "+TABLE_NAME + " WHERE "
               /* + CN_FECHA + " < " + int_now + " AND " */+ CN_TIPO + " = '" + TipoLog.TIPO_ACEITE + "' AND " + CN_REALIZADO + " = " + REALIZADO+ " AND " + CN_CAR + " = '" + matricula  +  "' ORDER BY "+ CN_FECHA + " DESC";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor LogsAceiteOrderByFechaString(int int_now, String matricula) { // Los de tipo aceite del futuro
        String sql = "SELECT _id, tipo, strftime('%d-%m-%Y',"+CN_FECHA+",'unixepoch') as fecha_string, aceite, matricula, realizado, kms FROM "+TABLE_NAME + " WHERE "
                /*+ CN_FECHA + " > " + int_now + " AND " */+ CN_TIPO + " = '" + TipoLog.TIPO_ACEITE + "' AND " + CN_REALIZADO + " = " + NO_REALIZADO+ " AND " + CN_CAR + " = '" + matricula + "' ORDER BY "+ CN_FECHA + " DESC";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor buscarTipo(String tipo, String matricula) {
        String sql = "SELECT _id, tipo, fecha, aceite, matricula, realizado, kms FROM "+TABLE_NAME + " WHERE "
                /*+ CN_FECHA + " > " + int_now + " AND " */+ CN_TIPO + " = '" + tipo + "' AND " + CN_CAR + " = '" + matricula + "'";
        Cursor c = db.rawQuery(sql, null);
        return c;
        /*
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA, CN_ACEITE, CN_CAR, CN_REALIZADO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{tipo}, null, null, null);*/

    }


    public void ActualizarFechaLogFuturo(int id, int fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FECHA + " = " + fecha + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void marcarRealizadoLog(int id, int fecha, int kms) {
        System.out.println("DATOS "+id + " "+fecha + " "+ kms);
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FECHA + " = " + fecha + ", " + CN_REALIZADO + " = " + REALIZADO + ", " + CN_KMS + " = " + kms+ " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarTipoAceiteLog(int id, int aceite) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_ACEITE + " = " + aceite+ " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

}
