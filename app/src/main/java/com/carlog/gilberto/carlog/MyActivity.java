package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;


public class MyActivity extends Activity {

    EditText marcaT, modeloT, yearT, kmsT, itvT;
    String marca, modelo, year, kms, itv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);




        //Instanciamos el Boton
        Button btn1 = (Button) findViewById(R.id.btn1);

        /*
          Definimos un método OnClickListener para que
          al pulsar el botón se nos muestre la segunda actividad
        */
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this,
                        siguiente.class);

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

                if (TextUtils.isEmpty(marca)) {
                    Toast.makeText(MyActivity.this, "Ha de introducir la marca.", Toast.LENGTH_LONG).show();
                } else
                if (TextUtils.isEmpty(modelo)) {
                    Toast.makeText(MyActivity.this, "Ha de introducir el modelo.", Toast.LENGTH_LONG).show();
                } else
                if (TextUtils.isEmpty(kms)) {
                    Toast.makeText(MyActivity.this, "Ha de introducir el nº de kilómetros.", Toast.LENGTH_LONG).show();
                } else {

                    intent.putExtra("marca", marca);
                    intent.putExtra("modelo", modelo);
                    intent.putExtra("year", year);
                    intent.putExtra("kms", kms);
                    intent.putExtra("itv", itv);

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
