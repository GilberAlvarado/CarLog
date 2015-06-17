package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.carlog.gilberto.carlog.negocio.CambiarCocheActivo;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carlog.gilberto.carlog.tiposClases.CocheEsNuevo;
import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.tiposClases.TipoCoche;
import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.data.DBMarcas;
import com.carlog.gilberto.carlog.data.DBModelos;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.negocio.ProcesarAceite;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyActivity extends ActionBarActivity {

    public final static String INICIAL_MARCA = "Introduzca marca";
    public final static String INICIAL_YEAR = "Introduzca año";
    public final static String INICIAL_KMS = "Introduzca Kms";
    public final static String INICIAL_MATRICULA = "Introduzca matrícula";
    public final static String INICIAL_MODELO = "Introduzca Modelo";


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


    private void RellenarMarcas(Cursor c) {
        lista_marcas = new ArrayList<String>();
        spinner_marcas = (Spinner) findViewById(R.id.cmb_marcas);

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

        if (c.moveToFirst() == true) {
            marca = c.getString(c.getColumnIndex(DBCar.CN_MARCA));
            int spinnerPostion = adaptador.getPosition(marca);
            spinner_marcas.setSelection(spinnerPostion);
        }

    }


    private void RellenarModelos(final Cursor c) {
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

                if (c.moveToFirst() == true) {
                    modelo = c.getString(c.getColumnIndex(DBCar.CN_MODELO));
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
        spinner_modelos = (Spinner) findViewById(R.id.cmb_modelo);

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

        if (c.moveToFirst() == true) {
            modelo = c.getString(c.getColumnIndex(DBCar.CN_MODELO));
            int spinnerPostion = adaptador.getPosition(modelo);
            spinner_modelos.setSelection(spinnerPostion);
        }

    }

    private void RellenarYears(Cursor c) {
        //spinner_marcas = (Spinner) findViewById(R.id.cmb_marcas);
        lista_years = new ArrayList<String>();
        spinner_years = (Spinner) findViewById(R.id.cmb_years);
        lista_years.add(INICIAL_YEAR);
        Calendar calen = Calendar.getInstance();
        Integer hoy = calen.get(Calendar.YEAR);
        for(int i = hoy; i >= 1900; i--) {
            lista_years.add(""+i);
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_years);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_years.setAdapter(adaptador);

        if (c.moveToFirst() == true) {
            year = String.valueOf(c.getInt(c.getColumnIndex(DBCar.CN_YEAR)));
            int spinnerPostion = adaptador.getPosition(year);
            spinner_years.setSelection(spinnerPostion);
        }

    }


    private void RellenarPantalla(Cursor c) {
        RellenarMarcas(c);
        RellenarModelos(c);
        RellenarYears(c);

        if (c.moveToFirst() == true) {
            matricula = c.getString(c.getColumnIndex(DBCar.CN_MATRICULA));
            int_kms = c.getInt(c.getColumnIndex(DBCar.CN_KMS));
            int_itv = c.getInt(c.getColumnIndex(DBCar.CN_ITV));
            int_kms_ini = c.getInt(c.getColumnIndex(DBCar.CN_KMS_INI));
            int_fecha_ini = c.getInt(c.getColumnIndex(DBCar.CN_FECHA_INI));
            kms = String.valueOf(int_kms);
            int_kms_anterior = int_kms;
            itv = funciones.int_a_string(int_itv);
        }

        TextView text = (TextView)findViewById(R.id.matricula);
        text.setText(matricula);
        //if(!matricula.equals("Introduzca Matrícula")) text.setEnabled(false); //no editable si ya está la matrícula

        FloatLabeledEditText f_text=(FloatLabeledEditText)findViewById(R.id.kms);
        // text=(TextView)findViewById(R.id.kms);
        f_text.getEditText().setText(kms);

        fechaITV = funciones.string_a_date(itv);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaITV);

        DatePicker datePicker2 = (DatePicker) findViewById(R.id.date_itv);
        datePicker2.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
    }



    private void procesar(Context context) {
        DBCar dbcar = new DBCar(context);
        Cursor c = dbcar.buscarCocheActivo();

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

        // Aunque no hagamos cambios se procesa siempre porque podemos haber eliminado logs o editado o marcados como realizados y se deben recalcular al mostrar
        DBLogs dbLogs= new DBLogs(context);
        int int_now = funciones.date_a_int(new Date());


        ///////////////////PARA EL ACEITE
        ProcesarAceite.procesar_aceite(dbLogs, int_now, context, int_kms, int_fecha_ini, int_kms_ini, matricula);
        //////////////////IR AÑADIENDO PARA EL RESTO DE TIPOS

        ////////////////////////////////////////////////////////
    }


    private void Siguiente(final Context context, final boolean EditarCoche) {
        ButtonRectangle btn_siguiente = (ButtonRectangle) findViewById(R.id.button_siguiente);

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, ListaLogs.class);

                LeerDatosPantalla();
                DBCar dbcar = new DBCar(context);

                if (comprobaciones(EditarCoche)) {
                    dbcar.ActualizarTodosCocheNOActivo(); // Nos aseguramos de que ponemos todos los coches a inactivos para marcar como activo el nuevo
                    if(!EditarCoche) { // Si no estamos editando es nuevo
                        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, funciones.date_a_int(new Date()), int_kms);
                        dbcar.insertinsertOrUpdate(miCoche);
                    } else if(int_kms_ini == 0) { // si el coche no existía (no es devuelto en cursor, no tiene históricos)  se inicializa
                        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, funciones.date_a_int(new Date()), int_kms);
                        dbcar.insertinsertOrUpdate(miCoche);
                    }
                    else if((int_kms_ini != 0) && (int_kms_anterior == int_kms)) { // si el coche existía y no actualizamos el nº de kms -> no necesitamos actualizar lasfechas de futuros logs (Todos los tipos)
                        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);
                        dbcar.insertinsertOrUpdate(miCoche);
                    }
                    else if((int_kms_ini != 0) && (int_kms_anterior != int_kms)) { // si el coche existía y actualizamos el nº de kms -> necesitamos actualizar las fechas de futuros logs (Todos los tipos)
                        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);
                        dbcar.insertinsertOrUpdate(miCoche);
                    }

                    Cursor c = dbcar.buscarCoches();

                    CambiarCocheActivo.CambiarCocheActivo(dbcar, c, MyActivity.this, context);  //actualizamos los coches en el navigation bar por si se crea uno nuevo



                    procesar(context);


                    startActivity(intent);
                    // Si agregamos un nuevo coche y volvemos hacia atras se sale de la app pero desde la pantalla de logs puesto que ya hemos agregado un coche y por lo tanto no se queda el drawer sin el coche nuevo al volver atras
                    // Si no queremos agregar nuevo coche y pulsamos hacia atras regresamos a la lista de logs anterior
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

            }
        });
    }

    public void VaciarPantalla() {
        CocheEsNuevo.getInstance().coche_es_nuevo = 1;

        TextView text = (TextView) findViewById(R.id.matricula);
        text.setText("");

        FloatLabeledEditText f_text= (FloatLabeledEditText) findViewById(R.id.kms);
        f_text.getEditText().setText("");

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

        //kmsT = (EditText) findViewById (R.id.kms);
        kmsT = (FloatLabeledEditText) findViewById (R.id.kms);
        kms = kmsT.getEditText().getText().toString();
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







    EditText matriculaT;
    FloatLabeledEditText kmsT;
    String matricula = "", marca = "", modelo = "", year = "", kms = "", itv = "";
    Date fechaITV = new Date();
    int int_year, int_kms, int_kms_anterior = 0, int_itv, int_kms_ini = 0, int_fecha_ini = 0;



    private void ocultar_campos() {
        // Se ocultan todos los campos obligatorios porque ya han sido agregados
        TextView text = (TextView)findViewById(R.id.matricula);
        text.setVisibility(View.GONE);
        spinner_marcas.setVisibility(View.GONE);
        spinner_modelos.setVisibility(View.GONE);

        // Los no obligatorios debemos comprobar que se hayan añadido (luego no se van a poder modificar)
        if (int_year != MyActivity.NO_KMS) {
            spinner_years.setVisibility(View.GONE);
        }
    }

    //comprobaciones
    private boolean comprobaciones(boolean EditarCoche) {
        System.out.println("editar cocheee" + EditarCoche + " " +kms + " " +int_kms_anterior);
        boolean ok = true;
        if(EditarCoche) {
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
            } else if (TextUtils.isEmpty(modelo) || modelo.equals(INICIAL_MODELO)) {
                Toast.makeText(MyActivity.this, "Ha de introducir el modelo.", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(kms)) {
                Toast.makeText(MyActivity.this, "Ha de introducir el nº de kilómetros.", Toast.LENGTH_LONG).show();
            } else {
                return true;

            }

        }
        return false;
    }

    private void addCarOrShowLogs() {
        Context context = getApplicationContext();

        DBCar dbcar = new DBCar(context);
        Cursor c = dbcar.buscarCoches();

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

        if ((c.moveToFirst() == false) || CocheNuevo || EditarCoche) {  // Desde que haya un coche no se mostrará la primera actividad o si añadirmos un coche nuevo
            setContentView(R.layout.activity_my);

            toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
            setSupportActionBar(toolbar);

            CambiarCocheActivo.CambiarCocheActivo(dbcar, c, MyActivity.this, context);

            c = dbcar.buscarCocheActivo();

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

            RellenarMarcas(c);
            RellenarModelos(c);
            RellenarYears(c);
            Siguiente(context, EditarCoche);
            if(CocheNuevo) {
                VaciarPantalla();
            }
            if(EditarCoche) {
                RellenarPantalla(c);
                ocultar_campos();
            }
        }
        else {
            Intent intent = new Intent(MyActivity.this, ListaLogs.class);
            procesar(context);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addCarOrShowLogs();

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
