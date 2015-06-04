package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carlog.gilberto.carlog.MyActivity;
import com.carlog.gilberto.carlog.TipoCoche;

/**
 * Created by Gilberto on 02/06/2015.
 */
public class DBMarcas {
    public static final String TABLE_NAME = "marcas";

    public static final String CN_ID = "_id";
    public static final String CN_MARCA = "marca";
    public static final String CN_IMG = "img";


    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_MARCA + " text not null,"
            + CN_IMG + " text not null);";


    private DbHelper helper;
    private static SQLiteDatabase db;

    public DBMarcas(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(String marca, String img) {
        ContentValues valores = new ContentValues();
        valores.put(CN_MARCA, marca);
        valores.put(CN_IMG, img);
        return valores;
    }

    public void insertar(String marca, String img) {
        db.insert(TABLE_NAME, null, generarContentValues(marca, img));
    }




    public Cursor buscarMarcas() {
        String[] columnas = new String[]{CN_ID, CN_MARCA, CN_IMG};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }


    public Cursor buscarMarcas(String marca) {
        String[] columnas = new String[]{CN_ID, CN_MARCA, CN_IMG};
        return db.query(TABLE_NAME, columnas, CN_MARCA + "=?", new String[]{marca}, null, null, null);

    }


}