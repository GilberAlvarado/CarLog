package com.carlog.gilberto.carlog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carlog.gilberto.carlog.activity.myActivity;
import com.carlog.gilberto.carlog.tiposClases.tipoCoche;

/**
 * Created by Gilberto on 19/05/2015.
 */
public class dbCar {
    public static final String TABLE_NAME = "cars";
    public static final int IMG_MODELO_CAMBIADA = 1;
    public static final int IMG_MODELO_NOCAMBIADA = 0;
    public static final int IMG_MODELO_REDIS_CAMBIADA = -1;

    public static final String CN_MATRICULA = "matricula";
    public static final String CN_MARCA = "marca";
    public static final String CN_MODELO = "modelo";
    public static final String CN_IMG_MODELO_PERSONALIZADA = "img_modelo_pers";
    public static final String CN_IMG_MODELO_CAMBIADA = "img_modelo"; // desde q el usuario la cambie se leera del movil no de drawable
    public static final String CN_YEAR = "year";
    public static final String CN_KMS = "kms";
    public static final String CN_ITV = "itv";
    public static final String CN_PROFILE = "profile"; // coche activo
    public static final String CN_FECHA_INI = "fecha_ini";
    public static final String CN_KMS_INI = "kms_ini";

    public static final String CREATE_TABLE = "create table " +TABLE_NAME + " ("
            + CN_MATRICULA + " text primary key,"
            + CN_MARCA + " text not null,"
            + CN_MODELO + " text not null,"
            + CN_IMG_MODELO_PERSONALIZADA + " text,"
            + CN_IMG_MODELO_CAMBIADA + " integer not null,"
            + CN_YEAR + " integer,"
            + CN_KMS + " integer,"
            + CN_ITV + " text,"
            + CN_PROFILE + " integer not null,"
            + CN_FECHA_INI + " text not null,"
            + CN_KMS_INI + " integer not null);";


    private dbHelper helper;
    private static SQLiteDatabase db;

    public dbCar(Context context) {
        helper = new dbHelper(context);
        db = helper.getWritableDatabase();
    }


    public ContentValues generarContentValues(tipoCoche miCoche) {
        ContentValues valores = new ContentValues();
        valores.put(CN_MATRICULA, miCoche.getMatricula(miCoche));
        valores.put(CN_MARCA, miCoche.getMarca(miCoche));
        valores.put(CN_MODELO, miCoche.getModelo(miCoche));
        valores.put(CN_IMG_MODELO_PERSONALIZADA, miCoche.getImgModeloPersonalizada(miCoche));
        valores.put(CN_IMG_MODELO_CAMBIADA, miCoche.getImgModeloCambiada(miCoche));
        if (miCoche.getYear(miCoche) != myActivity.NO_YEARS)
            valores.put(CN_YEAR, miCoche.getYear(miCoche));
        if (miCoche.getKms(miCoche) != myActivity.NO_KMS)
            valores.put(CN_KMS, miCoche.getKms(miCoche));
        valores.put(CN_ITV, miCoche.getItv(miCoche));
        valores.put(CN_PROFILE, miCoche.getProfile(miCoche));
        valores.put(CN_FECHA_INI, miCoche.getFechaIni(miCoche));
        valores.put(CN_KMS_INI, miCoche.getKmsIni(miCoche));
        return valores;
    }

    public void insertar(tipoCoche coche) {
        db.insert(TABLE_NAME, null, generarContentValues(coche));
    }


    public static void insertinsertOrUpdate(tipoCoche coche) {
        System.out.println("modificando kms " + coche.getKms(coche));
        String sql = "INSERT OR REPLACE INTO "+ TABLE_NAME +" (" +CN_MATRICULA+ ", " +CN_MARCA+ ", " +CN_MODELO+ ", " +CN_IMG_MODELO_PERSONALIZADA+ ", " +CN_IMG_MODELO_CAMBIADA+ ", " +CN_YEAR+ ", " +CN_KMS+ ", " +CN_ITV+ ", " +CN_PROFILE+ ", " +CN_FECHA_INI+ ", " +CN_KMS_INI + ") "
        +" VALUES ('" +coche.getMatricula(coche)+ "', '" +coche.getMarca(coche)+ "', '" + coche.getModelo(coche)+ "', '" + coche.getImgModeloPersonalizada(coche)+ "', '" + coche.getImgModeloCambiada(coche)+ "', '"
        + coche.getYear(coche)+ "', '" + coche.getKms(coche)+ "', '" + coche.getItv(coche)+ "', '" + coche.getProfile(coche)+ "', '" +coche.getFechaIni(coche)+ "', '" + coche.getKmsIni(coche)+ "');";
        db.execSQL(sql);
    }



