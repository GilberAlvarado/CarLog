package com.carlog.gilberto.carlog.activity;

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
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.gc.materialdesign.views.ButtonRectangle;

import java.util.ArrayList;

/**
 * Created by Gilberto on 30/07/2015.
 */
public class modificarFiltroAceite extends ActionBarActivity {
    private Toolbar toolbar;
    private Spinner spinner1;

    private void RellenarTiposFiltroAceite(dbFiltroAceite managerFiltroAceite) {
        tipoLog miTipo = (tipoLog)getIntent().getExtras().getSerializable("miTipo");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_fil_aceite);
        text.setText(txt_fecha);

        System.out.println("miTipo.getVecesFilAceite(miTipo)-1  " + miTipo.getVecesFilAceite(miTipo));

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

        spinner1.setSelection(miTipo.getVecesFilAceite(miTipo)-1);
    }

    private void ModificarLog(final dbLogs managerLogs, final dbFiltroAceite managerFiltroAceite) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar_fil_aceite);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_fil_aceite);
                String tipo_fil_aceite = spinner.getSelectedItem().toString();

                System.out.println("TipoFiltroVeces " + tipo_fil_aceite);
                Cursor c = dbFiltroAceite.buscarTiposFiltroAceite(tipo_fil_aceite);

                int int_veces = addLog.NO_ACEITE; // solo para inicializar

                if (c.moveToFirst() == true) {
                    int_veces = c.getInt(c.getColumnIndex(managerFiltroAceite.CN_VECES));
                }

                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_fil_aceite);
                String datetxt = txtTexto.getText().toString();

                Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
                Intent intent = new Intent(modificarFiltroAceite.this, listaLogs.class);
                System.out.println("Modificamos el Log con id " + idLog + " por filtro aceite " + int_veces);
                managerLogs.modificarFechaFiltroAceite(idLog, int_veces);

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
        ModificarLog(managerLog, managerFiltroAceite);
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
            u.logout(modificarFiltroAceite.this);
            login.deleteParamsAnonimo(modificarFiltroAceite.this);
            login.closeFacebookSession(modificarFiltroAceite.this, login.class);
            Intent intent = new Intent(modificarFiltroAceite.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

}