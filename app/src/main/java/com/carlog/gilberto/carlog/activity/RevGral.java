package com.carlog.gilberto.carlog.activity;

/**
 * Created by Gilberto on 15/07/2015.
 */

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
import com.carlog.gilberto.carlog.negocio.ProcesarTipos;
import com.carlog.gilberto.carlog.tiposClases.TipoCoche;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilberto on 19/05/2015.
 */
public class RevGral extends Activity {

    public final static String TIPO_5K_KM = "5 mil kms";
    public final static String TIPO_10K_KM = "10 mil kms";
    public final static String TIPO_15K_KM = "15 mil kms";
    public final static String TIPO_20K_KM = "20 mil kms";
    public final static String TIPO_25K_KM = "25 mil kms";
    public final static String TIPO_30K_KM = "30 mil kms";
    public final static String TIPO_35K_KM = "35 mil kms";
    public final static String TIPO_40K_KM = "40 mil kms";
    public final static String TIPO_45K_KM = "45 mil kms";
    public final static String TIPO_50K_KM = "50 mil kms";


    private Spinner spinner1;

    private void RellenarTiposRevGral(DBRevGral managerRevGral) {
        TipoLog miTipo = (TipoLog)getIntent().getExtras().getSerializable("miTipoLog");
        String txt_fecha = miTipo.getFechatxt(miTipo);
        TextView text=(TextView)findViewById(R.id.txt_fecha_revgral);
        text.setText(txt_fecha);

        //spinner1 = (Spinner) findViewById(R.id.cmb_tipos_aceite);
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
    }

    private void GuardarLog(final DBLogs managerLogs, final DBRevGral managerRevGral) {
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
                String datetxt = txtTexto.getText().toString();

                Date fecha = funciones.string_a_date(datetxt);
                long long_fecha = funciones.string_a_long(datetxt);

                TipoCoche miCoche = (TipoCoche)getIntent().getExtras().getSerializable("miCoche");

                System.out.println("SE inserta revgral id : "+int_revgral);
                TipoLog miTipoLog = new TipoLog(TipoLog.TIPO_REV_GENERAL, fecha, datetxt, long_fecha, AddLog.NO_ACEITE, int_revgral, miCoche.getMatricula(miCoche), DBLogs.NO_REALIZADO, miCoche.getKms(miCoche));


                if(miTipoLog.getFechalong(miTipoLog) < funciones.date_a_long(new Date())){ // si se ha creado es porque no existía ningún log ni futuro ni histórico
                    // Creamos el nuevo futuro log
                    // Se pone como REALIZADO!
                    miTipoLog = new TipoLog(TipoLog.TIPO_REV_GENERAL, fecha, datetxt, long_fecha, AddLog.NO_ACEITE, int_revgral, miCoche.getMatricula(miCoche), DBLogs.REALIZADO, miCoche.getKms(miCoche));
                }

                Intent intent = new Intent(RevGral.this, AddLog.class);

                managerLogs.insertar(miTipoLog);
                // Nada más insertar el nuevo log se procesa automáticamente para estimar mejor que el usuario siempre que sea posible
                ProcesarTipos.procesar(managerLogs, funciones.date_a_long(new Date()), getApplicationContext(), miCoche.getKms(miCoche), miCoche.getFechaIni(miCoche), miCoche.getKmsIni(miCoche), miCoche.getMatricula(miCoche), TipoLog.TIPO_REV_GENERAL); // actualizamos fechas

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

        RellenarTiposRevGral(managerRevGral);
        //SeleccionarTipo(managerTiposRevision);
        GuardarLog(managerLog, managerRevGral);
    }
}
