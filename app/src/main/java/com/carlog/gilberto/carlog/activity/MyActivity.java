package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.carlog.gilberto.carlog.data.dbCar;
import com.carlog.gilberto.carlog.data.dbMarcas;
import com.carlog.gilberto.carlog.data.dbSettings;
import com.carlog.gilberto.carlog.formats.utilities;
import com.carlog.gilberto.carlog.negocio.notificaciones;
import com.carlog.gilberto.carlog.negocio.procesarTipos;
import com.carlog.gilberto.carlog.tiposClases.cocheEsNuevo;
import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.tiposClases.tipoCoche;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.data.dbModelos;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.tiposClases.tipoSettings;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.carlog.gilberto.carlog.view.simpleDataView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class myActivity extends ActionBarActivity {

    public final static int FIRST_DATE = 157766400; // para comparar si es antes del 01/01/1975 da igual que haya fechas antes pq solo podemos hacer itv de hoy en adelante


    private Toolbar toolbar;
    private boolean add_car = false;

    private Spinner spinner_marcas;
    private Spinner spinner_modelos;
    private Spinner spinner_years;
    private List<String> lista_marcas;
    private List<String> lista_modelos;
    private List<String> lista_years;

    public final static int NO_YEARS = -1;
    public final static int NO_KMS = -1;
    public final static int NO_ITV = -1;

    View rootView;


    private void RellenarMarcas(Cursor c) {
        lista_marcas = new ArrayList<String>();
        spinner_marcas = (Spinner) findViewById(R.id.cmb_marcas);

        dbMarcas dbmarcas = new dbMarcas(getApplicationContext());
        Cursor cursor = dbmarcas.buscarMarcas();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String marca = cursor.getString(cursor.getColumnIndex(dbMarcas.CN_MARCA));
            lista_marcas.add(marca);
        }


        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_marcas);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_marcas.setAdapter(adaptador);

        if (c.moveToFirst() == true) {
            marca = c.getString(c.getColumnIndex(dbCar.CN_MARCA));
            int spinnerPostion = adaptador.getPosition(marca);
            spinner_marcas.setSelection(spinnerPostion);

            simpleDataView sdv = (simpleDataView) findViewById(R.id.marca_view);
            sdv.setTitle(myActivity.this.getString(R.string.marca));
            sdv.setValue(adaptador.getItem(spinnerPostion));
            sdv.setEditInvisible();
            sdv.setImage(getResources().getDrawable(R.drawable.ic_marca));
        }

    }


    private void RellenarModelos(final Cursor c) {
        spinner_marcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                lista_modelos = new ArrayList<String>(); // para borrar los anteriores y no añadir al final

                String marca_seleccionada = spinner_marcas.getSelectedItem().toString();
                dbMarcas dbmarcas = new dbMarcas(getApplicationContext());
                Cursor c_marcas = dbmarcas.buscarMarcas(marca_seleccionada);

                int id_marca = 0;
                for (c_marcas.moveToFirst(); !c_marcas.isAfterLast(); c_marcas.moveToNext()) {
                    id_marca = c_marcas.getInt(c_marcas.getColumnIndex(dbMarcas.CN_ID));
                }

                if(id_marca != 0) {
                    dbModelos dbmodelos = new dbModelos(getApplicationContext());
                    Cursor cursor = dbmodelos.buscarModelosDeMarca(id_marca);

                    lista_modelos.add(myActivity.this.getString(R.string.modelo));
                    if (id_marca > 0) { // No es inicial_marca
                        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                            String modelo = cursor.getString(cursor.getColumnIndex(dbModelos.CN_MODELO));
                            lista_modelos.add(modelo);
                        }
                    }

                    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(parentView.getContext(), android.R.layout.simple_spinner_item, lista_modelos);
                    adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_modelos.setAdapter(adaptador);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        lista_modelos = new ArrayList<String>();
        spinner_modelos = (Spinner) findViewById(R.id.cmb_modelo);

        String marca_seleccionada = spinner_marcas.getSelectedItem().toString();
        dbMarcas dbmarcas = new dbMarcas(getApplicationContext());
        Cursor c_marcas = dbmarcas.buscarMarcas(marca_seleccionada);

        int id_marca = 0;
        for (c_marcas.moveToFirst(); !c_marcas.isAfterLast(); c_marcas.moveToNext()) {
            id_marca = c_marcas.getInt(c_marcas.getColumnIndex(dbMarcas.CN_ID));
        }

        if(marca_seleccionada.equals(myActivity.this.getString(R.string.marca)) || marca_seleccionada.isEmpty()) {
            lista_modelos.add(myActivity.this.getString(R.string.modelo));
        }
        else {
            dbModelos dbmodelos = new dbModelos(getApplicationContext());
            Cursor cursor = dbmodelos.buscarModelosDeMarca(id_marca);

            lista_modelos.add(myActivity.this.getString(R.string.modelo));
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String modelo = cursor.getString(cursor.getColumnIndex(dbModelos.CN_MODELO));
                lista_modelos.add(modelo);
            }
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_modelos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_modelos.setAdapter(adaptador);

        if (c.moveToFirst() == true) {
            modelo = c.getString(c.getColumnIndex(dbCar.CN_MODELO));
            int spinnerPostion = adaptador.getPosition(modelo);
            spinner_modelos.setSelection(spinnerPostion);

            simpleDataView sdv = (simpleDataView) findViewById(R.id.modelo_view);
            sdv.setTitle(myActivity.this.getString(R.string.modelo));
            sdv.setValue(adaptador.getItem(spinnerPostion));
            sdv.setImage(getResources().getDrawable(R.drawable.ic_modelo));
            sdv.setEditInvisible();
        }

    }

    private void RellenarYears(Cursor c) {
        //spinner_marcas = (Spinner) findViewById(R.id.cmb_marcas);
        lista_years = new ArrayList<String>();
        spinner_years = (Spinner) findViewById(R.id.cmb_years);
        lista_years.add(myActivity.this.getString(R.string.year));
        Calendar calen = Calendar.getInstance();
        Integer hoy = calen.get(Calendar.YEAR);
        for(int i = hoy; i >= 1900; i--) {
            lista_years.add(""+i);
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_years);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_years.setAdapter(adaptador);

        if (c.moveToFirst() == true) {
            year = String.valueOf(c.getInt(c.getColumnIndex(dbCar.CN_YEAR)));
            int spinnerPostion = adaptador.getPosition(year);
            spinner_years.setSelection(spinnerPostion);

            simpleDataView sdv = (simpleDataView) findViewById(R.id.year_view);
            if(c.getInt(c.getColumnIndex(dbCar.CN_YEAR)) != myActivity.NO_YEARS) {
                sdv.setTitle(myActivity.this.getString(R.string.year));
                sdv.setValue(adaptador.getItem(spinnerPostion));
                sdv.setImage(getResources().getDrawable(R.drawable.ic_year));
                sdv.setEditInvisible();
            }
            else {
                // Porque aún se muestra el combo de los años (aún no se ha introducido el año)
                sdv.setVisibility(View.GONE);
            }
        }


    }


    private void RellenarPantalla(Cursor c) {
        RellenarMarcas(c);
        RellenarModelos(c);
        RellenarYears(c);

        if (c.moveToFirst() == true) {
            matricula = c.getString(c.getColumnIndex(dbCar.CN_MATRICULA));
            matricula_anterior = matricula;
            int_kms = c.getInt(c.getColumnIndex(dbCar.CN_KMS));
            long_itv = c.getInt(c.getColumnIndex(dbCar.CN_ITV));
            int_kms_ini = c.getInt(c.getColumnIndex(dbCar.CN_KMS_INI));
            int_fecha_ini = c.getInt(c.getColumnIndex(dbCar.CN_FECHA_INI));
            kms = String.valueOf(int_kms);
            int_kms_anterior = int_kms;
            itv = funciones.long_a_string(long_itv);
        }

        simpleDataView sdv = (simpleDataView) findViewById(R.id.matricula_view);
        sdv.setTitle(myActivity.this.getString(R.string.matricula));
        sdv.setValue(matricula);
        sdv.setEdit(matricula);
        sdv.setEditVisible();
        sdv.setImage(getResources().getDrawable(R.drawable.ic_matricula));

        sdv = (simpleDataView) findViewById(R.id.kms_view);
        sdv.setTitle(myActivity.this.getString(R.string.kms));
        sdv.setValue(kms);
        sdv.setEdit(kms);
        sdv.setEditVisible();
        sdv.setImage(getResources().getDrawable(R.drawable.ic_kms));

        fechaITV = funciones.string_a_date(itv);
        sdv = (simpleDataView) findViewById(R.id.fechaitv_view);
        sdv.setTitle(myActivity.this.getString(R.string.itv));
        sdv.setValue(itv);
        FloatingActionButton button_addItv = (FloatingActionButton) findViewById(R.id.button_additv);
        if(long_itv > FIRST_DATE) {
            sdv.setEditInvisible();
            button_addItv.setVisibility(View.GONE);
        }
        else {
            sdv.setEditVisible();
            button_addItv.setVisibility(View.VISIBLE);
        }
        sdv.setImage(getResources().getDrawable(R.drawable.ic_fecha));
    }


    public void procesado_revisiones(dbLogs dbLogs, Context context) {
        dbSettings dbs = new dbSettings(context);
        Cursor c_sett = dbs.getSettings();
        if(c_sett.moveToFirst() == true) {
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_ACEITE)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoAceite), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_FILACEITE)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoFiltroAceite), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_REVGEN)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoRevGen), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_CORREA)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoCorrea), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_BOMBAAGUA)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoBomba), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_ITV)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoItv), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_FILGASOLINA)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoFiltroGasolina), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_FILAIRE)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoFiltroAire), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_BUJIAS)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoBujias), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_LIMPIAPARABRISAS)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoLimpiaparabrisas), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_LIQFRENOS)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoLiqFrenos), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_FRENOS)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoFrenos), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_RUEDAS)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoRuedas), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_LUCES)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoLuces), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_EMBRAGUE)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoEmbrague), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_AMORTIGUADORES)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoAmortiguadores), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_ANTICONGELANTE)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoAnticongelante), int_year);
            if(c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_BATERIA)) == tipoSettings.ACTIVO)
                procesarTipos.procesar(dbLogs, context, int_kms, int_fecha_ini, int_kms_ini, matricula, myActivity.this.getString(R.string.tipoBateria), int_year);
        }
        // Las tipo personalizadas no se procesan,  sólo se avisa en su fecha

    }

    private void procesar(Context context) {
        dbCar dbcar = new dbCar(context);
        Cursor c = dbcar.buscarCocheActivo();

        if (c.moveToFirst() == true) {
            matricula = c.getString(c.getColumnIndex(dbCar.CN_MATRICULA));
            marca = c.getString(c.getColumnIndex(dbCar.CN_MARCA));
            modelo = c.getString(c.getColumnIndex(dbCar.CN_MODELO));
            int_year = c.getInt(c.getColumnIndex(dbCar.CN_YEAR));
            int_kms = c.getInt(c.getColumnIndex(dbCar.CN_KMS));
            long_itv = c.getInt(c.getColumnIndex(dbCar.CN_ITV));
            int_kms_ini = c.getInt(c.getColumnIndex(dbCar.CN_KMS_INI));
            int_fecha_ini = c.getInt(c.getColumnIndex(dbCar.CN_FECHA_INI));
        }

        // Aunque no hagamos cambios se procesa siempre porque podemos haber eliminado logs o editado o marcados como realizados y se deben recalcular al mostrar
        dbLogs dbLogs= new com.carlog.gilberto.carlog.data.dbLogs(context);

        procesado_revisiones(dbLogs, context);
    }


    private void procesando(Context context, boolean EditarCoche) {
        Intent intent = new Intent(myActivity.this, listaLogs.class);

        boolean leidos = LeerDatosPantalla();
        dbCar dbcar = new dbCar(context);

        if (comprobaciones(EditarCoche) && leidos) {
            dbcar.ActualizarTodosCocheNOActivo(); // Nos aseguramos de que ponemos todos los coches a inactivos para marcar como activo el nuevo
            if(!EditarCoche) { // Si no estamos editando es nuevo
                tipoCoche miCoche = new tipoCoche(matricula, marca, modelo, dbCar.IMG_MODELO_NOCAMBIADA, null, int_year, int_kms, long_itv, tipoCoche.PROFILE_ACTIVO, funciones.date_a_long(new Date()), int_kms);
                dbcar.insertinsertOrUpdate(miCoche);
            } else if(int_kms_ini == 0) { // si el coche no existía (no es devuelto en cursor, no tiene históricos)  se inicializa
                tipoCoche miCoche = new tipoCoche(matricula, marca, modelo, dbCar.IMG_MODELO_NOCAMBIADA, null, int_year, int_kms, long_itv, tipoCoche.PROFILE_ACTIVO, funciones.date_a_long(new Date()), int_kms);
                dbcar.insertinsertOrUpdate(miCoche);
                // Si el coche no existía no se va a dar el caso pq la matricula es la clave de la tabla entonces no modificamos
                        /*if(!matricula.equals(matricula_anterior)) {
                            dbLogs dbl = new dbLogs(context);
                            dbl.modificarMatriculaLogs(matricula, matricula_anterior);
                            dbcar.eliminarCoche(matricula_anterior);
                        }*/
            }
            else if((int_kms_ini != 0) && (int_kms_anterior == int_kms)) { // si el coche existía y no actualizamos el nº de kms -> no necesitamos actualizar lasfechas de futuros logs (Todos los tipos)
                Uri myUri = Uri.parse(img_modelo_personalizada);
                String uriEncoded = Uri.encode(utilities.getPathPictureFromUri(context, myUri), "UTF-8");
                tipoCoche miCoche = new tipoCoche(matricula, marca, modelo, img_modelo_cambiada, uriEncoded, int_year, int_kms, long_itv, tipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);
                dbcar.insertinsertOrUpdate(miCoche);
                if(!matricula.equals(matricula_anterior)) {
                    dbLogs dbl = new dbLogs(context);
                    dbl.modificarMatriculaLogs(matricula, matricula_anterior);
                    dbcar.eliminarCoche(matricula_anterior);
                }
            }
            else if((int_kms_ini != 0) && (int_kms_anterior != int_kms)) { // si el coche existía y actualizamos el nº de kms -> necesitamos actualizar las fechas de futuros logs (Todos los tipos)
                Uri myUri = Uri.parse(img_modelo_personalizada);
                String uriEncoded = Uri.encode(utilities.getPathPictureFromUri(context, myUri), "UTF-8");
                tipoCoche miCoche = new tipoCoche(matricula, marca, modelo, img_modelo_cambiada, uriEncoded, int_year, int_kms, long_itv, tipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);
                dbcar.insertinsertOrUpdate(miCoche);
                if(!matricula.equals(matricula_anterior)) {
                    dbLogs dbl = new dbLogs(context);
                    dbl.modificarMatriculaLogs(matricula, matricula_anterior);
                    dbcar.eliminarCoche(matricula_anterior);
                }
            }

            procesar(context);

            startActivity(intent);
            // Si agregamos un nuevo coche y volvemos hacia atras se sale de la app pero desde la pantalla de logs puesto que ya hemos agregado un coche y por lo tanto no se queda el drawer sin el coche nuevo al volver atras
            // Si no queremos agregar nuevo coche y pulsamos hacia atras regresamos a la lista de logs anterior
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }


    private void Siguiente(final Context context, final boolean EditarCoche) {
        ButtonRectangle btn_siguiente = (ButtonRectangle) findViewById(R.id.button_siguiente);
        if(EditarCoche)
            btn_siguiente.setText(myActivity.this.getString(R.string.actualizar));
        if(!EditarCoche)
            btn_siguiente.setText(myActivity.this.getString(R.string.crear));

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EditarCoche) {
                    try {
                        LeerDatosPantalla();
                        Integer mykms = Integer.parseInt(kms);
                        if (mykms < int_kms_anterior) {
                            Toast.makeText(myActivity.this, "", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(myActivity.this);
                            builder.setMessage(myActivity.this.getString(R.string.kmsMenor))
                                .setTitle(myActivity.this.getString(R.string.cambiarKms))
                                .setCancelable(false)
                                .setNegativeButton(myActivity.this.getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id_dialog) {
                                            dialog.cancel();
                                        }
                                    })
                                .setPositiveButton(myActivity.this.getString(R.string.continuar),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id_dialog) {
                                            // metodo que se debe implementar Sí
                                            procesando(myActivity.this.getApplicationContext(), EditarCoche);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
                            procesando(context, EditarCoche);
                        }
                    } catch (NumberFormatException nfe) {
                        Toast.makeText(myActivity.this, myActivity.this.getString(R.string.incorrectKms), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    procesando(context, EditarCoche);
                }

            }
        });
    }

    public void VaciarPantalla() {
        cocheEsNuevo.getInstance().coche_es_nuevo = 1;

        simpleDataView sdv = (simpleDataView) findViewById(R.id.matricula_view);
        sdv.setTitle(myActivity.this.getString(R.string.matricula));
        sdv.setValue("");
        sdv.setEdit("");
        sdv.setEditHint(myActivity.this.getString(R.string.matricula));
        sdv.setEditVisible();
        sdv.setImage(getResources().getDrawable(R.drawable.ic_matricula));

        sdv = (simpleDataView) findViewById(R.id.kms_view);
        sdv.setTitle(myActivity.this.getString(R.string.kms));
        sdv.setValue("");
        sdv.setEdit("");
        sdv.setEditHint(myActivity.this.getString(R.string.kms));
        sdv.setEditVisible();
        sdv.setImage(getResources().getDrawable(R.drawable.ic_kms));

        sdv = (simpleDataView) findViewById(R.id.year_view);
        sdv.setVisibility(View.GONE);

        sdv = (simpleDataView) findViewById(R.id.marca_view);
        sdv.setVisibility(View.GONE);

        sdv = (simpleDataView) findViewById(R.id.modelo_view);
        sdv.setVisibility(View.GONE);

        sdv = (simpleDataView) findViewById(R.id.fechaitv_view);
        sdv.setTitle(myActivity.this.getString(R.string.itv));
        sdv.setValue("");
        sdv.setEdit("");
        sdv.setLos2Invisible();
        sdv.setImage(getResources().getDrawable(R.drawable.ic_fecha));
        FloatingActionButton button_addItv = (FloatingActionButton) findViewById(R.id.button_additv);
        button_addItv.setVisibility(View.VISIBLE);

        marca = myActivity.this.getString(R.string.marca);
        modelo = myActivity.this.getString(R.string.modelo);
        kms = myActivity.this.getString(R.string.kms);
        year = myActivity.this.getString(R.string.year);
        matricula = myActivity.this.getString(R.string.matricula);

        spinner_years.setSelection(0);
        spinner_modelos.setSelection(0);
        spinner_marcas.setSelection(0);
    }

    private boolean LeerDatosPantalla() {
        boolean ok = true;
        Spinner spinner_marca = (Spinner)findViewById(R.id.cmb_marcas);
        marca = spinner_marca.getSelectedItem().toString();
        System.out.println(marca);

        Spinner spinner_modelo = (Spinner)findViewById(R.id.cmb_modelo);
        modelo = spinner_modelo.getSelectedItem().toString();
        System.out.println(modelo);

        simpleDataView sdv = (simpleDataView) findViewById(R.id.matricula_view);
        EditText et = sdv.getTextEditLeerPantalla();
        matricula = et.getText().toString().toUpperCase();
        System.out.println(matricula);

        Spinner spinner_year = (Spinner)findViewById(R.id.cmb_years);
        year = spinner_year.getSelectedItem().toString();
        System.out.println(year);
        if(year.equals(myActivity.this.getString(R.string.year))) int_year = NO_YEARS;
        else int_year = Integer.parseInt(year);

        sdv = (simpleDataView) findViewById(R.id.kms_view);
        et = sdv.getTextEditLeerPantalla();
        kms = et.getText().toString();
        if(kms.isEmpty() || kms.equals(myActivity.this.getString(R.string.kms))) int_kms = NO_KMS;
        else {
            try {
                int_kms = Integer.parseInt(kms);
            } catch(NumberFormatException nfe) {
                Toast.makeText(myActivity.this, myActivity.this.getString(R.string.incorrectKms), Toast.LENGTH_LONG).show();
                ok = false;
                return ok;
            }

        }

        System.out.println(kms);

        sdv = (simpleDataView) findViewById(R.id.fechaitv_view);
        itv = sdv.getValue();
        if(!itv.isEmpty() && (funciones.string_a_long(itv) > FIRST_DATE)) {
            fechaITV = funciones.string_a_date(itv);
            long_itv = funciones.string_a_long(itv);
        }
        else {
            fechaITV = new Date(0);
            long_itv = myActivity.NO_ITV;
        }
        return ok;
    }

    String matricula = "", marca = "", modelo = "", year = "", kms = "", itv = "", matricula_anterior = "", img_modelo_personalizada = "";
    Date fechaITV = new Date();
    int int_year, int_kms, int_kms_anterior = 0, int_kms_ini = 0, int_fecha_ini = 0, img_modelo_cambiada = dbCar.IMG_MODELO_NOCAMBIADA, int_profile = tipoCoche.PROFILE_INACTIVO;
    long long_itv = NO_ITV;

    private void ocultar_campos() { //
        // Se ocultan todos los campos obligatorios porque ya han sido agregados
        spinner_marcas.setVisibility(View.GONE);
        spinner_modelos.setVisibility(View.GONE);
        ImageView iv = (ImageView) findViewById(R.id.view_image_cmb_marca);
        iv.setVisibility(View.GONE);
        iv = (ImageView) findViewById(R.id.view_image_cmb_modelo);
        iv.setVisibility(View.GONE);

        // Los no obligatorios debemos comprobar que se hayan añadido (luego no se van a poder modificar)
        if (int_year != myActivity.NO_KMS) {
            spinner_years.setVisibility(View.GONE);
            iv = (ImageView) findViewById(R.id.view_image_cmb_year);
            iv.setVisibility(View.GONE);
        }
    }

    //comprobaciones
    private boolean comprobaciones(boolean EditarCoche) {
        boolean ok = true;

        try {
            Integer mykms = Integer.parseInt(kms);
            if (mykms < 0) {
                Toast.makeText(myActivity.this, myActivity.this.getString(R.string.positiveKms), Toast.LENGTH_LONG).show();
                ok = false;
            }
        } catch (NumberFormatException nfe) {
            Toast.makeText(myActivity.this, myActivity.this.getString(R.string.incorrectKms), Toast.LENGTH_LONG).show();
        }

        if (!TextUtils.isEmpty(year) && !year.equals(myActivity.this.getString(R.string.year))) { // el año no es obligatorio
            try {
                Integer myyear = Integer.parseInt(year);
                Calendar calen = Calendar.getInstance();
                Integer hoy = calen.get(Calendar.YEAR);
                if (myyear < 1900 || (myyear > hoy)) {
                    Toast.makeText(myActivity.this, myActivity.this.getString(R.string.incorrectYear), Toast.LENGTH_LONG).show();
                    ok = false;
                }
            } catch (NumberFormatException nfe) {
                Toast.makeText(myActivity.this, myActivity.this.getString(R.string.incorrectYear), Toast.LENGTH_LONG).show();
                ok = false;
            }
        }


        if(ok) {
            // Los campos marca, modelo y nº de kilometros deben de ser obligatorios
            if (TextUtils.isEmpty(matricula) || matricula.equals(myActivity.this.getString(R.string.matricula))) {
                Toast.makeText(myActivity.this, myActivity.this.getString(R.string.emptyMatricula), Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(marca) || marca.equals(myActivity.this.getString(R.string.marca))) {
                Toast.makeText(myActivity.this, myActivity.this.getString(R.string.emptyMarca), Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(modelo) || modelo.equals(myActivity.this.getString(R.string.modelo))) {
                Toast.makeText(myActivity.this, myActivity.this.getString(R.string.emptyModelo), Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(kms)) {
                Toast.makeText(myActivity.this, myActivity.this.getString(R.string.emptyKms), Toast.LENGTH_LONG).show();
            } else {
                return true;
            }
        }
        return false;
    }

    private void addItv() {
        FloatingActionButton btn_addItv = (FloatingActionButton) findViewById(R.id.button_additv);
        btn_addItv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity.this, addItv.class);
                intent.putExtra("fechaITV", fechaITV);
                startActivityForResult(intent, PETICION_ACTIVITY_ADDITV);
            }
        });
    }

    private void addCarOrShowLogs() {
        Context context = getApplicationContext();

        dbCar dbcar = new dbCar(context);
        Cursor c = dbcar.buscarCocheActivo();

        Boolean CocheNuevo = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            CocheNuevo = getIntent().getExtras().getBoolean("CocheNuevo");
        }
        catch (Exception e) {
            System.out.println("No es coche nuevo");
        }
        Boolean EditarCoche = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            EditarCoche = getIntent().getExtras().getBoolean("EditarCoche");
        }
        catch (Exception e) {
            System.out.println("No se está editando coche");
        }
        Boolean addItv = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            addItv = getIntent().getExtras().getBoolean("addItv");
        }
        catch (Exception e) {
            System.out.println("No se está añadiendo fecha itv");
        }

        if ((c.moveToFirst() == false) || CocheNuevo || EditarCoche || addItv) {  // Desde que haya un coche no se mostrará la primera actividad o si añadirmos un coche nuevo
            setContentView(R.layout.activity_my);

            toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
            setSupportActionBar(toolbar);

            if(CocheNuevo || EditarCoche || addItv) {  // El back arrow solo lo ponemos en la actividad principal si tiene actividad padre (sin coches es la principal)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            if (c.moveToFirst() == true) {
                matricula = c.getString(c.getColumnIndex(dbCar.CN_MATRICULA));
                marca = c.getString(c.getColumnIndex(dbCar.CN_MARCA));
                modelo = c.getString(c.getColumnIndex(dbCar.CN_MODELO));
                int_year = c.getInt(c.getColumnIndex(dbCar.CN_YEAR));
                int_kms = c.getInt(c.getColumnIndex(dbCar.CN_KMS));
                long_itv = c.getInt(c.getColumnIndex(dbCar.CN_ITV));
                int_kms_ini = c.getInt(c.getColumnIndex(dbCar.CN_KMS_INI));
                int_fecha_ini = c.getInt(c.getColumnIndex(dbCar.CN_FECHA_INI));
                img_modelo_cambiada = c.getInt(c.getColumnIndex(dbCar.CN_IMG_MODELO_CAMBIADA));
                img_modelo_personalizada = c.getString(c.getColumnIndex(dbCar.CN_IMG_MODELO_PERSONALIZADA));
                int_profile = c.getInt(c.getColumnIndex(dbCar.CN_PROFILE));
            }

            RellenarMarcas(c);
            RellenarModelos(c);
            RellenarYears(c);
            Siguiente(context, EditarCoche);
            if(CocheNuevo || (c.moveToFirst() == false)) {
                VaciarPantalla();
            }
            if(EditarCoche) {
                RellenarPantalla(c);
                ocultar_campos();
            }
            addItv();
        }
        else {
            Intent intent = new Intent(myActivity.this, listaLogs.class);
            procesar(context);
            startActivity(intent);
            finish();
        }
    }


    // lanzamos el servicio cada 30seg
    private void comprobarNotificaciones() {
        int comprobacionIntervaloSegundos = 10;
        PendingIntent pendingIntent;
        Intent myIntent = new Intent(myActivity.this, notificaciones.class);
        pendingIntent = PendingIntent.getService(myActivity.this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), comprobacionIntervaloSegundos * 1000, pendingIntent);
    }



    private Boolean es_anonimo() {
        Boolean anonimo = false;
        String txt = login.getNameAnonimo(this);
        if(txt != null) {
            if(txt.equals("Anonimo")) return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String getIdFacebook = login.getIdFacebook(this);
        usuario usuario = new com.carlog.gilberto.carlog.tiposClases.usuario();
        if(usuario.isUserLoggedIn(this) || (getIdFacebook != null) || es_anonimo()) {
            usuario.readUser(this);
            // aqui tenemos toda la info del usuario

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                    .build();
            ImageLoader.getInstance().init(config);

            addCarOrShowLogs();
            dbSettings dbs = new dbSettings(myActivity.this.getApplicationContext());
            Cursor c_sett = dbs.getSettings();
            if(c_sett.moveToFirst() == true) {
                if (c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_NOTIFICACIONES)) == tipoSettings.ACTIVO)
                    comprobarNotificaciones();
            }
        }
        else {
            Intent intent = new Intent(myActivity.this, login.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        simpleDataView sdv = (simpleDataView) findViewById(R.id.kms_view);
        String edit_kms = (String) sdv.getTextEditLeerPantalla().getText().toString();
        outState.putString("EditKms", edit_kms);
        sdv = (simpleDataView) findViewById(R.id.matricula_view);
        String edit_matricula = (String) sdv.getTextEditLeerPantalla().getText().toString();
        outState.putString("EditMatricula", edit_matricula);
        sdv = (simpleDataView) findViewById(R.id.fechaitv_view);
        String edit_itv = sdv.getValue().toString();
        outState.putString("NoEditItv", edit_itv);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        simpleDataView sdv = (simpleDataView) findViewById(R.id.kms_view);
        String kms_restore = savedInstanceState.getString("EditKms");
        sdv.setEdit(kms_restore);

        sdv = (simpleDataView) findViewById(R.id.matricula_view);
        String matricula_restore = savedInstanceState.getString("EditMatricula");
        sdv.setEdit(matricula_restore);

        String itv_restore = savedInstanceState.getString("NoEditItv").toString();
        sdv = (simpleDataView) findViewById(R.id.fechaitv_view);
        sdv.setValue(itv_restore);
        sdv.setEditInvisible();
        sdv.setEdit(itv_restore);
        sdv.setEditHint(itv_restore);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(myActivity.this, settings.class);
            myActivity.this.startActivity(i);
        }
        if (id == R.id.action_info) {
            if (id == R.id.action_info) {
                Intent i = new Intent(myActivity.this, info.class);
                myActivity.this.startActivity(i);
            }
        }
        if (id == R.id.action_logout) {
            if (login.getIdFacebook(this) == null){
                login.goToLoginScreen(this);
                usuario u = new usuario();
                u.logout(myActivity.this);
                login.deleteParamsAnonimo(myActivity.this);
                Intent intent = new Intent(myActivity.this, login.class);
                startActivity(intent);
            } else {
                usuario u = new usuario();
                u.logout(myActivity.this);
                login.deleteParamsAnonimo(myActivity.this);
                login.closeFacebookSession(this, login.class);
                Intent intent = new Intent(myActivity.this, login.class);
                startActivity(intent);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static final int PETICION_ACTIVITY_ADDITV = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (PETICION_ACTIVITY_ADDITV) : {
                if (resultCode == Activity.RESULT_OK) {
                    String itv_string = data.getExtras().getString("itv_string");
                    simpleDataView sdv = (simpleDataView) findViewById(R.id.fechaitv_view);
                    sdv.setTitle("Fecha ITV");
                    sdv.setValue(itv_string);
                    sdv.setEditInvisible();
                    sdv.setImage(getResources().getDrawable(R.drawable.ic_fecha));
                }
                break;
            }
        }
    }



}
