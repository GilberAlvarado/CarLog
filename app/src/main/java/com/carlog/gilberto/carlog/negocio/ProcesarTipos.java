package com.carlog.gilberto.carlog.negocio;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.activity.addLog;
import com.carlog.gilberto.carlog.activity.myActivity;
import com.carlog.gilberto.carlog.data.dbAceite;
import com.carlog.gilberto.carlog.data.dbBombaAgua;
import com.carlog.gilberto.carlog.data.dbBujias;
import com.carlog.gilberto.carlog.data.dbCar;
import com.carlog.gilberto.carlog.data.dbCorrea;
import com.carlog.gilberto.carlog.data.dbEmbrague;
import com.carlog.gilberto.carlog.data.dbFiltroAire;
import com.carlog.gilberto.carlog.data.dbFiltroGasolina;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.data.dbRevGral;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gilberto on 27/05/2015.
 */
public class procesarTipos {

    public final static int F_MAX_REV_ACEITE = 365;
    public final static int F_MAX_REV_REVGRAL = 365;
    public final static int F_MAX_CORREA = 365 * 6;
    public final static int F_MAX_BOMBA_AGUA = 365 * 6;
    public final static int F_MAX_ITV = 365;
    public final static int F_MAX_FIL_GASOLINA = 365 * 3;
    public final static int F_MAX_FIL_AIRE = 365 * 3;
    public final static int F_MAX_BUJIAS = 365 * 2;
    public final static int F_MAX_LIQUIDO_FRENOS = 365 * 2;
    public final static int F_MAX_LIMPIAPARABRISAS = 365;
    public final static int F_MAX_ANTICONGELANTE = 365 * 2;
    public final static int F_MAX_BATERIA = 365 * 5;
    // aceite y revgral no tienen kms pq se inician al valor q inserte el usuario y no automaticamente
    //public final static int KMS_CORREA = 100000;
    //public final static int KMS_BOMBA_AGUA = 100000;
    //public final static int KMS_FIL_GASOLINA = 30000;
    //public final static int KMS_FIL_AIRE = 30000;
    //public final static int KMS_BUJIAS = 60000;
    //public final static int KMS_EMBRAGUE = 60000;
    public final static int KMS_FRENOS = 15000;
    public final static int KMS_AMORTIGUADORES = 80000;
    public final static int KMS_LIQUIDO_FRENOS = 15000;
    public final static int KMS_LUCES = 30000;
    public final static int KMS_RUEDAS = 15000;




