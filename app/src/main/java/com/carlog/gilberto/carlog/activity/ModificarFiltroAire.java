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
import com.carlog.gilberto.carlog.data.dbFiltroAire;
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
public class modificarFiltroAire extends ActionBarActivity {
    private Spinner spinner1;
    private Toolbar toolbar;

    private void RellenarTiposFiltroAire(dbFiltroAire managerFiltroAire, tipoLog miTipo) {
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_faire);
        text.setText(txt_fecha);
        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos_faire);

        Cursor cursor = managerFiltroAire.buscarTiposFiltroAire();
        //Recorremos el cursor
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo_faire = cursor.getString(cursor.getColumnIndex(managerFiltroAire.CN_TIPO));
            tipos.add(tipo_faire);
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);

        spinner1.setSelection(miTipo.getFaire(miTipo)-1);

    }

    private void ChangeFFiltroAire(final String txt_fecha) {
        FloatingActionButton btn_modificarFFiltroAire = (FloatingActionButton) findViewById(R.id.button_modif_faire);
        btn_modificarFFiltroAire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(modificarFiltroAire.this, addItv.class);
                intent.putExtra("fechaITV", funciones.string_a_date(txt_fecha));
                startActivityForResult(intent, PETICION_ACTIVITY_MODIFY_FAIRE);
            }
        });
    }

    private void ModificarLog(final dbLogs managerLogs, final dbFiltroAire managerFiltroAire) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar_faire);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_faire);
                String tipo_faire = spinner.getSelectedItem().toString();

                Cursor c = dbFiltroAire.buscarFiltroAire(tipo_faire);

                int int_faire = addLog.NO_FAIRE; // solo para inicializar

                if (c.moveToFirst() == true) {
                    int_faire = c.getInt(c.getColumnIndex(managerFiltroAire.CN_ID));
                }

                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_faire);

                Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
                Intent intent = new Intent(modificarFiltroAire.this, listaLogs.class);
                Boolean es_historico = (Boolean) getIntent().getExtras().getSerializable("Historial");

                Boolean ok = true;
                if(es_historico) {
                    if (funciones.string_a_long(txtTexto.getText().toString()) > funciones.date_a_long(new Date())) {
                        Toast.makeText(modificarFiltroAire.this, "No puede haber logs históricos con fecha posterior a la de hoy.", Toast.LENGTH_LONG).show();
                        ok = false;
                    }
                }
                if(ok) {
                    Cursor c_log = managerLogs.buscarLogID(idLog);
                    if (c_log.moveToFirst() == true) {
                        long fecha_log = c_log.getLong(c_log.getColumnIndex(managerLogs.CN_FECHA));
                        String txt_fecha_log = funciones.long_a_string(fecha_log);
                        System.out.println("Modificamos el Log con id " + idLog + " por filtro aire " + int_faire);
                        if (txt_fecha_log.equals(txtTexto.getText().toString()))
                            managerLogs.modificarTipoFiltroAireLog(idLog, int_faire);
                        else
                            managerLogs.modificarFechaFiltroAireLog(idLog, int_faire, funciones.string_a_long(txtTexto.getText().toString()));
                    }

                /* NO HACE FALTA RECALCULAR procesar_aceite porque al cambiar el tipo de aceite del futuro cambio no tendrá efecto hasta que se haga esa revisión futura y pase a ser log histórico
                TipoCoche miCoche = (TipoCoche) getIntent().getExtras().getSerializable("miCoche");
                procesarAceite.procesar_aceite(managerLogs, funciones.date_a_int(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche)); // actualizamos fechas
                */

                    intent.putExtra("modifyFiltroAire", true);
                    setResult(Activity.RESULT_OK, intent);

                    finish();
                }
            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faire);
        Context contextNew = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbFiltroAire managerFiltroAire = new dbFiltroAire(contextNew);
        dbLogs managerLog = new dbLogs(contextNew);
        tipoLog miTipo = (tipoLog)getIntent().getExtras().getSerializable("miTipo");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        RellenarTiposFiltroAire(managerFiltroAire, miTipo);
        ChangeFFiltroAire(txt_fecha);
        ModificarLog(managerLog, managerFiltroAire);
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
            usuario u = new usuario();
            u.logout(modificarFiltroAire.this);
            login.closeFacebookSession(modificarFiltroAire.this, login.class);
            Intent intent = new Intent(modificarFiltroAire.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public static final int PETICION_ACTIVITY_MODIFY_FAIRE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCoderequestCode "+ requestCode);
        switch(requestCode) {
            case (PETICION_ACTIVITY_MODIFY_FAIRE) : {
                if (resultCode == Activity.RESULT_OK) {
                    String itv_string = data.getExtras().getString("faire_string");
                    TextView text=(TextView)findViewById(R.id.txt_fecha_faire);
                    text.setText(itv_string);
                }
                break;
            }

        }
    }
}