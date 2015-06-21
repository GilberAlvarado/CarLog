package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlog.gilberto.carlog.tiposClases.TipoLog;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "carlog.sqlite";
    private static final int DB_SCHEME_VERSION = 47;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     //   sqLiteDatabase.execSQL(DBCar.CREATE_TABLE);
     //   sqLiteDatabase.execSQL(DBLogs.CREATE_TABLE);
        sqLiteDatabase.execSQL(DBMarcas.CREATE_TABLE);
        sqLiteDatabase.execSQL(DBModelos.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(DBTiposRevision.CREATE_TABLE);
     //   sqLiteDatabase.execSQL(DBAceite.CREATE_TABLE);


        /// Inicializamos la tabla de modelos
        String[] lista_modelos = {"Modelo", "TT", "Z3"};
        String[] lista_img_modelos = {"modelo_inicio", "modelo_audi_tt", "modelo_bmw_z4"};
        int[] lista_marca_modelos = {0, 8, 14};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_modelos.length; i++) {
                values.put(DBModelos.CN_ID, i); // el 0 es para el inicio
                values.put(DBModelos.CN_MODELO, lista_modelos[i]);
                values.put(DBModelos.CN_IMG, lista_img_modelos[i]);
                values.put(DBModelos.CN_MARCA, lista_marca_modelos[i]);
                sqLiteDatabase.insert(DBModelos.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }


        /// Inicializamos la tabla de marcas
        String[] lista_marcas = {"Marca", "Abarth", "Alfa Romeo", "Alpina", "Alpine", "ARO", "Asia", "Aston Martin", "Audi", "Austin", "Autobianchi",
                "Bedford", "Bentley", "Bertone", "BMW", "Buick", "Cadillac", "Caterham", "Chevrolet", "Chrysler", "Citroën", "Dacia", "Daewoo", "DAF",
                "Daihatsu", "Daimler", "Dodge", "F.S.O. / Polski", "Ferrari", "Fiat", "Ford", "Ford USA", "Galloper", "Honda",
                "Hyundai", "Infiniti", "Innocenti", "Isuzu", "Iveco", "Jaguar", "Jeep", "KIA", "Lada", "Lamborghini", "Lancia", "Land Rover", "LDV",
                "Lexus", "Leyland", "Ligier", "Lotus", "LTI", "Mahindra", "Maruti", "Maserati", "Maybach", "Mazda", "Mercedes-Benz", "MG", "Mini",
                "Mitsubishi", "Morgan", "Morris", "Nissan", "Opel", "Peugeot", "Piaggio", "Pontiac", "Porsche", "Proton",
                "Renault", "Rolls-Royce", "Rover", "SAAB", "Seat", "Skoda", "Smart", "Ssangyong", "Subaru", "Suzuki", "Talbot", "TATA",
                "Tesla", "Toyota", "Triumph", "TVR", "Vauxhall", "Volkswagen", "Volvo", "Wartburg", "Yugo/Zastava", "ZAZ TAVRIA"};

        String[] lista_img_marcas = {"logo_inicio", "logo_abarth", "logo_alfaromeo", "logo_alpina", "logo_alpine", "logo_aro", "logo_asia", "logo_astonmartin",
                "logo_audi", "logo_austin", "logo_autobianchi", "logo_bedford", "logo_bentley", "logo_bertone", "logo_bmw", "logo_buick", "logo_cadillac",
                "logo_caterham", "logo_chebrolet", "logo_chrysler", "logo_citroen", "logo_dacia", "logo_daewoo", "logo_daf", "logo_daihatsu", "logo_daimler",
                "logo_dodge", "logo_fso", "logo_ferrari", "logo_fiat", "logo_ford", "logo_fordusa", "logo_galloper", "logo_honda", "logo_hyunday", "logo_infiniti",
                "logo_innocenti", "logo_isuzu", "logo_iveco", "logo_iaguar", "logo_jeep", "logo_kia", "logo_lada", "logo_lamborghini", "logo_lancia",
                "logo_landrover", "logo_ldv", "logo_lexus", "logo_leyland", "logo_ligier", "logo_lotus", "logo_lt1", "logo_mahindra", "logo_maruti", "logo_maserati",
                "logo_maybach", "logo_mazda", "logo_mercedes", "logo_mg", "logo_mini", "logo_mitsubishi", "logo_morgan", "logo_morris", "logo_nissan", "logo_opel",
                "logo_peugeot", "logo_piaggio", "logo_pontiac", "logo_porsche", "logo_proton", "logo_renault", "logo_rollsroyce", "logo_rover", "logo_saab",
                "logo_seat", "logo_skoda", "logo_smart", "logo_ssangyongg", "logo_subaru", "logo_suzuky", "logo_talbot", "logo_tata", "logo_tesla",
                "logo_toyota", "logo_triumph", "logo_tvr", "logo_vauxhall", "logo_volkswagen", "logo_volvo", "logo_wartburg", "logo_yugo", "logo_zaz"};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_marcas.length; i++) {
                values.put(DBMarcas.CN_ID, i); // el 0 es para el inicio
                values.put(DBMarcas.CN_MARCA, lista_marcas[i]);
                values.put(DBMarcas.CN_IMG, lista_img_marcas[i]);
                sqLiteDatabase.insert(DBMarcas.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        /*


        /// Inicializamos la tabla de tipos de revisión
        String[] lista_tipo = {TipoLog.TIPO_REV_GENERAL, TipoLog.TIPO_ACEITE, TipoLog.TIPO_FILTRO_AIRE, TipoLog.TIPO_FILTRO_ACEITE, TipoLog.TIPO_FILTRO_GASOLINA};
        String[] lista_img = {"logo_audi", "ic_aceite", "ic_coche", "ic_coche", "ic_coche"};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_tipo.length; i++) {
                values.put(DBTiposRevision.CN_TIPO, lista_tipo[i]);
                values.put(DBTiposRevision.CN_IMG, lista_img[i]);
                sqLiteDatabase.insert(DBTiposRevision.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }*/

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
      //  sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBLogs.TABLE_NAME);
       // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBCar.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBMarcas.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBModelos.TABLE_NAME);
      //  sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBTiposRevision.TABLE_NAME);
      //  sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBAceite.TABLE_NAME);
        onCreate(sqLiteDatabase);
        //sqLiteDatabase.execSQL(DBLogs.CREATE_TABLE);
    }
}
