package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carlog.gilberto.carlog.data.DataBaseManager;
import com.carlog.gilberto.carlog.data.DbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyActivity extends Activity {

    private Spinner spinner_marcas;
    private Spinner spinner_years;
    private List<String> lista_marcas;
    private List<String> lista_years;


    private void RellenarMarcas() {
        //spinner_marcas = (Spinner) findViewById(R.id.cmb_marcas);
        lista_marcas = new ArrayList<String>();
        spinner_marcas = (Spinner) this.findViewById(R.id.cmb_marcas);
        lista_marcas.add("Introduzca marca");
        lista_marcas.add("Audi");
        lista_marcas.add("BMW");
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_marcas);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_marcas.setAdapter(adaptador);

        if (!marca.equals("Introduzca Marca")) {
            int spinnerPostion = adaptador.getPosition(marca);
            spinner_marcas.setSelection(spinnerPostion);
            spinnerPostion = 0;
        }

    }

    private void RellenarYears() {
        //spinner_marcas = (Spinner) findViewById(R.id.cmb_marcas);
        lista_years = new ArrayList<String>();
        spinner_years = (Spinner) this.findViewById(R.id.cmb_years);
        lista_years.add("Introduzca año");
        Calendar calen = Calendar.getInstance();
        Integer hoy = calen.get(Calendar.YEAR);
        for(int i = hoy; i >= 1900; i--) {
            lista_years.add(""+i);
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_years);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_years.setAdapter(adaptador);


        if (!year.equals("Introduzca Año")) {
            int spinnerPostion = adaptador.getPosition(year);
            spinner_years.setSelection(spinnerPostion);
            spinnerPostion = 0;
        }

    }

    private void RellenarPantalla() {
        RellenarMarcas();
        RellenarYears();

        TextView text = (TextView)findViewById(R.id.modelo);
        text.setText(modelo);

        text=(TextView)findViewById(R.id.kms);
        text.setText(kms);

        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        try {
            // aca realizamos el parse, para obtener objetos de tipo Date de las Strings
            fechaITV = formato.parse(itv);
            System.out.println("FECHA ITV "+fechaITV);
        } catch (ParseException e) {
            // Log.e(TAG, "Funcion diferenciaFechas: Error Parse " + e);
        } catch (Exception e){
            // Log.e(TAG, "Funcion diferenciaFechas: Error " + e);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaITV);

        DatePicker datePicker2 = (DatePicker) findViewById(R.id.date_itv);
        datePicker2.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
    }

    private void LeerDatosPantalla() {
        Spinner spinner_marca = (Spinner)findViewById(R.id.cmb_marcas);
        marca = spinner_marca.getSelectedItem().toString();
        System.out.println(marca);

        modeloT = (EditText) findViewById (R.id.modelo);
        modelo = modeloT.getText().toString();
        System.out.println(modelo);

        Spinner spinner_year = (Spinner)findViewById(R.id.cmb_years);
        year = spinner_year.getSelectedItem().toString();
        System.out.println(year);

        kmsT = (EditText) findViewById (R.id.kms);
        kms = kmsT.getText().toString();
        System.out.println(kms);

        DatePicker datePicker = (DatePicker) findViewById(R.id.date_itv);


        // configuramos el formato en el que esta guardada la fecha en los strings que nos pasan
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

        if ((datePicker.getDayOfMonth() < 10) && ((datePicker.getMonth()+1) < 10)) {
            itv = "0"+datePicker.getDayOfMonth() +"-0"+ (datePicker.getMonth()+1) + "-" + datePicker.getYear();
        } else
        if ((datePicker.getDayOfMonth() < 10) && ((datePicker.getMonth()+1) >= 10)) {
            itv = "0"+datePicker.getDayOfMonth() +"-"+ (datePicker.getMonth()+1)+ "-" + datePicker.getYear();
        } else
        if ((datePicker.getDayOfMonth() >= 10) && ((datePicker.getMonth()+1) < 10)) {
            itv = ""+datePicker.getDayOfMonth() +"-0"+ (datePicker.getMonth()+1) + "-0" + datePicker.getYear();
        } else
            itv = ""+datePicker.getDayOfMonth() +"-"+ (datePicker.getMonth()+1)+ "-" + datePicker.getYear();


        try {
            // aca realizamos el parse, para obtener objetos de tipo Date de las Strings
            fechaITV = formato.parse(itv);
        } catch (ParseException e) {
            // Log.e(TAG, "Funcion diferenciaFechas: Error Parse " + e);
        } catch (Exception e){
            // Log.e(TAG, "Funcion diferenciaFechas: Error " + e);
        }


        System.out.println(""+datePicker.getDayOfMonth()+ "-" + (datePicker.getMonth()+1) + "-" + datePicker.getYear());
    }



    EditText modeloT, kmsT;
    String marca, modelo, year, kms, itv;
    Date fechaITV = null;

    //comprobaciones
    private boolean comprobaciones() {
        boolean ok = true;
        try {
            Integer mykms = Integer.parseInt(kms);
        } catch(NumberFormatException nfe) {
            Toast.makeText(MyActivity.this, "Ha de introducir un nº de kilómetros correcto.", Toast.LENGTH_LONG).show();
            ok = false;
        }
        if (!TextUtils.isEmpty(year) && !year.equals("Introduzca año")) { // el año no es obligatorio
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
            if (TextUtils.isEmpty(marca) || marca.equals("Introduzca marca")) {
                Toast.makeText(MyActivity.this, "Ha de introducir la marca.", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(modelo)) {
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

        // Obtenemos la instancia de las preferencias de la Activity
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        // Leemos el dato guardado
        marca = settings.getString("marca", "Introduzca Marca");
        modelo = settings.getString("modelo", "Introduzca Modelo");
        year = settings.getString("year", "Introduzca Año");
        kms = settings.getString("kms", "Introduzca Nº Kms");
        itv = settings.getString("itv", "Introduzca Fecha ITV");


        DataBaseManager manager = new DataBaseManager(this);

        RellenarPantalla();


        //Instanciamos el Boton
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


                if (comprobaciones()) {
                    // Necesitamos un editor para poder modificar los valores de la instancia settings
                    SharedPreferences settings = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    // Modificamos el valor deseado
                    editor.putString("marca", marca);
                    editor.putString("modelo", modelo);
                    editor.putString("year", year);
                    editor.putString("kms", kms);
                    editor.putString("itv", itv);
                    // Una vez finalizado, llámando a commit se guardan las preferencias en memoria no volatil
                    editor.commit();


                 /*   intent.putExtra("marca", marca); //Ya no guardamos las variables sueltas sino en un objeto Coche
                    intent.putExtra("modelo", modelo);
                    intent.putExtra("year", year);
                    intent.putExtra("kms", kms);
                    intent.putExtra("itv", itv); */

                    Coche miCoche = new Coche(marca, modelo, year, kms, itv);
                    intent.putExtra("miCoche", miCoche);

                    startActivity(intent);
                }

            }
        });

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

    //Este método se ejecutará cuando se presione el botón btn1
    public void siguiente(View view) {

    }





}
