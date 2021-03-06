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
import com.carlog.gilberto.carlog.data.dbSettings;
import com.carlog.gilberto.carlog.tiposClases.tipoCoche;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.data.dbAceite;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.negocio.procesarTipos;
import com.carlog.gilberto.carlog.tiposClases.tipoSettings;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.gc.materialdesign.views.ButtonRectangle;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilberto on 19/05/2015.
 */
public class aceite extends ActionBarActivity {
    private Toolbar toolbar;
    private Spinner spinner1;

    private void RellenarTiposAceite(dbAceite managerAceite) {
        tipoLog miTipo = (tipoLog)getIntent().getExtras().getSerializable("miTipoLog");
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

    private void GuardarLog(final dbLogs managerLogs, final dbAceite managerAceite) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar_aceite);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_aceite);
            String tipo_aceite = spinner.getSelectedItem().toString();

            Cursor c = dbAceite.buscarTiposAceite(tipo_aceite);
            int int_aceite = addLog.NO_ACEITE; // solo para inicializar

            if (c.moveToFirst() == true) {
                int_aceite = c.getInt(c.getColumnIndex(managerAceite.CN_ID));
            }

            TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_aceite);
            String datetxt = txtTexto.getText().toString();
            Date fecha = funciones.string_a_date(datetxt);
            long long_fecha = funciones.string_a_long(datetxt);
            tipoCoche miCoche = (tipoCoche)getIntent().getExtras().getSerializable("miCoche");

            tipoLog miTipoLog = new tipoLog(aceite.this.getString(R.string.tipoAceite), fecha, datetxt, long_fecha, int_aceite, addLog.NO_VECES_FIL_ACEITE, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, miCoche.getMatricula(miCoche), dbLogs.NO_REALIZADO, dbLogs.NO_FMODIFICADA, miCoche.getKms(miCoche));

            if(miTipoLog.getFechalong(miTipoLog) < funciones.date_a_long(new Date())){ // si se ha creado es porque no existía ningún log ni futuro ni histórico
                // Creamos el nuevo futuro log
                // Se pone como REALIZADO!
                miTipoLog = new tipoLog(aceite.this.getString(R.string.tipoAceite), fecha, datetxt, long_fecha, int_aceite, addLog.NO_VECES_FIL_ACEITE, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, miCoche.getMatricula(miCoche), dbLogs.REALIZADO, dbLogs.NO_FMODIFICADA, miCoche.getKms(miCoche));
            }
            Intent intent = new Intent(aceite.this, addLog.class);
            managerLogs.insertar(miTipoLog);
            // Nada más insertar el nuevo log se procesa automáticamente para estimar mejor que el usuario siempre que sea posible
            dbSettings dbs = new dbSettings(aceite.this.getApplicationContext());
            Cursor c_sett = dbs.getSettings();
            if(c_sett.moveToFirst() == true) {
                if (c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_ACEITE)) == tipoSettings.ACTIVO)
                    procesarTipos.procesar(managerLogs, getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche), miCoche.getMatricula(miCoche), aceite.this.getString(R.string.tipoAceite), miCoche.getYear(miCoche)); // actualizamos fechas
            }
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
        dbAceite managerAceite = new dbAceite(contextNew);
        dbLogs managerLog = new dbLogs(contextNew);
        RellenarTiposAceite(managerAceite);
        GuardarLog(managerLog, managerAceite);
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
            Intent i = new Intent(aceite.this, settings.class);
            aceite.this.startActivity(i);
        }
        if (id == R.id.action_info) {
            Intent i = new Intent(aceite.this, info.class);
            aceite.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(aceite.this);
            login.closeFacebookSession(aceite.this, login.class);
            login.deleteParamsAnonimo(aceite.this);
            Intent intent = new Intent(aceite.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}