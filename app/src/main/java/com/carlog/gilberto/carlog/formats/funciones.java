package com.carlog.gilberto.carlog.formats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    public static long string_a_long(String txt_fecha) {
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
        return ((fecha_newlog).getTime() / 1000);
    }

    public static long date_a_long(Date fecha) {
        return ((fecha).getTime() / 1000);
    }

    public static String long_a_string(long fecha) {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date(fecha * 1000L));
    }

    public static Date long_a_date(long dato) {
        String txt = long_a_string(dato);
        return string_a_date(txt);
    }

    public static long dias_entre_2_fechas(Date fecha1, Date fecha2) {
        final long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;
        long diferencia = ((fecha2).getTime() - (fecha1).getTime())/MILLISECS_PER_DAY;
        return diferencia;
    }

    public static Date fecha_mas_dias(Date fecha, int dias) {
        Calendar hoy = Calendar.getInstance();
        hoy.setTime(fecha);
        hoy.add(Calendar.DATE, dias);
        System.out.println(hoy.getTime());
        return(hoy.getTime());
    }

}
