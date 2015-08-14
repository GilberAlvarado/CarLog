package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carlog.gilberto.carlog.activity.myActivity;
import com.carlog.gilberto.carlog.tiposClases.tipoCoche;
import com.carlog.gilberto.carlog.tiposClases.tipoSettings;

/**
 * Created by Gilberto on 13/08/2015.
 */
public class dbSettings {
    public static final String TABLE_NAME = "settings";

    public static final String CN_ID = "_id";
    public static final String CN_NOTIFICACIONES = "notificaciones";
    public static final String CN_ACEITE = "aceite";
    public static final String CN_AMORTIGUADORES = "amortiguadores";
    public static final String CN_ANTICONGELANTE = "anticongelante";
    public static final String CN_BATERIA = "bateria";
    public static final String CN_BOMBAAGUA = "bomba_agua";
    public static final String CN_BUJIAS = "bujias";
    public static final String CN_CORREA = "correa";
    public static final String CN_EMBRAGUE = "embrague";
    public static final String CN_FILACEITE = "fil_aceite";
    public static final String CN_FILAIRE = "fil_aire";
    public static final String CN_FILGASOLINA = "fil_gasolina";
    public static final String CN_FRENOS = "frenos";
    public static final String CN_LUCES = "luces";
    public static final String CN_REVGEN = "revgen";
    public static final String CN_RUEDAS = "ruedas";
    public static final String CN_LIQFRENOS = "liq_frenos";
    public static final String CN_ITV = "ITV";
    public static final String CN_LIMPIAPARABRISAS = "limpiaparabrisas";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_NOTIFICACIONES + " integer not null,"
            + CN_ACEITE + " integer not null,"
            + CN_AMORTIGUADORES + " integer not null,"
            + CN_ANTICONGELANTE + " integer not null,"
            + CN_BATERIA + " integer not null,"
            + CN_BOMBAAGUA + " integer not null,"
            + CN_BUJIAS + " integer not null,"
            + CN_CORREA + " integer not null,"
            + CN_EMBRAGUE + " integer not null,"
            + CN_FILACEITE + " integer not null,"
            + CN_FILAIRE + " integer not null,"
            + CN_FILGASOLINA + " integer not null,"
            + CN_FRENOS + " integer not null,"
            + CN_LUCES + " integer not null,"
            + CN_LIQFRENOS + " integer not null,"
            + CN_LIMPIAPARABRISAS + " integer not null,"
            + CN_ITV + " integer not null,"
            + CN_REVGEN + " integer not null,"
            + CN_RUEDAS + " integer not null);";


    private dbHelper helper;
    private static SQLiteDatabase db;

    public dbSettings(Context context) {
        helper = new dbHelper(context);
        db = helper.getWritableDatabase();
    }


    public ContentValues generarContentValues(tipoSettings miSettings) {
        ContentValues valores = new ContentValues();
        valores.put(CN_NOTIFICACIONES, miSettings.getNotificaciones(miSettings));
        valores.put(CN_ACEITE, miSettings.getAceite(miSettings));
        valores.put(CN_AMORTIGUADORES, miSettings.getAmortiguadores(miSettings));
        valores.put(CN_ANTICONGELANTE, miSettings.getAnticongelante(miSettings));
        valores.put(CN_BATERIA, miSettings.getBateria(miSettings));
        valores.put(CN_BOMBAAGUA, miSettings.getBombaagua(miSettings));
        valores.put(CN_BUJIAS, miSettings.getBujias(miSettings));
        valores.put(CN_CORREA, miSettings.getCorrea(miSettings));
        valores.put(CN_EMBRAGUE, miSettings.getEmbrague(miSettings));
        valores.put(CN_FILACEITE, miSettings.getFilaceite(miSettings));
        valores.put(CN_FILAIRE, miSettings.getFilaire(miSettings));
        valores.put(CN_FILGASOLINA, miSettings.getFilgasolina(miSettings));
        valores.put(CN_FRENOS, miSettings.getFrenos(miSettings));
        valores.put(CN_ITV, miSettings.getItv(miSettings));
        valores.put(CN_LIMPIAPARABRISAS, miSettings.getLimpiaparabrisas(miSettings));
        valores.put(CN_LIQFRENOS, miSettings.getLiquidofrenos(miSettings));
        valores.put(CN_LUCES, miSettings.getLuces(miSettings));
        valores.put(CN_REVGEN, miSettings.getRevgen(miSettings));
        valores.put(CN_RUEDAS, miSettings.getRuedas(miSettings));
        return valores;
    }

    public void insertar(tipoSettings settings) {
        db.insert(TABLE_NAME, null, generarContentValues(settings));
    }


    public Cursor getSettings() {
        String[] columnas = new String[]{CN_NOTIFICACIONES, CN_ACEITE, CN_AMORTIGUADORES, CN_ANTICONGELANTE, CN_BATERIA, CN_BOMBAAGUA, CN_BUJIAS, CN_CORREA,
        CN_FILACEITE, CN_FILGASOLINA, CN_FILAIRE, CN_EMBRAGUE, CN_LIQFRENOS, CN_LIMPIAPARABRISAS, CN_LUCES, CN_ITV, CN_REVGEN, CN_RUEDAS, CN_FRENOS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }
    public void actualizarNotificaciones(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_NOTIFICACIONES + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarAceite(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_ACEITE + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarAmortiguadores(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_AMORTIGUADORES + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarAnticongelante(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_ANTICONGELANTE + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarBateria(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_BATERIA + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarBombaagua(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_BOMBAAGUA + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarBujias(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_BUJIAS + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarEmbrague(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_EMBRAGUE + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarFilaceite(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FILACEITE + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarFilgasolina(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FILGASOLINA + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarFilaire(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FILAIRE + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarLuces(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_LUCES + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarLimpiaparabrisas(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_LIMPIAPARABRISAS + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarLiqfrenos(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_LIQFRENOS + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarFrenos(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_FRENOS + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarRevgen(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_REVGEN + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarRuedas(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_RUEDAS + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarItv(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_ITV + " = " + activo;
        db.execSQL(sql);
    }
    public void actualizarCorrea(int activo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_CORREA + " = " + activo;
        db.execSQL(sql);
    }

}
