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
import android.widget.TextView;
import android.widget.Toast;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.data.dbCar;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.gc.materialdesign.views.ButtonRectangle;
import com.melnykov.fab.FloatingActionButton;

import java.util.Date;

/**
 * Created by Gilberto on 10/08/2015.
 */
public class modificarTaller extends ActionBarActivity {
    private Toolbar toolbar;


    private void ModificarLog(final dbLogs managerLogs) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.btn_guardar_taller);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_taller);

                Intent intent = new Intent(modificarTaller.this, listaLogs.class);
                Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
                Boolean es_historico = (Boolean) getIntent().getExtras().getSerializable("Historial");

                Boolean ok = true;
                if(es_historico) {
                    if (funciones.string_a_long(txtTexto.getText().toString()) > funciones.date_a_long(new Date())) {
                        Toast.makeText(modificarTaller.this, "No puede haber logs históricos con fecha posterior a la de hoy.", Toast.LENGTH_LONG).show();
                        ok = false;
                    }
                }
                if(ok) {
                    managerLogs.ActualizarFModificadaLogFuturo(idLog, funciones.string_a_long(txtTexto.getText().toString()));

                    /* NO HACE FALTA RECALCULAR procesar_aceite porque al cambiar el tipo de aceite del futuro cambio no tendrá efecto hasta que se haga esa revisión futura y pase a ser log histórico
                    TipoCoche miCoche = (TipoCoche) getIntent().getExtras().getSerializable("miCoche");
                    procesarAceite.procesar_aceite(managerLogs, funciones.date_a_int(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche)); // actualizamos fechas
                    */

                    intent.putExtra("itv_string", txtTexto.getText().toString());
                    intent.putExtra("modifyTaller", true);
                    setResult(Activity.RESULT_OK, intent);

                    finish();
                }
            }
        });
    };


    private void ChangeFTaller(final String txt_fecha) {
        FloatingActionButton btn_modificarFChangeFTaller = (FloatingActionButton) findViewById(R.id.button_modiftaller);
        btn_modificarFChangeFTaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(modificarTaller.this, addItv.class);
                intent.putExtra("fechaITV", funciones.string_a_date(txt_fecha));
                startActivityForResult(intent, PETICION_ACTIVITY_ADDITV);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taller);
        Context contextNew = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbLogs managerLog = new dbLogs(contextNew);
        tipoLog miTipo = (tipoLog)getIntent().getExtras().getSerializable("miTipo");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_taller);
        text.setText(txt_fecha);

        ChangeFTaller(txt_fecha);
        ModificarLog(managerLog);
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
        if (id == R.id.action_info) {
            Intent i = new Intent(modificarTaller.this, info.class);
            modificarTaller.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(modificarTaller.this);
            login.deleteParamsAnonimo(modificarTaller.this);
            login.closeFacebookSession(modificarTaller.this, login.class);
            Intent intent = new Intent(modificarTaller.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public static final int PETICION_ACTIVITY_ADDITV = 3;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCoderequestCode "+ requestCode);
        switch(requestCode) {
            case (PETICION_ACTIVITY_ADDITV) : {
                if (resultCode == Activity.RESULT_OK) {
                    String itv_string = data.getExtras().getString("taller_string");
                    TextView text=(TextView)findViewById(R.id.txt_fecha_taller);
                    text.setText(itv_string);
                }
                break;
            }

        }
    }
}
