package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.data.dbCar;
import com.carlog.gilberto.carlog.data.dbHelper;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.tiposClases.tipoCoche;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.data.dbTiposRevision;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.gc.materialdesign.views.ButtonRectangle;

import java.util.ArrayList;
import java.util.Date;


public class addLog extends ActionBarActivity {

    public final static int NO_ACEITE = -1;
    public final static int NO_VECES_FIL_ACEITE = -1;
    public final static int NO_CONTADOR_FIL_ACEITE = -1;
    public final static int NO_REVGRAL = -1;
    public final static int NO_CORREA = -1;
    public final static int NO_BOMBAAGUA = -1;
    public final static int NO_FGASOLINA = -1;
    public final static int NO_FAIRE = -1;
    public final static int NO_BUJIAS = -1;
    public final static int NO_EMBRAGUE = -1;
    private Spinner spinner1;
    private Toolbar toolbar;

    private void RellenarTipos(dbTiposRevision managerTiposRevision) {
        spinner1 = (Spinner) findViewById(R.id.cmb_tipos);
        spinner1 = (Spinner) this.findViewById(R.id.cmb_tipos);

        Cursor cursor = managerTiposRevision.cargarCursorTiposRevision();
        ArrayList<String> tipos = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tipo = cursor.getString(cursor.getColumnIndex(managerTiposRevision.CN_TIPO));
            tipos.add(tipo);
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);
    }

    private void SeleccionarTipo(final dbTiposRevision managerTiposRevision) {
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
        ButtonRectangle btm_agregar = (ButtonRectangle) findViewById (R.id.btm_addTipo);
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
                        managerTiposRevision.insertar(nuevoDato, "ic_launcher"); // los personalizados van a tener el logo de la app
                        RellenarTipos(managerTiposRevision);
                        edttxt.setVisibility(View.GONE);
                        tv_nt.setVisibility(View.GONE);
                        edttxt.setText("");
                    }
                    else Toast.makeText(getApplicationContext(), addLog.this.getString(R.string.tipoexiste), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ButtonRectangle btm_eliminar = (ButtonRectangle) findViewById (R.id.btm_elimTipo);
        btm_eliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Spinner spinner = (Spinner)findViewById(R.id.cmb_tipos);
                String elimdata = spinner.getSelectedItem().toString();
                //Solo podremos eliminar revisiones que no estén por defecto en la app
                dbTiposRevision dbtr = new dbTiposRevision(getApplicationContext());
                Cursor c_tr = dbtr.cargarCursorTiposRevision();
                int i = 0;
                String tipo = "";
                for(c_tr.moveToFirst(); !c_tr.isAfterLast(); c_tr.moveToNext()){
                    tipo = c_tr.getString(c_tr.getColumnIndex(dbTiposRevision.CN_TIPO));
                    if(tipo.equals(elimdata)) break;
                    i++;
                }
                if ((i > dbHelper.MAX_TIPOS_REV) && (elimdata.equals(tipo))) {
                    managerTiposRevision.eliminar(elimdata);
                    RellenarTipos(managerTiposRevision);
                }
                else {
                    Toast.makeText(getApplicationContext(), addLog.this.getString(R.string.noEliminarTipoDefecto), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void addlog(tipoLog miTipoLog, dbLogs managerLogs) {
        Intent intent = null;
        dbCar dbc = new dbCar(getApplicationContext());
        Cursor c_activo = dbc.buscarCocheActivo();
        String matricula = "", marca = "", modelo = "";
        int int_year = 0, int_kms = 0, int_kms_ini = 0, int_itv = 0, int_fecha_ini = 0;
        if (c_activo.moveToFirst() == true) {
            matricula = c_activo.getString(c_activo.getColumnIndex(dbCar.CN_MATRICULA));
            marca = c_activo.getString(c_activo.getColumnIndex(dbCar.CN_MARCA));
            modelo = c_activo.getString(c_activo.getColumnIndex(dbCar.CN_MODELO));
            int_year = c_activo.getInt(c_activo.getColumnIndex(dbCar.CN_YEAR));
            int_kms = c_activo.getInt(c_activo.getColumnIndex(dbCar.CN_KMS));
            int_itv = c_activo.getInt(c_activo.getColumnIndex(dbCar.CN_ITV));
            int_kms_ini = c_activo.getInt(c_activo.getColumnIndex(dbCar.CN_KMS_INI));
            int_fecha_ini = c_activo.getInt(c_activo.getColumnIndex(dbCar.CN_FECHA_INI));
        }
        tipoCoche miCoche = new tipoCoche(matricula, marca, modelo, dbCar.IMG_MODELO_NOCAMBIADA, null, int_year, int_kms, int_itv, tipoCoche.PROFILE_ACTIVO, int_fecha_ini, int_kms_ini);
        long long_now = funciones.date_a_long(new Date());
        long ahora = funciones.date_a_long(new Date());
        boolean seguir_rellenando = false; // si insertamos un filtro de aceite sin tener aceite tenemos q poder seguir añadiendo
        // Antes de hacer nada miramos si ya existe algun tipo igual pues no debemos tener más de uno
        Cursor c = managerLogs.buscarTipo(miTipoLog.getTipo(miTipoLog), miCoche.getMatricula(miCoche));
        if ((c.moveToFirst() == false) || (tipoLog.es_tipo_personalizado(miTipoLog.getTipo(miTipoLog), addLog.this)) || (miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoTaller)))) { // Si no hay logs (ni futuros ni históricos)
            if(miTipoLog.getFechalong(miTipoLog) >= ahora) {
               // Toast.makeText(getApplicationContext(), "Se recomienda insertar la última revisión de " + miTipoLog.getTipo(miTipoLog) + " hecha.", Toast.LENGTH_SHORT).show();
            }
            if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoAceite))) {
                intent = new Intent(addLog.this, aceite.class);
                intent.putExtra("miTipoLog", miTipoLog);
                intent.putExtra("miCoche", miCoche);
                startActivity(intent);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoRevGen))) {
                intent = new Intent(addLog.this, revGral.class);
                intent.putExtra("miTipoLog", miTipoLog);
                intent.putExtra("miCoche", miCoche);
                startActivity(intent);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoItv))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoCorrea))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoBomba))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoFiltroAceite))) {
                // Solo podemos agregar cambio de filtro de aceite si tenemos añadido el aceite
                Cursor c_ac = managerLogs.buscarTipo(addLog.this.getString(R.string.tipoAceite), matricula);
                if (c_ac.moveToFirst() == true) {
                    intent = new Intent(addLog.this, filtroAceite.class);
                    intent.putExtra("miTipoLog", miTipoLog);
                    intent.putExtra("miCoche", miCoche);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), addLog.this.getString(R.string.noAddFiltroSinAceite), Toast.LENGTH_SHORT).show();
                    seguir_rellenando = true;
                }
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoFiltroGasolina))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoFiltroAire))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoBujias))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoFrenos))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoLiqFrenos))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoLimpiaparabrisas))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoLuces))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoRuedas))) {
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else { // es un tipo agregado por el usuario
                intent = new Intent(addLog.this, listaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            if(!seguir_rellenando) {
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        } else Toast.makeText(getApplicationContext(), addLog.this.getString(R.string.tipoDuplicado) + miTipoLog.getTipo(miTipoLog), Toast.LENGTH_SHORT).show();
    }

    private void GuardarLog(final dbLogs managerLogs) {
        ButtonRectangle btn1 = (ButtonRectangle) findViewById(R.id.guardar);
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

                long long_fecha = funciones.string_a_long(txt_date_newlog);
                Date fecha_newlog = funciones.string_a_date(txt_date_newlog);
                dbCar dbcar = new dbCar(v.getContext());
                Cursor c = dbcar.buscarCocheActivo();

                String matricula = "";
                int int_kms = 0;
                if (c.moveToFirst() == true) {
                    matricula = c.getString(c.getColumnIndex(dbCar.CN_MATRICULA));
                    int_kms = c.getInt(c.getColumnIndex(dbCar.CN_KMS));
                }
                if (long_fecha > funciones.date_a_long(new Date())) {
                    // con NO_REALIZADO
                    final tipoLog miTipoLog = new tipoLog(tipo, fecha_newlog, txt_date_newlog, long_fecha, NO_ACEITE, NO_VECES_FIL_ACEITE, NO_CONTADOR_FIL_ACEITE, NO_REVGRAL, NO_CORREA, NO_BOMBAAGUA, NO_FGASOLINA, NO_FAIRE, NO_BUJIAS, NO_EMBRAGUE, matricula, dbLogs.NO_REALIZADO, dbLogs.NO_FMODIFICADA, int_kms);
                    addlog(miTipoLog, managerLogs);
                    if(tipo.equals(addLog.this.getString(R.string.tipoItv))) { // Solo para el caso de que no se haya introducido la fecha de ITV al crear el coche y se meta el itv por aquí y no rellenando su campo
                        dbCar dbc = new dbCar(getApplicationContext());
                        dbc.ActualizarITVCocheActivo(matricula, long_fecha);

                    }
                }
                else {
                    // con REALIZADO
                    final tipoLog miTipoLog = new tipoLog(tipo, fecha_newlog, txt_date_newlog, long_fecha, NO_ACEITE, NO_VECES_FIL_ACEITE, NO_CONTADOR_FIL_ACEITE, NO_REVGRAL, NO_CORREA, NO_BOMBAAGUA, NO_FGASOLINA, NO_FAIRE, NO_BUJIAS, NO_EMBRAGUE, matricula, dbLogs.REALIZADO, dbLogs.NO_FMODIFICADA, int_kms);
                    System.out.println("LOG " + tipo + " " + fecha_newlog + " " + txt_date_newlog + "INT FECHA! " + long_fecha);
                    AlertDialog.Builder builder = new AlertDialog.Builder(addLog.this);
                    builder.setMessage("" + addLog.this.getString(R.string.addLastRevision) + miTipoLog.getTipo(miTipoLog) + "?")
                        .setTitle(addLog.this.getString(R.string.historial))
                        .setCancelable(false)
                        .setNegativeButton(addLog.this.getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id_dialog) {
                                    dialog.cancel();
                                }
                            })
                        .setPositiveButton(addLog.this.getString(R.string.continuar),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id_dialog) {
                                // metodo que se debe implementar Sí
                                addlog(miTipoLog, managerLogs);
                                if(miTipoLog.getTipo(miTipoLog).equals(addLog.this.getString(R.string.tipoItv))) { // Solo para el caso de que no se haya introducido la fecha de ITV al crear el coche y se meta el itv por aquí y no rellenando su campo
                                    dbCar dbc = new dbCar(getApplicationContext());
                                    dbc.ActualizarITVCocheActivo(miTipoLog.getCoche(miTipoLog), miTipoLog.getFechalong(miTipoLog));
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
        setContentView(R.layout.activity_add_log);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbLogs managerLogs = new dbLogs(this);
        dbTiposRevision managerTiposRevision = new dbTiposRevision(this);
        RellenarTipos(managerTiposRevision);
        SeleccionarTipo(managerTiposRevision);
        GuardarLog(managerLogs);
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
            Intent i = new Intent(addLog.this, settings.class);
            addLog.this.startActivity(i);
        }
        if (id == R.id.action_info) {
            Intent i = new Intent(addLog.this, info.class);
            addLog.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(addLog.this);
            login.deleteParamsAnonimo(addLog.this);
            login.closeFacebookSession(addLog.this, login.class);
            Intent intent = new Intent(addLog.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
