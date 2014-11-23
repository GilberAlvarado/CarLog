package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Gilberto on 29/10/2014.
 */
public class DatosIniciales extends Activity {

    private ArrayList<String> datos_lista_log = new ArrayList<String>();

    private void rellenarListaLog() {
        //String[] datos_lista_log = new String[]{"Elemento inicial prueba"};

        datos_lista_log.add("Elemento inicial prueba");
        //Creación del adaptador, vamos a escoger el layout
        //simple_list_item_1, que los mostr
        ListAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, datos_lista_log);

        //Asociamos el adaptador a la vista.
        ListView lv = (ListView) findViewById(R.id.lista_log);
        lv.setAdapter(adaptador);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_iniciales);


        final Coche miCoche = (Coche)getIntent().getExtras().getSerializable("miCoche");

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

        rellenarListaLog();

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
                    TipoLog miTipo = (TipoLog) data.getExtras().getSerializable("miTipo");
                    String tipo = miTipo.getTipo(miTipo);

                    // Añdimos el nuevo elemento a la lista de logs
                    datos_lista_log.add(tipo);

                    //Creación del adaptador, vamos a escoger el layout
                    //simple_list_item_1, que los mostr
                    ListAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, datos_lista_log);

                    //Asociamos el adaptador a la vista.
                    ListView lv = (ListView) findViewById(R.id.lista_log);
                    lv.setAdapter(adaptador);
                }
                break;
            }
        }
    }


}
