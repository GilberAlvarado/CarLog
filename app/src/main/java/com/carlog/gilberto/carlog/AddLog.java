package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.data.DBTiposRevision;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddLog extends Activity {

    private Spinner spinner1;

    private void RellenarTipos(DBTiposRevision managerTiposRevision) {
        spinner1 = (Spinner) findViewById(R.id.cmb_tipos);
        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos);

        Cursor cursor = managerTiposRevision.cargarCursorTiposRevision();
        //Recorremos el cursor
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo = cursor.getString(1);
            tipos.add(tipo);
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);
    }

    private void SeleccionarTipo(final DBTiposRevision managerTiposRevision) {
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
        final TextView tv_nt = (TextView) findViewById(R.id.nuevoTipo);
        Button btm_agregar = (Button) findViewById (R.id.btm_addTipo);
        btm_agregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                edttxt.setVisibility(View.VISIBLE);
                tv_nt.setVisibility(View.VISIBLE);

                String nuevoDato = edttxt.getText().toString();
                nuevoDato=nuevoDato.trim();
                if (nuevoDato.length()>1){ // si ha escrito algo
                    //primero miramos si existe para no insertar duplicados
                    Cursor c = managerTiposRevision.buscarTipo(nuevoDato);
                    if (c.moveToFirst() == false) {
                        managerTiposRevision.insertar(nuevoDato);
                        RellenarTipos(managerTiposRevision);
                        edttxt.setVisibility(View.INVISIBLE);
                        tv_nt.setVisibility(View.INVISIBLE);
                        edttxt.setText("");
                    }
                    else Toast.makeText(getApplicationContext(), "El tipo ya existe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btm_eliminar = (Button) findViewById (R.id.btm_elimTipo);
        btm_eliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos);
                String elimdata = spinner.getSelectedItem().toString();
                managerTiposRevision.eliminar(elimdata);
                RellenarTipos(managerTiposRevision);
            }
        });
    }



    private void GuardarLog(final DBLogs managerLogs) {
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


                managerLogs.insertar(miTipo);



                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context contextNew = getApplicationContext();
        DBLogs managerLogs = new DBLogs(contextNew);
        DBTiposRevision managerTiposRevision = new DBTiposRevision(contextNew);

        setContentView(R.layout.activity_add_log);
        RellenarTipos(managerTiposRevision);
        SeleccionarTipo(managerTiposRevision);
        GuardarLog(managerLogs);
    }




}
