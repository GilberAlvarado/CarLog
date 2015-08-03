package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gilberto on 16/05/2015.
 */
public class dbHelper extends SQLiteOpenHelper {

    private static final String db_NAME = "carlog.sqlite";
    private static final int db_SCHEME_VERSION = 84;
    public static final int MAX_TIPOS_REV = 14; // 15 pq cuenta el 0

    public dbHelper(Context context) {
        super(context, db_NAME, null, db_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(dbCar.CREATE_TABLE);
     //   sqLiteDatabase.execSQL(dbLogs.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(dbRevGral.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(dbMarcas.CREATE_TABLE);
        sqLiteDatabase.execSQL(dbModelos.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(dbTiposRevision.CREATE_TABLE);
     //   sqLiteDatabase.execSQL(dbCorrea.CREATE_TABLE);
     //   sqLiteDatabase.execSQL(dbBombaAgua.CREATE_TABLE);
     //   sqLiteDatabase.execSQL(dbFiltroGasolina.CREATE_TABLE);
     //   sqLiteDatabase.execSQL(dbFiltroAire.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(dbBujias.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(dbEmbrague.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(dbAceite.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(dbFiltroAceite.CREATE_TABLE);
      //  sqLiteDatabase.execSQL(dbLogin.SQLCreateLogin);


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
                values.put(dbModelos.CN_ID, i); // el 0 es para el inicio
                values.put(dbModelos.CN_MODELO, lista_modelos[i]);
                values.put(dbModelos.CN_IMG, lista_img_modelos[i]);
                values.put(dbModelos.CN_MARCA, lista_marca_modelos[i]);
                sqLiteDatabase.insert(dbModelos.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

/*
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
                values.put(dbMarcas.CN_ID, i); // el 0 es para el inicio
                values.put(dbMarcas.CN_MARCA, lista_marcas[i]);
                values.put(dbMarcas.CN_IMG, lista_img_marcas[i]);
                sqLiteDatabase.insert(dbMarcas.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }


*/

        /// Inicializamos la tabla de tipos de revisión
        ////******************SI AÑADIMOS NUEVOS TIPOS O QUITAMOS DEBEMOS ACTUALIZAR LA CONSTANTE MAX_TIPOS_REV  para poder borrar los personalizados*************************//////
  /*      String[] lista_tipo = {TipoLog.TIPO_ACEITE, TipoLog.TIPO_BOMBA_AGUA, TipoLog.TIPO_BUJIAS, TipoLog.TIPO_CORREA, TipoLog.TIPO_EMBRAGUE,
                TipoLog.TIPO_FILTRO_ACEITE, TipoLog.TIPO_FILTRO_AIRE,
                TipoLog.TIPO_FILTRO_GASOLINA, TipoLog.TIPO_FRENOS, TipoLog.TIPO_ITV, TipoLog.TIPO_LIMPIAPARABRISAS, TipoLog.TIPO_LIQUIDO_FRENOS,
                TipoLog.TIPO_LUCES, TipoLog.TIPO_REV_GENERAL, TipoLog.TIPO_RUEDAS};
        String[] lista_img = {"ic_aceite", "ic_bomba_agua", "ic_bujias", "ic_correa", "ic_embrague", "ic_fil_aceite", "ic_fil_aire", "ic_fil_gasolina", "ic_frenos", "ic_itv_rev",
                "ic_limpiaparabrisas", "ic_liquido_frenos", "ic_luces", "ic_revgen", "ic_ruedas"};
        ////******************SI AÑADIMOS NUEVOS TIPOS O QUITAMOS DEBEMOS ACTUALIZAR LA CONSTANTE MAX_TIPOS_REV para poder borrar los personalizados *************************//////
/*
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_tipo.length; i++) {
                values.put(dbTiposRevision.CN_TIPO, lista_tipo[i]);
                values.put(dbTiposRevision.CN_IMG, lista_img[i]);
                sqLiteDatabase.insert(dbTiposRevision.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
*/


 /*       /// Inicializamos la tabla de tipos de revision general
        String[] lista_revgral = {RevGral.TIPO_30K_KM, RevGral.TIPO_5K_KM, RevGral.TIPO_10K_KM, RevGral.TIPO_15K_KM, RevGral.TIPO_20K_KM, RevGral.TIPO_25K_KM,
                RevGral.TIPO_35K_KM, RevGral.TIPO_40K_KM, RevGral.TIPO_45K_KM, RevGral.TIPO_50K_KM, RevGral.TIPO_60K_KM, RevGral.TIPO_80K_KM, RevGral.TIPO_100K_KM, RevGral.TIPO_120K_KM};
        int[] lista_kms =  {30000, 5000, 10000, 15000, 20000, 25000, 35000, 40000, 45000, 50000, 60000, 80000, 100000, 120000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_revgral.length; i++) {
                values.put(dbRevGral.CN_ID, i+1);
                values.put(dbRevGral.CN_TIPO, lista_revgral[i]);
                values.put(dbRevGral.CN_KMS, lista_kms[i]);
                sqLiteDatabase.insert(dbRevGral.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
*/

 /*       /// Inicializamos la tabla de tipos de revision general
        String[] lista_correa = {dbCorrea.TIPO_60K_KM, dbCorrea.TIPO_80K_KM, dbCorrea.TIPO_100K_KM, dbCorrea.TIPO_120K_KM};
        int[] lista_kms_correa =  {60000, 80000, 100000, 120000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_correa.length; i++) {
                values.put(dbCorrea.CN_ID, i+1);
                values.put(dbCorrea.CN_TIPO, lista_correa[i]);
                values.put(dbCorrea.CN_KMS, lista_kms_correa[i]);
                sqLiteDatabase.insert(dbCorrea.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        /// Inicializamos la tabla de tipos de bomba de agua
        String[] lista_bombaagua = {dbBombaAgua.TIPO_80K_KM, dbBombaAgua.TIPO_60K_KM, dbBombaAgua.TIPO_100K_KM, dbBombaAgua.TIPO_120K_KM};
        int[] lista_kms_bombaagua =  {80000, 60000, 100000, 120000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_bombaagua.length; i++) {
                values.put(dbBombaAgua.CN_ID, i+1);
                values.put(dbBombaAgua.CN_TIPO, lista_bombaagua[i]);
                values.put(dbBombaAgua.CN_KMS, lista_kms_bombaagua[i]);
                sqLiteDatabase.insert(dbBombaAgua.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }


        /// Inicializamos la tabla de tipos de filtro de gasolina
        String[] lista_fgasolina = {dbFiltroGasolina.TIPO_30K_KM, dbFiltroGasolina.TIPO_40K_KM, dbFiltroGasolina.TIPO_50K_KM, dbFiltroGasolina.TIPO_60K_KM, dbFiltroGasolina.TIPO_80K_KM};
        int[] lista_kms_fgasolina =  {30000, 40000, 500000, 60000, 80000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_fgasolina.length; i++) {
                values.put(dbFiltroGasolina.CN_ID, i+1);
                values.put(dbFiltroGasolina.CN_TIPO, lista_fgasolina[i]);
                values.put(dbFiltroGasolina.CN_KMS, lista_kms_fgasolina[i]);
                sqLiteDatabase.insert(dbFiltroGasolina.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        /// Inicializamos la tabla de tipos de filtro de aire
        String[] lista_faire = {dbFiltroAire.TIPO_30K_KM, dbFiltroAire.TIPO_40K_KM, dbFiltroAire.TIPO_50K_KM, dbFiltroAire.TIPO_60K_KM, dbFiltroAire.TIPO_80K_KM};
        int[] lista_kms_faire =  {30000, 40000, 50000, 60000, 80000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_faire.length; i++) {
                values.put(dbFiltroAire.CN_ID, i+1);
                values.put(dbFiltroAire.CN_TIPO, lista_faire[i]);
                values.put(dbFiltroAire.CN_KMS, lista_kms_faire[i]);
                sqLiteDatabase.insert(dbFiltroAire.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        /// Inicializamos la tabla de tipos de filtro de bujjias
        String[] lista_bujias = {dbBujias.TIPO_60K_KM, dbBujias.TIPO_50K_KM, dbBujias.TIPO_55K_KM, dbBujias.TIPO_65K_KM, dbBujias.TIPO_80K_KM};
        int[] lista_kms_bujias =  {60000, 50000, 55000, 65000, 80000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_bujias.length; i++) {
                values.put(dbBujias.CN_ID, i+1);
                values.put(dbBujias.CN_TIPO, lista_bujias[i]);
                values.put(dbBujias.CN_KMS, lista_kms_bujias[i]);
                sqLiteDatabase.insert(dbBujias.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        /// Inicializamos la tabla de tipos de filtro de embrague
        String[] lista_embrague = {dbEmbrague.TIPO_60K_KM, dbEmbrague.TIPO_70K_KM, dbEmbrague.TIPO_80K_KM, dbEmbrague.TIPO_100K_KM, dbEmbrague.TIPO_120K_KM};
        int[] lista_kms_embrague =  {30000, 40000, 500000, 60000, 80000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_embrague.length; i++) {
                values.put(dbEmbrague.CN_ID, i+1);
                values.put(dbEmbrague.CN_TIPO, lista_embrague[i]);
                values.put(dbEmbrague.CN_KMS, lista_kms_embrague[i]);
                sqLiteDatabase.insert(dbEmbrague.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
*/

        /// Inicializamos la tabla de tipos de aceite
  /*      String[] lista_aceite = {Aceite.TIPO_7K5_KM, Aceite.TIPO_10M_KM, Aceite.TIPO_15K_KM, Aceite.TIPO_20K_KM, Aceite.TIPO_30K_KM};
        int[] lista_kms2 =  {7500, 10000, 15000, 20000, 30000};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_aceite.length; i++) {
                values.put(dbAceite.CN_ID, i+1);
                values.put(dbAceite.CN_TIPO, lista_aceite[i]);
                values.put(dbAceite.CN_KMS, lista_kms2[i]);
                sqLiteDatabase.insert(dbAceite.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        */

/*
        /// Inicializamos la tabla de tipos de filtro de aceite
        String[] lista_fil_aceite = {FiltroAceite.TIPO_1, FiltroAceite.TIPO_2, FiltroAceite.TIPO_3};
        int[] lista_veces =  {1, 2, 3};

        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < lista_fil_aceite.length; i++) {
                values.put(dbFiltroAceite.CN_ID, i+1);
                values.put(dbFiltroAceite.CN_TIPO, lista_fil_aceite[i]);
                values.put(dbFiltroAceite.CN_VECES, lista_veces[i]);
                sqLiteDatabase.insert(dbFiltroAceite.TABLE_NAME, null, values);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbLogs.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbCar.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbMarcas.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbModelos.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbFiltroAceite.TABLE_NAME);
      //  sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTiposRevision.TABLE_NAME);
      //  sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbAceite.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbCorrea.TABLE_NAME);
      //  sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbBombaAgua.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbFiltroGasolina.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbFiltroAire.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbBujias.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbEmbrague.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbRevGral.TABLE_NAME);
     //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbLogin.Table);
        onCreate(sqLiteDatabase);
    }
}
