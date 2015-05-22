package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carlog.gilberto.carlog.AddLog;
import com.carlog.gilberto.carlog.MyActivity;
import com.carlog.gilberto.carlog.TipoCoche;
import com.carlog.gilberto.carlog.TipoLog;

/**
 * Created by Gilberto on 19/05/2015.
 */
public class DBCar {
    public static final String TABLE_NAME = "cars";

    public static final String CN_MATRICULA = "matricula";
    public static final String CN_MARCA = "marca";
    public static final String CN_MODELO = "modelo";
    public static final String CN_YEAR = "year";
    public static final String CN_KMS = "kms";
    public static final String CN_ITV = "itv";
    public static final String CN_FECHA_INI = "fecha_ini";
    public static final String CN_KMS_INI = "kms_ini";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_MATRICULA + " text primary key,"
            + CN_MARCA + " text not null,"
            + CN_MODELO + " text not null,"
            + CN_YEAR + " integer,"
            + CN_KMS + " integer,"
            + CN_ITV + " text,"
            + CN_FECHA_INI + " text not null,"
            + CN_KMS_INI + " integer not null);";


    private DbHelper helper;
    private static SQLiteDatabase db;

    public DBCar(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(TipoCoche miCoche) {
        ContentValues valores = new ContentValues();
        valores.put(CN_MATRICULA, miCoche.getMatricula(miCoche));
        valores.put(CN_MARCA, miCoche.getMarca(miCoche));
        valores.put(CN_MODELO, miCoche.getModelo(miCoche));
        if (miCoche.getYear(miCoche) != MyActivity.NO_YEARS)
            valores.put(CN_YEAR, miCoche.getYear(miCoche));
        if (miCoche.getKms(miCoche) != MyActivity.NO_KMS)
            valores.put(CN_KMS, miCoche.getKms(miCoche));
            valores.put(CN_ITV, miCoche.getItv(miCoche));
        valores.put(CN_FECHA_INI, miCoche.getFechaIni(miCoche));
        valores.put(CN_KMS_INI, miCoche.getKmsIni(miCoche));
        return valores;
    }

    public void insertar(TipoCoche coche) {
        db.insert(TABLE_NAME, null, generarContentValues(coche));
    }


    public static void insertinsertOrUpdate(TipoCoche coche) {
        String sql = "INSERT OR REPLACE INTO "+ TABLE_NAME +" (" +CN_MATRICULA+ ", " +CN_MARCA+ ", " +CN_MODELO+ ", " +CN_YEAR+ ", " +CN_KMS+ ", " +CN_ITV+ ", " +CN_FECHA_INI+ ", " +CN_KMS_INI + ") "
        +" VALUES ('" +coche.getMatricula(coche)+ "', '" +coche.getMarca(coche)+ "', '" + coche.getModelo(coche)+ "', '"
        + coche.getYear(coche)+ "', '" + coche.getKms(coche)+ "', '" + coche.getItv(coche)+ "', '" +coche.getFechaIni(coche)+ "', '" + coche.getKmsIni(coche)+ "');";
        db.execSQL(sql);
    }



    public Cursor buscarCoches() {
        String[] columnas = new String[]{CN_MATRICULA, CN_MARCA, CN_MODELO, CN_YEAR, CN_KMS, CN_ITV, CN_FECHA_INI, CN_KMS_INI};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }



  /*  public static Cursor buscarCoches() {
        String sql = "SELECT _id, matricula, marca, modelo, year, kms, itv FROM "+TABLE_NAME;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }*/


    public Cursor buscarCoche(String matricula) {
        String[] columnas = new String[]{CN_MATRICULA, CN_MARCA, CN_MODELO, CN_YEAR, CN_KMS, CN_ITV, CN_FECHA_INI, CN_KMS_INI};
        return db.query(TABLE_NAME, columnas, CN_MATRICULA + "=?", new String[]{matricula}, null, null, null);

    }


}
