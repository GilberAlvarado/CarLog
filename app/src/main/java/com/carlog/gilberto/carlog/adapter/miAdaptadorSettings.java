package com.carlog.gilberto.carlog.adapter;

import android.app.Activity;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.data.dbSettings;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.tiposClases.tipoSettings;

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
            rowView = inflater.inflate(R.layout.list_settings, null);

            // Hold the view objects in an object,
            // so they don't need to be re-fetched
            sqView = new LogView();
            sqView.tipo_settings = (TextView) rowView.findViewById(R.id.Lbl_tipo_settings);
            sqView.img_settings = (ImageView) rowView.findViewById(R.id.img_settings);
            sqView.chbx_settings = (CheckBox) rowView.findViewById(R.id.chbx_settings);

            // Cache the view objects in the tag,
            // so they can be re-accessed later
            rowView.setTag(sqView);
        } else {
            sqView = (LogView) rowView.getTag();
        }

        // Transfer the stock data from the data object
        // to the view objects
        int resID;

        sqView.tipo_settings.setText(values[position]);

        if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.ayuda))) {
            sqView.chbx_settings.setVisibility(View.GONE);
            resID = getContext().getResources().getIdentifier("ic_ayuda", "drawable", getContext().getPackageName());
        }
        else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.notificaciones))) {
            resID = getContext().getResources().getIdentifier("ic_notificaciones", "drawable", getContext().getPackageName());
            sqView.chbx_settings.setVisibility(View.VISIBLE);
        }
        else {
            sqView.chbx_settings.setVisibility(View.VISIBLE);
            resID = getContext().getResources().getIdentifier("ic_revisiones_settings", "drawable", getContext().getPackageName());
        }


        sqView.img_settings.setImageResource(resID);


        dbSettings manager = new dbSettings(getContext());
        Cursor c = manager.getSettings();
        int activo;
        if(c.moveToFirst() == true) {
            if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.notificaciones))) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_NOTIFICACIONES));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_ACEITE)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_ACEITE));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_ANTICONGELANTE)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_ANTICONGELANTE));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_AMORTIGUADORES)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_AMORTIGUADORES));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_BATERIA)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_BATERIA));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_BOMBA_AGUA)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_BOMBAAGUA));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_BUJIAS)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_BUJIAS));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_CORREA)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_CORREA));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_EMBRAGUE)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_EMBRAGUE));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_FRENOS)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_FRENOS));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_FILTRO_AIRE)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_FILAIRE));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_FILTRO_ACEITE)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_FILACEITE));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_FILTRO_GASOLINA)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_FILGASOLINA));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_LUCES)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_LUCES));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_LIQUIDO_FRENOS)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_LIQFRENOS));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_LIMPIAPARABRISAS)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_LIMPIAPARABRISAS));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_RUEDAS)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_RUEDAS));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_REV_GENERAL)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_REVGEN));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
            else if(sqView.tipo_settings.getText().toString().equals(activity.getString(R.string.predecir) +  tipoLog.TIPO_ITV)) {
                activo = c.getInt(c.getColumnIndex(dbSettings.CN_ITV));
                if (activo == tipoSettings.ACTIVO) sqView.chbx_settings.setChecked(true);
                else sqView.chbx_settings.setChecked(false);
            }
        }


        return rowView;
    }


    protected static class LogView {
        protected TextView tipo_settings;
        protected ImageView img_settings;
        protected CheckBox chbx_settings;
    }
}