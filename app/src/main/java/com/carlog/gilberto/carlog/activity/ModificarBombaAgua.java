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
import com.carlog.gilberto.carlog.data.DBBombaAgua;
import com.carlog.gilberto.carlog.data.DBCorrea;
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
public class ModificarBombaAgua extends ActionBarActivity {
    private Spinner spinner1;
    private Toolbar toolbar;

    private void RellenarTiposBombaAgua(DBBombaAgua managerBombaAgua, TipoLog miTipo) {
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_bombaagua);
        text.setText(txt_fecha);
        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos_bombaagua);

        Cursor cursor = managerBombaAgua.buscarTiposBombaAgua();
        //Recorremos el cursor
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo_bombaagua = cursor.getString(cursor.getColumnIndex(managerBombaAgua.CN_TIPO));
            tipos.add(tipo_bombaagua);
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);

        spinner1.setSelection(miTipo.getBombaagua(miTipo)-1);

    }

    private void ChangeFBombaAgua(final String txt_fecha) {
        FloatingActionButton btn_modificarFbombaagua = (FloatingActionButton) findViewById(R.id.button_modif_bombaagua);
        btn_modificarFbombaagua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModificarBombaAgua.this, AddItv.class);
                intent.putExtra("fechaITV", funciones.string_a_date(txt_fecha));
                startActivityForResult(intent, PETICION_ACTIVITY_MODIFY_BOMBAAGUA);
            }
        });
    }

    private void ModificarLog(final DBLogs managerLogs, final DBBombaAgua managerBombaAgua) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar_bombaagua);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_bombaagua);
                String tipo_bombaagua = spinner.getSelectedItem().toString();

                Cursor c = DBBombaAgua.buscarTiposBombaAgua(tipo_bombaagua);

                int int_bombaagua = AddLog.NO_BOMBAAGUA; // solo para inicializar

                if (c.moveToFirst() == true) {
                    int_bombaagua = c.getInt(c.getColumnIndex(managerBombaAgua.CN_ID));
                }

                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_bombaagua);

                Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
                Intent intent = new Intent(ModificarBombaAgua.this, ListaLogs.class);
                Cursor c_log = managerLogs.buscarLogID(idLog);
                if (c_log.moveToFirst() == true) {
                    long fecha_log = c_log.getLong(c_log.getColumnIndex(managerLogs.CN_FECHA));
                    String txt_fecha_log = funciones.long_a_string(fecha_log);
                    System.out.println("Modificamos el Log con id " + idLog + " por bomba agua " + int_bombaagua);
                    if (txt_fecha_log.equals(txtTexto.getText().toString())) managerLogs.modificarTipoBombaAguaLog(idLog, int_bombaagua);
                    else
                        managerLogs.modificarFechaBombaAguaLog(idLog, int_bombaagua, funciones.string_a_long(txtTexto.getText().toString()));
                }

                /* NO HACE FALTA RECALCULAR procesar_aceite porque al cambiar el tipo de aceite del futuro cambio no tendrá efecto hasta que se haga esa revisión futura y pase a ser log histórico
                TipoCoche miCoche = (TipoCoche) getIntent().getExtras().getSerializable("miCoche");
                procesarAceite.procesar_aceite(managerLogs, funciones.date_a_int(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche)); // actualizamos fechas
                */

                intent.putExtra("modifyBombaAgua", true);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bombaagua);
        Context contextNew = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DBBombaAgua managerBombaAgua = new DBBombaAgua(contextNew);
        DBLogs managerLog = new DBLogs(contextNew);
        TipoLog miTipo = (TipoLog)getIntent().getExtras().getSerializable("miTipo");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        RellenarTiposBombaAgua(managerBombaAgua, miTipo);
        ChangeFBombaAgua(txt_fecha);
        ModificarLog(managerLog, managerBombaAgua);
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
            u.logout(ModificarBombaAgua.this);
            Login.closeFacebookSession(ModificarBombaAgua.this, Login.class);
            Intent intent = new Intent(ModificarBombaAgua.this, Login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public static final int PETICION_ACTIVITY_MODIFY_BOMBAAGUA = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCoderequestCode "+ requestCode);
        switch(requestCode) {
            case (PETICION_ACTIVITY_MODIFY_BOMBAAGUA) : {
                if (resultCode == Activity.RESULT_OK) {
                    String itv_string = data.getExtras().getString("bombaagua_string");
                    TextView text=(TextView)findViewById(R.id.txt_fecha_bombaagua);
                    text.setText(itv_string);
                }
                break;
            }

        }
    }
}
