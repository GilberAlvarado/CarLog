package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carlog.gilberto.carlog.activity.addLog;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class dbLogs {
    public static final int NO_REALIZADO = 0;
    public static final int REALIZADO = 1;
    public static final int NO_FMODIFICADA = 0;
    public static final int FMODIFICADA = 1;

    public static final String TABLE_NAME = "logs";

    public static final String CN_ID = "_id";
    public static final String CN_TIPO = "tipo";
    public static final String CN_FECHA = "fecha";
    public static final String CN_ACEITE = "aceite";
    public static final String CN_VECES_FIL_ACEITE = "veces_aceite"; // cada cuantos cambios de aceite se cambia el filtro (para los tipo_rev filtro aceite)
    public static final String CN_CONTADOR_FIL_ACEITE = "contador_aceite"; // nº de veces que se ha cambiado el aceite sin cambiar el filtro (para tipo_rev aceite)
    public static final String CN_REVGRAL = "revgral";
    public static final String CN_CORREA = "correa";
    public static final String CN_BOMBAAGUA = "bombaagua";
    public static final String CN_FGASOLINA = "fgasolina";
    public static final String CN_FAIRE = "faire";
    public static final String CN_BUJIAS = "bujias";
    public static final String CN_EMBRAGUE = "embrague";
    public static final String CN_CAR = "matricula";
    public static final String CN_REALIZADO = "realizado";
    public static final String CN_FMODIFICADA = "f_modificada"; // se ha modificado la fecha procesada y se ha puesto una revisión antes a mano (ignorar la procesada)
    public static final String CN_KMS = "kms";  // kms que tenía el coche cuando se añadió el log

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null,"
            + CN_FECHA + " integer not null,"
            + CN_ACEITE + " int,"
            + CN_VECES_FIL_ACEITE + " int,"
            + CN_CONTADOR_FIL_ACEITE + " int,"
            + CN_REVGRAL + " int,"
            + CN_CORREA + " int,"
            + CN_BOMBAAGUA + " int,"
            + CN_FGASOLINA + " int,"
            + CN_FAIRE + " int,"
            + CN_BUJIAS + " int,"
            + CN_EMBRAGUE + " int,"
            + CN_CAR + " text not null,"
            + CN_REALIZADO + " integer not null,"
            + CN_FMODIFICADA + " integer not null,"
            + CN_KMS + " integer not null,"
            + " FOREIGN KEY("+CN_ACEITE+") REFERENCES "+ dbAceite.TABLE_NAME+"("+ dbAceite.CN_ID+"), "
            + " FOREIGN KEY("+CN_REVGRAL+") REFERENCES "+ dbRevGral.TABLE_NAME+"("+ dbRevGral.CN_ID+"), "
            + " FOREIGN KEY("+CN_CORREA+") REFERENCES "+ dbRevGral.TABLE_NAME+"("+ dbCorrea.CN_ID+"), "
            + " FOREIGN KEY("+CN_BOMBAAGUA+") REFERENCES "+ dbRevGral.TABLE_NAME+"("+ dbBombaAgua.CN_ID+"), "
            + " FOREIGN KEY("+CN_FGASOLINA+") REFERENCES "+ dbRevGral.TABLE_NAME+"("+ dbFiltroGasolina.CN_ID+"), "
            + " FOREIGN KEY("+CN_FAIRE+") REFERENCES "+ dbRevGral.TABLE_NAME+"("+ dbFiltroAire.CN_ID+"), "
            + " FOREIGN KEY("+CN_BUJIAS+") REFERENCES "+ dbRevGral.TABLE_NAME+"("+ dbBujias.CN_ID+"), "
            + " FOREIGN KEY("+CN_EMBRAGUE+") REFERENCES "+ dbRevGral.TABLE_NAME+"("+ dbEmbrague.CN_ID+"), "
            + " FOREIGN KEY("+CN_TIPO+") REFERENCES "+ dbTiposRevision.TABLE_NAME+"("+ dbTiposRevision.CN_TIPO+"), "
            + " FOREIGN KEY("+CN_CAR+") REFERENCES "+ dbCar.TABLE_NAME+"("+ dbCar.CN_MATRICULA+") ON DELETE CASCADE);";

    private dbHelper helper;
    private SQLiteDatabase db;

    public dbLogs(Context context) {
        helper = new dbHelper(context);
        db = helper.getWritableDatabase();
    }


    public ContentValues generarContentValues(tipoLog milog) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, milog.getTipo(milog));
        valores.put(CN_FECHA, milog.getFechalong(milog));
        if (milog.getAceite(milog) != addLog.NO_ACEITE) {
            valores.put(CN_ACEITE, milog.getAceite(milog));
        }
        if (milog.getVecesFilAceite(milog) != addLog.NO_VECES_FIL_ACEITE) {
            valores.put(CN_VECES_FIL_ACEITE, milog.getVecesFilAceite(milog));
        }
        if (milog.getVecesFilAceite(milog) != addLog.NO_CONTADOR_FIL_ACEITE) {
            valores.put(CN_CONTADOR_FIL_ACEITE, milog.getVecesFilAceite(milog));
        }
        else valores.put(CN_CONTADOR_FIL_ACEITE, 0);
        if (milog.getRevgral(milog) != addLog.NO_REVGRAL) {
            valores.put(CN_REVGRAL, milog.getRevgral(milog));
        }
        if (milog.getCorrea(milog) != addLog.NO_CORREA) {
            valores.put(CN_CORREA, milog.getCorrea(milog));
        }
        if (milog.getBombaagua(milog) != addLog.NO_BOMBAAGUA) {
            valores.put(CN_BOMBAAGUA, milog.getBombaagua(milog));
        }
        if (milog.getFgasolina(milog) != addLog.NO_FGASOLINA) {
            valores.put(CN_FGASOLINA, milog.getFgasolina(milog));
        }
        if (milog.getFaire(milog) != addLog.NO_FAIRE) {
            valores.put(CN_FAIRE, milog.getFaire(milog));
        }
        if (milog.getBujias(milog) != addLog.NO_BUJIAS) {
            valores.put(CN_BUJIAS, milog.getBujias(milog));
        }
        if (milog.getEmbrague(milog) != addLog.NO_EMBRAGUE) {
            valores.put(CN_EMBRAGUE, milog.getEmbrague(milog));
        }
        valores.put(CN_CAR, milog.getCoche(milog));
        valores.put(CN_REALIZADO, milog.getRealizado(milog));
        valores.put(CN_FMODIFICADA, milog.getFmodificada(milog));
        valores.put(CN_KMS, milog.getKms(milog));
        return valores;
    }

    public void insertar(tipoLog milog) {
        db.insert(TABLE_NAME, null, generarContentValues(milog));
    }


    public void eliminar_por_id(int id) {
        db.delete(TABLE_NAME, CN_ID + "=?", new String[]{Integer.toString(id)});
    }

    public void eliminar(tipoLog milog) {
        db.delete(TABLE_NAME, CN_TIPO + "=?", new String[]{milog.getTipo(milog)});
    }


    public Cursor cargarCursorLogs() {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA, CN_ACEITE, CN_VECES_FIL_ACEITE, CN_CONTADOR_FIL_ACEITE, CN_REVGRAL,
                CN_CORREA, CN_BOMBAAGUA, CN_FGASOLINA, CN_FAIRE, CN_BUJIAS, CN_EMBRAGUE, CN_CAR, CN_REALIZADO, CN_FMODIFICADA, CN_KMS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);
    }

    public Cursor buscarLogID(int id) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA, CN_ACEITE, CN_VECES_FIL_ACEITE, CN_CONTADOR_FIL_ACEITE, CN_REVGRAL,
                CN_CORREA, CN_BOMBAAGUA, CN_FGASOLINA, CN_FAIRE, CN_BUJIAS, CN_CAR, CN_REALIZADO, CN_FMODIFICADA, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_ID + "=?", new String[]{Integer.toString(id)}, null, null, null);
    }

    public Cursor LogsOrderByFecha() { // Todos los tipos y historicos o no
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_FECHA, CN_ACEITE, CN_VECES_FIL_ACEITE, CN_CONTADOR_FIL_ACEITE, CN_REVGRAL,
                CN_CORREA, CN_BOMBAAGUA, CN_FGASOLINA, CN_FAIRE, CN_BUJIAS, CN_CAR, CN_REALIZADO, CN_FMODIFICADA, CN_KMS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, CN_FECHA + " ASC");

    }


    public Cursor LogsTodosOrderByFechaString(String matricula) {  // todos los tipos
        String sql = "SELECT _id, tipo, strftime('%d-%m-%Y',"+CN_FECHA+",'unixepoch') as fecha_string, aceite, veces_aceite, contador_aceite, revgral, " +
                "correa, bombaagua, fgasolina, faire, bujias, embrague, matricula, realizado, f_modificada, kms FROM "+TABLE_NAME + " WHERE "
              /*  + CN_FECHA + " > " + int_now + " AND " + CN_REALIZADO + " = " + NO_REALIZADO + " AND "*/ + CN_CAR + " = '" + matricula + "' ORDER BY "+ CN_FECHA;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor LogsFuturosOrderByFechaString(String matricula) {  // todos los tipos pero solo los logs futuros y no realizados
        String sql = "SELECT _id, tipo, strftime('%d-%m-%Y',"+CN_FECHA+",'unixepoch') as fecha_string, aceite, veces_aceite, contador_aceite, revgral, " +
                "correa, bombaagua, fgasolina, faire, bujias, embrague, matricula, realizado, f_modificada, kms FROM "+TABLE_NAME + " WHERE "
              /*  + CN_FECHA + " > " + int_now + " AND " */+ CN_REALIZADO + " = " + NO_REALIZADO + " AND " + CN_CAR + " = '" + matricula + "' ORDER BY "+ CN_FECHA;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor LogsHistoricosOrderByFechaString(String matricula) {  // todos los tipos pero solo los logs historicos y realizados
        String sql = "SELECT _id, tipo, strftime('%d-%m-%Y',"+CN_FECHA+",'unixepoch') as fecha_string, aceite, veces_aceite, contador_aceite, revgral, " +
                "correa, bombaagua, fgasolina, faire, bujias, embrague, matricula, realizado, f_modificada, kms FROM "+TABLE_NAME + " WHERE "
              /*  + CN_FECHA + " > " + int_now + " AND " */+ CN_REALIZADO + " = " + REALIZADO + " AND " + CN_CAR + " = '" + matricula + "' ORDER BY "+ CN_FECHA + " DESC";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor LogsHistoricoTipoOrderByFechaString(String matricula, String tipo_rev) { // Los de tipo aceite del histórico para elegir el más reciente
        String sql = "SELECT _id, tipo, strftime('%d-%m-%Y',"+CN_FECHA+",'unixepoch') as fecha_string, aceite, veces_aceite, contador_aceite, revgral, " +
                "correa, bombaagua, fgasolina, faire, bujias, embrague, matricula, realizado, f_modificada, kms FROM "+TABLE_NAME + " WHERE "
               /* + CN_FECHA + " < " + int_now + " AND " */+ CN_TIPO + " = '" + tipo_rev + "' AND " + CN_REALIZADO + " = " + REALIZADO+ " AND " + CN_CAR + " = '" + matricula  +  "' ORDER BY "+ CN_FECHA + " DESC";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor LogsTipoOrderByFechaString(String matricula, String tipo_rev) { // Los de tipo tipo del futuro
        String sql = "SELECT _id, tipo, strftime('%d-%m-%Y',"+CN_FECHA+",'unixepoch') as fecha_string, aceite, veces_aceite, contador_aceite, revgral, " +
                "correa, bombaagua, fgasolina, faire, bujias, embrague, matricula, realizado, f_modificada, kms FROM "+TABLE_NAME + " WHERE "
                /*+ CN_FECHA + " > " + int_now + " AND " */+ CN_TIPO + " = '" + tipo_rev + "' AND " + CN_REALIZADO + " = " + NO_REALIZADO+ " AND " + CN_CAR + " = '" + matricula + "' ORDER BY "+ CN_FECHA + " DESC";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor LogsTipoTodosCochesOrderByFechaString(String tipo_rev) { // Los de tipo tipo del futuro de todos los coches (sólo para las notificaciones)
        String sql = "SELECT _id, tipo, strftime('%d-%m-%Y',"+CN_FECHA+",'unixepoch') as fecha_string, aceite, veces_aceite, contador_aceite, revgral, " +
                "correa, bombaagua, fgasolina, faire, bujias, embrague, matricula, realizado, f_modificada, kms FROM "+TABLE_NAME + " WHERE "
                /*+ CN_FECHA + " > " + int_now + " AND " */+ CN_TIPO + " = '" + tipo_rev + "' AND " + CN_REALIZADO + " = " + NO_REALIZADO + " ORDER BY "+ CN_FECHA + " DESC";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor buscarTipo(String tipo, String matricula) {
        String sql = "SELECT _id, tipo, fecha, aceite, veces_aceite, contador_aceite, revgral, correa, bombaagua, fgasolina, faire, bujias, embrague, matricula, realizado, f_modificada, kms FROM "+TABLE_NAME + " WHERE "
                /*+ CN_FECHA + " > " + int_now + " AND " */+ CN_TIPO + " = '" + tipo + "' AND " + CN_CAR + " = '" + matricula + "'";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public void ActualizarFechaLogFuturo(int id, long fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FECHA + " = " + fecha + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void ActualizarFModificadaLogFuturo(int id, long fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FECHA + " = " + fecha + ", " + CN_FMODIFICADA + " = " + FMODIFICADA + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void ActualizarContadorFilAceite(int id, int contador) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_CONTADOR_FIL_ACEITE + " = " + contador +  " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void marcarRealizadoLog(int id, long fecha, int kms) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FECHA + " = " + fecha + ", " + CN_REALIZADO + " = " + REALIZADO + ", " + CN_KMS + " = " + kms+ " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarTipoAceiteLog(int id, int aceite) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_ACEITE + " = " + aceite + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarFechaAceiteLog(int id, int aceite, long fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_ACEITE + " = " + aceite + ", " + CN_FECHA + " = " + fecha + ", " + CN_FMODIFICADA + " = " + FMODIFICADA + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarTipoRevGralLog(int id, int revgral) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_REVGRAL  + " = " + revgral + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarFechaRevGralLog(int id, int revgral, long fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_REVGRAL  + " = " + revgral + ", " + CN_FECHA + " = " + fecha + ", " + CN_FMODIFICADA + " = " + FMODIFICADA + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarTipoCorreaLog(int id, int correa) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_CORREA  + " = " + correa + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarFechaCorreaLog(int id, int correa, long fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_CORREA  + " = " + correa + ", " + CN_FECHA + " = " + fecha + ", " + CN_FMODIFICADA + " = " + FMODIFICADA + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarTipoBombaAguaLog(int id, int bombaagua) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_BOMBAAGUA  + " = " + bombaagua + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarFechaBombaAguaLog(int id, int bombaagua, long fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_BOMBAAGUA  + " = " + bombaagua + ", " + CN_FECHA + " = " + fecha + ", " + CN_FMODIFICADA + " = " + FMODIFICADA + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarTipoFiltroGasolinaLog(int id, int fgasolina) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FGASOLINA  + " = " + fgasolina + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarFechaFiltroGasolinaLog(int id, int fgasolina, long fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FGASOLINA  + " = " + fgasolina + ", " + CN_FECHA + " = " + fecha + ", " + CN_FMODIFICADA + " = " + FMODIFICADA + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarTipoFiltroAireLog(int id, int faire) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FAIRE  + " = " + faire + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarFechaFiltroAireLog(int id, int faire, long fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FAIRE  + " = " + faire + ", " + CN_FECHA + " = " + fecha + ", " + CN_FMODIFICADA + " = " + FMODIFICADA + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarFechaFiltroAceite(int id, int veces_faceite) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_VECES_FIL_ACEITE  + " = " + veces_faceite + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarTipoBujiasLog(int id, int bujias) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_BUJIAS  + " = " + bujias + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarFechaBujiasLog(int id, int bujias, long fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_BUJIAS  + " = " + bujias + ", " + CN_FECHA + " = " + fecha + ", " + CN_FMODIFICADA + " = " + FMODIFICADA + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarTipoEmbragueLog(int id, int embrague) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_EMBRAGUE  + " = " + embrague + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

    public void modificarFechaEmbragueLog(int id, int embrague, long fecha) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_EMBRAGUE  + " = " + embrague + ", " + CN_FECHA + " = " + fecha + ", " + CN_FMODIFICADA + " = " + FMODIFICADA + " WHERE " + CN_ID + " = " + id;
        db.execSQL(sql);
    }

}
