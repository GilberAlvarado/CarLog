package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Gilberto on 30/07/2015.
 */
public class DBFiltroAire {
    public static final String TABLE_NAME = "faire";

    public final static String TIPO_30K_KM = "30 mil kms (recomendada)";
    public final static String TIPO_40K_KM = "40 mil kms";
    public final static String TIPO_50K_KM = "50 mil kms";
    public final static String TIPO_60K_KM = "60 mil kms";
    public final static String TIPO_80K_KM = "80 mil kms";

    public static final String CN_ID = "_id";
    public static final String CN_TIPO = "tipo";
    public static final String CN_KMS = "kms";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null, "
            + CN_KMS + " int not null);";


    private DbHelper helper;
    private static SQLiteDatabase db;

    public DBFiltroAire(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(String mytipoFiltroAire, int num_kms) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, mytipoFiltroAire);
        valores.put(CN_KMS, num_kms);
        return valores;
    }

    public void insertar(String mytipoFiltroAire, int num_kms) {
        db.insert(TABLE_NAME, null, generarContentValues(mytipoFiltroAire, num_kms));
    }


    public Cursor buscarTiposFiltroAire() {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }



    public static Cursor buscarFiltroAire(String mytipoFiltroAire) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{mytipoFiltroAire}, null, null, null);

    }

    public static Cursor buscarTiposFiltroAire(int id) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_ID + "=?", new String[]{Integer.toString(id)}, null, null, null);
    }
}
