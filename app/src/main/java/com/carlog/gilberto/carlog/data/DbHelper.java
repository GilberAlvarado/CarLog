package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Spinner;

import com.carlog.gilberto.carlog.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "carlog.sqlite";
    private static final int DB_SCHEME_VERSION = 3;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //sqLiteDatabase.execSQL(DBLogs.CREATE_TABLE);
        sqLiteDatabase.execSQL(DBTiposRevision.CREATE_TABLE);



        /// Inicializamos la tabla de tipos de revisión
        String[] lista = {"Revisión general", "Cambio de aceite", "Cambio de filtros"};

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

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBTiposRevision.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
