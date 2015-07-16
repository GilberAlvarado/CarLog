package com.carlog.gilberto.carlog.negocio;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.carlog.gilberto.carlog.activity.AddLog;
import com.carlog.gilberto.carlog.activity.MyActivity;
import com.carlog.gilberto.carlog.data.DBAceite;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;

import java.util.Date;

/**
 * Created by Gilberto on 27/05/2015.
 */
public class ProcesarTipos {

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
            int kms_revgral_ultimo_log_hist = AddLog.NO_REVGRAL;
            if(tipo_rev.equals(TipoLog.TIPO_REV_GENERAL)) {
                kms_revgral_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(DBLogs.CN_REVGRAL));
            }

            System.out.println("int_media: " + int_media + " kms_ultimo_log_hist "+kms_ultimo_log_hist);

            int kms_que_faltan_x_hacer = kms_tipo_ultimo_log_hist - (int_kms - kms_ultimo_log_hist);

            Date fecha_log_futuro_recalculada = new Date();
            if((int_kms - kms_ultimo_log_hist) < kms_tipo_ultimo_log_hist) { // Actualizamos la fecha de la futura revisión de aceite
                int dias_en_hacer_kms_x_hacer = 0;
                if (int_media != 0) dias_en_hacer_kms_x_hacer = kms_que_faltan_x_hacer / int_media; // regla de 3 si en 1 día hago 5000kms, en cuantos haré X?
                else dias_en_hacer_kms_x_hacer = kms_que_faltan_x_hacer;

                if(dias_en_hacer_kms_x_hacer == 0) { // La media es mucho más grande que los kms que quedan o se llega al día que toca
                    fecha_log_futuro_recalculada = funciones.fecha_mas_dias(dias_en_hacer_kms_x_hacer + 1); //Mañana y aviso
                    if(tipo_rev.equals(TipoLog.TIPO_ACEITE)) {
                        Toast.makeText(context, "Debería cambiar el aceite cuanto antes.", Toast.LENGTH_LONG).show();
                    }
                    if(tipo_rev.equals(TipoLog.TIPO_REV_GENERAL)) {
                        Toast.makeText(context, "Debería realizar una revisión general cuanto antes.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    fecha_log_futuro_recalculada = funciones.fecha_mas_dias(dias_en_hacer_kms_x_hacer);
                }
                int int_fecha_log_futuro_recalculada = funciones.date_a_int(fecha_log_futuro_recalculada);

                if (c_logs_tipo.moveToFirst() == true) {
                    String txt_fecha_l = c_logs_tipo.getString(c_logs_tipo.getColumnIndex("fecha_string")); // recuperamos el último de los logs
                    int int_id_log = c_logs_tipo.getInt(c_logs_tipo.getColumnIndex(dbLogs.CN_ID));
                    Date fecha_log_futuro_puesta = funciones.string_a_date(txt_fecha_l);

                    int kms_supuestos_hasta_fecha_fut_tipo = (int) funciones.dias_entre_2_fechas(fecha_ultimo_log_hist, fecha_log_futuro_puesta) * int_media;

                    dbLogs.ActualizarFechaLogFuturo(int_id_log, int_fecha_log_futuro_recalculada);
                }
                else {
                    // Creamos el nuevo log futuro estimado a partir del ultimo log histórico
                    // Se pone por defecto el tipo de aceite del ultimo log historico, si se desea poner otro se deberá editar el log y cambiarlo de forma manual
                    // Se pone por defecto el kms_supuestos_hasta_fecha_fut_aceite, se deberá actualizar el nº de kms reales al volver a hacer la revisión de aceite futura
                    int kms_supuestos_hasta_fecha_fut_tipo = (int) funciones.dias_entre_2_fechas(fecha_ultimo_log_hist, fecha_log_futuro_recalculada) * int_media;
                    TipoLog miTipoLog = new TipoLog(tipo_rev, fecha_log_futuro_recalculada, funciones.int_a_string(int_fecha_log_futuro_recalculada), int_fecha_log_futuro_recalculada, id_aceite_ultimo_log_hist, kms_revgral_ultimo_log_hist, txt_matricula_ultimo_log_hist, DBLogs.NO_REALIZADO, kms_supuestos_hasta_fecha_fut_tipo);
                    dbLogs.insertar(miTipoLog);
                }

            }
            else {
                Toast.makeText(context, "Debería haber cambiado el aceite hace " + kms_que_faltan_x_hacer * -1 + " kms.", Toast.LENGTH_LONG).show();
                // Ponemos la fecha a mañana y alarma
                fecha_log_futuro_recalculada = funciones.fecha_mas_dias(1); //Mañana y aviso
                int int_fecha_log_futuro_recalculada = funciones.date_a_int(fecha_log_futuro_recalculada);
                if (c_logs_tipo.moveToFirst() == true) {
                    int int_id_log = c_logs_tipo.getInt(c_logs_tipo.getColumnIndex(dbLogs.CN_ID));
                    dbLogs.ActualizarFechaLogFuturo(int_id_log, int_fecha_log_futuro_recalculada);
                }
            }


        }
        else {
            // No se hace nada pq no habría logs viejos para poder calcular
            // en el resto de casos se creará a partir del viejo siempre el futuro

        }

    }
}
