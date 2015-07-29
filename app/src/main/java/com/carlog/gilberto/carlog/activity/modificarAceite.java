package com.carlog.gilberto.carlog.activity;

/**
 * Created by Gilberto on 14/06/2015.
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
import com.carlog.gilberto.carlog.data.DBAceite;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;
import com.carlog.gilberto.carlog.tiposClases.Usuario;
import com.gc.materialdesign.views.ButtonRectangle;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilberto on 26/05/2015.
 */
public class ModificarAceite extends ActionBarActivity {

    private Spinner spinner1;
    private Toolbar toolbar;

    private void RellenarTiposAceite(DBAceite managerAceite, TipoLog miTipo) {
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

        spinner1.setSelection(miTipo.getAceite(miTipo)-1);

    }

    private void ChangeFAceite(final String txt_fecha) {
        FloatingActionButton btn_modificarFItv = (FloatingActionButton) findViewById(R.id.button_modif_aceite);
        btn_modificarFItv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModificarAceite.this, AddItv.class);
                intent.putExtra("fechaITV", funciones.string_a_date(txt_fecha));
                startActivityForResult(intent, PETICION_ACTIVITY_MODIFY_ACEITE);
            }
        });
    }

    private void ModificarLog(final DBLogs managerLogs, final DBAceite managerAceite) {
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

                Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
                Intent intent = new Intent(ModificarAceite.this, ListaLogs.class);
                Cursor c_log = managerLogs.buscarLogID(idLog);
                if (c_log.moveToFirst() == true) {
                    long fecha_log = c_log.getLong(c_log.getColumnIndex(managerLogs.CN_FECHA));
                    String txt_fecha_log = funciones.long_a_string(fecha_log);
                    System.out.println("Modificamos el Log con id " + idLog + " por aceite " + int_aceite);
                    if(txt_fecha_log.equals(datetxt)) managerLogs.modificarTipoAceiteLog(idLog, int_aceite);
                    else managerLogs.modificarFechaAceiteLog(idLog, int_aceite, funciones.string_a_long(datetxt));
                }

/* NO HACE FALTA RECALCULAR procesar_aceite porque al cambiar el tipo de aceite del futuro cambio no tendrá efecto hasta que se haga esa revisión futura y pase a ser log histórico
                TipoCoche miCoche = (TipoCoche) getIntent().getExtras().getSerializable("miCoche");
                procesarAceite.procesar_aceite(managerLogs, funciones.date_a_int(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche)); // actualizamos fechas
*/
                intent.putExtra("modifyAceite", true);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aceite);

        Context contextNew = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DBAceite managerAceite = new DBAceite(contextNew);
        DBLogs managerLog = new DBLogs(contextNew);
        TipoLog miTipo = (TipoLog)getIntent().getExtras().getSerializable("miTipo");
        String txt_fecha = miTipo.getFechatxt(miTipo);

        RellenarTiposAceite(managerAceite, miTipo);
        ChangeFAceite(txt_fecha);
        ModificarLog(managerLog, managerAceite);
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
            u.logout(ModificarAceite.this);
            Login.closeFacebookSession(ModificarAceite.this, Login.class);
            Intent intent = new Intent(ModificarAceite.this, Login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public static final int PETICION_ACTIVITY_MODIFY_ACEITE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCoderequestCode "+ requestCode);
        switch(requestCode) {
            case (PETICION_ACTIVITY_MODIFY_ACEITE) : {
                if (resultCode == Activity.RESULT_OK) {
                    String itv_string = data.getExtras().getString("aceite_string");
                    TextView text=(TextView)findViewById(R.id.txt_fecha_aceite);
                    text.setText(itv_string);
                }
                break;
            }

        }
    }
}