    private static int procesarPorKms(Context context, String tipo_rev, int id_aceite_ultimo_log_hist, int id_revgral_ultimo_log_hist, int id_correa_ultimo_log_hist, int id_bombaagua_ultimo_log_hist, int id_fgasolina_ultimo_log_hist, int id_faire_ultimo_log_hist, int id_bujias_ultimo_log_hist, int id_embrague_ultimo_log_hist, String matricula, Cursor c_historico_tipo) {
        int kms_tipo_ultimo_log_hist = 0;
        ///////ACEITE tiene id pq tiene su propia tabla
        if(tipo_rev.equals(context.getString(R.string.tipoAceite))) {
            dbAceite dbaceite = new dbAceite(context);
            Cursor c_aceite = dbaceite.buscarTiposAceite(id_aceite_ultimo_log_hist);

            if (c_aceite.moveToFirst() == true) {
                kms_tipo_ultimo_log_hist = c_aceite.getInt(c_aceite.getColumnIndex(dbAceite.CN_KMS));
            }
        }
        ///////REV GRAL tiene id pq tiene su propia tabla
        if(tipo_rev.equals(context.getString(R.string.tipoRevGen))) {
            dbRevGral dbRevGral = new com.carlog.gilberto.carlog.data.dbRevGral(context);
            Cursor c_revgral = dbRevGral.buscarTiposRevGral(id_revgral_ultimo_log_hist);

            if (c_revgral.moveToFirst() == true) {
                kms_tipo_ultimo_log_hist = c_revgral.getInt(c_revgral.getColumnIndex(com.carlog.gilberto.carlog.data.dbRevGral.CN_KMS));
            }
        }
        ///////CORREA tiene id pq tiene su propia tabla
        if(tipo_rev.equals(context.getString(R.string.tipoCorrea))) {
            dbCorrea dbCorrea = new com.carlog.gilberto.carlog.data.dbCorrea(context);
            if(id_correa_ultimo_log_hist == 0) id_correa_ultimo_log_hist++; // pq es un tipo de revisión que se inicia automaticamente a la recomendada sin su pantalla que añada el id de bomba clave ajena a logs (y el valor 1 es la recomendada)
            Cursor c_correa = dbCorrea.buscarTiposCorrea(id_correa_ultimo_log_hist);

            if (c_correa.moveToFirst() == true) {
                kms_tipo_ultimo_log_hist = c_correa.getInt(c_correa.getColumnIndex(com.carlog.gilberto.carlog.data.dbCorrea.CN_KMS));
            }
        }
        ///////Bomba Agua tiene id pq tiene su propia tabla
        if(tipo_rev.equals(context.getString(R.string.tipoBomba))) {
            dbBombaAgua dbBombaagua = new dbBombaAgua(context);
            if(id_bombaagua_ultimo_log_hist == 0) id_bombaagua_ultimo_log_hist++; // pq es un tipo de revisión que se inicia automaticamente a la recomendada sin su pantalla que añada el id de bomba clave ajena a logs (y el valor 1 es la recomendada)
            Cursor c_bombaagua = dbBombaagua.buscarTiposBombaAgua(id_bombaagua_ultimo_log_hist);
            if (c_bombaagua.moveToFirst() == true) {
                kms_tipo_ultimo_log_hist = c_bombaagua.getInt(c_bombaagua.getColumnIndex(dbBombaAgua.CN_KMS));
            }
        }
        ///////Filtro Gasolina tiene id pq tiene su propia tabla
        if(tipo_rev.equals(context.getString(R.string.tipoFiltroGasolina))) {
            dbFiltroGasolina dbFG = new dbFiltroGasolina(context);
            if(id_fgasolina_ultimo_log_hist == 0) id_fgasolina_ultimo_log_hist++; // pq es un tipo de revisión que se inicia automaticamente a la recomendada sin su pantalla que añada el id de bomba clave ajena a logs (y el valor 1 es la recomendada)
            Cursor c_fg = dbFG.buscarTiposFiltroGasolina(id_fgasolina_ultimo_log_hist);

            if (c_fg.moveToFirst() == true) {
                kms_tipo_ultimo_log_hist = c_fg.getInt(c_fg.getColumnIndex(dbFiltroGasolina.CN_KMS));
            }
        }
        ///////Filtro Aire tiene id pq tiene su propia tabla
        if(tipo_rev.equals(context.getString(R.string.tipoFiltroAire))) {
            dbFiltroAire dbFa = new dbFiltroAire(context);
            if(id_faire_ultimo_log_hist == 0) id_faire_ultimo_log_hist++; // pq es un tipo de revisión que se inicia automaticamente a la recomendada sin su pantalla que añada el id de bomba clave ajena a logs (y el valor 1 es la recomendada)
            Cursor c_fa = dbFa.buscarTiposFiltroAire(id_faire_ultimo_log_hist);

            if (c_fa.moveToFirst() == true) {
                kms_tipo_ultimo_log_hist = c_fa.getInt(c_fa.getColumnIndex(dbFiltroAire.CN_KMS));
            }
        }
        ///////Bujias tiene id pq tiene su propia tabla
        if(tipo_rev.equals(context.getString(R.string.tipoBujias))) {
            dbBujias dbBujias = new com.carlog.gilberto.carlog.data.dbBujias(context);
            if(id_bujias_ultimo_log_hist == 0) id_bujias_ultimo_log_hist++; // pq es un tipo de revisión que se inicia automaticamente a la recomendada sin su pantalla que añada el id de bomba clave ajena a logs (y el valor 1 es la recomendada)
            Cursor c_bujias = dbBujias.buscarTiposBujias(id_bujias_ultimo_log_hist);

            if (c_bujias.moveToFirst() == true) {
                kms_tipo_ultimo_log_hist = c_bujias.getInt(c_bujias.getColumnIndex(com.carlog.gilberto.carlog.data.dbBujias.CN_KMS));
            }
        }
        ///////Embrague tiene id pq tiene su propia tabla
        if(tipo_rev.equals(context.getString(R.string.tipoEmbrague))) {
            dbEmbrague dbEmbrague = new com.carlog.gilberto.carlog.data.dbEmbrague(context);
            if(id_embrague_ultimo_log_hist == 0) id_embrague_ultimo_log_hist++; // pq es un tipo de revisión que se inicia automaticamente a la recomendada sin su pantalla que añada el id de bomba clave ajena a logs (y el valor 1 es la recomendada)
            Cursor c_embrague = dbEmbrague.buscarTiposEmbrague(id_bombaagua_ultimo_log_hist);

            if (c_embrague.moveToFirst() == true) {
                kms_tipo_ultimo_log_hist = c_embrague.getInt(c_embrague.getColumnIndex(com.carlog.gilberto.carlog.data.dbEmbrague.CN_KMS));
            }
        }
        // ruedas no tiene id pq no tiene tabla pq el valor de kms o años no va a ser editable
        if(tipo_rev.equals(context.getString(R.string.tipoEmbrague))) {
            kms_tipo_ultimo_log_hist = KMS_RUEDAS;
        }
        // luces no tiene id pq no tiene tabla pq el valor de kms o años no va a ser editable
        if(tipo_rev.equals(context.getString(R.string.tipoLuces))) {
            kms_tipo_ultimo_log_hist = KMS_LUCES;
        }
        // liquido frenos no tiene id pq no tiene tabla pq el valor de kms o años no va a ser editable
        if(tipo_rev.equals(context.getString(R.string.tipoLiqFrenos))) {
            kms_tipo_ultimo_log_hist = KMS_LIQUIDO_FRENOS;
        }
        // frenos no tiene id pq no tiene tabla pq el valor de kms o años no va a ser editable
        if(tipo_rev.equals(context.getString(R.string.tipoFrenos))) {
            kms_tipo_ultimo_log_hist = KMS_FRENOS;
        }
        // amortiguadores no tiene id pq no tiene tabla pq el valor de kms o años no va a ser editable
        if(tipo_rev.equals(context.getString(R.string.tipoAmortiguadores))) {
            kms_tipo_ultimo_log_hist = KMS_AMORTIGUADORES;
        }
        // filtro aceite
        if(tipo_rev.equals(context.getString(R.string.tipoFiltroAceite))) {
            int int_veces = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(dbLogs.CN_VECES_FIL_ACEITE));
            dbLogs dbl = new dbLogs(context);
            Cursor c_log_ac_hist = dbl.LogsHistoricoTipoOrderByFechaString(matricula, context.getString(R.string.tipoAceite)); // en tipofiltro aceite cogemos el tipo aceite
            if (c_log_ac_hist.moveToFirst() == true) {
                int id_aceite_hist = c_log_ac_hist.getInt(c_log_ac_hist.getColumnIndex(dbLogs.CN_ACEITE));
                dbAceite dba = new dbAceite(context);
                Cursor c_ac_hist = dba.buscarTiposAceite(id_aceite_hist);
                if (c_ac_hist.moveToFirst() == true) {
                    int kms_hist_aceite = c_ac_hist.getInt(c_ac_hist.getColumnIndex(dbAceite.CN_KMS));
                    int kms_veces = kms_hist_aceite * int_veces;
                    kms_tipo_ultimo_log_hist = kms_veces;
                }
            }
          /*  else { // dejamos la misma fecha pq ya se procesaría al marcar como realizado
            }*/
        }
        // itv no va por kms así que no se hacen comprobaciones aquí

