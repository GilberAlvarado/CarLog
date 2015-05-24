package com.carlog.gilberto.carlog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Gilberto on 19/05/2015.
 */
public class funciones {

    public static Date string_a_date(String txt_fecha) {
        Date fecha_newlog = new Date();
        try {

            // aca realizamos el parse, para obtener objetos de tipo Date de las Strings
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            formato.setTimeZone(TimeZone.getTimeZone("UTC")); // hay días mal en gmt+1
            fecha_newlog = formato.parse(txt_fecha);



        } catch (ParseException e) {
            // Log.e(TAG, "Funcion diferenciaFechas: Error Parse " + e);
        } catch (Exception e) {
            // Log.e(TAG, "Funcion diferenciaFechas: Error " + e);
        }
        return fecha_newlog;
    }

    public static int string_a_int(String txt_fecha) {
        Date fecha_newlog = new Date();
        try {

            // aca realizamos el parse, para obtener objetos de tipo Date de las Strings
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            formato.setTimeZone(TimeZone.getTimeZone("UTC")); // hay días mal en gmt+1
            fecha_newlog = formato.parse(txt_fecha);



        } catch (ParseException e) {
            // Log.e(TAG, "Funcion diferenciaFechas: Error Parse " + e);
        } catch (Exception e) {
            // Log.e(TAG, "Funcion diferenciaFechas: Error " + e);
        }
        int int_fecha = (int) ((fecha_newlog).getTime() / 1000);
        return int_fecha;
    }

    public static int date_a_int(Date fecha) {
        int int_fecha = (int) ((fecha).getTime() / 1000);
        return int_fecha;
    }

    public static String int_a_string(int fecha) {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date(fecha * 1000L));


    }

    public static Date int_a_date(int dato) {
        String txt = int_a_string(dato);
        return string_a_date(txt);
    }

    public static long dias_entre_2_fechas(Date fecha1, Date fecha2) {
        final long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;
        long diferencia = ((fecha2).getTime() - (fecha1).getTime())/MILLISECS_PER_DAY;
        System.out.println("DIAS ENTRE " + fecha2 + " y " + fecha1 + " = " + diferencia);
        return diferencia;

    }

    public static Date fecha_mas_dias(int dias) {
        Calendar hoy = Calendar.getInstance();
        System.out.println("DIA " + hoy.getTime() + " + "  + dias + " días =  ");
        hoy.add(Calendar.DATE, dias);
        System.out.println(hoy.getTime());
        return(hoy.getTime());
    }


}
