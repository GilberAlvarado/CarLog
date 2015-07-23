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
import com.carlog.gilberto.carlog.data.DBAceite;
import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DBFiltroAceite;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.negocio.ProcesarTipos;
import com.carlog.gilberto.carlog.tiposClases.TipoCoche;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;
import com.gc.materialdesign.views.ButtonRectangle;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilberto on 22/07/2015.
 */
public class FiltroAceite extends ActionBarActivity {

    public final static String TIPO_1 = "Siempre que cambie el aceite (recomendada)";
    public final static String TIPO_2 = "Cada 2 cambios de aceite";
    public final static String TIPO_3 = "Cada 3 cambios de aceite";

    private Toolbar toolbar;
    private Spinner spinner1;

    private void RellenarTiposFiltroAceite(DBFiltroAceite managerFiltroAceite) {
        TipoLog miTipo = (TipoLog)getIntent().getExtras().getSerializable("miTipoLog");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_fil_aceite);
        text.setText(txt_fecha);

        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos_fil_aceite);

        Cursor cursor = managerFiltroAceite.buscarTiposFiltroAceite();
        //Recorremos el cursor
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo_aceite = cursor.getString(cursor.getColumnIndex(managerFiltroAceite.CN_TIPO));
            tipos.add(tipo_aceite);
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);
    }

    private void GuardarLog(final Context context, final DBLogs managerLogs, final DBFiltroAceite managerFiltroAceite) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar_fil_aceite);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_fil_aceite);
                String tipo_fil_aceite = spinner.getSelectedItem().toString();

                Cursor c = DBFiltroAceite.buscarTiposFiltroAceite(tipo_fil_aceite);

                int int_veces = AddLog.NO_ACEITE; // solo para inicializar

                if (c.moveToFirst() == true) {
                    int_veces = c.getInt(c.getColumnIndex(managerFiltroAceite.CN_VECES));
                }

                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_fil_aceite);
                String datetxt = txtTexto.getText().toString();

                //Date fecha = funciones.string_a_date(datetxt);
                Date fecha = funciones.fecha_mas_dias(new Date(), ProcesarTipos.F_MAX_REV_ACEITE); // da igual la fecha siempre va a poner un año y cuando toque el contador la misma fecha del aceite
                //long long_fecha = funciones.string_a_long(datetxt);
                long long_fecha = funciones.date_a_long(fecha);

                TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");

                TipoLog miTipoLog = new TipoLog(TipoLog.TIPO_FILTRO_ACEITE, fecha, datetxt, long_fecha, AddLog.NO_ACEITE, int_veces, AddLog.NO_CONTADOR_FIL_ACEITE, AddLog.NO_REVGRAL, miCoche.getMatricula(miCoche), DBLogs.NO_REALIZADO, miCoche.getKms(miCoche));


                if(miTipoLog.getFechalong(miTipoLog) < funciones.date_a_long(new Date())){ // si se ha creado es porque no existía ningún log ni futuro ni histórico
                    // Creamos el nuevo futuro log
                    // Se pone como REALIZADO!
                    miTipoLog = new TipoLog(TipoLog.TIPO_FILTRO_ACEITE, fecha, datetxt, long_fecha, AddLog.NO_ACEITE, int_veces, AddLog.NO_CONTADOR_FIL_ACEITE, AddLog.NO_REVGRAL, miCoche.getMatricula(miCoche), DBLogs.REALIZADO, miCoche.getKms(miCoche));
                }

                Intent intent = new Intent(FiltroAceite.this, AddLog.class);

                managerLogs.insertar(miTipoLog);
                // Nada más insertar el nuevo log se procesa automáticamente para estimar mejor que el usuario siempre que sea posible
                ProcesarTipos.procesar(managerLogs, funciones.date_a_long(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche), miCoche.getMatricula(miCoche), TipoLog.TIPO_ACEITE); // actualizamos fechas

                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fil_aceite);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Context contextNew = getApplicationContext();
        DBFiltroAceite managerFiltroAceite = new DBFiltroAceite(contextNew);
        DBLogs managerLog = new DBLogs(contextNew);

        RellenarTiposFiltroAceite(managerFiltroAceite);
        GuardarLog(contextNew, managerLog, managerFiltroAceite);
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
