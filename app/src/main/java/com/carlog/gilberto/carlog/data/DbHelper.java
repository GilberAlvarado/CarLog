package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlog.gilberto.carlog.activity.Aceite;
import com.carlog.gilberto.carlog.activity.FiltroAceite;
import com.carlog.gilberto.carlog.activity.RevGral;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "carlog.sqlite";
    private static final int DB_SCHEME_VERSION = 71;
    public static final int MAX_TIPOS_REV = 14; // 15 pq cuenta el 0

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     //   sqLiteDatabase.execSQL(DBCar.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(DBLogs.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(DBRevGral.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(DBMarcas.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(DBModelos.CREATE_TABLE);
        sqLiteDatabase.execSQL(DBTiposRevision.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(DBAceite.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(DBFiltroAceite.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(DBLogin.SQLCreateLogin);

/*
        /// Inicializamos la tabla de modelos
        String[] lista_modelos = {"Modelo", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3",
                                  "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3",
                                  "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3",
                                  "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3",
                                  "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3", "TT", "Z3"};

        String[] lista_img_modelos = {"modelo_inicio", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4",
                "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"
                , "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"
                , "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"
                , "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"
                , "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"
                , "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"
                , "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"
                , "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"
                , "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"
                , "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"
                , "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4", "modelo_audi_tt", "modelo_bmw_z4"};

        int[] lista_marca_modelos = {0, 8, 14, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
                                     32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61,
                                     62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92};

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


*/

        /// Inicializamos la tabla de tipos de revisión
        ////******************SI AÑADIMOS NUEVOS TIPOS O QUITAMOS DEBEMOS ACTUALIZAR LA CONSTANTE MAX_TIPOS_REV *************************//////
        String[] lista_tipo = {TipoLog.TIPO_ACEITE, TipoLog.TIPO_BOMBA_AGUA, TipoLog.TIPO_BUJIAS, TipoLog.TIPO_CORREA, TipoLog.TIPO_EMBRAGUE,
                TipoLog.TIPO_FILTRO_ACEITE, TipoLog.TIPO_FILTRO_AIRE,
                TipoLog.TIPO_FILTRO_GASOLINA, TipoLog.TIPO_FRENOS, TipoLog.TIPO_ITV, TipoLog.TIPO_LIMPIAPARABRISAS, TipoLog.TIPO_LIQUIDO_FRENOS,
                TipoLog.TIPO_LUCES, TipoLog.TIPO_REV_GENERAL, TipoLog.TIPO_RUEDAS};
        String[] lista_img = {"ic_aceite", "ic_bomba_agua", "ic_bujias", "ic_correa", "ic_embrague", "ic_fil_aceite", "ic_fil_aire", "ic_fil_gasolina", "ic_frenos", "ic_itv_rev",
                "ic_limpiaparabrisas", "ic_liquido_frenos", "ic_luces", "ic_revgen", "ic_ruedas"};
        ////******************SI AÑADIMOS NUEVOS TIPOS O QUITAMOS DEBEMOS ACTUALIZAR LA CONSTANTE MAX_TIPOS_REV *************************//////

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
        }
/*
        /// Inicializamos la tabla de tipos de revision general
        String[] lista_revgral = {RevGral.TIPO_5K_KM, RevGral.TIPO_10K_KM, RevGral.TIPO_15K_KM, RevGral.TIPO_20K_KM, RevGral.TIPO_25K_KM,
                RevGral.TIPO_30K_KM, RevGral.TIPO_35K_KM, RevGral.TIPO_40K_KM, RevGral.TIPO_45K_KM, RevGral.TIPO_50K_KM};
        int[] lista_kms =  {5000, 10000, 15000, 20000, 25000, 30000, 35000, 40000, 45000, 50000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_revgral.length; i++) {
                values.put(DBRevGral.CN_ID, i+1);
                values.put(DBRevGral.CN_TIPO, lista_revgral[i]);
                values.put(DBRevGral.CN_KMS, lista_kms[i]);
                sqLiteDatabase.insert(DBRevGral.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
*/
        /// Inicializamos la tabla de tipos de aceite
   /*     String[] lista_aceite = {Aceite.TIPO_10K_KM, Aceite.TIPO_7M_KM, Aceite.TIPO_5K_KM};
        int[] lista_kms2 =  {10000, 7000, 5000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_aceite.length; i++) {
                values.put(DBAceite.CN_ID, i+1);
                values.put(DBAceite.CN_TIPO, lista_aceite[i]);
                values.put(DBAceite.CN_KMS, lista_kms2[i]);
                sqLiteDatabase.insert(DBAceite.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }


        /// Inicializamos la tabla de tipos de filtro de aceite
        String[] lista_fil_aceite = {FiltroAceite.TIPO_1, FiltroAceite.TIPO_2, FiltroAceite.TIPO_3};
        int[] lista_veces =  {1, 2, 3};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_fil_aceite.length; i++) {
                values.put(DBFiltroAceite.CN_ID, i+1);
                values.put(DBFiltroAceite.CN_TIPO, lista_fil_aceite[i]);
                values.put(DBFiltroAceite.CN_VECES, lista_veces[i]);
                sqLiteDatabase.insert(DBFiltroAceite.TABLE_NAME, null, values);
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
      //  sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBCar.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBMarcas.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBModelos.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBFiltroAceite.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBTiposRevision.TABLE_NAME);
      //  sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBAceite.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBRevGral.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBLogin.Table);
        onCreate(sqLiteDatabase);
    }
}
