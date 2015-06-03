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
import android.widget.TextView;

import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.gc.materialdesign.views.ButtonFloat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
                                                String matricula = cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR));
                                                manager.eliminar_por_id(id);
                                                ConsultarLogs(matricula);

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
        final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");

        //Recorremos el cursor
        int i = 0;
        String tipo = "";
        Intent intent = new Intent();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (i == posicion) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));
                tipo = cursor.getString(cursor.getColumnIndex(DBLogs.CN_TIPO));
                if(tipo.equals(TipoLog.TIPO_ACEITE)) {
                    intent = new Intent(DatosIniciales.this, modificarAceite.class);
                }
                ///////////ELSE PARA LOS DEMAS TIPOS QUE SE PUEDAN MODIFICAR CREAR ACTIVITIES
                //************************************************************************


                String txt_fecha = cursor.getString(cursor.getColumnIndex("fecha_string"));
                int aceite = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ACEITE));
                String matricula = cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR));
                int kms = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_KMS));


                TipoLog miTipo = new TipoLog(tipo, funciones.string_a_date(txt_fecha), txt_fecha, funciones.string_a_int(txt_fecha), aceite, matricula, DBLogs.NO_REALIZADO, kms);
                intent.putExtra("miTipo", miTipo);
                intent.putExtra("miCoche", miCoche);
                intent.putExtra("idLog", id);
                break;
            }
            i++;
        }

        if(tipo.equals(TipoLog.TIPO_ACEITE)) {
            startActivity(intent);
        }
        ///////////ELSE PARA LOS DEMAS TIPOS QUE SE PUEDAN MODIFICAR CREAR ACTIVITIES
        //************************************************************************


    }

    private void realizadoLogpulsado(ListView lv, Cursor cursor, DBLogs manager, int posicion) {
        final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");

        //Recorremos el cursor
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

            if (i == posicion) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ID));
                String matricula = cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR));

                Date now = new Date();
                System.out.println("fecha "+now);
                System.out.println("id "+id);
                System.out.println("int fecha "+funciones.date_a_int(now));

                manager.marcarRealizadoLog(id, funciones.date_a_int(now), miCoche.getKms(miCoche)); //hoy

                ConsultarLogs(matricula);

                break;
            }
            i++;
        }

    }



    private void ConsultarLogs(String matricula) {
        System.out.println("CONSULTANDO LOGS "+ matricula);
        Context contextNew = getApplicationContext();
        final DBLogs manager = new DBLogs(contextNew);
       // String[] from = new String[]{manager.CN_TIPO, "fecha_string"};
       // int[] to = new int[] {android.R.id.text1, android.R.id.text2};


        int int_now = funciones.date_a_int(new Date());

        final Cursor cursor = manager.LogsTodosOrderByFechaString(int_now, matricula);



        List<TipoLog> listaLogs = new ArrayList<TipoLog>();
        //Recorremos el cursor
        int k = 0;
        cursor.moveToFirst();
        //while (cursor.moveToNext()) {
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            System.out.println("KKKK "+k);
            if(cursor.getString(cursor.getColumnIndex(DBLogs.CN_TIPO)).equals(TipoLog.TIPO_ACEITE)) {
                System.out.println("en consultarlogs date " + funciones.string_a_date(cursor.getString(cursor.getColumnIndex("fecha_string"))));
                System.out.println("en consultarlogs string " + cursor.getString(cursor.getColumnIndex("fecha_string")));
                System.out.println("en consultarlogs int " + funciones.string_a_int(cursor.getString(cursor.getColumnIndex("fecha_string"))));
            }
            TipoLog miTipoLog = new TipoLog(cursor.getString(cursor.getColumnIndex(DBLogs.CN_TIPO)),funciones.string_a_date(cursor.getString(cursor.getColumnIndex("fecha_string"))), cursor.getString(cursor.getColumnIndex("fecha_string")),
                    funciones.string_a_int(cursor.getString(cursor.getColumnIndex("fecha_string"))), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_ACEITE)), cursor.getString(cursor.getColumnIndex(DBLogs.CN_CAR)),
                    cursor.getInt(cursor.getColumnIndex(DBLogs.CN_REALIZADO)), cursor.getInt(cursor.getColumnIndex(DBLogs.CN_KMS)));
            listaLogs.add(miTipoLog);

            k++;
        }




       // final SimpleCursorAdapter adaptador = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, from, to);
        //Asociamos el adaptador a la vista.
        ListView lv = (ListView) findViewById(R.id.lista_log);


        miAdaptadorLog adapter = new miAdaptadorLog(this, listaLogs);


        lv.setAdapter(adapter);
       // lv.setAdapter(adaptador);




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
        Cursor c = dbcar.buscarCocheActivo();

        String matricula = "", marca = "", modelo = "", year = "", kms = "", itv = "";
        int int_year = MyActivity.NO_YEARS, int_kms = MyActivity.NO_KMS, int_itv = MyActivity.NO_ITV, int_kms_ini = 0, int_fecha_ini = 0;


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

        final TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);


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

        ConsultarLogs(matricula);

        //Instanciamos el Boton
        ButtonFloat btn1 = (ButtonFloat) findViewById(R.id.add_log);

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



        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        TextView text=(TextView)findViewById(R.id.matricula2);


        final Cursor cursor = manager.LogsTodosOrderByFechaString(int_now, text.getText().toString());

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
                    final TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");
                    System.out.println("RESULTADO ACTIVITY "+miCoche.getMatricula(miCoche));
                    ConsultarLogs(miCoche.getMatricula(miCoche));


                }
                break;
            }
        }
    }



}