        return kms_tipo_ultimo_log_hist;
    }





    private static Date procesarPorFecha(Context context, String tipo_rev, Date fecha_ultimo_log_hist) {
        Date f_revision_por_fecha = new Date();
        if (tipo_rev.equals(context.getString(R.string.tipoAceite))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_REV_ACEITE);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoFiltroAceite))) { // si pasa 1 año cambiamos a la misma fecha maxima que el aceite
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_REV_ACEITE);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoRevGen))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_REV_REVGRAL);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoCorrea))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_CORREA);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoBomba))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_BOMBA_AGUA);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoFiltroAire))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_FIL_AIRE);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoFiltroGasolina))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_FIL_GASOLINA);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoBujias))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_BUJIAS);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoLimpiaparabrisas))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_LIMPIAPARABRISAS);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoLiqFrenos))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_LIQUIDO_FRENOS);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoAnticongelante))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_ANTICONGELANTE);
        }
        if (tipo_rev.equals(context.getString(R.string.tipoBateria))) {
            f_revision_por_fecha = funciones.fecha_mas_dias(fecha_ultimo_log_hist, F_MAX_BATERIA);
        }
        // la itv solo se procesa al marcar el log como realizado pq no depende de los kms ni de fecha sino cuando la realice

        return f_revision_por_fecha;
    }



    private static void avisoSobrepasadaRev(Context context, String tipo_rev) {
        if (tipo_rev.equals(context.getString(R.string.tipoAceite))) {
            Toast.makeText(context, context.getString(R.string.changeAceite), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoFiltroAceite))) {
            Toast.makeText(context, context.getString(R.string.changeFiltroAceite), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoRevGen))) {
            Toast.makeText(context, context.getString(R.string.changeRevGen), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoCorrea))) {
            Toast.makeText(context, context.getString(R.string.changeCorrea), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoBomba))) {
            Toast.makeText(context, context.getString(R.string.changeBomba), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoFiltroGasolina))) {
            Toast.makeText(context, context.getString(R.string.changeFilGasolina), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoFiltroAire))) {
            Toast.makeText(context, context.getString(R.string.changeFilAire), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoBujias))) {
            Toast.makeText(context, context.getString(R.string.changeBujias), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoLimpiaparabrisas))) {
            Toast.makeText(context, context.getString(R.string.changeLimpia), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoLiqFrenos))) {
            Toast.makeText(context, context.getString(R.string.changeLiqFrenos), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoRuedas))) {
            Toast.makeText(context, context.getString(R.string.changeRuedas), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoFrenos))) {
            Toast.makeText(context, context.getString(R.string.changeFrenos), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoLuces))) {
            Toast.makeText(context, context.getString(R.string.changeLuces), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoEmbrague))) {
            Toast.makeText(context, context.getString(R.string.changeEmbrague), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoAnticongelante))) {
            Toast.makeText(context, context.getString(R.string.changeAnticongelante), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoBateria))) {
            Toast.makeText(context, context.getString(R.string.changeBateria), Toast.LENGTH_LONG).show();
        }
        if (tipo_rev.equals(context.getString(R.string.tipoAmortiguadores))) {
            Toast.makeText(context, context.getString(R.string.changeAmortiguadores), Toast.LENGTH_LONG).show();
        }
    }



    private static int calcular_media(long long_fecha_ini, int int_kms, int int_kms_ini, int car_year) {
        int dias_coche_app = (int) funciones.dias_entre_2_fechas(funciones.long_a_date(long_fecha_ini), new Date());
        int int_media = 0;
        if(dias_coche_app == 0) { // se creó hoy el coche (la media es la completa del coche) (se estima a mediados de año)
            if(car_year != myActivity.NO_KMS) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.DAY_OF_MONTH, 15);
                calendar.set(Calendar.MONTH, 06);
                calendar.set(Calendar.YEAR, car_year);
                Date car_year_date = calendar.getTime();

                int dias_coche_todos = (int) funciones.dias_entre_2_fechas(car_year_date, new Date());
                if (dias_coche_todos <= 0) {	// Caso de que el coche sea nuevo de este año y sea antes del mes 06
                    int_media = 0;
                }
                else
                    int_media = int_kms  / dias_coche_todos;
            }
            else {
                int_media = 0;
            }
        }
        else int_media = int_kms - int_kms_ini / dias_coche_app; //(la media será la q se calcule durante la app, cada vez mas exacta)
        return int_media;
    }

    public static void procesar(dbLogs dbLogs, Context context, int int_kms, long long_fecha_ini, int int_kms_ini, String matricula, String tipo_rev, int year) {
        int int_media = calcular_media(long_fecha_ini, int_kms, int_kms_ini, year);

        Cursor c_historico_tipo = dbLogs.LogsHistoricoTipoOrderByFechaString(matricula, tipo_rev);
        Cursor c_logs_tipo = dbLogs.LogsTipoOrderByFechaString(matricula, tipo_rev);
        int fmodificada = com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA;
        if (c_logs_tipo.moveToFirst() == true) {
            fmodificada = c_logs_tipo.getInt(c_logs_tipo.getColumnIndex(dbLogs.CN_FMODIFICADA));
        }
        if(fmodificada == 0) { // si modificamos no se procesa pq se deja la fecha que cambie el usuario (tendrá cita para esa revisión ese día)
            if (c_historico_tipo.moveToFirst() == true) { // Si existen logs históricos de tipo hay que actualizar la fecha del futuro (pq siempre tiene q existir) log de aceite
                String txt_fecha_h = c_historico_tipo.getString(c_historico_tipo.getColumnIndex("fecha_string")); // recuperamos el último de los logs
                Date fecha_ultimo_log_hist = funciones.string_a_date(txt_fecha_h);
                int kms_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_KMS));
                String txt_matricula_ultimo_log_hist = c_historico_tipo.getString(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_CAR));
                int id_aceite_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_ACEITE));
                int id_correa_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_CORREA));
                int id_bombaagua_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_BOMBAAGUA));
                int id_fgasolina_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_FGASOLINA));
                int id_faire_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_FAIRE));
                int id_bujias_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_BUJIAS));
                int id_embrague_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_EMBRAGUE));
                int id_revgral_ultimo_log_hist = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_REVGRAL));

                // Los tipo de revisión que se procesan unicamente por fecha y no por kms
                if ((tipo_rev.equals(context.getString(R.string.tipoLimpiaparabrisas))) || (tipo_rev.equals(context.getString(R.string.tipoAnticongelante))) || (tipo_rev.equals(context.getString(R.string.tipoBateria)))) {
                    if (c_logs_tipo.moveToFirst() == false) { // Si existen no se tienen que procesar pq se procesan unicamente al actualizar el nº de kms excepto los especiales aceite y filtro de aceite
                        Date f_revision_por_fecha = procesarPorFecha(context, tipo_rev, fecha_ultimo_log_hist);
                        long long_f_revision_por_fecha = funciones.date_a_long(f_revision_por_fecha);
                        tipoLog miTipoLog = new tipoLog(tipo_rev, f_revision_por_fecha, funciones.long_a_string(long_f_revision_por_fecha), long_f_revision_por_fecha, addLog.NO_ACEITE, addLog.NO_VECES_FIL_ACEITE, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, txt_matricula_ultimo_log_hist, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, myActivity.NO_KMS);
                        dbLogs.insertar(miTipoLog);
                    }
                } else {
                    int kms_tipo_ultimo_log_hist = procesarPorKms(context, tipo_rev, id_aceite_ultimo_log_hist, id_revgral_ultimo_log_hist, id_correa_ultimo_log_hist, id_bombaagua_ultimo_log_hist, id_fgasolina_ultimo_log_hist, id_faire_ultimo_log_hist, id_bujias_ultimo_log_hist, id_embrague_ultimo_log_hist, matricula, c_historico_tipo);

                    int kms_que_faltan_x_hacer = kms_tipo_ultimo_log_hist - (int_kms - kms_ultimo_log_hist);

                    Date fecha_log_futuro_recalculada = new Date();
                    if (!tipo_rev.equals(context.getString(R.string.tipoItv))) {
                        if ((int_kms - kms_ultimo_log_hist) <= kms_tipo_ultimo_log_hist) { // Actualizamos la fecha de la futura revisión de tipo
                            int dias_en_hacer_kms_x_hacer = 0;
                            if (int_media != 0)
                                dias_en_hacer_kms_x_hacer = (int) kms_que_faltan_x_hacer / int_media; // regla de 3 si en 1 día hago 5000kms, en cuantos haré X?
                            else dias_en_hacer_kms_x_hacer = kms_que_faltan_x_hacer;

                            if (dias_en_hacer_kms_x_hacer == 0) { // La media es mucho más grande que los kms que quedan o se llega al día que toca
                                fecha_log_futuro_recalculada = funciones.fecha_mas_dias(new Date(), dias_en_hacer_kms_x_hacer + 1); //Mañana y aviso
                                avisoSobrepasadaRev(context, tipo_rev);
                            } else {
                                fecha_log_futuro_recalculada = funciones.fecha_mas_dias(new Date(), dias_en_hacer_kms_x_hacer);
                            }
                            long long_fecha_log_futuro_recalculada = funciones.date_a_long(fecha_log_futuro_recalculada);

                            Date f_revision_por_fecha = procesarPorFecha(context, tipo_rev, fecha_ultimo_log_hist);

                            long long_f_revision_por_fecha = funciones.date_a_long(f_revision_por_fecha);

                            if ((long_fecha_log_futuro_recalculada > long_f_revision_por_fecha) &&
                                    ((!tipo_rev.equals(context.getString(R.string.tipoRuedas))) && (!tipo_rev.equals(context.getString(R.string.tipoEmbrague))) && (!tipo_rev.equals(context.getString(R.string.tipoLuces)))
                                    && (!tipo_rev.equals(context.getString(R.string.tipoAmortiguadores))) && (!tipo_rev.equals(context.getString(R.string.tipoFrenos))))) { // el cambio sería por fecha y no por kms (menos revisiones de solo por kms)
                                long_fecha_log_futuro_recalculada = long_f_revision_por_fecha;
                            }
                            if (c_logs_tipo.moveToFirst() == true) { // Si existen no se tienen que procesar pq se procesan unicamente al actualizar el nº de kms excepto los especiales aceite y filtro de aceite
                                String txt_fecha_l = c_logs_tipo.getString(c_logs_tipo.getColumnIndex("fecha_string")); // recuperamos el último de los logs del tipo
                                int int_id_log = c_logs_tipo.getInt(c_logs_tipo.getColumnIndex(dbLogs.CN_ID));
                                Date fecha_log_futuro_puesta = funciones.string_a_date(txt_fecha_l);

                                int kms_supuestos_hasta_fecha_fut_tipo = (int) funciones.dias_entre_2_fechas(fecha_ultimo_log_hist, fecha_log_futuro_recalculada) * int_media;

                                // El caso de filtro de aceite y aceite es especial y se debe de tener en cuenta que existan pq se comparan de 2 en 2 y no siempre están los 2 eliminados
                                // por eso se debe de actualizar el log si existe
                                if (tipo_rev.equals(context.getString(R.string.tipoFiltroAceite))) {
                                /*if (c_historico_tipo.moveToFirst() == true) {
                                    int int_veces = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(DBLogs.CN_VECES_FIL_ACEITE));
                                    TipoLog miTipoLog = new TipoLog(tipo_rev, fecha_log_futuro_recalculada, funciones.long_a_string(long_fecha_log_futuro_recalculada), long_fecha_log_futuro_recalculada, id_aceite_ultimo_log_hist, int_veces, AddLog.NO_CONTADOR_FIL_ACEITE, id_revgral_ultimo_log_hist, txt_matricula_ultimo_log_hist, DBLogs.NO_REALIZADO, kms_supuestos_hasta_fecha_fut_tipo);
                                    dbLogs.insertar(miTipoLog);
                                }*/
                                    // prefiero hacerlo en vez de precalculando poner siempre en la feha xx/xx/xx hasta que toque cambiar el aceite y copiar la fecha de aceite (as´´i siempre coincidirán)
                                    int int_veces = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_VECES_FIL_ACEITE));
                                    Cursor c_contador_aceite_hist = dbLogs.LogsHistoricoTipoOrderByFechaString(matricula, context.getString(R.string.tipoAceite));
                                    Cursor c_copiar_fecha_aceite_futuro = dbLogs.LogsTipoOrderByFechaString(matricula, context.getString(R.string.tipoAceite));
                                    if (c_contador_aceite_hist.moveToFirst() == true) {
                                        int contador = c_contador_aceite_hist.getInt(c_contador_aceite_hist.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_CONTADOR_FIL_ACEITE));
                                        if (c_copiar_fecha_aceite_futuro.moveToFirst() == true) {
                                            String fecha_aceite_futuro = c_copiar_fecha_aceite_futuro.getString(c_copiar_fecha_aceite_futuro.getColumnIndex("fecha_string"));
                                            if (contador == int_veces) {
                                                tipoLog miTipoLog = new tipoLog(tipo_rev, funciones.string_a_date(fecha_aceite_futuro), fecha_aceite_futuro, funciones.string_a_long(fecha_aceite_futuro), id_aceite_ultimo_log_hist, int_veces, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, txt_matricula_ultimo_log_hist, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, kms_supuestos_hasta_fecha_fut_tipo);
                                                dbLogs.eliminar_por_id(int_id_log);
                                                dbLogs.insertar(miTipoLog);
                                            } else {
                                                Date f_max_aceite_provisional = funciones.fecha_mas_dias(new Date(), F_MAX_REV_ACEITE); //provisional pq no se va a cambiar hasta que llegue a contador
                                                long long_max_aceite_provisional = funciones.date_a_long(f_max_aceite_provisional);
                                                tipoLog miTipoLog = new tipoLog(tipo_rev, f_max_aceite_provisional, funciones.long_a_string(long_max_aceite_provisional), long_max_aceite_provisional, id_aceite_ultimo_log_hist, int_veces, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, txt_matricula_ultimo_log_hist, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, kms_supuestos_hasta_fecha_fut_tipo);
                                                dbLogs.eliminar_por_id(int_id_log);
                                                dbLogs.insertar(miTipoLog);
                                            }
                                        } else {
                                            Date f_max_aceite_provisional = funciones.fecha_mas_dias(new Date(), F_MAX_REV_ACEITE); //provisional pq no se va a cambiar hasta que llegue a contador
                                            long long_max_aceite_provisional = funciones.date_a_long(f_max_aceite_provisional);
                                            tipoLog miTipoLog = new tipoLog(tipo_rev, f_max_aceite_provisional, funciones.long_a_string(long_max_aceite_provisional), long_max_aceite_provisional, id_aceite_ultimo_log_hist, int_veces, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, txt_matricula_ultimo_log_hist, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, kms_supuestos_hasta_fecha_fut_tipo);
                                            dbLogs.eliminar_por_id(int_id_log);
                                            dbLogs.insertar(miTipoLog);
                                        }
                                    }
                                } else if (tipo_rev.equals(context.getString(R.string.tipoAceite))) {
                                    if (c_historico_tipo.moveToFirst() == true) {
                                        int int_contador = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_CONTADOR_FIL_ACEITE));
                                        tipoLog miTipoLog = new tipoLog(tipo_rev, fecha_log_futuro_recalculada, funciones.long_a_string(long_fecha_log_futuro_recalculada), long_fecha_log_futuro_recalculada, id_aceite_ultimo_log_hist, addLog.NO_VECES_FIL_ACEITE, int_contador + 1, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, txt_matricula_ultimo_log_hist, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, kms_supuestos_hasta_fecha_fut_tipo);
                                        dbLogs.eliminar_por_id(int_id_log);
                                        dbLogs.insertar(miTipoLog);
                                    }
                                } else { // para el resto
                                    tipoLog miTipoLog = new tipoLog(tipo_rev, fecha_log_futuro_recalculada, funciones.long_a_string(long_fecha_log_futuro_recalculada), long_fecha_log_futuro_recalculada, id_aceite_ultimo_log_hist, addLog.NO_VECES_FIL_ACEITE, addLog.NO_CONTADOR_FIL_ACEITE, id_revgral_ultimo_log_hist, id_correa_ultimo_log_hist, id_bombaagua_ultimo_log_hist, id_fgasolina_ultimo_log_hist, id_faire_ultimo_log_hist, id_bujias_ultimo_log_hist, id_embrague_ultimo_log_hist, txt_matricula_ultimo_log_hist, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, kms_supuestos_hasta_fecha_fut_tipo);
                                    Cursor c_sobreescribir = dbLogs.LogsTipoOrderByFechaString(matricula, tipo_rev);
                                    if(c_sobreescribir.moveToFirst() == true) {
                                        int int_id = c_sobreescribir.getInt(c_sobreescribir.getColumnIndex(dbLogs.CN_ID));
                                        dbLogs.eliminar_por_id(int_id);
                                    }
                                    dbLogs.insertar(miTipoLog);
                                }
                            } else {
                                // Creamos el nuevo log futuro estimado a partir del ultimo log histórico
                                // Se pone por defecto el tipo de aceite del ultimo log historico, si se desea poner otro se deberá editar el log y cambiarlo de forma manual
                                // Se pone por defecto el kms_supuestos_hasta_fecha_fut_aceite, se deberá actualizar el nº de kms reales al volver a hacer la revisión de aceite futura
                                int kms_supuestos_hasta_fecha_fut_tipo = (int) funciones.dias_entre_2_fechas(fecha_ultimo_log_hist, fecha_log_futuro_recalculada) * int_media;
                                if (tipo_rev.equals(context.getString(R.string.tipoFiltroAceite))) {
                                /*if (c_historico_tipo.moveToFirst() == true) {
                                    int int_veces = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(DBLogs.CN_VECES_FIL_ACEITE));
                                    TipoLog miTipoLog = new TipoLog(tipo_rev, fecha_log_futuro_recalculada, funciones.long_a_string(long_fecha_log_futuro_recalculada), long_fecha_log_futuro_recalculada, id_aceite_ultimo_log_hist, int_veces, AddLog.NO_CONTADOR_FIL_ACEITE, id_revgral_ultimo_log_hist, txt_matricula_ultimo_log_hist, DBLogs.NO_REALIZADO, kms_supuestos_hasta_fecha_fut_tipo);
                                    dbLogs.insertar(miTipoLog);
                                }*/
                                    // prefiero hacerlo en vez de precalculando poner siempre en la feha xx/xx/xx hasta que toque cambiar el aceite y copiar la fecha de aceite (as´´i siempre coincidirán)
                                    int int_veces = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_VECES_FIL_ACEITE));
                                    Cursor c_contador_aceite_hist = dbLogs.LogsHistoricoTipoOrderByFechaString(matricula, context.getString(R.string.tipoAceite));
                                    Cursor c_copiar_fecha_aceite_futuro = dbLogs.LogsTipoOrderByFechaString(matricula, context.getString(R.string.tipoAceite));
                                    if (c_contador_aceite_hist.moveToFirst() == true) {
                                        int contador = c_contador_aceite_hist.getInt(c_contador_aceite_hist.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_CONTADOR_FIL_ACEITE));
                                        if (c_copiar_fecha_aceite_futuro.moveToFirst() == true) {
                                            String fecha_aceite_futuro = c_copiar_fecha_aceite_futuro.getString(c_copiar_fecha_aceite_futuro.getColumnIndex("fecha_string"));
                                            if (contador == int_veces) {
                                                tipoLog miTipoLog = new tipoLog(tipo_rev, funciones.string_a_date(fecha_aceite_futuro), fecha_aceite_futuro, funciones.string_a_long(fecha_aceite_futuro), id_aceite_ultimo_log_hist, int_veces, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, txt_matricula_ultimo_log_hist, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, kms_supuestos_hasta_fecha_fut_tipo);
                                                dbLogs.insertar(miTipoLog);
                                            } else {
                                                Date f_max_aceite_provisional = funciones.fecha_mas_dias(new Date(), F_MAX_REV_ACEITE); //provisional pq no se va a cambiar hasta que llegue a contador
                                                long long_max_aceite_provisional = funciones.date_a_long(f_max_aceite_provisional);
                                                tipoLog miTipoLog = new tipoLog(tipo_rev, f_max_aceite_provisional, funciones.long_a_string(long_max_aceite_provisional), long_max_aceite_provisional, id_aceite_ultimo_log_hist, int_veces, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, txt_matricula_ultimo_log_hist, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, kms_supuestos_hasta_fecha_fut_tipo);
                                                dbLogs.insertar(miTipoLog);
                                            }
                                        }
                                    }
                                } else if (tipo_rev.equals(context.getString(R.string.tipoAceite))) {
                                    if (c_historico_tipo.moveToFirst() == true) {
                                        int int_contador = c_historico_tipo.getInt(c_historico_tipo.getColumnIndex(com.carlog.gilberto.carlog.data.dbLogs.CN_CONTADOR_FIL_ACEITE));
                                        tipoLog miTipoLog = new tipoLog(tipo_rev, fecha_log_futuro_recalculada, funciones.long_a_string(long_fecha_log_futuro_recalculada), long_fecha_log_futuro_recalculada, id_aceite_ultimo_log_hist, addLog.NO_VECES_FIL_ACEITE, int_contador + 1, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, txt_matricula_ultimo_log_hist, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, kms_supuestos_hasta_fecha_fut_tipo);
                                        dbLogs.insertar(miTipoLog);
                                    }
                                } else {
                                    tipoLog miTipoLog = new tipoLog(tipo_rev, fecha_log_futuro_recalculada, funciones.long_a_string(long_fecha_log_futuro_recalculada), long_fecha_log_futuro_recalculada, id_aceite_ultimo_log_hist, addLog.NO_VECES_FIL_ACEITE, addLog.NO_CONTADOR_FIL_ACEITE, id_revgral_ultimo_log_hist, id_correa_ultimo_log_hist, id_bombaagua_ultimo_log_hist, id_fgasolina_ultimo_log_hist, id_faire_ultimo_log_hist, id_bujias_ultimo_log_hist, id_embrague_ultimo_log_hist, txt_matricula_ultimo_log_hist, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, kms_supuestos_hasta_fecha_fut_tipo);
                                    dbLogs.insertar(miTipoLog);
                                }
                            }

                        } else {
                            Toast.makeText(context, "Debería haber realizado una revisión de " + tipo_rev + " hace " + kms_que_faltan_x_hacer * -1 + " kms.", Toast.LENGTH_LONG).show();
                            // Ponemos la fecha a mañana y alarma
                            fecha_log_futuro_recalculada = funciones.fecha_mas_dias(new Date(), 1); //Mañana y aviso
                            long long_fecha_log_futuro_recalculada = funciones.date_a_long(fecha_log_futuro_recalculada);
                            if (c_logs_tipo.moveToFirst() == true) {
                                int int_id_log = c_logs_tipo.getInt(c_logs_tipo.getColumnIndex(dbLogs.CN_ID));
                                dbLogs.ActualizarFechaLogFuturo(int_id_log, long_fecha_log_futuro_recalculada);
                            }
                        }
                    }
                }

            } else {
                // No se hace nada pq no habría logs viejos para poder calcular
                // en el resto de casos se creará a partir del histórico siempre el futuro

                // en caso de itv sí se añade solo si hemos rellenado el campo itv al crear el coche
                if (tipo_rev.equals(context.getString(R.string.tipoItv))) {
                    dbCar dbc = new dbCar(context);
                    Cursor c_itv = dbc.buscarCoche(matricula);
                    if ((c_itv.moveToFirst() == true) && (!c_logs_tipo.moveToFirst())) { // Si no hay logs futuros de itv pq sino agregaría 1 cada vez q entramos
                        int int_itv = c_itv.getInt(c_itv.getColumnIndex(dbc.CN_ITV));
                        Date f_itv = funciones.long_a_date(int_itv);
                        if (int_itv != myActivity.NO_ITV) { // menor de 1975 (no se ha insertado fecha itv)
                            tipoLog miTipoLog = new tipoLog(tipo_rev, f_itv, funciones.long_a_string(int_itv), int_itv, addLog.NO_ACEITE, addLog.NO_VECES_FIL_ACEITE, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, matricula, com.carlog.gilberto.carlog.data.dbLogs.NO_REALIZADO, com.carlog.gilberto.carlog.data.dbLogs.NO_FMODIFICADA, myActivity.NO_KMS); // los kms de itv da igual pq la itv solo va por fecha
                            dbLogs.insertar(miTipoLog);
                        }
                    }
                }
            }
        }
    }
}
