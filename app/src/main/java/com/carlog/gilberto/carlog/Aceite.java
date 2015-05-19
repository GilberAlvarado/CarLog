package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.carlog.gilberto.carlog.data.DBAceite;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.data.DBTiposRevision;

import java.util.ArrayList;

/**
 * Created by Gilberto on 19/05/2015.
 */
public class Aceite extends Activity {

    private Spinner spinner1;

    private void RellenarTiposAceite(DBAceite managerAceite) {
        //spinner1 = (Spinner) findViewById(R.id.cmb_tipos_aceite);
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aceite);

        Context contextNew = getApplicationContext();
        DBAceite managerAceite = new DBAceite(contextNew);

        RellenarTiposAceite(managerAceite);
        //SeleccionarTipo(managerTiposRevision);
        //GuardarLog(managerLogs);
    }
}
