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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.formats.DocumentHelper;
import com.carlog.gilberto.carlog.formats.Utilities;
import com.carlog.gilberto.carlog.negocio.CambiarCocheActivo;
import com.carlog.gilberto.carlog.negocio.ProcesarTipos;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;
import com.carlog.gilberto.carlog.adapter.miAdaptadorLog;
import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.Usuario;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


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
    private CircleImageView profileImg;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;
    private int mToolbarColor;
    private View mToolbar;
    private Toolbar toolbar;
    private FloatingActionMenu mFloatMenu;

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
                            ConsultarLogs(getApplicationContext(), ListaLogs.this);
                            break;
                        }
                        i++;
                    }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void modificarLogpulsado(final Cursor cursor, int posicion, Activity context) {
        //Recorremos el cursor
        int i = 0;
        String tipo = "";
        Intent intent = new Intent();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (i == posicion-1) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));
                tipo = cursor.getString(cursor.getColumnIndex(DBLogs.CN_TIPO));
                if(tipo.equals(TipoLog.TIPO_ACEITE)) {
                    intent = new Intent(context, ModificarAceite.class);
                }
                if(tipo.equals(TipoLog.TIPO_REV_GENERAL)) {
                    intent = new Intent(context, ModificarRevGral.class);
                }
                if(tipo.equals(TipoLog.TIPO_CORREA)) {
                    intent = new Intent(context, ModificarCorrea.class);
                }
                if(tipo.equals(TipoLog.TIPO_BOMBA_AGUA)) {
                    intent = new Intent(context, ModificarBombaAgua.class);
                }
                if(tipo.equals(TipoLog.TIPO_FILTRO_GASOLINA)) {
                    intent = new Intent(context, ModificarFiltroGasolina.class);
                }
                if(tipo.equals(TipoLog.TIPO_FILTRO_AIRE)) {
                    intent = new Intent(context, ModificarFiltroAire.class);
                }
                if(tipo.equals(TipoLog.TIPO_BUJIAS)) {
                    intent = new Intent(context, ModificarBujias.class);
                }
                if(tipo.equals(TipoLog.TIPO_EMBRAGUE)) {
                    intent = new Intent(context, ModificarEmbrague.class);
                }
                if(tipo.equals(TipoLog.TIPO_ITV)) {
                    intent = new Intent(context, ModificarItv.class);
                }
                if(tipo.equals(TipoLog.TIPO_FILTRO_ACEITE)) {
                    intent = new Intent(context, ModificarFiltroAceite.class);
                }
                //************************************************************************


                String txt_fecha = cursor.getString(cursor.getColumnIndex("fecha_string"));
                int aceite = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ACEITE));
                int revgral = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_REVGRAL));
                int correa = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_CORREA));
                int bombaagua = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_BOMBAAGUA));
                int fgasolina = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_FGASOLINA));
                int faire = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_FAIRE));
                int bujias = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_BUJIAS));
                int embrague = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_EMBRAGUE));
                String matricula = cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR));
                int kms = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_KMS));
                int veces_filaceite = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_VECES_FIL_ACEITE));


                TipoLog miTipo = new TipoLog(tipo, funciones.string_a_date(txt_fecha), txt_fecha, funciones.string_a_long(txt_fecha), aceite, veces_filaceite, AddLog.NO_CONTADOR_FIL_ACEITE, revgral, correa, bombaagua, fgasolina, faire, bujias, embrague, matricula, DBLogs.NO_REALIZADO, DBLogs.NO_FMODIFICADA, kms);
                intent.putExtra("miTipo", miTipo);
                intent.putExtra("idLog", id);
                break;
            }
            i++;
        }

        if(tipo.equals(TipoLog.TIPO_ACEITE)) {
            context.startActivityForResult(intent, PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(TipoLog.TIPO_REV_GENERAL)) {
            context.startActivityForResult(intent, PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(TipoLog.TIPO_ITV)) {
            context.startActivityForResult(intent, PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(TipoLog.TIPO_CORREA)) {
            context.startActivityForResult(intent, PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(TipoLog.TIPO_BOMBA_AGUA)) {
            context.startActivityForResult(intent, PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(TipoLog.TIPO_FILTRO_GASOLINA)) {
            context.startActivityForResult(intent, PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(TipoLog.TIPO_FILTRO_AIRE)) {
            context.startActivityForResult(intent, PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(TipoLog.TIPO_EMBRAGUE)) {
            context.startActivityForResult(intent, PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(TipoLog.TIPO_BUJIAS)) {
            context.startActivityForResult(intent, PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(TipoLog.TIPO_FILTRO_ACEITE)) {
            context.startActivity(intent); // No cambia la fecha solo cada cuantos cambios de aceite se hará
        }
        ///////////Todo ELSE PARA LOS DEMAS TIPOS QUE SE PUEDAN MODIFICAR CREAR ACTIVITIES
        //************************************************************************


    }

    private void realizadoLogpulsado(Cursor cursor, DBLogs manager, int posicion, boolean hoy) {
        Date f_hoy = new Date();
        Date f_revision_por_fecha = new Date();
        //Recorremos el cursor
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (i == posicion-1) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));
                String tipo_rev = cursor.getString(cursor.getColumnIndex(DBLogs.CN_TIPO));
                String txtfecha_rev = cursor.getString(cursor.getColumnIndex("fecha_string"));
                Date f_rev = funciones.string_a_date(txtfecha_rev);
                if(tipo_rev.equals(TipoLog.TIPO_ITV)) {
                    if(hoy) f_revision_por_fecha = funciones.fecha_mas_dias(f_hoy, ProcesarTipos.F_MAX_ITV);
                    else f_revision_por_fecha = funciones.fecha_mas_dias(f_rev, ProcesarTipos.F_MAX_ITV);
                    DBCar dbc = new DBCar(getApplicationContext());
                    long long_revision_por_fecha = funciones.date_a_long(f_revision_por_fecha);
                    dbc.ActualizarITVCocheActivo(matricula, long_revision_por_fecha);
                    TipoLog miTipoLog = new TipoLog(tipo_rev, f_revision_por_fecha, funciones.long_a_string(long_revision_por_fecha), long_revision_por_fecha, AddLog.NO_ACEITE, AddLog.NO_VECES_FIL_ACEITE, AddLog.NO_CONTADOR_FIL_ACEITE, AddLog.NO_REVGRAL, AddLog.NO_CORREA, AddLog.NO_BOMBAAGUA, AddLog.NO_FGASOLINA, AddLog.NO_FAIRE, AddLog.NO_BUJIAS, AddLog.NO_EMBRAGUE, matricula, DBLogs.NO_REALIZADO, DBLogs.NO_FMODIFICADA, MyActivity.NO_KMS); // no depende de los kms sino de la fecha de realizado
                    manager.insertar(miTipoLog);
                }
                if(tipo_rev.equals(TipoLog.TIPO_ACEITE)) {
                    // Si tiene revisiones de filtro de aceite tenemos que leer cada cuantos cambios de aceite se cambia el filtro
                    Cursor c_fil = manager.LogsTipoOrderByFechaString(matricula, TipoLog.TIPO_FILTRO_ACEITE);
                    if (c_fil.moveToFirst() == true) {
                        int id_log_fil = c_fil.getInt(c_fil.getColumnIndex(DBLogs.CN_ID));
                        int int_veces = c_fil.getInt(c_fil.getColumnIndex(DBLogs.CN_VECES_FIL_ACEITE));
                        // tenemos que ver como va el contador y compararlo con los cambios defiltro que se han realizado
                        Cursor c_ac_hist = manager.LogsHistoricoTipoOrderByFechaString(matricula, TipoLog.TIPO_ACEITE);
                        if (c_ac_hist.moveToFirst() == true) {
                            int int_contador = c_ac_hist.getInt(c_ac_hist.getColumnIndex(DBLogs.CN_CONTADOR_FIL_ACEITE));
                            if (int_contador < int_veces) { // + 1 pq empieza en 0
                                manager.ActualizarContadorFilAceite(id, int_contador + 1);
                            } else { // si se sobrepasa marcamos como realizado también el log del filtro y reseteamos el contador
                                if(hoy) manager.marcarRealizadoLog(id_log_fil, funciones.date_a_long(f_hoy), int_kms); //hoy
                                else manager.marcarRealizadoLog(id_log_fil, funciones.date_a_long(f_rev), int_kms);
                                manager.ActualizarContadorFilAceite(id, 1);
                            }
                        }
                        else { // si tiene futuro filtro pero no tiene histórico de aceite hay que incrementar el contador
                            //pero si justo int_veces es 1 hay que marcar como realizado tambien el log de filtro y resetear el contador
                            if(int_veces == 1) {
                                if(hoy) manager.marcarRealizadoLog(id_log_fil, funciones.date_a_long(f_hoy), int_kms); //hoy
                                else manager.marcarRealizadoLog(id_log_fil, funciones.date_a_long(f_rev), int_kms);
                                manager.ActualizarContadorFilAceite(id, 1);
                            }
                            else manager.ActualizarContadorFilAceite(id, 2); // se incrementa el contador en 1
                        }
                    }
                }
                if(tipo_rev.equals(TipoLog.TIPO_FILTRO_ACEITE)) {
                    // Marcamos como realizado tambien el futuro de aceite y reseteamos el contador pq acabamos de hacer un cambio de filtro
                    Cursor c_ac = manager.LogsTipoOrderByFechaString(matricula, TipoLog.TIPO_ACEITE);
                    if (c_ac.moveToFirst() == true) {
                        int id_log_ac = c_ac.getInt(c_ac.getColumnIndex(DBLogs.CN_ID));
                        if(hoy) manager.marcarRealizadoLog(id_log_ac, funciones.date_a_long(f_hoy), int_kms); //hoy
                        else manager.marcarRealizadoLog(id_log_ac, funciones.date_a_long(f_rev), int_kms);
                        manager.ActualizarContadorFilAceite(id_log_ac, 1);
                    }
                }
                if(hoy) manager.marcarRealizadoLog(id, funciones.date_a_long(f_hoy), int_kms); //hoy
                else manager.marcarRealizadoLog(id, funciones.date_a_long(f_rev), int_kms);

                ConsultarLogs(getApplicationContext(), ListaLogs.this);
                break;
            }
            i++;
        }
    }

    private void modificarLogPulsando(final Activity act) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final DBLogs manager = new DBLogs(act.getApplicationContext());
                final Cursor cursor = manager.LogsFuturosOrderByFechaString(matricula);
                modificarLogpulsado(cursor, position, act);
            }
        });
    }

    public void ConsultarLogs(Context context, Activity act) {
        DBCar dbc = new DBCar(context);
        Cursor c_activo = dbc.buscarCocheActivo();
        if (c_activo.moveToFirst() == true) {
            matricula = c_activo.getString(c_activo.getColumnIndex(DBCar.CN_MATRICULA));
        }
        final DBLogs manager = new DBLogs(context);
        final Cursor cursor = manager.LogsFuturosOrderByFechaString(matricula);

        List<TipoLog> listaLogs = new ArrayList<TipoLog>();
        //Recorremos el cursor
        int k = 0;
        cursor.moveToFirst();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            TipoLog miTipoLog = new TipoLog(cursor.getString(cursor.getColumnIndex(DBLogs.CN_TIPO)),funciones.string_a_date(cursor.getString(cursor.getColumnIndex("fecha_string"))), cursor.getString(cursor.getColumnIndex("fecha_string")),
                    funciones.string_a_long(cursor.getString(cursor.getColumnIndex("fecha_string"))), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ACEITE)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_VECES_FIL_ACEITE)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_CONTADOR_FIL_ACEITE)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_REVGRAL))
                    , cursor.getInt(cursor.getColumnIndex(DBLogs.CN_CORREA)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_BOMBAAGUA)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_FGASOLINA)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_FAIRE)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_BUJIAS)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_EMBRAGUE)), cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR)),
                    cursor.getInt(cursor.getColumnIndex(DBLogs.CN_REALIZADO)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_FMODIFICADA)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_KMS)));
            listaLogs.add(miTipoLog);

            k++;
        }

        miAdaptadorLog adapter = new miAdaptadorLog(act, listaLogs);
        listView.setAdapter(adapter);
        modificarLogPulsando(act);
        //Asociamos el menú contextual a los controles para las opciones en longClick
        registerForContextMenu(listView);
    }

    private void imagenFB(final Activity activity) {
        String idFB = Login.getIdFacebook(activity);
        profileImg = (CircleImageView) findViewById(R.id.profilePicture);
        if (idFB != null) {
            try {
                mTitleView = (TextView) findViewById(R.id.title);
                //mTitleView.setText("Próximas revisiones");
                mTitleView.setText(Login.getNameFacebook(activity));
                System.out.println("profileImg "+profileImg);
                if (Login.getImgProfileFacebook(activity) != null) {
                    profileImg.setImageBitmap(Login.getImgProfileFacebook(activity));
                }
                else {
                    Thread thread=  new Thread(){
                        @Override
                        public void run(){
                            try {
                                synchronized(this){
                                    while (Login.getImgProfileFacebook(activity) == null){
                                        wait(1000);
                                        if (Login.getImgProfileFacebook(activity) != null) {
                                            ListaLogs.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    profileImg.setImageBitmap(Login.getImgProfileFacebook(activity));
                                                }
                                            });
                                            break;
                                        }
                                    }
                                }
                            }
                            catch(InterruptedException ex){
                            }
                        }
                    };

                    thread.start();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else { //sesión iniciada por registro
            Usuario usuario = new Usuario();
            if(usuario.isUserLoggedIn(this)) {
                System.out.println("Logueado");
                usuario.readUser(this);
                String nombre = usuario.getName();
                mTitleView = (TextView) findViewById(R.id.title);
                mTitleView.setText(nombre);
            }
        }
    }

    private void ObservableScrollView(Activity activity) {
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        imagenFB(activity);

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
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mFlexibleSpaceImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);

        listView.addHeaderView(paddingView);
        mTitleView = (TextView) findViewById(R.id.title);
        setTitle(null);
        mFab = findViewById(R.id.fab);
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        // mListBackgroundView makes ListView's background except header view.
        mListBackgroundView = findViewById(R.id.list_background);

        ScrollUtils.addOnGlobalLayoutListener(listView, new Runnable() {
            @Override
            public void run() {
                listView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);
                listView.scrollTo(0, 1);
                listView.scrollTo(0, 0);
            }
        });

        LinearLayout difuminado = (LinearLayout) findViewById(R.id.difuminado_layout);
        difuminado.setBackgroundColor(ScrollUtils.getColorWithAlpha(0.4f, mToolbarColor));
        buildFloatingMenu();
    }


    private void buildFloatingMenu() {
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setLayoutParams(new FrameLayout.LayoutParams(140, 140));

        // Añadir imagen
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_actualizarcoche));
        itemIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        SubActionButton addImageButton = itemBuilder.setContentView(itemIcon).build();
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.selectImage(ListaLogs.this);
            }
        });

        // Actualizar coche
        itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_actualizarcoche));
        itemIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        SubActionButton edytCarButton = itemBuilder.setContentView(itemIcon).build();
        edytCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaLogs.this, MyActivity.class);
                intent.putExtra("EditarCoche", true);
                startActivity(intent);
            }
        });

        // Añadir nuevo coche
        itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_addcoche));
        itemIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        SubActionButton addCarButton = itemBuilder.setContentView(itemIcon).build();
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaLogs.this, MyActivity.class);
                intent.putExtra("CocheNuevo", true);
                startActivityForResult(intent, PETICION_ACTIVITY_DONT_BACK_IF_ADDCAR);
                // Si agregamos un nuevo coche y volvemos hacia atras se sale de la app pero desde la pantalla de logs puesto que ya hemos agregado un coche y por lo tanto no se queda el drawer sin el coche nuevo al volver atras
                // Si no queremos agregar nuevo coche y pulsamos hacia atras regresamos a la lista de logs anterior
            }
        });

        // Añadir nuevo Log
        itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_addlog));
        itemIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        SubActionButton addLogButton = itemBuilder.setContentView(itemIcon).build();
        addLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaLogs.this, AddLog.class);
                startActivityForResult(intent, PETICION_ACTIVITY_ADD_LOG);
            }
        });

        mFloatMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(addImageButton)
                .addSubActionView(edytCarButton)
                .addSubActionView(addCarButton)
                .addSubActionView(addLogButton)
                .attachTo(mFab)
                .build();
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


    private void modificarFechasRevisiones(Context context) {
        Boolean modifyItv = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyItv = getIntent().getExtras().getBoolean("modifyItv");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha itv");
        }
        Boolean modifyRevGral = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyRevGral = getIntent().getExtras().getBoolean("modifyRevGral");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha revgral");
        }
        Boolean modifyAceite = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyAceite = getIntent().getExtras().getBoolean("modifyAceite");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha aceite");
        }
        Boolean modifyCorrea = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyCorrea = getIntent().getExtras().getBoolean("modifyCorrea");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha correa");
        }
        Boolean modifyBombaAgua = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyBombaAgua = getIntent().getExtras().getBoolean("modifyBombaAgua");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha bomba de agua");
        }
        Boolean modifyFiltroGasolina = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyFiltroGasolina = getIntent().getExtras().getBoolean("modifyFiltroGasolina");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha filtro de gasolina");
        }
        Boolean modifyFiltroAire = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyFiltroAire = getIntent().getExtras().getBoolean("modifyFiltroAire");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha filtro de aire");
        }
        Boolean modifyBujias = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyBujias = getIntent().getExtras().getBoolean("modifyBujias");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha bujías");
        }
        Boolean modifyEmbrague = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyEmbrague = getIntent().getExtras().getBoolean("modifyEmbrague");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha embrague");
        }
        if((modifyItv) || (modifyAceite) || (modifyRevGral) || (modifyCorrea) || (modifyBombaAgua) ||
                (modifyFiltroGasolina) || (modifyFiltroAire) || (modifyBujias) || (modifyEmbrague))
            ConsultarLogs(context, ListaLogs.this); // para actualizar la fecha de revision modificada
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listalogs);
        Context context = this;

        modificarFechasRevisiones(context);

        DBCar dbcar = new DBCar(context);
        Cursor c = dbcar.buscarCoches();
        CambiarCocheActivo.CambiarCocheActivo(dbcar, c, ListaLogs.this, context);
        ObservableScrollView(ListaLogs.this);
        c = dbcar.buscarCocheActivo();

        int img_modelo_cambiada = DBCar.IMG_MODELO_NOCAMBIADA;
        String img_modelo_personalizada = "";
        if (c.moveToFirst() == true) {
            matricula = c.getString(c.getColumnIndex(DBCar.CN_MATRICULA));
            marca = c.getString(c.getColumnIndex(DBCar.CN_MARCA));
            modelo = c.getString(c.getColumnIndex(DBCar.CN_MODELO));
            img_modelo_cambiada = c.getInt(c.getColumnIndex(DBCar.CN_IMG_MODELO_CAMBIADA));
            int_year = c.getInt(c.getColumnIndex(DBCar.CN_YEAR));
            int_kms = c.getInt(c.getColumnIndex(DBCar.CN_KMS));
            int_itv = c.getInt(c.getColumnIndex(DBCar.CN_ITV));
            int_kms_ini = c.getInt(c.getColumnIndex(DBCar.CN_KMS_INI));
            int_fecha_ini = c.getInt(c.getColumnIndex(DBCar.CN_FECHA_INI));
            img_modelo_personalizada = c.getString(c.getColumnIndex(DBCar.CN_IMG_MODELO_PERSONALIZADA));
        }

        CambiarCocheActivo.CambiarImgLogs(context, ListaLogs.this, img_modelo_personalizada, modelo, img_modelo_cambiada);
        ConsultarLogs(context, ListaLogs.this);
    }



    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, +ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
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
        // Scale image fb
        ViewHelper.setPivotX(profileImg, 0);
        ViewHelper.setPivotY(profileImg, 0);
        ViewHelper.setScaleX(profileImg, scale);
        ViewHelper.setScaleY(profileImg, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        if (TOOLBAR_IS_STICKY) {
            titleTranslationY = Math.max(0, titleTranslationY);
        }
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);
        ViewHelper.setTranslationX(mTitleView, 90);
        // Translate fb image
        ViewHelper.setTranslationY(profileImg, titleTranslationY);
        ViewHelper.setTranslationX(profileImg, 90);


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
            ViewHelper.setPivotX(profileImg, findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
            ViewHelper.setPivotX(profileImg, 0);
        }
    }

    private void showFab() {
        if (!mFabIsShown) {
            mFab.setVisibility(View.VISIBLE);
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();

            ViewPropertyAnimator.animate(mFab).setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    mFloatMenu.close(true);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!mFabIsShown) {
                        mFab.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            mFabIsShown = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            if (Login.getIdFacebook(this) == null){
                Login.goToLoginScreen(this);
                Usuario u = new Usuario();
                u.logout(ListaLogs.this);
                Intent intent = new Intent(ListaLogs.this, Login.class);
                startActivity(intent);
            } else {
                Login.closeFacebookSession(this, Login.class);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, "Marcar como Realizado hoy");
        menu.add(1, 1, 1, "Marcar como Realizado en su fecha");
        menu.add(2, 2, 2, "Eliminar");
     //   MenuInflater inflater = getMenuInflater();
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
 //       inflater.inflate(R.menu.modificar_log, menu);
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final DBLogs manager = new DBLogs(this);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        DBCar dbc = new DBCar(this);
        Cursor c_activo = dbc.buscarCocheActivo();
        if (c_activo.moveToFirst() == true) {
            matricula = c_activo.getString(c_activo.getColumnIndex(DBCar.CN_MATRICULA));
        }

        final Cursor cursor = manager.LogsFuturosOrderByFechaString(matricula);

        switch (item.getItemId()) {
            //case R.id.menu_realizadoLog:
            case 0:
                realizadoLogpulsado(cursor, manager, info.position, true);
                return true;
            case 1:
                realizadoLogpulsado(cursor, manager, info.position, false);
                return true;
            //case R.id.menu_eliminarLog:
            case 2:
                borrarLogpulsado(cursor, manager, info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public static final int PETICION_ACTIVITY_ADD_LOG = 1;
    public static final int PETICION_ACTIVITY_DONT_BACK_IF_ADDCAR = 2;
    public static final int PETICION_ACTIVITY_MODIFYITV = 3;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("result "+ requestCode);
        switch(requestCode) {
            case (PETICION_ACTIVITY_ADD_LOG) : {
                if (resultCode == Activity.RESULT_OK) {
                    ConsultarLogs(this, ListaLogs.this);

                }
                break;
            }
            // Si agregamos un nuevo coche y volvemos hacia atras se sale de la app pero desde la pantalla de logs puesto que ya hemos agregado un coche y por lo tanto no se queda el drawer sin el coche nuevo al volver atras
            // Si no queremos agregar nuevo coche y pulsamos hacia atras regresamos a la lista de logs anterior
            case (PETICION_ACTIVITY_DONT_BACK_IF_ADDCAR) : {
                if(resultCode == Activity.RESULT_OK) {
                    finish();
                }
            }
            case (PETICION_ACTIVITY_MODIFYITV) : {
                if (resultCode == Activity.RESULT_OK) {
                    ConsultarLogs(getApplicationContext(), ListaLogs.this);
                }
                break;
            }
            case (Utilities.GALLERY_INTENT) : {
                if (data == null)
                    return;
                Uri selectedPictureUri = data.getData();
                if (selectedPictureUri == null)
                    return;
                try {
                    Bitmap img_fondo = Utilities.getBitMapFromUri(this, selectedPictureUri);
                    File chosenFile = new File(DocumentHelper.getPath(this, selectedPictureUri));

                    DBCar dbc = new DBCar(getApplicationContext());
                    Cursor c_act = dbc.buscarCocheActivo();
                    if (c_act.moveToFirst() == true) {
                        System.out.println("paso");
                        String matricula = c_act.getString(c_act.getColumnIndex(DBCar.CN_MATRICULA));
                        String uriEncoded = Uri.encode(DocumentHelper.getPath(this, selectedPictureUri), "UTF-8");
                        dbc.ActualizarImgModelo(matricula, uriEncoded);
                        ImageView img_listalogs = (ImageView) ListaLogs.this.findViewById(R.id.image);
                        img_listalogs.setImageURI(selectedPictureUri);
                    }
                    //File chosenFile = new File(DocumentHelper.getPath(this, selectedPictureUri));
                   // Utilities.changeImage(this, chosenFile, Utilities.getBitMapFromUri(this, selectedPictureUri), selectedPictureUri.getPath(), LoginMethods.getIdFacebook(this), mSendero.getServerId(), 0, 0, pd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case (Utilities.CAMERA_INTENT) : {
              // Utilities.getCameraPictureAndUpload(this, mSendero.getServerId());
            }
        }
    }




}
