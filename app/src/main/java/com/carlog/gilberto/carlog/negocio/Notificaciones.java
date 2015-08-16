package com.carlog.gilberto.carlog.negocio;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.activity.listaLogs;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.data.dbTiposRevision;
import com.carlog.gilberto.carlog.formats.funciones;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gilberto on 23/07/2015.
 */
public class notificaciones extends IntentService {

    public static int DIAS_SEMANA = 7;
    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public notificaciones() {
        super("Notificaciones");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Calendar firingCal= Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 10); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, 0); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second

        if(firingCal.compareTo(currentCal) < 0) {
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        long endTime = firingCal.getTimeInMillis();
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        //long endTime = System.currentTimeMillis() + 5*1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                    searchNotifications();
                } catch (Exception e) {
                }
            }
        }
    }

    private void displayNotifications(String matricula, String tipo_rev, String fecha, int contador) {
        int notificationID = contador;

        Intent intent = new Intent(this, listaLogs.class);
        intent.putExtra("notificationID", notificationID);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        CharSequence ticker ="Tiene " + contador + " revisiones cercanas";
        CharSequence contentTitle = "" + matricula + " " + tipo_rev + " -> " + fecha;
        CharSequence contentText = "Y " + (contador - 1) + " revisiones más...";
       /* for (int i = 0; i < l_texto.size(); i++) {
            contentText = contentText+l_texto.get(i).toString();
        }
        contentText = contentText+l_texto.get(i).toString();*/
        Notification noti = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setTicker(ticker)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_launcher)
                .addAction(R.drawable.ic_launcher, ticker, pendingIntent)
                .setLights(Color.BLUE, 500, 500)
                .setVibrate(new long[] {100, 250, 100, 500})
                .build();
        noti.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        //noti.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
        nm.notify(notificationID, noti);


    }

    // procedimiento que se ejecuta cada día a las 10:00 para comprobar si hay alarmas
    private void searchNotifications() {
        dbLogs dbl = new dbLogs(getApplicationContext());
        dbTiposRevision dbtr = new dbTiposRevision(getApplicationContext()); // necesitamos notificaciones para todos los tiprev incluidas las personalizadas por el usuario
        Cursor c_tr = dbtr.cargarCursorTiposRevision();
        int contador_alarmas = 0;
        String matricula = "";
        String tipo_rev = "";
        String txt_date = "";
        //List<String> l_texto = new ArrayList<String>();
        for(c_tr.moveToFirst(); !c_tr.isAfterLast(); c_tr.moveToNext()) {
            String tipo = c_tr.getString(c_tr.getColumnIndex(dbTiposRevision.CN_TIPO));
            Cursor c_log = dbl.LogsTipoTodosCochesOrderByFechaString(tipo);
            for(c_log.moveToFirst(); !c_log.isAfterLast(); c_log.moveToNext()) {
                txt_date = c_log.getString(c_log.getColumnIndex("fecha_string"));
                Date f_semana = funciones.fecha_mas_dias(new Date(), DIAS_SEMANA);
                if (funciones.string_a_long(txt_date) < funciones.date_a_long(f_semana)) {
                    matricula = c_log.getString(c_log.getColumnIndex(dbLogs.CN_CAR));
                    tipo_rev = c_log.getString(c_log.getColumnIndex(dbLogs.CN_TIPO));
                    contador_alarmas++;
                    //l_texto.add("Coche: " + matricula + " " + tipo_rev + " -> " + txt_date);
                }
            }
        }
        if(contador_alarmas > 0) {
            displayNotifications(matricula, tipo_rev, txt_date, contador_alarmas);
        }

    }
}