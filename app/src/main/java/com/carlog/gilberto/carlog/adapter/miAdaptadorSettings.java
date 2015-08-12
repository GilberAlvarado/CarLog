package com.carlog.gilberto.carlog.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.data.dbTiposRevision;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.negocio.notificaciones;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;

import java.util.Date;
import java.util.List;

/**
 * Created by Gilberto on 12/08/2015.
 */
public class miAdaptadorSettings extends ArrayAdapter<String> {
    private final Activity activity;
    private final String[] values;

    public miAdaptadorSettings(Activity activity, String[] values) {
        super(activity, R.layout.activity_settings , values);
        this.activity = activity;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        LogView sqView = null;

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.activity_settings, null);

            // Hold the view objects in an object,
            // so they don't need to be re-fetched
            sqView = new LogView();
            sqView.tipo_settings = (TextView) rowView.findViewById(R.id.Lbl_tipo_settings);
            sqView.img_settings = (ImageView) rowView.findViewById(R.id.img_settings);

            // Cache the view objects in the tag,
            // so they can be re-accessed later
            rowView.setTag(sqView);
        } else {
            sqView = (LogView) rowView.getTag();
        }

        // Transfer the stock data from the data object
        // to the view objects
        String img = "logo_audi";
        int resID = getContext().getResources().getIdentifier(img, "drawable", getContext().getPackageName());

        sqView.tipo_settings.setText(values[position]);
        sqView.img_settings.setImageResource(resID);


        sqView.tipo_settings.setTextColor(Color.GRAY);
        sqView.tipo_settings.setTypeface(null, Typeface.NORMAL);

        return rowView;
    }

    protected static class LogView {
        protected TextView tipo_settings;
        protected ImageView img_settings;
    }
}