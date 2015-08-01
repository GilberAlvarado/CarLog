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
import com.carlog.gilberto.carlog.data.DBBujias;
import com.carlog.gilberto.carlog.data.DBFiltroGasolina;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;
import com.carlog.gilberto.carlog.tiposClases.Usuario;
import com.gc.materialdesign.views.ButtonRectangle;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Gilberto on 30/07/2015.
 */
public class ModificarBujias extends ActionBarActivity{
    private Spinner spinner1;
    private Toolbar toolbar;

    private void RellenarTiposBujias(DBBujias managerBujias, TipoLog miTipo) {
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_bujias);
        text.setText(txt_fecha);
        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos_bujias);

        Cursor cursor = managerBujias.buscarTiposBujias();
        //Recorremos el cursor
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo_bujias = cursor.getString(cursor.getColumnIndex(managerBujias.CN_TIPO));
            tipos.add(tipo_bujias);
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);

        spinner1.setSelection(miTipo.getBujias(miTipo)-1);

    }

    private void ChangeFBujias(final String txt_fecha) {
        FloatingActionButton btn_modificarFBujias = (FloatingActionButton) findViewById(R.id.button_modif_bujias);
        btn_modificarFBujias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModificarBujias.this, AddItv.class);
                intent.putExtra("fechaITV", funciones.string_a_date(txt_fecha));
                startActivityForResult(intent, PETICION_ACTIVITY_MODIFY_BUJIAS);
            }
        });
    }

    private void ModificarLog(final DBLogs managerLogs, final DBBujias managerBujias) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar_bujias);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_bujias);
                String tipo_bujias = spinner.getSelectedItem().toString();

                Cursor c = DBBujias.buscarTiposBujias(tipo_bujias);

                int int_bujias = AddLog.NO_BUJIAS; // solo para inicializar

                if (c.moveToFirst() == true) {
                    int_bujias = c.getInt(c.getColumnIndex(managerBujias.CN_ID));
                }

                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_bujias);

                Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
                Intent intent = new Intent(ModificarBujias.this, ListaLogs.class);
                Cursor c_log = managerLogs.buscarLogID(idLog);
                if (c_log.moveToFirst() == true) {
                    long fecha_log = c_log.getLong(c_log.getColumnIndex(managerLogs.CN_FECHA));
                    String txt_fecha_log = funciones.long_a_string(fecha_log);
                    System.out.println("Modificamos el Log con id " + idLog + " por bujias " + int_bujias);
                    if (txt_fecha_log.equals(txtTexto.getText().toString())) managerLogs.modificarTipoBujiasLog(idLog, int_bujias);
                    else
                        managerLogs.modificarFechaBujiasLog(idLog, int_bujias, funciones.string_a_long(txtTexto.getText().toString()));
                }

                /* NO HACE FALTA RECALCULAR procesar_aceite porque al cambiar el tipo de aceite del futuro cambio no tendrá efecto hasta que se haga esa revisión futura y pase a ser log histórico
                TipoCoche miCoche = (TipoCoche) getIntent().getExtras().getSerializable("miCoche");
                procesarAceite.procesar_aceite(managerLogs, funciones.date_a_int(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche)); // actualizamos fechas
                */

                intent.putExtra("modifyBujias", true);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bujias);
        Context contextNew = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DBBujias managerBujias = new DBBujias(contextNew);
        DBLogs managerLog = new DBLogs(contextNew);
        TipoLog miTipo = (TipoLog)getIntent().getExtras().getSerializable("miTipo");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        RellenarTiposBujias(managerBujias, miTipo);
        ChangeFBujias(txt_fecha);
        ModificarLog(managerLog, managerBujias);
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
        if (id == R.id.action_logout) {
            Usuario u = new Usuario();
            u.logout(ModificarBujias.this);
            Login.closeFacebookSession(ModificarBujias.this, Login.class);
            Intent intent = new Intent(ModificarBujias.this, Login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public static final int PETICION_ACTIVITY_MODIFY_BUJIAS = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCoderequestCode "+ requestCode);
        switch(requestCode) {
            case (PETICION_ACTIVITY_MODIFY_BUJIAS) : {
                if (resultCode == Activity.RESULT_OK) {
                    String itv_string = data.getExtras().getString("bujias_string");
                    TextView text=(TextView)findViewById(R.id.txt_fecha_bujias);
                    text.setText(itv_string);
                }
                break;
            }

        }
    }
}