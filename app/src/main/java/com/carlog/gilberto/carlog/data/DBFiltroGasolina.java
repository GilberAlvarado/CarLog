package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Gilberto on 30/07/2015.
 */
public class dbFiltroGasolina {
    public static final String TABLE_NAME = "fgasolina";

    public final static String TIPO_30K_KM = "30.000 kms (recomendada)";
    public final static String TIPO_40K_KM = "40.000 kms";
    public final static String TIPO_50K_KM = "50.000 kms";
    public final static String TIPO_60K_KM = "60.000 kms";
    public final static String TIPO_80K_KM = "80.000 kms";

    public static final String CN_ID = "_id";
    public static final String CN_TIPO = "tipo";
    public static final String CN_KMS = "kms";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null, "
            + CN_KMS + " int not null);";


    private dbHelper helper;
    private static SQLiteDatabase db;

    public dbFiltroGasolina(Context context) {
        helper = new dbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(String mytipoFiltroGasolina, int num_kms) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, mytipoFiltroGasolina);
        valores.put(CN_KMS, num_kms);
        return valores;
    }

    public void insertar(String mytipoFiltroGasolina, int num_kms) {
        db.insert(TABLE_NAME, null, generarContentValues(mytipoFiltroGasolina, num_kms));
    }


    public Cursor buscarTiposFiltroGasolina() {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }



    public static Cursor buscarFiltroGasolina(String mytipoFiltroGasolina) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{mytipoFiltroGasolina}, null, null, null);

    }

    public static Cursor buscarTiposFiltroGasolina(int id) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_ID + "=?", new String[]{Integer.toString(id)}, null, null, null);

    }
}