package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carlog.gilberto.carlog.tiposClases.TipoCoche;

/**
 * Created by Gilberto on 02/06/2015.
 */
public class DBModelos {
    public static final String TABLE_NAME = "modelos";

    public static final String CN_ID = "_id";
    public static final String CN_MODELO = "modelo";
    public static final String CN_IMG = "img";
    public static final String CN_MARCA = "marca";


    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_MODELO + " text not null,"
            + CN_IMG + " text not null,"
            + CN_MARCA + " integer not null,"
            + " FOREIGN KEY("+CN_MARCA+") REFERENCES "+ DBMarcas.TABLE_NAME+"("+DBMarcas.CN_ID+"));";


    private DbHelper helper;
    private static SQLiteDatabase db;

    public DBModelos(Context context) {
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();


    }


    public ContentValues generarContentValues(String modelo, String img, String marca) {
        ContentValues valores = new ContentValues();
        valores.put(CN_MODELO, modelo);
        valores.put(CN_IMG, img);
        valores.put(CN_MARCA, marca);
        return valores;
    }

    public void insertar(String modelo, String img, String marca) {
        db.insert(TABLE_NAME, null, generarContentValues(modelo, img, marca));
    }


    public Cursor buscarModelos() {
        String[] columnas = new String[]{CN_ID, CN_MODELO, CN_IMG, CN_MARCA};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }

    public Cursor buscarModelosDeMarca(int marca) {
        String[] columnas = new String[]{CN_ID, CN_MARCA, CN_MODELO, CN_IMG};
        return db.query(TABLE_NAME, columnas, CN_MARCA + "=?", new String[]{Integer.toString(marca)}, null, null, null);

    }

    public Cursor buscarModelos(String modelo) {
        String[] columnas = new String[]{CN_ID, CN_MARCA, CN_MODELO, CN_IMG};
        return db.query(TABLE_NAME, columnas, CN_MODELO + "=?", new String[]{modelo}, null, null, null);

    }



}
