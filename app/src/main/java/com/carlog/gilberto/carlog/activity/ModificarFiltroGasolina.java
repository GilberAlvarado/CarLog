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
import com.carlog.gilberto.carlog.data.dbFiltroGasolina;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.gc.materialdesign.views.ButtonRectangle;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilberto on 30/07/2015.
 */
public class modificarFiltroGasolina extends ActionBarActivity {
    private Spinner spinner1;
    private Toolbar toolbar;

    private void RellenarTiposFiltroGasolina(dbFiltroGasolina managerFiltroGasolina, tipoLog miTipo) {
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_fgasolina);
        text.setText(txt_fecha);
        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos_fgasolina);
        Cursor cursor = managerFiltroGasolina.buscarTiposFiltroGasolina();
        //Recorremos el cursor
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo_fgasolina = cursor.getString(cursor.getColumnIndex(managerFiltroGasolina.CN_TIPO));
            tipos.add(tipo_fgasolina);
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);
        spinner1.setSelection(miTipo.getFgasolina(miTipo)-1);
    }

    private void ChangeFFiltroGasolina(final String txt_fecha) {
        FloatingActionButton btn_modificarFFiltroGasolina = (FloatingActionButton) findViewById(R.id.button_modif_fgasolina);
        btn_modificarFFiltroGasolina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(modificarFiltroGasolina.this, addItv.class);
            intent.putExtra("fechaITV", funciones.string_a_date(txt_fecha));
            startActivityForResult(intent, PETICION_ACTIVITY_MODIFY_FGASOLINA);
            }
        });
    }

    private void ModificarLog(final dbLogs managerLogs, final dbFiltroGasolina managerFiltroGasolina) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar_fgasolina);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_fgasolina);
            String tipo_fgasolina = spinner.getSelectedItem().toString();
            Cursor c = dbFiltroGasolina.buscarFiltroGasolina(tipo_fgasolina);
            int int_fgasolina = addLog.NO_FGASOLINA; // solo para inicializar
            if (c.moveToFirst() == true) {
                int_fgasolina = c.getInt(c.getColumnIndex(managerFiltroGasolina.CN_ID));
            }
            TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_fgasolina);
            Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
            Intent intent = new Intent(modificarFiltroGasolina.this, listaLogs.class);
            Boolean es_historico = (Boolean) getIntent().getExtras().getSerializable("Historial");
            Boolean ok = true;
            if(es_historico) {
                if (funciones.string_a_long(txtTexto.getText().toString()) > funciones.date_a_long(new Date())) {
                    Toast.makeText(modificarFiltroGasolina.this, modificarFiltroGasolina.this.getString(R.string.noHistFuturos), Toast.LENGTH_LONG).show();
                    ok = false;
                }
            }
            if(ok) {
                Cursor c_log = managerLogs.buscarLogID(idLog);
                if (c_log.moveToFirst() == true) {
                    long fecha_log = c_log.getLong(c_log.getColumnIndex(managerLogs.CN_FECHA));
                    String txt_fecha_log = funciones.long_a_string(fecha_log);
                    if (txt_fecha_log.equals(txtTexto.getText().toString()))
                        managerLogs.modificarTipoFiltroAireLog(idLog, int_fgasolina);
                    else
                        managerLogs.modificarFechaFiltroGasolinaLog(idLog, int_fgasolina, funciones.string_a_long(txtTexto.getText().toString()));
                }
                /* NO HACE FALTA RECALCULAR procesar_aceite porque al cambiar el tipo de aceite del futuro cambio no tendrá efecto hasta que se haga esa revisión futura y pase a ser log histórico
                TipoCoche miCoche = (TipoCoche) getIntent().getExtras().getSerializable("miCoche");
                procesarAceite.procesar_aceite(managerLogs, funciones.date_a_int(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche)); // actualizamos fechas
                */
                intent.putExtra("modifyFiltroGasolina", true);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fgasolina);
        Context contextNew = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbFiltroGasolina managerFiltroGasolina = new dbFiltroGasolina(contextNew);
        dbLogs managerLog = new dbLogs(contextNew);
        tipoLog miTipo = (tipoLog)getIntent().getExtras().getSerializable("miTipo");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        RellenarTiposFiltroGasolina(managerFiltroGasolina, miTipo);
        ChangeFFiltroGasolina(txt_fecha);
        ModificarLog(managerLog, managerFiltroGasolina);
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
            Intent i = new Intent(modificarFiltroGasolina.this, settings.class);
            modificarFiltroGasolina.this.startActivity(i);
        }
        if (id == R.id.action_info) {
            Intent i = new Intent(modificarFiltroGasolina.this, info.class);
            modificarFiltroGasolina.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(modificarFiltroGasolina.this);
            login.deleteParamsAnonimo(modificarFiltroGasolina.this);
            login.closeFacebookSession(modificarFiltroGasolina.this, login.class);
            Intent intent = new Intent(modificarFiltroGasolina.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public static final int PETICION_ACTIVITY_MODIFY_FGASOLINA = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (PETICION_ACTIVITY_MODIFY_FGASOLINA) : {
                if (resultCode == Activity.RESULT_OK) {
                    String itv_string = data.getExtras().getString("fgasolina_string");
                    TextView text=(TextView)findViewById(R.id.txt_fecha_fgasolina);
                    text.setText(itv_string);
                }
                break;
            }
        }
    }
}
