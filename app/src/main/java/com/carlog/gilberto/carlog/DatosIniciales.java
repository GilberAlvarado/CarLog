package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.carlog.gilberto.carlog.data.DBLogs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Gilberto on 29/10/2014.
 */
public class DatosIniciales extends Activity {

    private ArrayList<String> datos_lista_log = new ArrayList<String>();


    private void borrarLogpulsado(ListView lv, final Cursor cursor, final DBLogs manager) {
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DatosIniciales.this);
                builder.setMessage("¿Está seguro de querer eliminar?")
                        .setTitle("Borrar de la lista")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id_dialog) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Continuar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id_dialog) {
                                        // metodo que se debe implementar Sí
                                        //Recorremos el cursor
                                        ArrayList<String> tipos = new ArrayList<String>();
                                        int j = 0;
                                        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                                            if (j == i) { // la posicion del cursor coincide con la del que pulsamos en la lista
                                                int id = cursor.getInt(0);
                                                manager.eliminar_por_id(id);
                                                ConsultarLogs();

                                                break;
                                            }
                                            j++;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();







                return false;
            }
        });
    }

    private void ConsultarLogs() {
        Context contextNew = getApplicationContext();
        final DBLogs manager = new DBLogs(contextNew);
        String[] from = new String[]{manager.CN_TIPO, "fecha_string"};
        int[] to = new int[] {android.R.id.text1, android.R.id.text2};
        final Cursor cursor = manager.LogsOrderByFechaString();





  /*      for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            int colum_fecha = cursor.getColumnIndex(manager.CN_FECHA);
            Date fecha = new Date(cursor.getLong(colum_fecha)*1000);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String fecha_txt = dateFormat.format(fecha);
System.out.println("FECHOTE1 "+fecha_txt);
        }*/



        final SimpleCursorAdapter adaptador = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, from, to);
        //Asociamos el adaptador a la vista.
        ListView lv = (ListView) findViewById(R.id.lista_log);
        lv.setAdapter(adaptador);

        borrarLogpulsado(lv, cursor, manager);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_iniciales);


        final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");

        String marca = miCoche.getMarca(miCoche);
        String modelo = miCoche.getModelo(miCoche);
        String year = miCoche.getYear(miCoche);
        String kms = miCoche.getKms(miCoche);
        String itv = miCoche.getItv(miCoche);

        TextView text=(TextView)findViewById(R.id.marca2);
        text.setText(marca);

        text=(TextView)findViewById(R.id.modelo2);
        text.setText(modelo);

        text=(TextView)findViewById(R.id.year2);
        text.setText(year);

        text=(TextView)findViewById(R.id.kms2);
        text.setText(kms);

        text=(TextView)findViewById(R.id.itv2);
        text.setText(itv);

        ConsultarLogs();

        //Instanciamos el Boton
        Button btn1 = (Button) findViewById(R.id.add_log);

        /*
          Definimos un método OnClickListener para que
          al pulsar el botón se nos muestre la segunda actividad
        */
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatosIniciales.this, AddLog.class);


                intent.putExtra("miCoche", miCoche);

                startActivityForResult(intent, PETICION_ACTIVITY_ADD_LOG);
            }
        });






    }

    private final int PETICION_ACTIVITY_ADD_LOG = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (PETICION_ACTIVITY_ADD_LOG) : {
                if (resultCode == Activity.RESULT_OK) {
                    //TipoLog miTipo = (TipoLog) data.getExtras().getSerializable("miTipo");
                    ConsultarLogs();

                }
                break;
            }
        }
    }


}
