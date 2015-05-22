package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBLogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyActivity extends Activity {

    private Spinner spinner_marcas;
    private Spinner spinner_years;
    private List<String> lista_marcas;
    private List<String> lista_years;

    public final static int NO_YEARS = -1;
    public final static int NO_KMS = -1;
    public final static int NO_ITV = -1;


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

        TextView text = (TextView)findViewById(R.id.matricula);
        text.setText(matricula);

        text = (TextView)findViewById(R.id.modelo);
        text.setText(modelo);

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

        matriculaT = (EditText) findViewById (R.id.matricula);
        matricula = matriculaT.getText().toString();
        System.out.println(matricula);

        modeloT = (EditText) findViewById (R.id.modelo);
        modelo = modeloT.getText().toString();
        System.out.println(modelo);

        Spinner spinner_year = (Spinner)findViewById(R.id.cmb_years);
        year = spinner_year.getSelectedItem().toString();
        System.out.println(year);
        if(year.equals("Introduzca año")) int_year = NO_YEARS;
        else int_year = Integer.parseInt(year);

        kmsT = (EditText) findViewById (R.id.kms);
        kms = kmsT.getText().toString();
        if(kms.isEmpty() || kms.equals("Introduzca Kms")) int_kms = NO_KMS;
        else int_kms = Integer.parseInt(kms);

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
        try {
            Integer mykms = Integer.parseInt(kms);
            if(mykms < int_kms_anterior) {
                Toast.makeText(MyActivity.this, "Ha introducido un número de kms menor que el anterior.", Toast.LENGTH_LONG).show();
                ok = false;
            }
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
            if (TextUtils.isEmpty(matricula) || matricula.equals("Introduzca matrícula")) {
                Toast.makeText(MyActivity.this, "Ha de introducir la matrícula.", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(marca) || marca.equals("Introduzca marca")) {
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

        /*
        // Obtenemos la instancia de las preferencias de la Activity
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        // Leemos el dato guardado
        matricula = settings.getString("matricula", "Introduzca Matrícula");
        marca = settings.getString("marca", "Introduzca Marca");
        modelo = settings.getString("modelo", "Introduzca Modelo");
        year = settings.getString("year", "Introduzca Año");
        kms = settings.getString("kms", "Introduzca Nº Kms");
        itv = settings.getString("itv", "Introduzca Fecha ITV");
*/

        DBCar dbcar = new DBCar(this);
        Cursor c = dbcar.buscarCoches();

        //DE MOMENTO USAMOS EL PRIMERO pero contamos lo que se crean (Intento no cambiar la matricula para no crear más
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
            int_kms_anterior = int_kms;

            System.out.println("Fecha de creación del coche: " + funciones.int_a_string(int_fecha_ini));
            System.out.println("Kms al crear el coche: " + int_kms_ini);
        }

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
         /*           // Necesitamos un editor para poder modificar los valores de la instancia settings
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
                    */



                    if(int_kms_ini == 0) { // si el coche no existía (no es devuelto en cursor)
                        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, funciones.date_a_int(new Date()), int_kms);
                        DBCar.insertinsertOrUpdate(miCoche);
                    }
                    else if((int_kms_ini != 0) && (int_kms_anterior != int_kms)) {
                       /******************** media para cuando haga los4 casos de añadir log pero ver q media = kms ini / dias coche puede ser así o kms /dias coche sin el ini dependiendo de los 4 casos
                       int dias_coche = (int) funciones.dias_entre_2_fechas(funciones.int_a_date(int_fecha_ini), new Date());
                        int int_media = int_kms_ini / dias_coche; */
                        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, int_fecha_ini, int_kms_ini);
                        DBCar.insertinsertOrUpdate(miCoche);
                    }






                    //intent.putExtra("miCoche", miCoche);

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
