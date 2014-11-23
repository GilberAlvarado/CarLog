package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;


public class MyActivity extends Activity {

    EditText marcaT, modeloT, yearT, kmsT, itvT;
    String marca, modelo, year, kms, itv;

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

        TextView text=(TextView)findViewById(R.id.marca);
        text.setText(marca);
        text=(TextView)findViewById(R.id.modelo);
        text.setText(modelo);
        text=(TextView)findViewById(R.id.year);
        text.setText(year);
        text=(TextView)findViewById(R.id.kms);
        text.setText(kms);
        text=(TextView)findViewById(R.id.itv);
        text.setText(itv);


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

                marcaT = (EditText) findViewById (R.id.marca);
                marca = marcaT.getText().toString();
                System.out.println(marca);

                modeloT = (EditText) findViewById (R.id.modelo);
                modelo = modeloT.getText().toString();
                System.out.println(modelo);

                yearT = (EditText) findViewById (R.id.year);
                year = yearT.getText().toString();
                System.out.println(year);

                kmsT = (EditText) findViewById (R.id.kms);
                kms = kmsT.getText().toString();
                System.out.println(kms);

                itvT = (EditText) findViewById (R.id.itv);
                itv = itvT.getText().toString();
                System.out.println(itv);

                // Los campos marca, modelo y nº de kilometros deben de ser obligatorios
                if (TextUtils.isEmpty(marca)) {
                    Toast.makeText(MyActivity.this, "Ha de introducir la marca.", Toast.LENGTH_LONG).show();
                } else
                if (TextUtils.isEmpty(modelo)) {
                    Toast.makeText(MyActivity.this, "Ha de introducir el modelo.", Toast.LENGTH_LONG).show();
                } else
                if (TextUtils.isEmpty(kms)) {
                    Toast.makeText(MyActivity.this, "Ha de introducir el nº de kilómetros.", Toast.LENGTH_LONG).show();
                } else {


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
