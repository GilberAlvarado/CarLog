package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.tiposClases.TipoCoche;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;
import com.carlog.gilberto.carlog.data.DBAceite;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.negocio.ProcesarTipos;
import com.gc.materialdesign.views.ButtonRectangle;


import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilberto on 19/05/2015.
 */
public class Aceite extends ActionBarActivity {

    public final static String TIPO_10K_KM = "10 mil kms - 5w30 – 5w40 – 5w50";
    public final static String TIPO_7M_KM = "7 mil kms - 10w40";
    public final static String TIPO_5K_KM = "5 mil kms - 15w40 - 20w50 - 25w60";

    private Toolbar toolbar;
    private Spinner spinner1;

    private void RellenarTiposAceite(DBAceite managerAceite) {
        TipoLog miTipo = (TipoLog)getIntent().getExtras().getSerializable("miTipoLog");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_aceite);
        text.setText(txt_fecha);

        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos_aceite);

        Cursor cursor = managerAceite.buscarTiposAceite();
        //Recorremos el cursor
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo_aceite = cursor.getString(cursor.getColumnIndex(managerAceite.CN_TIPO));
            tipos.add(tipo_aceite);
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);
    }

    private void GuardarLog(final DBLogs managerLogs, final DBAceite managerAceite) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar_aceite);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_aceite);
                String tipo_aceite = spinner.getSelectedItem().toString();

                Cursor c = DBAceite.buscarTiposAceite(tipo_aceite);

                int int_aceite = AddLog.NO_ACEITE; // solo para inicializar

                if (c.moveToFirst() == true) {
                    int_aceite = c.getInt(c.getColumnIndex(managerAceite.CN_ID));
                }

                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_aceite);
                String datetxt = txtTexto.getText().toString();

                Date fecha = funciones.string_a_date(datetxt);
                long long_fecha = funciones.string_a_long(datetxt);

                TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");

                System.out.println("SE inserta aceite id : "+int_aceite);
                TipoLog miTipoLog = new TipoLog(TipoLog.TIPO_ACEITE, fecha, datetxt, long_fecha, int_aceite, AddLog.NO_VECES_FIL_ACEITE, AddLog.NO_CONTADOR_FIL_ACEITE, AddLog.NO_REVGRAL, miCoche.getMatricula(miCoche), DBLogs.NO_REALIZADO, miCoche.getKms(miCoche));


                if(miTipoLog.getFechalong(miTipoLog) < funciones.date_a_long(new Date())){ // si se ha creado es porque no existía ningún log ni futuro ni histórico
                    // Creamos el nuevo futuro log
                    // Se pone como REALIZADO!
                    miTipoLog = new TipoLog(TipoLog.TIPO_ACEITE, fecha, datetxt, long_fecha, int_aceite, AddLog.NO_VECES_FIL_ACEITE, AddLog.NO_CONTADOR_FIL_ACEITE, AddLog.NO_REVGRAL, miCoche.getMatricula(miCoche), DBLogs.REALIZADO, miCoche.getKms(miCoche));
                }

                Intent intent = new Intent(Aceite.this, AddLog.class);

                managerLogs.insertar(miTipoLog);
                // Nada más insertar el nuevo log se procesa automáticamente para estimar mejor que el usuario siempre que sea posible
                ProcesarTipos.procesar(managerLogs, getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche), miCoche.getMatricula(miCoche), TipoLog.TIPO_ACEITE); // actualizamos fechas

                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aceite);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Context contextNew = getApplicationContext();
        DBAceite managerAceite = new DBAceite(contextNew);
        DBLogs managerLog = new DBLogs(contextNew);

        RellenarTiposAceite(managerAceite);
        GuardarLog(managerLog, managerAceite);
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

}
