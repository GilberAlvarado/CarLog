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
import com.carlog.gilberto.carlog.data.dbFiltroAceite;
import com.carlog.gilberto.carlog.data.dbLogs;
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
 * Created by Gilberto on 22/07/2015.
 */
public class filtroAceite extends ActionBarActivity {

    public final static String TIPO_1 = "Siempre que cambie el aceite (recomendada)";
    public final static String TIPO_2 = "Cada 2 cambios de aceite";
    public final static String TIPO_3 = "Cada 3 cambios de aceite";

    private Toolbar toolbar;
    private Spinner spinner1;

    private void RellenarTiposFiltroAceite(dbFiltroAceite managerFiltroAceite) {
        tipoLog miTipo = (tipoLog)getIntent().getExtras().getSerializable("miTipoLog");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_fil_aceite);
        text.setText(txt_fecha);

        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos_fil_aceite);

        Cursor cursor = managerFiltroAceite.buscarTiposFiltroAceite();
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo_aceite = cursor.getString(cursor.getColumnIndex(managerFiltroAceite.CN_TIPO));
            tipos.add(tipo_aceite);
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);
    }

    private void GuardarLog(final Context context, final dbLogs managerLogs, final dbFiltroAceite managerFiltroAceite) {
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar_fil_aceite);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_fil_aceite);
                String tipo_fil_aceite = spinner.getSelectedItem().toString();

                Cursor c = dbFiltroAceite.buscarTiposFiltroAceite(tipo_fil_aceite);
                int int_veces = addLog.NO_VECES_FIL_ACEITE; // solo para inicializar

                if (c.moveToFirst() == true) {
                    int_veces = c.getInt(c.getColumnIndex(managerFiltroAceite.CN_VECES));
                }
                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_fil_aceite);
                String datetxt = txtTexto.getText().toString();

                Date fecha = funciones.fecha_mas_dias(new Date(), procesarTipos.F_MAX_REV_ACEITE); // da igual la fecha siempre va a poner un año y cuando toque el contador la misma fecha del aceite
                long long_fecha = funciones.date_a_long(fecha);
                tipoCoche miCoche = (tipoCoche)getIntent().getExtras().getSerializable("miCoche");
                tipoLog miTipoLog = new tipoLog(tipoLog.TIPO_FILTRO_ACEITE, fecha, datetxt, long_fecha, addLog.NO_ACEITE, int_veces, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, miCoche.getMatricula(miCoche), dbLogs.NO_REALIZADO, dbLogs.NO_FMODIFICADA, miCoche.getKms(miCoche));

                if(miTipoLog.getFechalong(miTipoLog) < funciones.date_a_long(new Date())){ // si se ha creado es porque no existía ningún log ni futuro ni histórico
                    // Creamos el nuevo futuro log
                    // Se pone como REALIZADO!
                    miTipoLog = new tipoLog(tipoLog.TIPO_FILTRO_ACEITE, fecha, datetxt, long_fecha, addLog.NO_ACEITE, int_veces, addLog.NO_CONTADOR_FIL_ACEITE, addLog.NO_REVGRAL, addLog.NO_CORREA, addLog.NO_BOMBAAGUA, addLog.NO_FGASOLINA, addLog.NO_FAIRE, addLog.NO_BUJIAS, addLog.NO_EMBRAGUE, miCoche.getMatricula(miCoche), dbLogs.REALIZADO, dbLogs.NO_FMODIFICADA, miCoche.getKms(miCoche));
                }

                Intent intent = new Intent(filtroAceite.this, addLog.class);
                managerLogs.insertar(miTipoLog);
                // Nada más insertar el nuevo log se procesa automáticamente para estimar mejor que el usuario siempre que sea posible
                dbSettings dbs = new dbSettings(context);
                Cursor c_sett = dbs.getSettings();
                if(c_sett.moveToFirst() == true) {
                    if (c_sett.getInt(c_sett.getColumnIndex(dbSettings.CN_FILACEITE)) == tipoSettings.ACTIVO)
                        procesarTipos.procesar(managerLogs, getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche), miCoche.getMatricula(miCoche), tipoLog.TIPO_ACEITE, miCoche.getYear(miCoche)); // actualizamos fechas
                }
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
        dbFiltroAceite managerFiltroAceite = new dbFiltroAceite(contextNew);
        dbLogs managerLog = new dbLogs(contextNew);

        RellenarTiposFiltroAceite(managerFiltroAceite);
        GuardarLog(contextNew, managerLog, managerFiltroAceite);
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
            Intent i = new Intent(filtroAceite.this, settings.class);
            filtroAceite.this.startActivity(i);
        }
        if (id == R.id.action_info) {
            Intent i = new Intent(filtroAceite.this, info.class);
            filtroAceite.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(filtroAceite.this);
            login.deleteParamsAnonimo(filtroAceite.this);
            login.closeFacebookSession(filtroAceite.this, login.class);
            Intent intent = new Intent(filtroAceite.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
