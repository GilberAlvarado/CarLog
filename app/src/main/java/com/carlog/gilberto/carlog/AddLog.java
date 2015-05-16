package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.carlog.gilberto.carlog.data.DataBaseManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                //Toast.makeText(arg0.getContext(), "Seleccionado: " + arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
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



    private void GuardarLog() {
        //Instanciamos el Boton
        Button btn1 = (Button) findViewById(R.id.guardar);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddLog.this, DatosIniciales.class);

                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos);
                String tipo = spinner.getSelectedItem().toString();

                DatePicker datePicker = (DatePicker) findViewById(R.id.date_newlog);
                String txt_date_newlog = "";
                Date fecha_newlog = new Date();

                // configuramos el formato en el que esta guardada la fecha en los strings que nos pasan
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

                if ((datePicker.getDayOfMonth() < 10) && ((datePicker.getMonth()+1) < 10)) {
                    txt_date_newlog = "0"+datePicker.getDayOfMonth() +"-0"+ (datePicker.getMonth()+1) + "-" + datePicker.getYear();
                } else
                if ((datePicker.getDayOfMonth() < 10) && ((datePicker.getMonth()+1) >= 10)) {
                    txt_date_newlog = "0"+datePicker.getDayOfMonth() +"-"+ (datePicker.getMonth()+1)+ "-" + datePicker.getYear();
                } else
                if ((datePicker.getDayOfMonth() >= 10) && ((datePicker.getMonth()+1) < 10)) {
                    txt_date_newlog = ""+datePicker.getDayOfMonth() +"-0"+ (datePicker.getMonth()+1) + "-0" + datePicker.getYear();
                } else
                    txt_date_newlog = ""+datePicker.getDayOfMonth() +"-"+ (datePicker.getMonth()+1)+ "-" + datePicker.getYear();


                try {
                    // aca realizamos el parse, para obtener objetos de tipo Date de las Strings
                    fecha_newlog = formato.parse(txt_date_newlog);
                } catch (ParseException e) {
                    // Log.e(TAG, "Funcion diferenciaFechas: Error Parse " + e);
                } catch (Exception e){
                    // Log.e(TAG, "Funcion diferenciaFechas: Error " + e);
                }


                System.out.println("FECHA NEW LOG "+datePicker.getDayOfMonth()+ "-" + (datePicker.getMonth()+1) + "-" + datePicker.getYear());


                TipoLog miTipo = new TipoLog(tipo, fecha_newlog, txt_date_newlog);
                System.out.println("LOG "+tipo + " " + fecha_newlog + " " + txt_date_newlog);
                //intent.putExtra("miTipo", miTipo);

                Context contextNew = getApplicationContext();
                DataBaseManager manager = new DataBaseManager(contextNew);
                manager.insertar(miTipo);



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
        GuardarLog();
    }



}
