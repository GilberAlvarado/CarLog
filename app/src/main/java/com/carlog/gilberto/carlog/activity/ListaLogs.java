package com.carlog.gilberto.carlog.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.negocio.CambiarCocheActivo;
import com.carlog.gilberto.carlog.tiposClases.TipoCoche;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;
import com.carlog.gilberto.carlog.adapter.miAdaptadorLog;
import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.gc.materialdesign.views.ButtonFloat;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;




/**
 * Created by Gilberto on 29/10/2014.
 */
public class ListaLogs extends ActionBarActivity implements ObservableScrollViewCallbacks  {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final boolean TOOLBAR_IS_STICKY = true;

    private static ObservableListView listView;
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
    private int mToolbarColor;
    private View mToolbar;

    String matricula = "", marca = "", modelo = "", year = "", kms = "", itv = "";
    int int_year = MyActivity.NO_YEARS, int_kms = MyActivity.NO_KMS, int_itv = MyActivity.NO_ITV, int_kms_ini = 0, int_fecha_ini = 0;


    private void borrarLogpulsado(final Cursor cursor, final DBLogs manager, final int posicion) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListaLogs.this);
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
                                                ConsultarLogs(matricula, getApplicationContext(), ListaLogs.this);

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

        //Recorremos el cursor
        int i = 0;
        String tipo = "";
        Intent intent = new Intent();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (i == posicion-1) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));
                tipo = cursor.getString(cursor.getColumnIndex(DBLogs.CN_TIPO));
                if(tipo.equals(TipoLog.TIPO_ACEITE)) {
                    intent = new Intent(ListaLogs.this, ModificarAceite.class);
                }
                ///////////ELSE PARA LOS DEMAS TIPOS QUE SE PUEDAN MODIFICAR CREAR ACTIVITIES
                //************************************************************************


                String txt_fecha = cursor.getString(cursor.getColumnIndex("fecha_string"));
                int aceite = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ACEITE));
                String matricula = cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR));
                int kms = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_KMS));


                TipoLog miTipo = new TipoLog(tipo, funciones.string_a_date(txt_fecha), txt_fecha, funciones.string_a_int(txt_fecha), aceite, matricula, DBLogs.NO_REALIZADO, kms);
                intent.putExtra("miTipo", miTipo);
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

        //Recorremos el cursor
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

            if (i == posicion-1) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));

                Date now = new Date();
                System.out.println("fecha "+now);
                System.out.println("id "+id);
                System.out.println("int fecha "+funciones.date_a_int(now));

                manager.marcarRealizadoLog(id, funciones.date_a_int(now), int_kms); //hoy

                ConsultarLogs(matricula, getApplicationContext(), ListaLogs.this);

                break;
            }
            i++;
        }

    }

    private void modificarLogPulsando(final String matricula, final Context context) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                int int_now = funciones.date_a_int(new Date());
                Context contextNew = context;
                final DBLogs manager = new DBLogs(contextNew);

                final Cursor cursor = manager.LogsTodosOrderByFechaString(int_now, matricula);

                modificarLogpulsado(cursor, manager, position);
            }
        });
    }

    public void ConsultarLogs(String matricula, Context context, Activity act) {

        final DBLogs manager = new DBLogs(context);

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


        miAdaptadorLog adapter = new miAdaptadorLog(act, listaLogs);


        listView.setAdapter(adapter);


        modificarLogPulsando(matricula, context);
        //Asociamos el menú contextual a los controles para las opciones en longClick
        registerForContextMenu(listView);


    }

    private void DiseñoObservableScrollView() {
        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize();
        mToolbarColor = getResources().getColor(R.color.ColorPrimary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar = findViewById(R.id.tool_bar);
        if (!TOOLBAR_IS_STICKY) {
            mToolbar.setBackgroundColor(Color.TRANSPARENT);
        }


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
        mTitleView.setText("Próximas revisiones");
        setTitle(null);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListaLogs.this, "FAB is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        // mListBackgroundView makes ListView's background except header view.
        mListBackgroundView = findViewById(R.id.list_background);

        ScrollUtils.addOnGlobalLayoutListener(listView, new Runnable() {
            @Override
            public void run() {
                listView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);

                // If you'd like to start from scrollY == 0, don't write like this:
                //mScrollView.scrollTo(0, 0);
                // The initial scrollY is 0, so it won't invoke onScrollChanged().
                // To do this, use the following:
                //onScrollChanged(0, false, false);

                // You can also achieve it with the following codes.
                // This causes scroll change from 1 to 0.
                listView.scrollTo(0, 1);
                listView.scrollTo(0, 0);
            }
        });

        LinearLayout difuminado = (LinearLayout) findViewById(R.id.difuminado_layout);
        difuminado.setBackgroundColor(ScrollUtils.getColorWithAlpha(0.4f, mToolbarColor));

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

    private void AgregarLog(final TipoCoche miCoche) {
        //Instanciamos el Boton
        ButtonFloat btn1 = (ButtonFloat) findViewById(R.id.add_log);

        /*
          Definimos un método OnClickListener para que
          al pulsar el botón se nos muestre la segunda actividad
        */
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaLogs.this, AddLog.class);


                intent.putExtra("miCoche", miCoche);

                startActivityForResult(intent, PETICION_ACTIVITY_ADD_LOG);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("ADIOSSSSSSSSSSS");


        setContentView(R.layout.activity_listalogs);



        Context context = getApplicationContext();

        DBCar dbcar = new DBCar(context);
        Cursor c = dbcar.buscarCoches();

        CambiarCocheActivo.CambiarCocheActivo(dbcar, c, ListaLogs.this, context);

        DiseñoObservableScrollView();

        c = dbcar.buscarCocheActivo();

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
        }

        final TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);




        ConsultarLogs(matricula, context, ListaLogs.this);
        AgregarLog(miCoche);


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
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        if (TOOLBAR_IS_STICKY) {
            titleTranslationY = Math.max(0, titleTranslationY);
        }
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);
        ViewHelper.setTranslationX(mTitleView, 90);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 3;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 3,
                mActionBarSize - mFab.getHeight() / 3,
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
        if (TOOLBAR_IS_STICKY) {
            // Change alpha of toolbar background
            if (-scrollY + mFlexibleSpaceImageHeight <= mActionBarSize) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, mToolbarColor));
            } else {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolbarColor));
            }
        } else {
            // Translate Toolbar
            if (scrollY < mFlexibleSpaceImageHeight) {
                ViewHelper.setTranslationY(mToolbar, 0);
            } else {
                ViewHelper.setTranslationY(mToolbar, -scrollY);
            }
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



        final Cursor cursor = manager.LogsTodosOrderByFechaString(int_now, matricula);

        switch (item.getItemId()) {
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
                    System.out.println("RESULTADO ACTIVITY "+matricula);
                    ConsultarLogs(matricula, getApplicationContext(), ListaLogs.this);


                }
                break;
            }
        }
    }



}
