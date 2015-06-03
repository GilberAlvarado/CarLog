package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Spinner;

import com.carlog.gilberto.carlog.Aceite;
import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.TipoLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "carlog.sqlite";
    private static final int DB_SCHEME_VERSION = 36;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
      //  sqLiteDatabase.execSQL(DBCar.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(DBLogs.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(DBMarcas.CREATE_TABLE);
        sqLiteDatabase.execSQL(DBModelos.CREATE_TABLE);
     //   sqLiteDatabase.execSQL(DBTiposRevision.CREATE_TABLE);
     //   sqLiteDatabase.execSQL(DBAceite.CREATE_TABLE);


        /// Inicializamos la tabla de modelos
        String[] lista_modelos = {"TT Quattro 225", "Z3"};
        String[] lista_img_modelos = {"modelo_audi_tt", "modelo_bmw_z4"};
        int[] lista_marca_modelos = {1, 2};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_modelos.length; i++) {
                values.put(DBModelos.CN_ID, i+1);
                values.put(DBModelos.CN_MODELO, lista_modelos[i]);
                values.put(DBModelos.CN_IMG, lista_img_modelos[i]);
                values.put(DBModelos.CN_MARCA, lista_marca_modelos[i]);
                sqLiteDatabase.insert(DBModelos.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }


      /*  /// Inicializamos la tabla de marcas
        String[] lista_marcas = {"Audi", "BMW"};
        String[] lista_img_marcas = {"logo_audi", "logo_bmw"};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_marcas.length; i++) {
                values.put(DBMarcas.CN_ID, i+1);
                values.put(DBMarcas.CN_MARCA, lista_marcas[i]);
                values.put(DBMarcas.CN_IMG, lista_img_marcas[i]);
                sqLiteDatabase.insert(DBMarcas.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }*/
/*

        /// Inicializamos la tabla de tipos de revisión
        String[] lista = {TipoLog.TIPO_REV_GENERAL, TipoLog.TIPO_ACEITE, TipoLog.TIPO_FILTRO_AIRE, TipoLog.TIPO_FILTRO_ACEITE, TipoLog.TIPO_FILTRO_GASOLINA};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista.length; i++) {
                values.put(DBTiposRevision.CN_ID, i+1);
                values.put(DBTiposRevision.CN_TIPO, lista[i]);
                sqLiteDatabase.insert(DBTiposRevision.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        */
/*
        /// Inicializamos la tabla de tipos de aceite
        String[] lista_aceite = {Aceite.TIPO_10K_KM, Aceite.TIPO_7M_KM, Aceite.TIPO_5K_KM};
        int[] lista_kms =  {10000, 7000, 5000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_aceite.length; i++) {
                values.put(DBAceite.CN_ID, i+1);
                values.put(DBAceite.CN_TIPO, lista_aceite[i]);
                values.put(DBAceite.CN_KMS, lista_kms[i]);
                sqLiteDatabase.insert(DBAceite.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBLogs.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBCar.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBMarcas.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBModelos.TABLE_NAME);
        onCreate(sqLiteDatabase);
        //sqLiteDatabase.execSQL(DBLogs.CREATE_TABLE);
    }
}
