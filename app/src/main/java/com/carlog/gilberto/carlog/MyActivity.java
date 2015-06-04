package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.data.DBMarcas;
import com.carlog.gilberto.carlog.data.DBModelos;
import com.gc.materialdesign.views.ButtonFloat;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class MyActivity extends ActionBarActivity {

    public final static String INICIAL_MARCA = "Introduzca marca";
    public final static String INICIAL_YEAR = "Introduzca año";
    public final static String INICIAL_KMS = "Introduzca Kms";
    public final static String INICIAL_MATRICULA = "Introduzca matrícula";
    public final static String INICIAL_MODELO = "Introduzca Modelo";




    private Toolbar toolbar;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    //RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    miAdaptadorCoches mAdapter;
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;



    private Spinner spinner_marcas;
    private Spinner spinner_modelos;
    private Spinner spinner_years;
    private List<String> lista_marcas;
    private List<String> lista_modelos;
    private List<String> lista_years;

    public final static int NO_YEARS = -1;
    public final static int NO_KMS = -1;
    public final static int NO_ITV = -1;


    private void RellenarMarcas() {
        //spinner_marcas = (Spinner) findViewById(R.id.cmb_marcas);
        lista_marcas = new ArrayList<String>();
        spinner_marcas = (Spinner) this.findViewById(R.id.cmb_marcas);

        DBMarcas dbmarcas = new DBMarcas(getApplicationContext());
        Cursor cursor = dbmarcas.buscarMarcas();

        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String marca = cursor.getString(cursor.getColumnIndex(DBMarcas.CN_MARCA));
            lista_marcas.add(marca);
        }


        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_marcas);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_marcas.setAdapter(adaptador);

        if (!marca.equals(INICIAL_MARCA)) {
            int spinnerPostion = adaptador.getPosition(marca);
            spinner_marcas.setSelection(spinnerPostion);
            spinnerPostion = 0;
        }

    }


    private void RellenarModelos() {
        spinner_marcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                lista_modelos = new ArrayList<String>(); // para borrar los anteriores y no añadir al final

                String marca_seleccionada = spinner_marcas.getSelectedItem().toString();
                DBMarcas dbmarcas = new DBMarcas(getApplicationContext());
                Cursor c_marcas = dbmarcas.buscarMarcas(marca_seleccionada);

                System.out.println("marca_seleccionada "+marca_seleccionada);

                int id_marca = 0;
                for (c_marcas.moveToFirst(); !c_marcas.isAfterLast(); c_marcas.moveToNext()) {
                    id_marca = c_marcas.getInt(c_marcas.getColumnIndex(DBMarcas.CN_ID));
                }

                DBModelos dbmodelos = new DBModelos(getApplicationContext());
                Cursor cursor = dbmodelos.buscarModelosDeMarca(id_marca);

                lista_modelos.add(INICIAL_MODELO);
                if(id_marca > 0 ) { // No es inicial_marca
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        String modelo = cursor.getString(cursor.getColumnIndex(DBModelos.CN_MODELO));
                        lista_modelos.add(modelo);
                    }
                }

                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(parentView.getContext(), android.R.layout.simple_spinner_item, lista_modelos);
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_modelos.setAdapter(adaptador);

                if (!modelo.equals(INICIAL_MODELO)) {
                    int spinnerPostion = adaptador.getPosition(modelo);
                    spinner_modelos.setSelection(spinnerPostion);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        lista_modelos = new ArrayList<String>();
        spinner_modelos = (Spinner) this.findViewById(R.id.cmb_modelo);

        String marca_seleccionada = spinner_marcas.getSelectedItem().toString();
        DBMarcas dbmarcas = new DBMarcas(getApplicationContext());
        Cursor c_marcas = dbmarcas.buscarMarcas(marca_seleccionada);

        int id_marca = 0;
        for (c_marcas.moveToFirst(); !c_marcas.isAfterLast(); c_marcas.moveToNext()) {
            id_marca = c_marcas.getInt(c_marcas.getColumnIndex(DBMarcas.CN_ID));
        }

        if(marca_seleccionada.equals(INICIAL_MARCA) || marca_seleccionada.isEmpty()) {
            lista_modelos.add(INICIAL_MODELO);
        }
        else {
            DBModelos dbmodelos = new DBModelos(getApplicationContext());
            Cursor cursor = dbmodelos.buscarModelosDeMarca(id_marca);

            lista_modelos.add(INICIAL_MODELO);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String modelo = cursor.getString(cursor.getColumnIndex(DBModelos.CN_MODELO));
                lista_modelos.add(modelo);
            }
        }




        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_modelos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_modelos.setAdapter(adaptador);

        if (!modelo.equals(INICIAL_MODELO)) {
            int spinnerPostion = adaptador.getPosition(modelo);
            spinner_modelos.setSelection(spinnerPostion);
        }

    }

    private void RellenarYears() {
        //spinner_marcas = (Spinner) findViewById(R.id.cmb_marcas);
        lista_years = new ArrayList<String>();
        spinner_years = (Spinner) this.findViewById(R.id.cmb_years);
        lista_years.add(INICIAL_YEAR);
        Calendar calen = Calendar.getInstance();
        Integer hoy = calen.get(Calendar.YEAR);
        for(int i = hoy; i >= 1900; i--) {
            lista_years.add(""+i);
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_years);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_years.setAdapter(adaptador);


        if (!year.equals(INICIAL_YEAR)) {
            int spinnerPostion = adaptador.getPosition(year);
            spinner_years.setSelection(spinnerPostion);
            spinnerPostion = 0;
        }

    }

    private void Siguiente(final Context context) {
        //Instanciamos el Boton siguiente
        Button btn1 = (Button) findViewById(R.id.btn1);

        /*
          Definimos un método OnClickListener para que
          al pulsar el botón se nos muestre la segunda actividad
        */
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, DatosIniciales.class);

                LeerDatosPantalla();
                DBCar dbcar = new DBCar(context);

                if (comprobaciones()) {
                    dbcar.ActualizarTodosCocheNOActivo(); // Nos aseguramos de que ponemos todos los coches a inactivos para marcar como activo el nuevo
                    if(CocheEsNuevo.getInstance().coche_es_nuevo == 1) {
                        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, funciones.date_a_int(new Date()), int_kms);
                        intent.putExtra("miCoche", miCoche);
                        dbcar.insertinsertOrUpdate(miCoche);
                    } else if(int_kms_ini == 0) { // si el coche no existía (no es devuelto en cursor, no tiene históricos)  se inicializa
                        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, funciones.date_a_int(new Date()), int_kms);
                        intent.putExtra("miCoche", miCoche);
                        dbcar.insertinsertOrUpdate(miCoche);
                    }
                    else if((int_kms_ini != 0) && (int_kms_anterior == int_kms)) { // si el coche existía y no actualizamos el nº de kms -> no necesitamos actualizar lasfechas de futuros logs (Todos los tipos)
                        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);
                        intent.putExtra("miCoche", miCoche);
                        dbcar.insertinsertOrUpdate(miCoche);
                    }
                    else if((int_kms_ini != 0) && (int_kms_anterior != int_kms)) { // si el coche existía y actualizamos el nº de kms -> necesitamos actualizar las fechas de futuros logs (Todos los tipos)


                        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);
                        dbcar.insertinsertOrUpdate(miCoche);
                        intent.putExtra("miCoche", miCoche);
                    }

                    Cursor c = dbcar.buscarCoches();
                    coches_en_NavigationDrawer(dbcar, c);  //actualizamos los coches en el navigation bar por si se crea uno nuevo


                    // Aunque no hagamos cambios se procesa siempre porque podemos haber eliminado logs o editado o marcados como realizados y se deben recalcular al mostrar
                    DBLogs dbLogs= new DBLogs(context);
                    int int_now = funciones.date_a_int(new Date());


                    ///////////////////PARA EL ACEITE
                    procesarAceite.procesar_aceite(dbLogs, int_now, context, int_kms, int_fecha_ini, int_kms_ini, matricula);
                    //////////////////IR AÑADIENDO PARA EL RESTO DE TIPOS

                    ////////////////////////////////////////////////////////




                    startActivity(intent);
                }

            }
        });
    }

    private void VaciarPantalla() {
        CocheEsNuevo.getInstance().coche_es_nuevo = 1;

        TextView text = (TextView)findViewById(R.id.matricula);
        text.setText("");

        text=(TextView)findViewById(R.id.kms);
        text.setText("");

        marca = INICIAL_MARCA;
        modelo = INICIAL_MODELO;
        kms = INICIAL_KMS;
        year = INICIAL_YEAR;
        matricula = INICIAL_MATRICULA;


        spinner_years.setSelection(0);
        spinner_modelos.setSelection(0);
        spinner_marcas.setSelection(0);



        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        DatePicker datePicker2 = (DatePicker) findViewById(R.id.date_itv);
        datePicker2.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
    }

    private void AddCar(final Context context) {
        //Instanciamos el Boton añadir
        ButtonFloat btn_addCar = (ButtonFloat) findViewById(R.id.buttonFloat);


        btn_addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VaciarPantalla();

            }
        });
    }

    private void ActualizarCochesDrawer(DBCar dbcar) {
        // Volvemos a actualizar los coches para mostrar el activo en la barra de navegacion
        Cursor cursor = dbcar.buscarCoches();

            coches_en_NavigationDrawer(dbcar, cursor);
        if (cursor.moveToFirst() == true) {
            // Actualizamos la pantalla main activity con el coche activo
            Cursor c_coche_activo = dbcar.buscarCocheActivo();
            for (c_coche_activo.moveToFirst(); !c_coche_activo.isAfterLast(); c_coche_activo.moveToNext()) {
                matricula = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_MATRICULA));
                marca = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_MARCA));
                year = String.valueOf(c_coche_activo.getInt(c_coche_activo.getColumnIndex(DBCar.CN_YEAR)));
                modelo = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_MODELO));
                kms = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_KMS));
                itv = c_coche_activo.getString(c_coche_activo.getColumnIndex(DBCar.CN_ITV));

            }

            RellenarPantalla();

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

                System.out.println("Fecha de creación del coche: " + funciones.int_a_string(int_fecha_ini));
                System.out.println("Kms al crear el coche: " + int_kms_ini);
            } else {// no se da el caso pq si entra en el primer if ya existe minimo un coche y ya hemos forzado a q sea el activo
            }
        }
        else {// Solo puede ser el caso de que borramos un coche y no tengamos más (no se pudo poner activo ningun otro
            //Ya se vació la pantalla y se pusieron las imagenes a las de por defecto
            // O al abrir el programa sin coches
            RellenarPantalla(); // para rellenar los combobox
            VaciarPantalla(); // Para quitar el coche anterior

        }
    }


    private void coches_en_NavigationDrawer(final DBCar dbcar, Cursor c) {
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size


        mAdapter = new miAdaptadorCoches(dbcar, c, getApplicationContext());       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)

        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matricula_seleccionada = mAdapter.getMatriculaSeleccionada(mRecyclerView.getChildPosition(v) - 1);
                String matricula_NoSeleccionada = "";

                DBLogs dblogs = new DBLogs(getApplicationContext());
                Cursor cc = dblogs.cargarCursorLogs();
                for(cc.moveToFirst(); !cc.isAfterLast(); cc.moveToNext()) {
                    String mm = cc.getString(cc.getColumnIndex(DBLogs.CN_CAR));
                    System.out.println("MATRICULA "+mm);
                }

                Cursor c = dbcar.buscarCocheActivo();

                if (c.moveToFirst() == true) {
                    matricula_NoSeleccionada = c.getString(c.getColumnIndex(DBCar.CN_MATRICULA));

                }


                dbcar.ActualizarCocheNOActivo(matricula_NoSeleccionada);
                dbcar.ActualizarCocheActivo(matricula_seleccionada);

                ActualizarCochesDrawer(dbcar);
            }
        });

        mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                String matricula_seleccionada = mAdapter.getMatriculaSeleccionada(mRecyclerView.getChildPosition(v) - 1);

                final MaterialDialog mMaterialDialog = new MaterialDialog(v.getContext());
                mMaterialDialog.setTitle("Eliminar coche");
                mMaterialDialog.setMessage("¿Está seguro de eliminar el coche con matrícula " + matricula_seleccionada + " y todos sus logs?");
                mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View w) {
                        mMaterialDialog.dismiss();
                        String matricula_seleccionada = mAdapter.getMatriculaSeleccionada(mRecyclerView.getChildPosition(v) - 1);
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
                                VaciarPantalla();
                                ImageView img_marca = (ImageView) mRecyclerView.findViewById(R.id.circleView);
                                ImageView img_modelo = (ImageView) mRecyclerView.findViewById(R.id.background_modelo);
                                img_modelo.setBackgroundResource(R.drawable.modelo_inicio);
                                img_marca.setImageResource(R.drawable.logo_inicio);

                            }
                        } else {
                            // Si no era el activo da igual pq seguirá activo el que estaba o pq no hay mas coches
                        }


                        ActualizarCochesDrawer(dbcar);
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


        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager




        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view

        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar, R.string.open_drawer, R.string.close_drawer){

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

    private void RellenarPantalla() {
        RellenarMarcas();
        RellenarModelos();
        RellenarYears();

        TextView text = (TextView)findViewById(R.id.matricula);
        text.setText(matricula);
        //if(!matricula.equals("Introduzca Matrícula")) text.setEnabled(false); //no editable si ya está la matrícula


        text=(TextView)findViewById(R.id.kms);
        text.setText(kms);

        fechaITV = funciones.string_a_date(itv);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaITV);

        DatePicker datePicker2 = (DatePicker) findViewById(R.id.date_itv);
        datePicker2.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
    }

    private void LeerDatosPantalla() {
        Spinner spinner_marca = (Spinner)findViewById(R.id.cmb_marcas);
        marca = spinner_marca.getSelectedItem().toString();
        System.out.println(marca);

        Spinner spinner_modelo = (Spinner)findViewById(R.id.cmb_modelo);
        modelo = spinner_modelo.getSelectedItem().toString();
        System.out.println(modelo);

        matriculaT = (EditText) findViewById (R.id.matricula);
        matricula = matriculaT.getText().toString();
        System.out.println(matricula);

        Spinner spinner_year = (Spinner)findViewById(R.id.cmb_years);
        year = spinner_year.getSelectedItem().toString();
        System.out.println(year);
        if(year.equals(INICIAL_YEAR)) int_year = NO_YEARS;
        else int_year = Integer.parseInt(year);

        kmsT = (EditText) findViewById (R.id.kms);
        kms = kmsT.getText().toString();
        if(kms.isEmpty() || kms.equals(INICIAL_KMS)) int_kms = NO_KMS;
        else {
            try {
                int_kms = Integer.parseInt(kms);
            } catch(NumberFormatException nfe) {
                Toast.makeText(MyActivity.this, "Ha de introducir un nº de kilómetros correcto.", Toast.LENGTH_LONG).show();
            }

        }

        System.out.println(kms);

        DatePicker datePicker = (DatePicker) findViewById(R.id.date_itv);



        if ((datePicker.getDayOfMonth() < 10) && ((datePicker.getMonth()+1) < 10)) {
            itv = "0"+datePicker.getDayOfMonth() +"-0"+ (datePicker.getMonth()+1) + "-" + datePicker.getYear();
        } else
        if ((datePicker.getDayOfMonth() < 10) && ((datePicker.getMonth()+1) >= 10)) {
            itv = "0"+datePicker.getDayOfMonth() +"-"+ (datePicker.getMonth()+1)+ "-" + datePicker.getYear();
        } else
        if ((datePicker.getDayOfMonth() >= 10) && ((datePicker.getMonth()+1) < 10)) {
            itv = datePicker.getDayOfMonth() +"-0"+ (datePicker.getMonth()+1) + "-" + datePicker.getYear();
        } else
            itv = datePicker.getDayOfMonth() +"-"+ (datePicker.getMonth()+1)+ "-" + datePicker.getYear();


        fechaITV = funciones.string_a_date(itv);
        int_itv = funciones.string_a_int(itv);



        System.out.println(""+datePicker.getDayOfMonth()+ "-" + (datePicker.getMonth()+1) + "-" + datePicker.getYear());
    }







    EditText matriculaT, modeloT, kmsT;
    String matricula = "", marca = "", modelo = "", year = "", kms = "", itv = "";
    Date fechaITV = new Date();
    int int_year, int_kms, int_kms_anterior = 0, int_itv, int_kms_ini = 0, int_fecha_ini = 0;





    //comprobaciones
    private boolean comprobaciones() {
        boolean ok = true;
        if(CocheEsNuevo.getInstance().coche_es_nuevo == 0) {
            try {
                Integer mykms = Integer.parseInt(kms);
                if (mykms < int_kms_anterior) {
                    Toast.makeText(MyActivity.this, "Ha introducido un número de kms menor que el anterior.", Toast.LENGTH_LONG).show();
                    ok = false;
                }
            } catch (NumberFormatException nfe) {
                Toast.makeText(MyActivity.this, "Ha de introducir un nº de kilómetros correcto.", Toast.LENGTH_LONG).show();
                ok = false;
            }
        }
        if (!TextUtils.isEmpty(year) && !year.equals(INICIAL_YEAR)) { // el año no es obligatorio
            try {
                Integer myyear = Integer.parseInt(year);
                Calendar calen = Calendar.getInstance();
                Integer hoy = calen.get(Calendar.YEAR);
                if (myyear < 1900 || (myyear > hoy)) {
                    Toast.makeText(MyActivity.this, "Ha de introducir un año válido.", Toast.LENGTH_LONG).show();
                    ok = false;
                }
            } catch (NumberFormatException nfe) {
                Toast.makeText(MyActivity.this, "Ha de introducir un año válido.", Toast.LENGTH_LONG).show();
                ok = false;
            }
        }


        if(ok) {
            // Los campos marca, modelo y nº de kilometros deben de ser obligatorios
            if (TextUtils.isEmpty(matricula) || matricula.equals(INICIAL_MATRICULA)) {
                Toast.makeText(MyActivity.this, "Ha de introducir la matrícula.", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(marca) || marca.equals(INICIAL_MARCA)) {
                Toast.makeText(MyActivity.this, "Ha de introducir la marca.", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(modelo) || marca.equals(INICIAL_MODELO)) {
                Toast.makeText(MyActivity.this, "Ha de introducir el modelo.", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(kms)) {
                Toast.makeText(MyActivity.this, "Ha de introducir el nº de kilómetros.", Toast.LENGTH_LONG).show();
            } else {
                return true;

            }

        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);


        final Context context = this;
        DBCar dbcar = new DBCar(context);
        Cursor c = dbcar.buscarCoches();


        if (c.moveToFirst() == true) { // Si no hay coches no se tiene que rellenar el drawer ni la pantalla

            coches_en_NavigationDrawer(dbcar, c);

            Cursor c_coche_activo = dbcar.buscarCocheActivo();
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

                System.out.println("Fecha de creación del coche: " + funciones.int_a_string(int_fecha_ini));
                System.out.println("Kms al crear el coche: " + int_kms_ini);
            }



        }
        else {
            coches_en_NavigationDrawer(dbcar, c);

        }
        RellenarPantalla();
        Siguiente(context);
        AddCar(context);



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
        return super.onOptionsItemSelected(item);

    }












}
