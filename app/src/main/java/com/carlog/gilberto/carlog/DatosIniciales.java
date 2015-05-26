package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBLogs;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Gilberto on 29/10/2014.
 */
public class DatosIniciales extends Activity {

    private ArrayList<String> datos_lista_log = new ArrayList<String>();


    private void borrarLogpulsado(ListView lv, final Cursor cursor, final DBLogs manager, final int posicion) {


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
                                        int i = 0;
                                        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                                            if (i == posicion) { // la posicion del cursor coincide con la del que pulsamos en la lista
                                                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));
                                                manager.eliminar_por_id(id);
                                                ConsultarLogs();

                                                break;
                                            }
                                            i++;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();


    }


    private void modificarLogpulsado(ListView lv, final Cursor cursor, final DBLogs manager, int posicion) {
        //final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");
        Intent intent = new Intent(DatosIniciales.this, modificarAceite.class);
        //Recorremos el cursor
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (i == posicion) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));
                String tipo = cursor.getString(cursor.getColumnIndex(DBLogs.CN_TIPO));
                String txt_fecha = cursor.getString(cursor.getColumnIndex("fecha_string"));
                int aceite = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ACEITE));
                String matricula = cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR));
                int kms = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_KMS));


                TipoLog miTipo = new TipoLog(tipo, funciones.string_a_date(txt_fecha), txt_fecha, funciones.string_a_int(txt_fecha), aceite, matricula, DBLogs.NO_REALIZADO, kms);
                intent.putExtra("miTipo", miTipo);
                intent.putExtra("idLog", id);
                break;
            }
            i++;
        }


        startActivity(intent);

    }

    private void realizadoLogpulsado(ListView lv, final Cursor cursor, final DBLogs manager, int posicion) {
        final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");
        //Recorremos el cursor
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (i == posicion) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(0);
                manager.marcarRealizadoLog(id, funciones.date_a_int(new Date()), miCoche.getKms(miCoche)); //hoy
                ConsultarLogs();

                break;
            }
            i++;
        }

    }



    private void ConsultarLogs() {
        Context contextNew = getApplicationContext();
        final DBLogs manager = new DBLogs(contextNew);
        String[] from = new String[]{manager.CN_TIPO, "fecha_string"};
        int[] to = new int[] {android.R.id.text1, android.R.id.text2};

        int int_now = funciones.date_a_int(new Date());

        final Cursor cursor = manager.LogsTodosOrderByFechaString(int_now);



        final SimpleCursorAdapter adaptador = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, from, to);
        //Asociamos el adaptador a la vista.
        ListView lv = (ListView) findViewById(R.id.lista_log);
        lv.setAdapter(adaptador);

        //Asociamos el menú contextual a los controles
        registerForContextMenu(lv);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_iniciales);


       // final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");
        DBCar dbcar = new DBCar(this);
        Cursor c = dbcar.buscarCoches();

        String matricula = "", marca = "", modelo = "", year = "", kms = "", itv = "";
        int int_year = MyActivity.NO_YEARS, int_kms = MyActivity.NO_KMS, int_itv = MyActivity.NO_ITV, int_kms_ini = 0, int_fecha_ini = 0;

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
        }

        final TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, int_fecha_ini, int_kms_ini);


        TextView text=(TextView)findViewById(R.id.matricula2);
        text.setText(matricula);

        text=(TextView)findViewById(R.id.marca2);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)menuInfo;


        inflater.inflate(R.menu.modificar_log, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int int_now = funciones.date_a_int(new Date());
        Context contextNew = getApplicationContext();
        final DBLogs manager = new DBLogs(contextNew);
        ListView lv = (ListView) findViewById(R.id.lista_log);
        final Cursor cursor = manager.LogsTodosOrderByFechaString(int_now);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menu_modificarLog:
                modificarLogpulsado(lv, cursor, manager, info.position);
                return true;
            case R.id.menu_eliminarLog:
                borrarLogpulsado(lv, cursor, manager, info.position);
                return true;
            case R.id.menu_realizadoLog:
                realizadoLogpulsado(lv, cursor, manager, info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
