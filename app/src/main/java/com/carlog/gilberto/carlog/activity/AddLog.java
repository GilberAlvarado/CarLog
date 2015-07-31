package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import com.carlog.gilberto.carlog.data.DBCar;
import com.carlog.gilberto.carlog.data.DbHelper;
import com.carlog.gilberto.carlog.tiposClases.TipoCoche;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;
import com.carlog.gilberto.carlog.data.DBLogs;
import com.carlog.gilberto.carlog.data.DBTiposRevision;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.Usuario;
import com.gc.materialdesign.views.ButtonRectangle;

import java.util.ArrayList;
import java.util.Date;


public class AddLog extends ActionBarActivity {

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
                        edttxt.setVisibility(View.INVISIBLE);
                        tv_nt.setVisibility(View.INVISIBLE);
                        edttxt.setText("");
                    }
                    else Toast.makeText(getApplicationContext(), "El tipo ya existe", Toast.LENGTH_SHORT).show();
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
                DBTiposRevision dbtr = new DBTiposRevision(getApplicationContext());
                Cursor c_tr = dbtr.cargarCursorTiposRevision();
                int i = 0;
                String tipo = "";
                for(c_tr.moveToFirst(); !c_tr.isAfterLast(); c_tr.moveToNext()){
                    tipo = c_tr.getString(c_tr.getColumnIndex(DBTiposRevision.CN_TIPO));
                    if(tipo.equals(elimdata)) break;
                    i++;
                }
                if ((i > DbHelper.MAX_TIPOS_REV) && (elimdata.equals(tipo))) {
                    managerTiposRevision.eliminar(elimdata);
                    RellenarTipos(managerTiposRevision);
                }
                else {
                    Toast.makeText(getApplicationContext(), "No se pueden eliminar revisiones por defecto.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void addlog(TipoLog miTipoLog, DBLogs managerLogs) {

        Intent intent = null;
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
        long long_now = funciones.date_a_long(new Date());
        long ahora = funciones.date_a_long(new Date());
        boolean seguir_rellenando = false; // si insertamos un filtro de aceite sin tener aceite tenemos q poder seguir añadiendo
        // Antes de hacer nada miramos si ya existe algun tipo igual pues no debemos tener más de uno
        Cursor c = managerLogs.buscarTipo(miTipoLog.getTipo(miTipoLog), miCoche.getMatricula(miCoche));
        if (c.moveToFirst() == false) { // Si no hay logs (ni futuros ni históricos)
            if(miTipoLog.getFechalong(miTipoLog) >= ahora) {
               // Toast.makeText(getApplicationContext(), "Se recomienda insertar la última revisión de " + miTipoLog.getTipo(miTipoLog) + " hecha.", Toast.LENGTH_SHORT).show();
            }
            if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_ACEITE)) {
                intent = new Intent(AddLog.this, Aceite.class);
                intent.putExtra("miTipoLog", miTipoLog);
                intent.putExtra("miCoche", miCoche);
                startActivity(intent);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_REV_GENERAL)) {
                intent = new Intent(AddLog.this, RevGral.class);
                intent.putExtra("miTipoLog", miTipoLog);
                intent.putExtra("miCoche", miCoche);
                startActivity(intent);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_ITV)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_CORREA)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_BOMBA_AGUA)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_FILTRO_ACEITE)) {
                // Solo podemos agregar cambio de filtro de aceite si tenemos añadido el aceite
                Cursor c_ac = managerLogs.buscarTipo(TipoLog.TIPO_ACEITE, matricula);
                if (c_ac.moveToFirst() == true) {
                    intent = new Intent(AddLog.this, FiltroAceite.class);
                    intent.putExtra("miTipoLog", miTipoLog);
                    intent.putExtra("miCoche", miCoche);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "No puede agregar revisión de filtro de aceite sin tener una revisión de aceite.", Toast.LENGTH_SHORT).show();
                    seguir_rellenando = true;
                }
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_FILTRO_GASOLINA)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_FILTRO_AIRE)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_BUJIAS)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_FRENOS)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_LIQUIDO_FRENOS)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_LIMPIAPARABRISAS)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_LUCES)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else if(miTipoLog.getTipo(miTipoLog).equals(TipoLog.TIPO_RUEDAS)) {
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            else { // es un tipo agregado por el usuario
                intent = new Intent(AddLog.this, ListaLogs.class);
                managerLogs.insertar(miTipoLog);
            }
            if(!seguir_rellenando) {
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        } else Toast.makeText(getApplicationContext(), "Ya tiene pendiente una revisión de " + miTipoLog.getTipo(miTipoLog), Toast.LENGTH_SHORT).show();
    }

    private void GuardarLog(final DBLogs managerLogs) {
        //Instanciamos el Boton
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

                System.out.println("FECHA NEW LOG " + datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear());

                long long_fecha = funciones.string_a_long(txt_date_newlog);
                Date fecha_newlog = funciones.string_a_date(txt_date_newlog);

                DBCar dbcar = new DBCar(v.getContext());
                Cursor c = dbcar.buscarCocheActivo();

                String matricula = "";
                int int_kms = 0;
                if (c.moveToFirst() == true) {
                    matricula = c.getString(c.getColumnIndex(DBCar.CN_MATRICULA));
                    int_kms = c.getInt(c.getColumnIndex(DBCar.CN_KMS));
                }

                if (long_fecha > funciones.date_a_long(new Date())) {
                    // con NO_REALIZADO
                    final TipoLog miTipoLog = new TipoLog(tipo, fecha_newlog, txt_date_newlog, long_fecha, NO_ACEITE, NO_VECES_FIL_ACEITE, NO_CONTADOR_FIL_ACEITE, NO_REVGRAL, NO_CORREA, NO_BOMBAAGUA, NO_FGASOLINA, NO_FAIRE, NO_BUJIAS, NO_EMBRAGUE, matricula, DBLogs.NO_REALIZADO, DBLogs.NO_FMODIFICADA, int_kms);
                    System.out.println("LOG " + tipo + " " + fecha_newlog + " " + txt_date_newlog + "INT FECHA! " + long_fecha);
                    addlog(miTipoLog, managerLogs);
                    if(tipo.equals(TipoLog.TIPO_ITV)) { // Solo para el caso de que no se haya introducido la fecha de ITV al crear el coche y se meta el itv por aquí y no rellenando su campo
                        DBCar dbc = new DBCar(getApplicationContext());
                        dbc.ActualizarITVCocheActivo(matricula, long_fecha);

                    }
                }
                else {
                    // con REALIZADO
                    final TipoLog miTipoLog = new TipoLog(tipo, fecha_newlog, txt_date_newlog, long_fecha, NO_ACEITE, NO_VECES_FIL_ACEITE, NO_CONTADOR_FIL_ACEITE, NO_REVGRAL, NO_CORREA, NO_BOMBAAGUA, NO_FGASOLINA, NO_FAIRE, NO_BUJIAS, NO_EMBRAGUE, matricula, DBLogs.REALIZADO, DBLogs.NO_FMODIFICADA, int_kms);
                    System.out.println("LOG " + tipo + " " + fecha_newlog + " " + txt_date_newlog + "INT FECHA! " + long_fecha);
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

        DBLogs managerLogs = new DBLogs(this);
        DBTiposRevision managerTiposRevision = new DBTiposRevision(this);
        RellenarTipos(managerTiposRevision);
        SeleccionarTipo(managerTiposRevision);
        GuardarLog(managerLogs);
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
            u.logout(AddLog.this);
            Login.closeFacebookSession(AddLog.this, Login.class);
            Intent intent = new Intent(AddLog.this, Login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

}
