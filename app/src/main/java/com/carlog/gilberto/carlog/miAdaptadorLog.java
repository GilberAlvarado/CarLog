package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class miAdaptadorLog extends ArrayAdapter {
    private final Activity activity;
    private final List datos;

    public miAdaptadorLog(Activity activity, List datos) {
        super(activity, R.layout.list_logs , datos);
        this.activity = activity;
        this.datos = datos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        LogView sqView = null;

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_logs, null);

            // Hold the view objects in an object,
            // so they don't need to be re-fetched
            sqView = new LogView();
            sqView.tipo = (TextView) rowView.findViewById(R.id.Lbl_tipo_log);
            sqView.fecha = (TextView) rowView.findViewById(R.id.Lbl_fecha_log);

            // Cache the view objects in the tag,
            // so they can be re-accessed later
            rowView.setTag(sqView);
        } else {
            sqView = (LogView) rowView.getTag();
        }

        // Transfer the stock data from the data object
        // to the view objects
        TipoLog miLog = (TipoLog) datos.get(position);
        sqView.tipo.setText(miLog.getTipo((TipoLog) datos.get(position)));
        sqView.fecha.setText(miLog.getFechatxt((TipoLog) datos.get(position)));
        if(miLog.getFechaint((TipoLog) datos.get(position)) <= funciones.date_a_int(funciones.fecha_mas_dias(1))) {
            sqView.tipo.setBackgroundColor(Color.RED);
            sqView.fecha.setBackgroundColor(Color.RED);
        }

        return rowView;
    }

    protected static class LogView {
        protected TextView tipo;
        protected TextView fecha;
    }
}