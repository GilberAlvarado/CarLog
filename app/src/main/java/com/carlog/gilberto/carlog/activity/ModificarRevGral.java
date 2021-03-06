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
import android.widget.Toast;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.data.dbRevGral;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.gc.materialdesign.views.ButtonRectangle;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilberto on 16/07/2015.
 */
public class modificarRevGral extends ActionBarActivity {
    private Spinner spinner1;
    private Toolbar toolbar;

    private void RellenarTiposRevGral(dbRevGral managerRevGral, tipoLog miTipo) {
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
        spinner1.setSelection(miTipo.getRevgral(miTipo)-1);
    }

    private void ChangeFRevGral(final String txt_fecha) {
        FloatingActionButton btn_modificarFItv = (FloatingActionButton) findViewById(R.id.button_modif_revgral);
        btn_modificarFItv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(modificarRevGral.this, addItv.class);
            intent.putExtra("fechaITV", funciones.string_a_date(txt_fecha));
            startActivityForResult(intent, PETICION_ACTIVITY_MODIFY_REVGRAL);
            }
        });
    }

    private void ModificarLog(final dbLogs managerLogs, final dbRevGral managerRevGral) {
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
            Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
            Intent intent = new Intent(modificarRevGral.this, listaLogs.class);
            Boolean es_historico = (Boolean) getIntent().getExtras().getSerializable("Historial");
            Boolean ok = true;
            if(es_historico) {
                if (funciones.string_a_long(txtTexto.getText().toString()) > funciones.date_a_long(new Date())) {
                    Toast.makeText(modificarRevGral.this, modificarRevGral.this.getString(R.string.noHistFuturos), Toast.LENGTH_LONG).show();
                    ok = false;
                }
            }
            if(ok) {
                Cursor c_log = managerLogs.buscarLogID(idLog);
                if (c_log.moveToFirst() == true) {
                    long fecha_log = c_log.getLong(c_log.getColumnIndex(managerLogs.CN_FECHA));
                    String txt_fecha_log = funciones.long_a_string(fecha_log);
                    if (txt_fecha_log.equals(txtTexto.getText().toString()))
                        managerLogs.modificarTipoRevGralLog(idLog, int_revgral);
                    else
                        managerLogs.modificarFechaRevGralLog(idLog, int_revgral, funciones.string_a_long(txtTexto.getText().toString()));
                }
                /* NO HACE FALTA RECALCULAR procesar_aceite porque al cambiar el tipo de aceite del futuro cambio no tendrá efecto hasta que se haga esa revisión futura y pase a ser log histórico
                TipoCoche miCoche = (TipoCoche) getIntent().getExtras().getSerializable("miCoche");
                procesarAceite.procesar_aceite(managerLogs, funciones.date_a_int(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche)); // actualizamos fechas
                */
                intent.putExtra("modifyRevGral", true);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revgral);
        Context contextNew = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbRevGral managerRevGral = new dbRevGral(contextNew);
        dbLogs managerLog = new dbLogs(contextNew);
        tipoLog miTipo = (tipoLog)getIntent().getExtras().getSerializable("miTipo");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        RellenarTiposRevGral(managerRevGral, miTipo);
        ChangeFRevGral(txt_fecha);
        ModificarLog(managerLog, managerRevGral);
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
            Intent i = new Intent(modificarRevGral.this, settings.class);
            modificarRevGral.this.startActivity(i);
        }
        if (id == R.id.action_info) {
            Intent i = new Intent(modificarRevGral.this, info.class);
            modificarRevGral.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(modificarRevGral.this);
            login.deleteParamsAnonimo(modificarRevGral.this);
            login.closeFacebookSession(modificarRevGral.this, login.class);
            Intent intent = new Intent(modificarRevGral.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public static final int PETICION_ACTIVITY_MODIFY_REVGRAL = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (PETICION_ACTIVITY_MODIFY_REVGRAL) : {
                if (resultCode == Activity.RESULT_OK) {
                    String itv_string = data.getExtras().getString("revgral_string");
                    TextView text=(TextView)findViewById(R.id.txt_fecha_revgral);
                    text.setText(itv_string);
                }
                break;
            }

        }
    }
}
