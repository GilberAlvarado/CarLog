package com.carlog.gilberto.carlog.negocio;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.carlog.gilberto.carlog.data.DBAceite;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;

import java.util.Date;

/**
 * Created by Gilberto on 27/05/2015.
 */
public class ProcesarAceite {

    public static void procesar_aceite(DBLogs dbLogs, int int_now, Context context, int int_kms, int int_fecha_ini, int int_kms_ini, String matricula) {

        int dias_coche = (int) funciones.dias_entre_2_fechas(funciones.int_a_date(int_fecha_ini), new Date());
        int int_media = 0;
        if(dias_coche == 0) { // se creó hoy el coche
            int_media = int_kms - int_kms_ini;
        }
        else int_media = int_kms - int_kms_ini / dias_coche;

        Cursor c_historico_aceite = dbLogs.LogsHistoricoAceiteOrderByFechaString(int_now, matricula);
        Cursor c_logs_aceite = dbLogs.LogsAceiteOrderByFechaString(int_now, matricula);


        if (c_historico_aceite.moveToFirst() == true) { // Si existen logs históricos de aceite hay que actualizar la fecha del futuro (pq siempre tiene q existir) log de aceite
            String txt_fecha_h = c_historico_aceite.getString(c_historico_aceite.getColumnIndex("fecha_string")); // recuperamos el último de los logs
            Date fecha_ultimo_log_hist = funciones.string_a_date(txt_fecha_h);
            int kms_ultimo_log_hist = c_historico_aceite.getInt(c_historico_aceite.getColumnIndex(DBLogs.CN_KMS));
            int id_aceite_ultimo_log_hist = c_historico_aceite.getInt(c_historico_aceite.getColumnIndex(DBLogs.CN_ACEITE));
            String txt_matricula_ultimo_log_hist = c_historico_aceite.getString(c_historico_aceite.getColumnIndex(DBLogs.CN_CAR));

            DBAceite dbaceite = new DBAceite(context);
            Cursor c_aceite = dbaceite.buscarTiposAceite(id_aceite_ultimo_log_hist);

            int kms_aceite_ultimo_log_hist = 0;
            if (c_aceite.moveToFirst() == true) {
                kms_aceite_ultimo_log_hist = c_aceite.getInt(c_aceite.getColumnIndex(DBAceite.CN_KMS));
                System.out.println("KMS Aceite ultimo log hist: " + kms_aceite_ultimo_log_hist);
            }



            System.out.println("Aceite ultimo log hist id: " +id_aceite_ultimo_log_hist);
            System.out.println("int_media: " + int_media + " kms_ultimo_log_hist "+kms_ultimo_log_hist);


            int kms_que_faltan_x_hacer = kms_aceite_ultimo_log_hist - (int_kms - kms_ultimo_log_hist);


            System.out.println("int_kms calidad = " + int_kms);
            System.out.println("kms_que_faltan_x_hacer = " + kms_que_faltan_x_hacer);
            if((int_kms - kms_ultimo_log_hist) < kms_aceite_ultimo_log_hist) { // Actualizamos la fecha de la futura revisión de aceite
                int dias_en_hacer_kms_x_hacer = 0;
                if (int_media != 0) dias_en_hacer_kms_x_hacer = kms_que_faltan_x_hacer / int_media; // regla de 3 si en 1 día hago 5000kms, en cuantos haré X?
                else dias_en_hacer_kms_x_hacer = kms_que_faltan_x_hacer;
                System.out.println("dias_en_hacer_kms_x_hacer: " + dias_en_hacer_kms_x_hacer);

                Date fecha_log_futuro_recalculada = new Date();
                if(dias_en_hacer_kms_x_hacer == 0) { // La media es mucho más grande que los kms que quedan o se llega al día que toca
                    fecha_log_futuro_recalculada = funciones.fecha_mas_dias(dias_en_hacer_kms_x_hacer + 1); //Mañana y aviso
                    Toast.makeText(context, "Debería cambiar el aceite cuanto antes.", Toast.LENGTH_LONG).show();
                }
                else {
                    fecha_log_futuro_recalculada = funciones.fecha_mas_dias(dias_en_hacer_kms_x_hacer);
                }
                int int_fecha_log_futuro_recalculada = funciones.date_a_int(fecha_log_futuro_recalculada);

                if (c_logs_aceite.moveToFirst() == true) {
                    String txt_fecha_l = c_logs_aceite.getString(c_logs_aceite.getColumnIndex("fecha_string")); // recuperamos el último de los logs
                    int int_id_log = c_logs_aceite.getInt(c_logs_aceite.getColumnIndex(dbLogs.CN_ID));
                    Date fecha_log_futuro_puesta = funciones.string_a_date(txt_fecha_l);

                    int kms_supuestos_hasta_fecha_fut_aceite = (int) funciones.dias_entre_2_fechas(fecha_ultimo_log_hist, fecha_log_futuro_puesta) * int_media;

                    System.out.println("kms_supuestos_hasta_fecha_fut_aceite: " + kms_supuestos_hasta_fecha_fut_aceite);
                    System.out.println("ID LOG FUTURO: " +int_id_log);

                    dbLogs.ActualizarFechaLogFuturo(int_id_log, int_fecha_log_futuro_recalculada);
                }
                else {
                    // Creamos el nuevo log futuro estimado a partir del ultimo log histórico
                    // Se pone por defecto el tipo de aceite del ultimo log historico, si se desea poner otro se deberá editar el log y cambiarlo de forma manual
                    // Se pone por defecto el kms_supuestos_hasta_fecha_fut_aceite, se deberá actualizar el nº de kms reales al volver a hacer la revisión de aceite futura
                    int kms_supuestos_hasta_fecha_fut_aceite = (int) funciones.dias_entre_2_fechas(fecha_ultimo_log_hist, fecha_log_futuro_recalculada) * int_media;
                    TipoLog miTipoLog = new TipoLog(TipoLog.TIPO_ACEITE, fecha_log_futuro_recalculada, funciones.int_a_string(int_fecha_log_futuro_recalculada), int_fecha_log_futuro_recalculada, id_aceite_ultimo_log_hist,  txt_matricula_ultimo_log_hist, DBLogs.NO_REALIZADO, kms_supuestos_hasta_fecha_fut_aceite);
                    dbLogs.insertar(miTipoLog);
                }

            }
            else {
                Toast.makeText(context, "Debería haber cambiado el aceite hace " + kms_que_faltan_x_hacer + " kms.", Toast.LENGTH_LONG).show();
            }


        }
        else {
            // No se hace nada pq no habría logs viejos para poder calcular
            // en el resto de casos se creará a partir del viejo siempre el futuro

        }

    }
}
