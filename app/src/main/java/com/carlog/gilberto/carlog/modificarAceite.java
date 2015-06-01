package com.carlog.gilberto.carlog;

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

import com.carlog.gilberto.carlog.data.DBAceite;
import com.carlog.gilberto.carlog.data.DBLogs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilberto on 26/05/2015.
 */
public class modificarAceite extends Activity {

    private Spinner spinner1;

    private void RellenarTiposAceite(DBAceite managerAceite, TipoLog miTipo) {

        String txt_fecha = miTipo.getFechatxt(miTipo);

        TextView text=(TextView)findViewById(R.id.txt_fecha_aceite_modificar);
        text.setText(txt_fecha);

        //spinner1 = (Spinner) findViewById(R.id.cmb_tipos_aceite);
        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos_aceite_modificar);

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

    private void ModificarLog(final DBLogs managerLogs, final DBAceite managerAceite) {
        //Instanciamos el Boton
        Button btn1 = (Button) findViewById(R.id.btn_modificar_aceite);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos_aceite_modificar);
                String tipo_aceite = spinner.getSelectedItem().toString();

                Cursor c = DBAceite.buscarTiposAceite(tipo_aceite);

                int int_aceite = AddLog.NO_ACEITE; // solo para inicializar

                if (c.moveToFirst() == true) {
                    int_aceite = c.getInt(c.getColumnIndex(managerAceite.CN_ID));
                }

                TextView txtTexto = (TextView)findViewById(R.id.txt_fecha_aceite_modificar);
                String datetxt = txtTexto.getText().toString();

                Date fecha = funciones.string_a_date(datetxt);
                int int_fecha = funciones.string_a_int(datetxt);


                Integer idLog = (Integer) getIntent().getExtras().getSerializable("idLog");
                Intent intent = new Intent(modificarAceite.this, DatosIniciales.class);

                System.out.println("Modificamos el Log con id " + idLog + " por aceite " + int_aceite);
                managerLogs.modificarTipoAceiteLog(idLog, int_aceite);

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
        setContentView(R.layout.activity_modificar_aceite);

        Context contextNew = getApplicationContext();
        DBAceite managerAceite = new DBAceite(contextNew);
        DBLogs managerLog = new DBLogs(contextNew);
        TipoLog miTipo = (TipoLog)getIntent().getExtras().getSerializable("miTipo");

        RellenarTiposAceite(managerAceite, miTipo);
        //SeleccionarTipo(managerTiposRevision);
        ModificarLog(managerLog, managerAceite);
    }
}
