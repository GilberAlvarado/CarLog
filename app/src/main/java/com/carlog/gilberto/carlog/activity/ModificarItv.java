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
 * Created by Gilberto on 16/07/2015.
 */
public class modificarItv extends ActionBarActivity {
    private Toolbar toolbar;

    private void ModificarLog(final dbLogs managerLogs) {
        //Instanciamos el Boton
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.btn_guardar_itv);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_itv);

                Intent intent = new Intent(modificarItv.this, listaLogs.class);
                Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
                Boolean es_historico = (Boolean) getIntent().getExtras().getSerializable("Historial");

                Boolean ok = true;
                if(es_historico) {
                    if (funciones.string_a_long(txtTexto.getText().toString()) > funciones.date_a_long(new Date())) {
                        Toast.makeText(modificarItv.this, "No puede haber logs históricos con fecha posterior a la de hoy.", Toast.LENGTH_LONG).show();
                        ok = false;
                    }
                }
                if(ok) {
                    managerLogs.ActualizarFModificadaLogFuturo(idLog, funciones.string_a_long(txtTexto.getText().toString()));
                    //Hay que modificar la fecha itv también en el coche
                    dbCar dbc = new dbCar(getApplicationContext());
                    Cursor c = dbc.buscarCocheActivo();
                    if (c.moveToFirst() == true) {
                        String matricula = c.getString(c.getColumnIndex(dbCar.CN_MATRICULA));
                        dbc.ActualizarITVCocheActivo(matricula, funciones.string_a_long(txtTexto.getText().toString()));
                    }
                    /* NO HACE FALTA RECALCULAR procesar_aceite porque al cambiar el tipo de aceite del futuro cambio no tendrá efecto hasta que se haga esa revisión futura y pase a ser log histórico
                    TipoCoche miCoche = (TipoCoche) getIntent().getExtras().getSerializable("miCoche");
                    procesarAceite.procesar_aceite(managerLogs, funciones.date_a_int(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche)); // actualizamos fechas
                    */
                    intent.putExtra("itv_string", txtTexto.getText().toString());
                    intent.putExtra("modifyItv", true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    };


    private void ChangeFItv(final String txt_fecha) {
        FloatingActionButton btn_modificarFItv = (FloatingActionButton) findViewById(R.id.button_modifitv);
        btn_modificarFItv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(modificarItv.this, addItv.class);
            intent.putExtra("fechaITV", funciones.string_a_date(txt_fecha));
            startActivityForResult(intent, PETICION_ACTIVITY_ADDITV);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itv);
        Context contextNew = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbLogs managerLog = new dbLogs(contextNew);
        tipoLog miTipo = (tipoLog)getIntent().getExtras().getSerializable("miTipo");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_itv);
        text.setText(txt_fecha);

        ChangeFItv(txt_fecha);
        ModificarLog(managerLog);
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
            Intent i = new Intent(modificarItv.this, settings.class);
            modificarItv.this.startActivity(i);
        }
        if (id == R.id.action_info) {
            Intent i = new Intent(modificarItv.this, info.class);
            modificarItv.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(modificarItv.this);
            login.deleteParamsAnonimo(modificarItv.this);
            login.closeFacebookSession(modificarItv.this, login.class);
            Intent intent = new Intent(modificarItv.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public static final int PETICION_ACTIVITY_ADDITV = 3;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (PETICION_ACTIVITY_ADDITV) : {
                if (resultCode == Activity.RESULT_OK) {
                    String itv_string = data.getExtras().getString("itv_string");
                    TextView text=(TextView)findViewById(R.id.txt_fecha_itv);
                    text.setText(itv_string);
                }
                break;
            }

        }
    }
}