    public Cursor buscarCoches() {
        String[] columnas = new String[]{CN_MATRICULA, CN_MARCA, CN_MODELO, CN_IMG_MODELO_PERSONALIZADA, CN_IMG_MODELO_CAMBIADA, CN_YEAR, CN_KMS, CN_ITV, CN_PROFILE, CN_FECHA_INI, CN_KMS_INI};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);

    }

    public Cursor buscarCocheActivo() {
        String[] columnas = new String[]{CN_MATRICULA, CN_MARCA, CN_MODELO, CN_IMG_MODELO_PERSONALIZADA, CN_IMG_MODELO_CAMBIADA, CN_YEAR, CN_KMS, CN_ITV, CN_PROFILE, CN_FECHA_INI, CN_KMS_INI};
        return db.query(TABLE_NAME, columnas, CN_PROFILE + "=?", new String[]{Integer.toString(tipoCoche.PROFILE_ACTIVO)}, null, null, null);

    }
    public Cursor buscarCochesNoActivos() {
        String[] columnas = new String[]{CN_MATRICULA, CN_MARCA, CN_MODELO, CN_IMG_MODELO_PERSONALIZADA, CN_IMG_MODELO_CAMBIADA, CN_YEAR, CN_KMS, CN_ITV, CN_PROFILE, CN_FECHA_INI, CN_KMS_INI};
        return db.query(TABLE_NAME, columnas, CN_PROFILE + "=?", new String[]{Integer.toString(tipoCoche.PROFILE_INACTIVO)}, null, null, null);

    }

    public void ActualizarITVCocheActivo(String matricula, long itv) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_ITV + " = '" + itv + "' WHERE " + CN_PROFILE + " = '" + tipoCoche.PROFILE_ACTIVO + "' AND " +CN_MATRICULA + " = '" + matricula + "'";
        db.execSQL(sql);
    }

    public void ActualizarCocheActivo(String matricula) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_PROFILE + " = '" + tipoCoche.PROFILE_ACTIVO + "' WHERE " + CN_MATRICULA + " = '" + matricula + "'";
        db.execSQL(sql);
    }

    public void ActualizarCocheNOActivo(String matricula) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_PROFILE + " = '" + tipoCoche.PROFILE_INACTIVO + "' WHERE " + CN_MATRICULA + " = '" + matricula + "'";
        db.execSQL(sql);
    }

    public void ActualizarTodosCocheNOActivo() {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_PROFILE + " = '" + tipoCoche.PROFILE_INACTIVO + "'";
        db.execSQL(sql);
    }

    public void ActualizarImgModelo(String matricula, String img) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_IMG_MODELO_CAMBIADA + " = " + IMG_MODELO_CAMBIADA + ", " + CN_IMG_MODELO_PERSONALIZADA + " = '" + img + "' WHERE " + CN_MATRICULA + " = '" + matricula + "'";
        db.execSQL(sql);
    }

    public void ActualizarCambiadaModelo(String matricula) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + CN_IMG_MODELO_CAMBIADA + " = " + IMG_MODELO_NOCAMBIADA + " WHERE " + CN_MATRICULA + " = '" + matricula + "'";
        db.execSQL(sql);
    }

    public Cursor buscarCoche(String matricula) {
        String[] columnas = new String[]{CN_MATRICULA, CN_MARCA, CN_MODELO, CN_IMG_MODELO_PERSONALIZADA, CN_IMG_MODELO_CAMBIADA, CN_YEAR, CN_KMS, CN_ITV, CN_PROFILE, CN_FECHA_INI, CN_KMS_INI};
        return db.query(TABLE_NAME, columnas, CN_MATRICULA + "=?", new String[]{matricula}, null, null, null);

    }

    public void eliminarCoche(String matricula) {
        db.delete(TABLE_NAME, CN_MATRICULA + "=?", new String[]{matricula});
    }


}
