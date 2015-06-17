package com.carlog.gilberto.carlog.negocio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.activity.ListaLogs;
import com.carlog.gilberto.carlog.activity.MyActivity;
import com.carlog.gilberto.carlog.adapter.miAdaptadorCoches;
import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBModelos;
import com.carlog.gilberto.carlog.formats.funciones;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Gilberto on 10/06/2015.
 */
public class CambiarCocheActivo {
    public static Toolbar toolbar;
    public static RecyclerView mRecyclerView;                           // Declaring RecyclerView
    public static miAdaptadorCoches mAdapter;
    public static RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    public static DrawerLayout Drawer;                                  // Declaring DrawerLayout

    public static ActionBarDrawerToggle mDrawerToggle;


    public static void CambiarImgLogs(Context context, Activity act, String modelo) {
        DBModelos dbm = new DBModelos(context);
        Cursor c = dbm.buscarModelos(modelo);
        if (c.moveToFirst() == true) {
            String mDrawableImg = c.getString(c.getColumnIndex(DBModelos.CN_IMG));
            int resID = context.getResources().getIdentifier(mDrawableImg, "drawable", context.getPackageName());

            ImageView img_listalogs = (ImageView) act.findViewById(R.id.image);
            img_listalogs.setImageResource(resID);
        }
    }

    // Actualiza la lista de coches en el menu
    public static void CambiarCocheActivo(final DBCar dbcar, Cursor c, final Activity act, final Context context) {
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

                Cursor c = dbcar.buscarCocheActivo();

                if (c.moveToFirst() == true) {
                    matricula_NoSeleccionada = c.getString(c.getColumnIndex(DBCar.CN_MATRICULA));
                }


                dbcar.ActualizarCocheNOActivo(matricula_NoSeleccionada);
                dbcar.ActualizarCocheActivo(matricula_seleccionada);

                c = dbcar.buscarCocheActivo();
                if(c.moveToFirst() == true) {
                    modelo_Seleccionado = c.getString(c.getColumnIndex(DBCar.CN_MODELO));
                }

                CambiarImgLogs(context, act, modelo_Seleccionado);
                
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
                mMaterialDialog.setTitle("Eliminar coche");
                mMaterialDialog.setMessage("¿Está seguro de eliminar el coche con matrícula " + matricula_seleccionada + " y todos sus logs?");
                mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View w) {
                        mMaterialDialog.dismiss();
                        TextView txtV_seleccionada = (TextView) v.findViewById(R.id.rowText);

                        String matricula_seleccionada = txtV_seleccionada.getText().toString();

                        DBCar dbcar = new DBCar(v.getContext());
                        Cursor c = dbcar.buscarCoche(matricula_seleccionada); // Necesitamos saber si el coche que vamos a borrar es el activo

                        int activo = 0;

                        if (c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(DBCar.CN_PROFILE));
                        }

                        dbcar.eliminarCoche(matricula_seleccionada); // Todo borrado en cascada de logs del coche

                        // Si el borrado era el activo debemos poner activo otro (si hay más coches)
                        if (activo == 1) {
                            Cursor c_todos = dbcar.buscarCoches();
                            if (c_todos.moveToFirst() == true) { // desde que haya un coche lo ponemos activo
                                matricula_seleccionada = c_todos.getString(c_todos.getColumnIndex(DBCar.CN_MATRICULA));
                                dbcar.ActualizarCocheActivo(matricula_seleccionada);
                            } else {
                                // Si no hay más coches no se puede poner ninguno a activo, debemos poner las etiquetas vacío y las imagenes por defecto en el drawer
                                ImageView img_marca = (ImageView) mRecyclerView.findViewById(R.id.circleView);
                                ImageView img_modelo = (ImageView) mRecyclerView.findViewById(R.id.background_modelo);
                                img_modelo.setBackgroundResource(R.drawable.modelo_inicio);
                                img_marca.setImageResource(R.drawable.logo_inicio);
                                //Al no tener coches vamos a la actividad inicial a pedir insertar coche
                                Intent intent = new Intent(context, MyActivity.class);
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

    public static void ActualizarCochesDrawer(DBCar dbcar, Activity act, Context context) {
        // Volvemos a actualizar los coches para mostrar el activo en la barra de navegacion
        Cursor cursor = dbcar.buscarCoches();

        CambiarCocheActivo(dbcar, cursor, act, context);
    /*    if (cursor.moveToFirst() == true) {
            // Actualizamos la pantalla main activity con el coche activo
            Cursor c_coche_activo = dbcar.buscarCocheActivo();



            // pero también hay que actualizar las variables globales al coche activo

            if (c_coche_activo.moveToFirst() == true) {
                matricula = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_MATRICULA));
                marca = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_MARCA));
                modelo = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_MODELO));
                int_year = c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_YEAR));
                int_kms = c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_KMS));
                int_itv = c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_ITV));
                int_kms_ini = c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_KMS_INI));
                int_fecha_ini = c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_FECHA_INI));
                year = String.valueOf(int_year);
                kms = String.valueOf(int_kms);
                itv = funciones.int_a_string(int_itv);
                int_kms_anterior = int_kms;

                RellenarPantalla();

            } else {// no se da el caso pq si entra en el primer if ya existe minimo un coche y ya hemos forzado a q sea el activo
            }
        }
        else {// Solo puede ser el caso de que borramos un coche y no tengamos más (no se pudo poner activo ningun otro
            //Ya se vació la pantalla y se pusieron las imagenes a las de por defecto
            // O al abrir el programa sin coches
            RellenarPantalla(); // para rellenar los combobox
            VaciarPantalla(); // Para quitar el coche anterior

        }*/

        if (cursor.moveToFirst() == true) {
            // Actualizamos la pantalla de logs con el coche activo
            Cursor c_coche_activo = dbcar.buscarCocheActivo();

            // pero también hay que actualizar las variables globales al coche activo

            if (c_coche_activo.moveToFirst() == true) {
                String matricula = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_MATRICULA));
               /* marca = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_MARCA));
                modelo = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_MODELO));
                int_year = c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_YEAR));
                int_kms = c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_KMS));
                int_itv = c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_ITV));
                int_kms_ini = c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_KMS_INI));
                int_fecha_ini = c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_FECHA_INI));
                year = String.valueOf(int_year);
                kms = String.valueOf(int_kms);
                itv = funciones.int_a_string(int_itv);
                int_kms_anterior = int_kms; */

                // RellenarPantalla();
                ListaLogs ll = new ListaLogs();
                ll.ConsultarLogs(context, act);


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
