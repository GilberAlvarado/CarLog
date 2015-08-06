package com.carlog.gilberto.carlog.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.negocio.notificaciones;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.data.dbTiposRevision;
import com.carlog.gilberto.carlog.formats.funciones;

import java.util.Date;
import java.util.List;


public class miAdaptadorLog extends ArrayAdapter {
    private final Activity activity;
    private final List datos;

    public miAdaptadorLog(Activity activity, List datos) {
        super(activity, R.layout.list_logs , datos);
        this.activity = activity;
        this.datos = datos;
    }



    public static Bitmap decodeFile(Context context,int resId) {
        try {
            // decode image size
            Context mcontext=context;
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(mcontext.getResources(), resId, o);
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 200;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true)
            {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeResource(mcontext.getResources(), resId, o2);
        } catch (Exception e) {
        }
        return null;
    }


    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage,int width) {
        int targetWidth = width;
        int targetHeight = width;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
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
            sqView.img_tipo_log = (ImageView) rowView.findViewById(R.id.img_tipo_log);

            // Cache the view objects in the tag,
            // so they can be re-accessed later
            rowView.setTag(sqView);
        } else {
            sqView = (LogView) rowView.getTag();
        }

        // Transfer the stock data from the data object
        // to the view objects
        tipoLog miLog = (tipoLog) datos.get(position);
        String tipo_log = miLog.getTipo((tipoLog) datos.get(position));

        dbTiposRevision dbtr = new dbTiposRevision(getContext());
        Cursor c_img = dbtr.buscarTipo(tipo_log);
        String img = "";
        if (c_img.moveToFirst() == true) {
            img = c_img.getString(c_img.getColumnIndex(dbTiposRevision.CN_IMG));
        }
        int resID = getContext().getResources().getIdentifier(img, "drawable", getContext().getPackageName());

        sqView.tipo.setText(miLog.getTipo((tipoLog) datos.get(position)));
        sqView.fecha.setText(miLog.getFechatxt((tipoLog) datos.get(position)));
        sqView.img_tipo_log.setImageBitmap(getRoundedShape(decodeFile(getContext(), resID), 200));

        if(miLog.getRealizado((tipoLog) datos.get(position)) == dbLogs.NO_REALIZADO) {
            if (miLog.getFechalong((tipoLog) datos.get(position)) <= funciones.date_a_long(funciones.fecha_mas_dias(new Date(), notificaciones.DIAS_SEMANA))) {
                sqView.tipo.setTextColor(Color.RED);
                sqView.fecha.setTextColor(Color.RED);
                sqView.tipo.setTypeface(null, Typeface.BOLD);
                sqView.fecha.setTypeface(null, Typeface.BOLD);
            }
            else {
                sqView.tipo.setTextColor(Color.BLACK);
                sqView.fecha.setTextColor(Color.GRAY);
                sqView.tipo.setTypeface(null, Typeface.BOLD);
                sqView.fecha.setTypeface(null, Typeface.NORMAL);
            }
        }
        else if(miLog.getRealizado((tipoLog) datos.get(position)) == dbLogs.REALIZADO) {
            sqView.tipo.setTextColor(Color.parseColor("#2196F3"));
            sqView.fecha.setTextColor(Color.GRAY);
            sqView.tipo.setTypeface(null, Typeface.BOLD);
            sqView.fecha.setTypeface(null, Typeface.NORMAL);
        }

        return rowView;
    }

    protected static class LogView {
        protected TextView tipo;
        protected TextView fecha;
        protected ImageView img_tipo_log;
    }
}