package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Gilberto on 30/07/2015.
 */
public class DBEmbrague {
    public static final String TABLE_NAME = "embrague";

    public final static String TIPO_60K_KM = "60 mil kms (recomendada)";
    public final static String TIPO_70K_KM = "70 mil kms";
    public final static String TIPO_80K_KM = "80 mil kms";
    public final static String TIPO_100K_KM = "100 mil kms";
    public final static String TIPO_120K_KM = "120 mil kms";

    public static final String CN_ID = "_id";
    public static final String CN_TIPO = "tipo";
    public static final String CN_KMS = "kms";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TIPO + " text not null, "
            + CN_KMS + " int not null);";


    private DbHelper helper;
    private static SQLiteDatabase db;

    public DBEmbrague(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(String mytipoEmbrague, int num_kms) {
        ContentValues valores = new ContentValues();
        valores.put(CN_TIPO, mytipoEmbrague);
        valores.put(CN_KMS, num_kms);
        return valores;
    }

    public void insertar(String mytipoEmbrague, int num_kms) {
        db.insert(TABLE_NAME, null, generarContentValues(mytipoEmbrague, num_kms));
    }


    public Cursor buscarTiposEmbrague() {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }



    public static Cursor buscarTiposEmbrague(String mytipoEmbrague) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_TIPO + "=?", new String[]{mytipoEmbrague}, null, null, null);

    }

    public static Cursor buscarTiposEmbrague(int id) {
        String[] columnas = new String[]{CN_ID, CN_TIPO, CN_KMS};
        return db.query(TABLE_NAME, columnas, CN_ID + "=?", new String[]{Integer.toString(id)}, null, null, null);

    }
}

