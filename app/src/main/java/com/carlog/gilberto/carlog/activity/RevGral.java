package com.carlog.gilberto.carlog.activity;

/**
 * Created by Gilberto on 15/07/2015.
 */

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
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.data.dbRevGral;
import com.carlog.gilberto.carlog.data.dbSettings;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.negocio.procesarTipos;
import com.carlog.gilberto.carlog.tiposClases.tipoCoche;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.tiposClases.tipoSettings;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.gc.materialdesign.views.ButtonRectangle;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilberto on 19/05/2015.
 */
public class revGral extends ActionBarActivity {

    public final static String TIPO_30K_KM = "30.000 kms";
    public final static String TIPO_5K_KM = "5.000 kms";
    public final static String TIPO_10K_KM = "10.000 kms";
    public final static String TIPO_15K_KM = "15.000 kms";
    public final static String TIPO_20K_KM = "20.000 kms";
    public final static String TIPO_25K_KM = "25.000 kms";
    public final static String TIPO_35K_KM = "35.000 kms";
    public final static String TIPO_40K_KM = "40.000 kms";
    public final static String TIPO_45K_KM = "45.000 kms";
    public final static String TIPO_50K_KM = "50.000 kms";
    public final static String TIPO_60K_KM = "60.000 kms";
    public final static String TIPO_80K_KM = "80.000 kms";
    public final static String TIPO_100K_KM = "100.000 kms";
    public final static String TIPO_120K_KM = "120.000 kms";

    private Toolbar toolbar;
    private Spinner spinner1;

    private void RellenarTiposRevGral(dbRevGral managerRevGral) {
        tipoLog miTipo = (tipoLog)getIntent().getExtras().getSerializable("miTipoLog");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_revgral);
        text.setText(txt_fecha);

        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos_revgral);
        Cursor cursor = managerRevGral.buscarTiposRevGral();
        //Recorremos el cursor
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo_revgral = cursor.getString(cursor.getColumnIndex(managerRevGral.CN_TIPO));
            tipos.add(tipo_revgral);
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);
    }

    private void GuardarLog(final dbLogs managerLogs, final dbRevGral managerRevGral) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar_revgral);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_revgral);
            String tipo_revgral = spinner.getSelectedItem().toString();
            Cursor c = dbRevGral.buscarTiposRevGral(tipo_revgral);
            int int_revgral = addLog.NO_REVGRAL; // solo para inicializar
            if (c.moveToFirst() == true) {
                int_revgral = c.getInt(c.getColumnIndex(managerRevGral.CN_ID));
            }
            TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_revgral);
            String datetxt = txtTexto.getText().toString();
            Date fecha = funciones.string_a_date(datetxt);
            long long_fecha = funciones.string_a_long(datetxt);
            tipoCoche miCoche = (tipoCoche)getIntent().getExtras().getSerializable("miCoche");
            tipoLog miTipoLog = new tipoLog(revGral.this.getString(R.string.tipoRevGen), fecha, datetxt, long_fecha, addLog.NO_ACEITE, addLog.NO_VECES_FIL_ACEITE, addLog.NO_CONTADOR_FIL_ACEITE, int_revgral, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, miCoche.getMatricula(miCoche), dbLogs.NO_REALIZADO, dbLogs.NO_FMODIFICADA, miCoche.getKms(miCoche));

            if(miTipoLog.getFechalong(miTipoLog) < funciones.date_a_long(new Date())){ // si se ha creado es porque no existía ningún log ni futuro ni histórico
                // Creamos el nuevo futuro log
                // Se pone como REALIZADO!
                miTipoLog = new tipoLog(revGral.this.getString(R.string.tipoRevGen), fecha, datetxt, long_fecha, addLog.NO_ACEITE, addLog.NO_VECES_FIL_ACEITE, addLog.NO_CONTADOR_FIL_ACEITE, int_revgral, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, miCoche.getMatricula(miCoche), dbLogs.REALIZADO, dbLogs.NO_FMODIFICADA, miCoche.getKms(miCoche));
            }
            Intent intent = new Intent(revGral.this, addLog.class);
            managerLogs.insertar(miTipoLog);
            // Nada más insertar el nuevo log se procesa automáticamente para estimar mejor que el usuario siempre que sea posible
            dbSettings dbs = new dbSettings(revGral.this.getApplicationContext());
            Cursor c_sett = dbs.getSettings();
            if(c_sett.moveToFirst() == true) {
                if (c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_REVGEN)) == tipoSettings.ACTIVO)
                    procesarTipos.procesar(managerLogs, getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche), miCoche.getMatricula(miCoche), revGral.this.getString(R.string.tipoRevGen), miCoche.getKms(miCoche)); // actualizamos fechas
            }
            setResult(Activity.RESULT_OK, intent);
            finish();
            }
        });
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revgral);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Context contextNew = getApplicationContext();
        dbRevGral managerRevGral = new dbRevGral(contextNew);
        dbLogs managerLog = new dbLogs(contextNew);

        RellenarTiposRevGral(managerRevGral);
        //SeleccionarTipo(managerTiposRevision);
        GuardarLog(managerLog, managerRevGral);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(revGral.this, settings.class);
            revGral.this.startActivity(i);
        }
        if (id == R.id.action_info) {
            Intent i = new Intent(revGral.this, info.class);
            revGral.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(revGral.this);
            login.deleteParamsAnonimo(revGral.this);
            login.closeFacebookSession(revGral.this, login.class);
            Intent intent = new Intent(revGral.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

