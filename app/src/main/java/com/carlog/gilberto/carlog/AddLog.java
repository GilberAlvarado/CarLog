package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddLog extends Activity {

    private Spinner spinner1;
    private List<String> lista;


    private void RellenarTipos() {
        spinner1 = (Spinner) findViewById(R.id.cmb_tipos);
        lista = new ArrayList<String>();
        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos);
        lista.add("Revisión general");
        lista.add("Cambio de aceite");
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);
    }

    private void SeleccionarTipo() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(arg0.getContext(), "Seleccionado: " + arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        final EditText edttxt = (EditText) findViewById (R.id.txt_nuevoTipo);
        Button btm_agregar = (Button) findViewById (R.id.btm_addTipo);
        btm_agregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                String nuevoDato = edttxt.getText().toString();
                nuevoDato=nuevoDato.trim();
                if (nuevoDato.length()>1){
                    lista.add(nuevoDato);
                    edttxt.setText("");
                }
            }
        });
    }



    private void Guardar() {
        //Instanciamos el Boton
        Button btn1 = (Button) findViewById(R.id.guardar);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddLog.this, DatosIniciales.class);

                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos);
                String tipo = spinner.getSelectedItem().toString();
                Date fecha = new Date();

                TipoLog miTipo = new TipoLog(tipo, fecha);
                intent.putExtra("miTipo", miTipo);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);
        RellenarTipos();
        SeleccionarTipo();
        Guardar();
    }



}
