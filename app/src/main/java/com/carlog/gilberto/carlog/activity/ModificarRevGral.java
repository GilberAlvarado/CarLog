package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.data.DBAceite;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.data.DBRevGral;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilberto on 16/07/2015.
 */
public class ModificarRevGral extends Activity {
    private Spinner spinner1;

    private void RellenarTiposRevGral(DBRevGral managerRevGral, TipoLog miTipo) {
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

    private void ModificarLog(final DBLogs managerLogs, final DBRevGral managerRevGral) {
        //Instanciamos el Boton
        Button btn1 = (Button) findViewById(R.id.guardar_revgral);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_revgral);
                String tipo_revgral = spinner.getSelectedItem().toString();

                Cursor c = DBRevGral.buscarTiposRevGral(tipo_revgral);

                int int_revgral = AddLog.NO_REVGRAL; // solo para inicializar

                if (c.moveToFirst() == true) {
                    int_revgral = c.getInt(c.getColumnIndex(managerRevGral.CN_ID));
                }

                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_revgral);

                Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
                Intent intent = new Intent(ModificarRevGral.this, ListaLogs.class);

                System.out.println("Modificamos el Log con id " + idLog + " por revgral " + int_revgral);
                managerLogs.modificarTipoRevGralLog(idLog, int_revgral);

                /* NO HACE FALTA RECALCULAR procesar_aceite porque al cambiar el tipo de aceite del futuro cambio no tendrá efecto hasta que se haga esa revisión futura y pase a ser log histórico
                TipoCoche miCoche = (TipoCoche) getIntent().getExtras().getSerializable("miCoche");
                procesarAceite.procesar_aceite(managerLogs, funciones.date_a_int(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche)); // actualizamos fechas
                */

                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revgral);
        Context contextNew = getApplicationContext();
        DBRevGral managerRevGral = new DBRevGral(contextNew);
        DBLogs managerLog = new DBLogs(contextNew);
        TipoLog miTipo = (TipoLog)getIntent().getExtras().getSerializable("miTipo");
        RellenarTiposRevGral(managerRevGral, miTipo);
        ModificarLog(managerLog, managerRevGral);
    }
}
