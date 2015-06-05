package com.carlog.gilberto.carlog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.gc.materialdesign.views.ButtonFloat;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;




/**
 * Created by Gilberto on 29/10/2014.
 */
public class DatosIniciales extends AppCompatActivity implements ObservableScrollViewCallbacks  {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private ObservableListView listView;
    private View mImageView;
    private View mOverlayView;
    private View mListBackgroundView;
    private TextView mTitleView;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;






    private void borrarLogpulsado(final Cursor cursor, final DBLogs manager, final int posicion) {


                AlertDialog.Builder builder = new AlertDialog.Builder(DatosIniciales.this);
                builder.setMessage("¿Está seguro de querer eliminar?")
                        .setTitle("Borrar de la lista")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id_dialog) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Continuar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id_dialog) {
                                        // metodo que se debe implementar Sí
                                        //Recorremos el cursor
                                        int i = 0;
                                        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                                            if (i == posicion-1) { // la posicion del cursor coincide con la del que pulsamos en la lista
                                                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));
                                                String matricula = cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR));
                                                manager.eliminar_por_id(id);
                                                ConsultarLogs(matricula);

                                                break;
                                            }
                                            i++;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();


    }


    private void modificarLogpulsado(final Cursor cursor, final DBLogs manager, int posicion) {
        final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");

        //Recorremos el cursor
        int i = 0;
        String tipo = "";
        Intent intent = new Intent();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (i == posicion-1) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));
                tipo = cursor.getString(cursor.getColumnIndex(DBLogs.CN_TIPO));
                if(tipo.equals(TipoLog.TIPO_ACEITE)) {
                    intent = new Intent(DatosIniciales.this, modificarAceite.class);
                }
                ///////////ELSE PARA LOS DEMAS TIPOS QUE SE PUEDAN MODIFICAR CREAR ACTIVITIES
                //************************************************************************


                String txt_fecha = cursor.getString(cursor.getColumnIndex("fecha_string"));
                int aceite = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ACEITE));
                String matricula = cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR));
                int kms = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_KMS));


                TipoLog miTipo = new TipoLog(tipo, funciones.string_a_date(txt_fecha), txt_fecha, funciones.string_a_int(txt_fecha), aceite, matricula, DBLogs.NO_REALIZADO, kms);
                intent.putExtra("miTipo", miTipo);
                intent.putExtra("miCoche", miCoche);
                intent.putExtra("idLog", id);
                break;
            }
            i++;
        }

        if(tipo.equals(TipoLog.TIPO_ACEITE)) {
            startActivity(intent);
        }
        ///////////ELSE PARA LOS DEMAS TIPOS QUE SE PUEDAN MODIFICAR CREAR ACTIVITIES
        //************************************************************************


    }

    private void realizadoLogpulsado(Cursor cursor, DBLogs manager, int posicion) {
        final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");

        //Recorremos el cursor
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

            if (i == posicion-1) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));
                String matricula = cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR));

                Date now = new Date();
                System.out.println("fecha "+now);
                System.out.println("id "+id);
                System.out.println("int fecha "+funciones.date_a_int(now));

                manager.marcarRealizadoLog(id, funciones.date_a_int(now), miCoche.getKms(miCoche)); //hoy

                ConsultarLogs(matricula);

                break;
            }
            i++;
        }

    }



    private void ConsultarLogs(String matricula) {
        System.out.println("CONSULTANDO LOGS "+ matricula);
        Context contextNew = getApplicationContext();
        final DBLogs manager = new DBLogs(contextNew);
       // String[] from = new String[]{manager.CN_TIPO, "fecha_string"};
       // int[] to = new int[] {android.R.id.text1, android.R.id.text2};


        int int_now = funciones.date_a_int(new Date());

        final Cursor cursor = manager.LogsTodosOrderByFechaString(int_now, matricula);



        List<TipoLog> listaLogs = new ArrayList<TipoLog>();
        //Recorremos el cursor
        int k = 0;
        cursor.moveToFirst();
        //while (cursor.moveToNext()) {
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            TipoLog miTipoLog = new TipoLog(cursor.getString(cursor.getColumnIndex(DBLogs.CN_TIPO)),funciones.string_a_date(cursor.getString(cursor.getColumnIndex("fecha_string"))), cursor.getString(cursor.getColumnIndex("fecha_string")),
                    funciones.string_a_int(cursor.getString(cursor.getColumnIndex("fecha_string"))), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ACEITE)), cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR)),
                    cursor.getInt(cursor.getColumnIndex(DBLogs.CN_REALIZADO)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_KMS)));
            listaLogs.add(miTipoLog);

            k++;
        }




       // final SimpleCursorAdapter adaptador = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, from, to);
        //Asociamos el adaptador a la vista.


        miAdaptadorLog adapter = new miAdaptadorLog(this, listaLogs);


        listView.setAdapter(adapter);




        //Asociamos el menú contextual a los controles
        registerForContextMenu(listView);


    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_iniciales);



        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize();
        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);
        listView = (ObservableListView) findViewById(R.id.list);
        listView.setScrollViewCallbacks(this);

        // Set padding view for ListView. This is the flexible space.
        View paddingView = new View(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                mFlexibleSpaceImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);

        listView.addHeaderView(paddingView);
        //setDummyData(listView);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText("Logs próximos");
        setTitle(null);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DatosIniciales.this, "FAB is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        // mListBackgroundView makes ListView's background except header view.
        mListBackgroundView = findViewById(R.id.list_background);





       // final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");
        DBCar dbcar = new DBCar(this);
        Cursor c = dbcar.buscarCocheActivo();

        String matricula = "", marca = "", modelo = "", year = "", kms = "", itv = "";
        int int_year = MyActivity.NO_YEARS, int_kms = MyActivity.NO_KMS, int_itv = MyActivity.NO_ITV, int_kms_ini = 0, int_fecha_ini = 0;


        if (c.moveToFirst() == true) {
            matricula = c.getString(c.getColumnIndex(DBCar.CN_MATRICULA));
            marca = c.getString(c.getColumnIndex(DBCar.CN_MARCA));
            modelo = c.getString(c.getColumnIndex(DBCar.CN_MODELO));
            int_year = c.getInt(c.getColumnIndex(DBCar.CN_YEAR));
            int_kms = c.getInt(c.getColumnIndex(DBCar.CN_KMS));
            int_itv = c.getInt(c.getColumnIndex(DBCar.CN_ITV));
            int_kms_ini = c.getInt(c.getColumnIndex(DBCar.CN_KMS_INI));
            int_fecha_ini = c.getInt(c.getColumnIndex(DBCar.CN_FECHA_INI));
            year = String.valueOf(int_year);
            kms = String.valueOf(int_kms);
            itv = funciones.int_a_string(int_itv);
        }

        final TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);


        TextView text=(TextView)findViewById(R.id.matricula2);
        text.setText(matricula);

        text=(TextView)findViewById(R.id.marca2);
        text.setText(marca);

        text=(TextView)findViewById(R.id.modelo2);
        text.setText(modelo);

        text=(TextView)findViewById(R.id.year2);
        text.setText(year);

        text=(TextView)findViewById(R.id.kms2);
        text.setText(kms);

        text=(TextView)findViewById(R.id.itv2);
        text.setText(itv);

        ConsultarLogs(matricula);

        //Instanciamos el Boton
        ButtonFloat btn1 = (ButtonFloat) findViewById(R.id.add_log);

        /*
          Definimos un método OnClickListener para que
          al pulsar el botón se nos muestre la segunda actividad
        */
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatosIniciales.this, AddLog.class);


                intent.putExtra("miCoche", miCoche);

                startActivityForResult(intent, PETICION_ACTIVITY_ADD_LOG);
            }
        });






    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle();
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle() {
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
        }
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)menuInfo;


        inflater.inflate(R.menu.modificar_log, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int int_now = funciones.date_a_int(new Date());
        Context contextNew = getApplicationContext();
        final DBLogs manager = new DBLogs(contextNew);



        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        TextView text=(TextView)findViewById(R.id.matricula2);


        final Cursor cursor = manager.LogsTodosOrderByFechaString(int_now, text.getText().toString());

        switch (item.getItemId()) {
            case R.id.menu_modificarLog:
                modificarLogpulsado(cursor, manager, info.position);
                return true;
            case R.id.menu_eliminarLog:
                borrarLogpulsado(cursor, manager, info.position);
                return true;
            case R.id.menu_realizadoLog:
                realizadoLogpulsado(cursor, manager, info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private final int PETICION_ACTIVITY_ADD_LOG = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (PETICION_ACTIVITY_ADD_LOG) : {
                if (resultCode == Activity.RESULT_OK) {
                    //TipoLog miTipo = (TipoLog) data.getExtras().getSerializable("miTipo");
                    final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");
                    System.out.println("RESULTADO ACTIVITY "+miCoche.getMatricula(miCoche));
                    ConsultarLogs(miCoche.getMatricula(miCoche));


                }
                break;
            }
        }
    }



}
