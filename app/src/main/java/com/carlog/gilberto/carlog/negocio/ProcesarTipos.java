package com.carlog.gilberto.carlog.negocio;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.carlog.gilberto.carlog.activity.AddLog;
import com.carlog.gilberto.carlog.activity.MyActivity;
import com.carlog.gilberto.carlog.data.DBAceite;
import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.data.DBRevGral;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;

import java.util.Date;

/**
 * Created by Gilberto on 27/05/2015.
 */
public class ProcesarTipos {

    public final static int F_MAX_REV_ACEITE = 365;
    public final static int F_MAX_REV_REVGRAL = 365;
    public final static int F_MAX_CORREA = 365 * 6;
    public final static int F_MAX_BOMBA_AGUA = 365 * 6;
    public final static int F_MAX_ITV = 365;
    public final static int KMS_CORREA = 100000;
    public final static int KMS_BOMBA_AGUA = 100000;

    public static void procesar(DBLogs dbLogs, int int_now, Context context, int int_kms, int int_fecha_ini, int int_kms_ini, String matricula, String tipo_rev) {
        int dias_coche = (int) funciones.dias_entre_2_fechas(funciones.int_a_date(int_fecha_ini), new Date());
        int int_media = 0;
        if(dias_coche == 0) { // se creó hoy el coche
            int_media = int_kms - int_kms_ini;
        }
        else int_media = int_kms - int_kms_ini / dias_coche;

        Cursor c_historico_tipo = dbLogs.LogsHistoricoTipoOrderByFechaString(int_now, matricula, tipo_rev);
        Cursor c_logs_tipo = dbLogs.LogsTipoOrderByFechaString(int_now, matricula, tipo_rev);


        if (c_historico_tipo.moveToFirst() == true) { // Si existen logs históricos de aceite hay que actualizar la fecha del futuro (pq siempre tiene q existir) log de aceite
            String txt_fecha_h = c_historico_tipo.getString(c_historico_tipo.getColumnIndex("fecha_string")); // recuperamos el último de los logs
            Date fecha_ultimo_log_hist = funciones.string_a_date(txt_fecha_h);
            int kms_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(DBLogs.CN_KMS));
            String txt_matricula_ultimo_log_hist = c_historico_tipo.getString(c_historico_tipo.getColumnIndex(DBLogs.CN_CAR));
            int kms_tipo_ultimo_log_hist = 0;

            int id_aceite_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(DBLogs.CN_ACEITE));
            if(tipo_rev.equals(TipoLog.TIPO_ACEITE)) {
                DBAceite dbaceite = new DBAceite(context);
                Cursor c_aceite = dbaceite.buscarTiposAceite(id_aceite_ultimo_log_hist);

                if (c_aceite.moveToFirst() == true) {
                    kms_tipo_ultimo_log_hist = c_aceite.getInt(c_aceite.getColumnIndex(DBAceite.CN_KMS));
                    System.out.println("KMS " + tipo_rev + " ultimo log hist: " + kms_tipo_ultimo_log_hist);
                    System.out.println(tipo_rev + " ultimo log hist id: " +id_aceite_ultimo_log_hist);
                }
            }
            int id_revgral_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(DBLogs.CN_REVGRAL));
            if(tipo_rev.equals(TipoLog.TIPO_REV_GENERAL)) {
                DBRevGral dbRevGral = new DBRevGral(context);
                Cursor c_revgral = dbRevGral.buscarTiposRevGral(id_revgral_ultimo_log_hist);

                if (c_revgral.moveToFirst() == true) {
                    kms_tipo_ultimo_log_hist = c_revgral.getInt(c_revgral.getColumnIndex(DBRevGral.CN_KMS));
                    System.out.println("KMS " + tipo_rev + " ultimo log hist: " + kms_tipo_ultimo_log_hist);
                    System.out.println(tipo_rev + " ultimo log hist id: " +id_revgral_ultimo_log_hist);
                }
            }
            // correa no tiene id pq no tiene tabla pq el valor de kms o años no va a ser editable
            if(tipo_rev.equals(TipoLog.TIPO_CORREA)) {
                kms_tipo_ultimo_log_hist = KMS_CORREA;
            }
            // bomba de agua no tiene id pq no tiene tabla pq el valor de kms o años no va a ser editable
            if(tipo_rev.equals(TipoLog.TIPO_BOMBA_AGUA)) {
                kms_tipo_ultimo_log_hist = KMS_BOMBA_AGUA;
            }
            // itv no va por kms así que no se hacen comprobaciones aquí

            System.out.println("int_media: " + int_media + " kms_ultimo_log_hist "+kms_ultimo_log_hist);

            int kms_que_faltan_x_hacer = kms_tipo_ultimo_log_hist - (int_kms - kms_ultimo_log_hist);

            Date fecha_log_futuro_recalculada = new Date();
            if(!tipo_rev.equals(TipoLog.TIPO_ITV)) {
                if ((int_kms - kms_ultimo_log_hist) < kms_tipo_ultimo_log_hist) { // Actualizamos la fecha de la futura revisión de aceite
                    int dias_en_hacer_kms_x_hacer = 0;
                    if (int_media != 0)
                        dias_en_hacer_kms_x_hacer = kms_que_faltan_x_hacer / int_media; // regla de 3 si en 1 día hago 5000kms, en cuantos haré X?
                    else dias_en_hacer_kms_x_hacer = kms_que_faltan_x_hacer;

                    if (dias_en_hacer_kms_x_hacer == 0) { // La media es mucho más grande que los kms que quedan o se llega al día que toca
                        fecha_log_futuro_recalculada = funciones.fecha_mas_dias(new Date(), dias_en_hacer_kms_x_hacer + 1); //Mañana y aviso
                        if (tipo_rev.equals(TipoLog.TIPO_ACEITE)) {
                            Toast.makeText(context, "Debería cambiar el aceite cuanto antes.", Toast.LENGTH_LONG).show();
                        }
                        if (tipo_rev.equals(TipoLog.TIPO_REV_GENERAL)) {
                            Toast.makeText(context, "Debería realizar una revisión general cuanto antes.", Toast.LENGTH_LONG).show();
                        }
                        if (tipo_rev.equals(TipoLog.TIPO_CORREA)) {
                            Toast.makeText(context, "Debería cambiar la correa de distribución cuanto antes.", Toast.LENGTH_LONG).show();
                        }
                        if (tipo_rev.equals(TipoLog.TIPO_BOMBA_AGUA)) {
                            Toast.makeText(context, "Debería cambiar la bomba de agua cuanto antes.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        fecha_log_futuro_recalculada = funciones.fecha_mas_dias(new Date(), dias_en_hacer_kms_x_hacer);
                    }
                    int int_fecha_log_futuro_recalculada = funciones.date_a_int(fecha_log_futuro_recalculada);
                    Date f_revision_por_fecha = new Date();
                    if (tipo_rev.equals(TipoLog.TIPO_ACEITE)) {
                        f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_REV_ACEITE);
                    }
                    if (tipo_rev.equals(TipoLog.TIPO_REV_GENERAL)) {
                        f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_REV_REVGRAL);
                    }
                    if (tipo_rev.equals(TipoLog.TIPO_CORREA)) {
                        f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_CORREA);
                    }
                    if (tipo_rev.equals(TipoLog.TIPO_BOMBA_AGUA)) {
                        f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_BOMBA_AGUA);
                    }
                    // la itv solo se procesa al marcar el log como realizado pq no depende de los kms ni de fecha sino cuando la realice

                    int int_f_revision_por_fecha = funciones.date_a_int(f_revision_por_fecha);
                    if (int_fecha_log_futuro_recalculada > int_f_revision_por_fecha) { // el cambio sería por fecha y no por kms
                        int_fecha_log_futuro_recalculada = int_f_revision_por_fecha;
                        System.out.println("EL cambio es por fecha: fecha calculada " + fecha_log_futuro_recalculada + " y por fecha " + f_revision_por_fecha);
                    }

                    if (c_logs_tipo.moveToFirst() == true) {
                        String txt_fecha_l = c_logs_tipo.getString(c_logs_tipo.getColumnIndex("fecha_string")); // recuperamos el último de los logs del tiporev
                        int int_id_log = c_logs_tipo.getInt(c_logs_tipo.getColumnIndex(dbLogs.CN_ID));
                        Date fecha_log_futuro_puesta = funciones.string_a_date(txt_fecha_l);

                        int kms_supuestos_hasta_fecha_fut_tipo = (int) funciones.dias_entre_2_fechas(fecha_ultimo_log_hist, fecha_log_futuro_puesta) * int_media;
                    } else {
                        // Creamos el nuevo log futuro estimado a partir del ultimo log histórico
                        // Se pone por defecto el tipo de aceite del ultimo log historico, si se desea poner otro se deberá editar el log y cambiarlo de forma manual
                        // Se pone por defecto el kms_supuestos_hasta_fecha_fut_aceite, se deberá actualizar el nº de kms reales al volver a hacer la revisión de aceite futura
                        int kms_supuestos_hasta_fecha_fut_tipo = (int) funciones.dias_entre_2_fechas(fecha_ultimo_log_hist, fecha_log_futuro_recalculada) * int_media;
                        TipoLog miTipoLog = new TipoLog(tipo_rev, fecha_log_futuro_recalculada, funciones.int_a_string(int_fecha_log_futuro_recalculada), int_fecha_log_futuro_recalculada, id_aceite_ultimo_log_hist, id_revgral_ultimo_log_hist, txt_matricula_ultimo_log_hist, DBLogs.NO_REALIZADO, kms_supuestos_hasta_fecha_fut_tipo);
                        dbLogs.insertar(miTipoLog);
                    }

                } else {
                    Toast.makeText(context, "Debería haber realizado un " + tipo_rev + " hace " + kms_que_faltan_x_hacer * -1 + " kms.", Toast.LENGTH_LONG).show();
                    // Ponemos la fecha a mañana y alarma
                    fecha_log_futuro_recalculada = funciones.fecha_mas_dias(new Date(), 1); //Mañana y aviso
                    int int_fecha_log_futuro_recalculada = funciones.date_a_int(fecha_log_futuro_recalculada);
                    if (c_logs_tipo.moveToFirst() == true) {
                        int int_id_log = c_logs_tipo.getInt(c_logs_tipo.getColumnIndex(dbLogs.CN_ID));
                        dbLogs.ActualizarFechaLogFuturo(int_id_log, int_fecha_log_futuro_recalculada);
                    }
                }
            }

        }
        else {
            // No se hace nada pq no habría logs viejos para poder calcular
            // en el resto de casos se creará a partir del viejo siempre el futuro

            // en caso de itv sí se añade solo si hemos rellenado el campo itv al crear el coche
            if(tipo_rev.equals(TipoLog.TIPO_ITV)) {
                DBCar dbc = new DBCar(context);
                Cursor c_itv = dbc.buscarCoche(matricula);
                if((c_itv.moveToFirst() == true) && (!c_logs_tipo.moveToFirst())) { // Si no hay logs futuros de itv pq sino agregaría 1 cada vez q entramos
                    int int_itv = c_itv.getInt(c_itv.getColumnIndex(dbc.CN_ITV));
                    Date f_itv = funciones.int_a_date(int_itv);
                    if(int_itv != MyActivity.NO_ITV) { // menor de 1975 (no se ha insertado fecha itv)
                        TipoLog miTipoLog = new TipoLog(tipo_rev, f_itv, funciones.int_a_string(int_itv), int_itv, AddLog.NO_ACEITE, AddLog.NO_REVGRAL, matricula, DBLogs.NO_REALIZADO, MyActivity.NO_KMS); // los kms de itv da igual pq la itv solo va por fecha
                        dbLogs.insertar(miTipoLog);
                    }
                }
            }
        }

    }
}
