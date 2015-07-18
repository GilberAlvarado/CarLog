package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.tiposClases.TipoCoche;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.data.DBTiposRevision;
import com.carlog.gilberto.carlog.formats.funciones;

import java.util.ArrayList;
import java.util.Date;


public class AddLog extends Activity {

    public final static int NO_ACEITE = -1;
    public final static int NO_REVGRAL = -1;
    private Spinner spinner1;

    private void RellenarTipos(DBTiposRevision managerTiposRevision) {
        spinner1 = (Spinner) findViewById(R.id.cmb_tipos);
        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos);

        Cursor cursor = managerTiposRevision.cargarCursorTiposRevision();
        //Recorremos el cursor
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo = cursor.getString(cursor.getColumnIndex(managerTiposRevision.CN_TIPO));
            tipos.add(tipo);
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);
    }

    private void SeleccionarTipo(final DBTiposRevision managerTiposRevision) {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Toast.makeText(arg0.getContext(), "Seleccionado: " + arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        final EditText edttxt = (EditText) findViewById (R.id.txt_nuevoTipo);
        final TextView tv_nt = (TextView) findViewById(R.id.nuevoTipo);
        Button btm_agregar = (Button) findViewById (R.id.btm_addTipo);
        btm_agregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                edttxt.setVisibility(View.VISIBLE);
                tv_nt.setVisibility(View.VISIBLE);

                String nuevoDato = edttxt.getText().toString();
                nuevoDato=nuevoDato.trim();
                if (nuevoDato.length() > 1) { // si ha escrito algo
                    //primero miramos si existe para no insertar duplicados
                    Cursor c = managerTiposRevision.buscarTipo(nuevoDato);
                    if (c.moveToFirst() == false) {
                        managerTiposRevision.insertar(nuevoDato, "ic_coche");
                        RellenarTipos(managerTiposRevision);
                        edttxt.setVisibility(View.INVISIBLE);
                        tv_nt.setVisibility(View.INVISIBLE);
                        edttxt.setText("");
                    }
                    else Toast.makeText(getApplicationContext(), "El tipo ya existe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btm_eliminar = (Button) findViewById (R.id.btm_elimTipo);
        btm_eliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos);
                String elimdata = spinner.getSelectedItem().toString();
                managerTiposRevision.eliminar(elimdata);
                RellenarTipos(managerTiposRevision);
            }
        });
    }


    private void addlog(TipoLog miTipoLog, DBLogs managerLogs) {

        Intent intent;
        DBCar dbc = new DBCar(getApplicationContext());
        Cursor c_activo = dbc.buscarCocheActivo();
        String matricula = "", marca = "", modelo = "";
        int int_year = 0, int_kms = 0, int_kms_ini = 0, int_itv = 0, int_fecha_ini = 0;
        if (c_activo.moveToFirst() == true) {
            matricula = c_activo.getString(c_activo.getColumnIndex(DBCar.CN_MATRICULA));
            marca = c_activo.getString(c_activo.getColumnIndex(DBCar.CN_MARCA));
            modelo = c_activo.getString(c_activo.getColumnIndex(DBCar.CN_MODELO));
            int_year = c_activo.getInt(c_activo.getColumnIndex(DBCar.CN_YEAR));
            int_kms = c_activo.getInt(c_activo.getColumnIndex(DBCar.CN_KMS));
            int_itv = c_activo.getInt(c_activo.getColumnIndex(DBCar.CN_ITV));
            int_kms_ini = c_activo.getInt(c_activo.getColumnIndex(DBCar.CN_KMS_INI));
            int_fecha_ini = c_activo.getInt(c_activo.getColumnIndex(DBCar.CN_FECHA_INI));
        }
        TipoCoche miCoche = new TipoCoche(matricula, marca, modelo, int_year, int_kms, int_itv, TipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);
        int int_now = funciones.date_a_int(new Date());
        int ahora = funciones.date_a_int(new Date());

        if (miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_ACEITE)) {
            // Antes de hacer nada miramos si ya existe algun tipo de aceite pues no debemos tener más de uno
            Cursor c = managerLogs.buscarTipo(TipoLog.TIPO_ACEITE, miCoche.getMatricula(miCoche));
            if (c.moveToFirst() == false) { // Si no hay logs (ni futuros ni históricos)
                if(miTipoLog.getFechaint(miTipoLog) >= ahora) {
                    Toast.makeText(getApplicationContext(), "Se recomienda insertar la última revisión de " + TipoLog.TIPO_ACEITE + " hecha.", Toast.LENGTH_SHORT).show();
                }
                intent = new Intent(AddLog.this, Aceite.class);
                intent.putExtra("miTipoLog", miTipoLog);
                intent.putExtra("miCoche", miCoche);
                startActivity(intent);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else Toast.makeText(getApplicationContext(), "Ya tiene pendiente un " + TipoLog.TIPO_ACEITE, Toast.LENGTH_SHORT).show();

        }
        else if (miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_REV_GENERAL)) {
            // Antes de hacer nada miramos si ya existe algun tipo de revgral pues no debemos tener más de una
            Cursor c = managerLogs.buscarTipo(TipoLog.TIPO_REV_GENERAL, miCoche.getMatricula(miCoche));
            if (c.moveToFirst() == false) { // Si no hay logs (ni futuros ni históricos)
                if(miTipoLog.getFechaint(miTipoLog) >= ahora) {
                    Toast.makeText(getApplicationContext(), "Se recomienda insertar la última revisión de " + TipoLog.TIPO_REV_GENERAL + " hecha.", Toast.LENGTH_SHORT).show();
                }
                intent = new Intent(AddLog.this, RevGral.class);
                intent.putExtra("miTipoLog", miTipoLog);
                intent.putExtra("miCoche", miCoche);
                startActivity(intent);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else Toast.makeText(getApplicationContext(), "Ya tiene pendiente un " + TipoLog.TIPO_REV_GENERAL, Toast.LENGTH_SHORT).show();

        }else {
            intent = new Intent(AddLog.this, ListaLogs.class);
            managerLogs.insertar(miTipoLog);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private void GuardarLog(final DBLogs managerLogs) {
        //Instanciamos el Boton
        Button btn1 = (Button) findViewById(R.id.guardar);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos);
                String tipo = spinner.getSelectedItem().toString();
                DatePicker datePicker = (DatePicker) findViewById(R.id.date_newlog);
                String txt_date_newlog = "";

                if ((datePicker.getDayOfMonth() < 10) && ((datePicker.getMonth() + 1) < 10)) {
                    txt_date_newlog = "0" + datePicker.getDayOfMonth() + "-0" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                } else if ((datePicker.getDayOfMonth() < 10) && ((datePicker.getMonth() + 1) >= 10)) {
                    txt_date_newlog = "0" + datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                } else if ((datePicker.getDayOfMonth() >= 10) && ((datePicker.getMonth() + 1) < 10)) {
                    txt_date_newlog = datePicker.getDayOfMonth() + "-0" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                } else
                    txt_date_newlog = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();

                System.out.println("FECHA NEW LOG " + datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear());

                int int_fecha = funciones.string_a_int(txt_date_newlog);
                Date fecha_newlog = funciones.string_a_date(txt_date_newlog);

                DBCar dbcar = new DBCar(v.getContext());
                Cursor c = dbcar.buscarCocheActivo();

                String matricula = "";
                int int_kms = 0;
                if (c.moveToFirst() == true) {
                    matricula = c.getString(c.getColumnIndex(DBCar.CN_MATRICULA));
                    int_kms = c.getInt(c.getColumnIndex(DBCar.CN_KMS));
                }

                if (int_fecha > funciones.date_a_int(new Date())) {
                    // con NO_REALIZADO
                    final TipoLog miTipoLog = new TipoLog(tipo, fecha_newlog, txt_date_newlog, int_fecha, NO_ACEITE, NO_REVGRAL, matricula, DBLogs.NO_REALIZADO, int_kms);
                    System.out.println("LOG " + tipo + " " + fecha_newlog + " " + txt_date_newlog + "INT FECHA! " + int_fecha);
                    addlog(miTipoLog, managerLogs);
                    if(tipo.equals(TipoLog.TIPO_ITV)) { // Solo para el caso de que no se haya introducido la fecha de ITV al crear el coche y se meta el itv por aquí y no rellenando su campo
                        DBCar dbc = new DBCar(getApplicationContext());
                        dbc.ActualizarITVCocheActivo(matricula, int_fecha);
                    }
                }
                else {
                    // con REALIZADO
                    final TipoLog miTipoLog = new TipoLog(tipo, fecha_newlog, txt_date_newlog, int_fecha, NO_ACEITE, NO_REVGRAL, matricula, DBLogs.REALIZADO, int_kms);
                    System.out.println("LOG " + tipo + " " + fecha_newlog + " " + txt_date_newlog + "INT FECHA! " + int_fecha);
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddLog.this);
                    builder.setMessage("¿Quiere añadir la última revisión hecha de " + miTipoLog.getTipo(miTipoLog) + "?")
                            .setTitle("Historial")
                            .setCancelable(false)
                            .setNegativeButton("Cancelar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id_dialog) {
                                            dialog.cancel();
                                        }
                                    })
                            .setPositiveButton("Continuar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id_dialog) {
                                            // metodo que se debe implementar Sí
                                            addlog(miTipoLog, managerLogs);
                                            if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_ITV)) { // Solo para el caso de que no se haya introducido la fecha de ITV al crear el coche y se meta el itv por aquí y no rellenando su campo
                                                DBCar dbc = new DBCar(getApplicationContext());
                                                dbc.ActualizarITVCocheActivo(miTipoLog.getCoche(miTipoLog), miTipoLog.getFechaint(miTipoLog));
                                            }
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }


            }
        });
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBLogs managerLogs = new DBLogs(this);
        DBTiposRevision managerTiposRevision = new DBTiposRevision(this);

        setContentView(R.layout.activity_add_log);
        RellenarTipos(managerTiposRevision);
        SeleccionarTipo(managerTiposRevision);
        GuardarLog(managerLogs);
    }






}
