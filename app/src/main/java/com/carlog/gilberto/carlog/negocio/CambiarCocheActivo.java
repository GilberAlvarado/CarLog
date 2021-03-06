package com.carlog.gilberto.carlog.negocio;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.activity.listaLogs;
import com.carlog.gilberto.carlog.activity.myActivity;
import com.carlog.gilberto.carlog.adapter.miAdaptadorCoches;
import com.carlog.gilberto.carlog.data.dbCar;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.data.dbModelos;
import com.carlog.gilberto.carlog.formats.documentHelper;
import com.carlog.gilberto.carlog.formats.utilities;
import com.carlog.gilberto.carlog.fragments.fragmentHistorial;
import com.carlog.gilberto.carlog.fragments.fragmentLogs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Gilberto on 10/06/2015.
 */
public class cambiarCocheActivo {
    public static int ID_FRAGMENT_LOGS_FUTUROS = 2131230878;

    public static Toolbar toolbar;
    public static RecyclerView mRecyclerView;                           // Declaring RecyclerView
    public static miAdaptadorCoches mAdapter;
    public static RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    public static DrawerLayout Drawer;                                  // Declaring DrawerLayout
    public static ActionBarDrawerToggle mDrawerToggle;

    public static void CambiarImgLogs(final Context context, final Activity act, final String matricula, String img_modelo_personalizada, String modelo, int img_modelo_cambiada) {
        final ImageView img_listalogs = (ImageView) act.findViewById(R.id.image);
        if(img_modelo_cambiada == dbCar.IMG_MODELO_NOCAMBIADA) {
            dbModelos dbm = new dbModelos(context);
            Cursor c = dbm.buscarModelos(modelo);
            if (c.moveToFirst() == true) {
                final String txt_modelo = c.getString(c.getColumnIndex(dbModelos.CN_IMG));
                System.out.println("Buscando imagen de modelo " + txt_modelo);
                final String descargar = "http://gilberdesign.com/Carlogserver/bbddmodelos/" + txt_modelo + ".jpg";

                final ImageLoader imageLoader = ImageLoader.getInstance();
                // Solo lee de server la 1ª vez
                imageLoader.loadImage(descargar, new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        try {
                            Uri  selectedPictureUri = utilities.getImageUri(act, loadedImage, txt_modelo);
                            String uriEncoded = Uri.encode(documentHelper.getPath(act.getApplicationContext(), selectedPictureUri), "UTF-8");
                            dbCar dbc = new dbCar(context);
                            dbc.ActualizarImgModelo(matricula, uriEncoded);
                            img_listalogs.setImageURI(selectedPictureUri);
                            cambiarCocheActivo.ActualizarCochesDrawer(dbc, act, act.getApplicationContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        }
        else {
            Uri myUri = Uri.parse(img_modelo_personalizada);
            File imgFile = new  File(utilities.getPathPictureFromUri(context, myUri));
            if(imgFile.exists()) {
                Drawable d = Drawable.createFromPath(imgFile.getAbsolutePath());
                img_listalogs.setImageDrawable(d);
            }
        }
    }

    // Actualiza la lista de coches en el menu
    public static void CambiarCocheActivo(final dbCar dbcar, Cursor c, final Activity act, final Context context) {
        mRecyclerView = (RecyclerView) act.findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new miAdaptadorCoches(c, context);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            TextView txtV_seleccionada = (TextView) v.findViewById(R.id.rowText);
            String matricula_seleccionada = txtV_seleccionada.getText().toString();
            String matricula_NoSeleccionada = "";
            String modelo_Seleccionado = "";
            int img_changed_seleccionado = dbCar.IMG_MODELO_NOCAMBIADA;
            Cursor c = dbcar.buscarCocheActivo();
            if (c.moveToFirst() == true) {
                matricula_NoSeleccionada = c.getString(c.getColumnIndex(dbCar.CN_MATRICULA));
            }
            dbcar.ActualizarCocheNOActivo(matricula_NoSeleccionada);
            dbcar.ActualizarCocheActivo(matricula_seleccionada);
            c = dbcar.buscarCocheActivo();
            String img_modelo_personalizada = "";
            if(c.moveToFirst() == true) {
                modelo_Seleccionado = c.getString(c.getColumnIndex(dbCar.CN_MODELO));
                img_changed_seleccionado = c.getInt(c.getColumnIndex(dbCar.CN_IMG_MODELO_CAMBIADA));
                img_modelo_personalizada = c.getString(c.getColumnIndex(dbCar.CN_IMG_MODELO_PERSONALIZADA));
System.out.println("lolololo "+img_changed_seleccionado + " "+img_modelo_personalizada+ " " + matricula_seleccionada);
            }
            CambiarImgLogs(context, act, matricula_seleccionada, img_modelo_personalizada, modelo_Seleccionado, img_changed_seleccionado);
            ActualizarCochesDrawer(dbcar, act, context);
            }
        });

        eliminarCoche(act, context);

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(context);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        Drawer = (DrawerLayout) act.findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        toolbar = (Toolbar) act.findViewById(R.id.tool_bar);
        mDrawerToggle = new ActionBarDrawerToggle(act, Drawer, toolbar, R.string.open_drawer, R.string.close_drawer)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
    }

    private static void eliminarCoche(final Activity act, final Context context) {
        mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
            TextView txtV_seleccionada = (TextView) v.findViewById(R.id.rowText);
            String matricula_seleccionada = txtV_seleccionada.getText().toString();
            final MaterialDialog mMaterialDialog = new MaterialDialog(v.getContext());
            mMaterialDialog.setTitle(act.getString(R.string.eliminarCoche));
            mMaterialDialog.setMessage("" + act.getString(R.string.seguroEliminarCoche) + matricula_seleccionada + " " + act.getString(R.string.seguroEliminarCoche2));
            mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View w) {
                    mMaterialDialog.dismiss();
                    TextView txtV_seleccionada = (TextView) v.findViewById(R.id.rowText);
                    String matricula_seleccionada = txtV_seleccionada.getText().toString();
                    dbCar dbcar = new dbCar(v.getContext());
                    Cursor c = dbcar.buscarCoche(matricula_seleccionada); // Necesitamos saber si el coche que vamos a borrar es el activo
                    int activo = 0;
                    if (c.moveToFirst() == true) {
                        activo = c.getInt(c.getColumnIndex(dbCar.CN_PROFILE));
                    }

                    dbLogs dbl = new dbLogs(context);
                    Cursor c_logs = dbl.LogsTodosOrderByFechaString(matricula_seleccionada);
                    dbcar.eliminarCoche(matricula_seleccionada);
                    // se borran todos los logs del coche (se debería de hacer en cascada)
                    for(c_logs.moveToFirst(); !c_logs.isAfterLast(); c_logs.moveToNext()) {
                        int id_log = c_logs.getInt(c_logs.getColumnIndex(dbLogs.CN_ID));
                        dbl.eliminar_por_id(id_log);
                    }

                    // Si el borrado era el activo debemos poner activo otro (si hay más coches)
                    if (activo == 1) {
                        Cursor c_todos = dbcar.buscarCoches();
                        if (c_todos.moveToFirst() == true) { // desde que haya un coche lo ponemos activo
                            matricula_seleccionada = c_todos.getString(c_todos.getColumnIndex(dbCar.CN_MATRICULA));
                            String img_modelo_personalizada = c_todos.getString(c_todos.getColumnIndex(dbCar.CN_IMG_MODELO_PERSONALIZADA));
                            int img_modelo_cambiada = c_todos.getInt(c_todos.getColumnIndex(dbCar.CN_IMG_MODELO_CAMBIADA));
                            String modelo = c_todos.getString(c_todos.getColumnIndex(dbCar.CN_MODELO));
                            dbcar.ActualizarCocheActivo(matricula_seleccionada);
                            CambiarImgLogs(context, act, matricula_seleccionada, img_modelo_personalizada, modelo, img_modelo_cambiada);
                        } else {
                            // Si no hay más coches no se puede poner ninguno a activo, debemos poner las etiquetas vacío y las imagenes por defecto en el drawer
                            ImageView img_marca = (ImageView) mRecyclerView.findViewById(R.id.circleView);
                            ImageView img_modelo = (ImageView) mRecyclerView.findViewById(R.id.background_modelo);
                            img_modelo.setBackgroundResource(R.drawable.modelo_inicio);
                            img_marca.setImageResource(R.drawable.logo_inicio);
                            //Al no tener coches vamos a la actividad inicial a pedir insertar coche
                            Intent intent = new Intent(context, myActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            act.finish();
                        }
                    } else {
                        // Si no era el activo da igual pq seguirá activo
                    }
                    ActualizarCochesDrawer(dbcar, act, v.getContext());
                }
            });
            mMaterialDialog.setNegativeButton("CANCEL", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMaterialDialog.dismiss();

                }
            });
            mMaterialDialog.show();
            return true;
            }
        });
    }

    public static void ActualizarCochesDrawer(dbCar dbcar, Activity act, Context context) {
        // Volvemos a actualizar los coches para mostrar el activo en la barra de navegacion
        Cursor cursor = dbcar.buscarCoches();
        CambiarCocheActivo(dbcar, cursor, act, context);
        if (cursor.moveToFirst() == true) {
            // Actualizamos la pantalla de logs con el coche activo
            Cursor c_coche_activo = dbcar.buscarCocheActivo();
            // pero también hay que actualizar las variables globales al coche activo
            if (c_coche_activo.moveToFirst() == true) {
                // RellenarPantalla();
                listaLogs ll = (listaLogs) act;
                List<Fragment> list_frag = ll.getCurrentFragment().getFragmentManager().getFragments();
                for(int i = 0; i < list_frag.size(); i++) {
                    try{
                        fragmentLogs fl = (fragmentLogs)list_frag.get(i);
                        fl.ConsultarLogs(context, act);
                    }
                    catch (ClassCastException e) {
                        try {
                            fragmentHistorial fh = (fragmentHistorial) list_frag.get(i);
                            fh.ConsultarLogsHistoricos(context, act);
                        } catch (ClassCastException e2) {

                        }
                    }
                }

            } else {// no se da el caso pq si entra en el primer if ya existe minimo un coche y ya hemos forzado a q sea el activo
            }
        }
        else {// Solo puede ser el caso de que borramos un coche y no tengamos más (no se pudo poner activo ningun otro
            //Ya se vació la pantalla y se pusieron las imagenes a las de por defecto
            // O al abrir el programa sin coches
            //  RellenarPantalla(); // para rellenar los combobox
            //   VaciarPantalla(); // Para quitar el coche anterior
        }
    }
}
