package com.carlog.gilberto.carlog.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.activity.addLog;
import com.carlog.gilberto.carlog.activity.listaLogs;
import com.carlog.gilberto.carlog.activity.modificarAceite;
import com.carlog.gilberto.carlog.activity.modificarBombaAgua;
import com.carlog.gilberto.carlog.activity.modificarBujias;
import com.carlog.gilberto.carlog.activity.modificarCorrea;
import com.carlog.gilberto.carlog.activity.modificarEmbrague;
import com.carlog.gilberto.carlog.activity.modificarFiltroAceite;
import com.carlog.gilberto.carlog.activity.modificarFiltroAire;
import com.carlog.gilberto.carlog.activity.modificarFiltroGasolina;
import com.carlog.gilberto.carlog.activity.modificarItv;
import com.carlog.gilberto.carlog.activity.modificarRevGral;
import com.carlog.gilberto.carlog.adapter.miAdaptadorLog;
import com.carlog.gilberto.carlog.data.dbCar;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.view.BaseFragment;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gilberto on 03/08/2015.
 */
public class fragmentHistorial extends BaseFragment {
    private View rootView;
    ObservableListView listView;

    public static void borrarLogpulsado(final Cursor cursor, final dbLogs manager, final int posicion, final Context context, final Activity act) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setMessage("¿Está seguro de querer eliminar?")
            .setTitle("Borrar de la lista")
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
                            //Recorremos el cursor
                            int i = 0;
                            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                                if (i == posicion) { // la posicion del cursor coincide con la del que pulsamos en la lista
                                    int id = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_ID));
                                    String matricula = cursor.getString(cursor.getColumnIndex(dbLogs.CN_CAR));
                                    manager.eliminar_por_id(id);
                                    listaLogs a = (listaLogs) act;
                                    fragmentHistorial fh = (fragmentHistorial) a.getCurrentFragment();
                                    fh.ConsultarLogsHistoricos(context, act);
                                    break;
                                }
                                i++;
                            }
                        }
                    });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void modificarLogpulsado(final Cursor cursor, int posicion, Activity context) {
        //Recorremos el cursor
        int i = 0;
        String tipo = "";
        Intent intent = new Intent();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if (i == posicion) { // la posicion del cursor coincide con la del que pulsamos en la lista
                int id = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_ID));
                tipo = cursor.getString(cursor.getColumnIndex(dbLogs.CN_TIPO));
                if(tipo.equals(tipoLog.TIPO_ACEITE)) {
                    intent = new Intent(context, modificarAceite.class);
                }
                if(tipo.equals(tipoLog.TIPO_REV_GENERAL)) {
                    intent = new Intent(context, modificarRevGral.class);
                }
                if(tipo.equals(tipoLog.TIPO_CORREA)) {
                    intent = new Intent(context, modificarCorrea.class);
                }
                if(tipo.equals(tipoLog.TIPO_BOMBA_AGUA)) {
                    intent = new Intent(context, modificarBombaAgua.class);
                }
                if(tipo.equals(tipoLog.TIPO_FILTRO_GASOLINA)) {
                    intent = new Intent(context, modificarFiltroGasolina.class);
                }
                if(tipo.equals(tipoLog.TIPO_FILTRO_AIRE)) {
                    intent = new Intent(context, modificarFiltroAire.class);
                }
                if(tipo.equals(tipoLog.TIPO_BUJIAS)) {
                    intent = new Intent(context, modificarBujias.class);
                }
                if(tipo.equals(tipoLog.TIPO_EMBRAGUE)) {
                    intent = new Intent(context, modificarEmbrague.class);
                }
                if(tipo.equals(tipoLog.TIPO_ITV)) {
                    intent = new Intent(context, modificarItv.class);
                }
                if(tipo.equals(tipoLog.TIPO_FILTRO_ACEITE)) {
                    intent = new Intent(context, modificarFiltroAceite.class);
                }
                //************************************************************************


                String txt_fecha = cursor.getString(cursor.getColumnIndex("fecha_string"));
                int aceite = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_ACEITE));
                int revgral = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_REVGRAL));
                int correa = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_CORREA));
                int bombaagua = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_BOMBAAGUA));
                int fgasolina = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_FGASOLINA));
                int faire = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_FAIRE));
                int bujias = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_BUJIAS));
                int embrague = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_EMBRAGUE));
                String matricula = cursor.getString(cursor.getColumnIndex(dbLogs.CN_CAR));
                int kms = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_KMS));
                int veces_filaceite = cursor.getInt(cursor.getColumnIndex(dbLogs.CN_VECES_FIL_ACEITE));


                tipoLog miTipo = new tipoLog(tipo, funciones.string_a_date(txt_fecha), txt_fecha, funciones.string_a_long(txt_fecha), aceite, veces_filaceite, addLog.NO_CONTADOR_FIL_ACEITE, revgral, correa, bombaagua, fgasolina, faire, bujias, embrague, matricula, dbLogs.NO_REALIZADO, dbLogs.NO_FMODIFICADA, kms);
                intent.putExtra("miTipo", miTipo);
                intent.putExtra("idLog", id);
                intent.putExtra("Historial", true);
                break;
            }
            i++;
        }

        if(tipo.equals(tipoLog.TIPO_ACEITE)) {
            context.startActivityForResult(intent, listaLogs.PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(tipoLog.TIPO_REV_GENERAL)) {
            context.startActivityForResult(intent, listaLogs.PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(tipoLog.TIPO_ITV)) {
            context.startActivityForResult(intent, listaLogs.PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(tipoLog.TIPO_CORREA)) {
            context.startActivityForResult(intent, listaLogs.PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(tipoLog.TIPO_BOMBA_AGUA)) {
            context.startActivityForResult(intent, listaLogs.PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(tipoLog.TIPO_FILTRO_GASOLINA)) {
            context.startActivityForResult(intent, listaLogs.PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(tipoLog.TIPO_FILTRO_AIRE)) {
            context.startActivityForResult(intent, listaLogs.PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(tipoLog.TIPO_EMBRAGUE)) {
            context.startActivityForResult(intent, listaLogs.PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(tipoLog.TIPO_BUJIAS)) {
            context.startActivityForResult(intent, listaLogs.PETICION_ACTIVITY_MODIFYITV);
        }
        if(tipo.equals(tipoLog.TIPO_FILTRO_ACEITE)) {
            context.startActivity(intent); // No cambia la fecha solo cada cuantos cambios de aceite se hará
        }
    }

    public void modificarLogPulsando(final Activity act, final String matricula) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final dbLogs manager = new dbLogs(act.getApplicationContext());
                final Cursor cursor = manager.LogsHistoricosOrderByFechaString(matricula);
                modificarLogpulsado(cursor, position, act);
            }
        });
    }


    public void ConsultarLogsHistoricos(Context context, Activity act) {
        dbCar dbc = new dbCar(context);
        Cursor c_activo = dbc.buscarCocheActivo();
        String matricula = "";
        if (c_activo.moveToFirst() == true) {
            matricula = c_activo.getString(c_activo.getColumnIndex(dbCar.CN_MATRICULA));
        }
        final dbLogs manager = new dbLogs(context);
        final Cursor cursor = manager.LogsHistoricosOrderByFechaString(matricula);

        List<tipoLog> listaLogs = new ArrayList<tipoLog>();
        //Recorremos el cursor
        int k = 0;
        cursor.moveToFirst();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            tipoLog miTipoLog = new tipoLog(cursor.getString(cursor.getColumnIndex(dbLogs.CN_TIPO)), funciones.string_a_date(cursor.getString(cursor.getColumnIndex("fecha_string"))), cursor.getString(cursor.getColumnIndex("fecha_string")),
                    funciones.string_a_long(cursor.getString(cursor.getColumnIndex("fecha_string"))), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_ACEITE)), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_VECES_FIL_ACEITE)), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_CONTADOR_FIL_ACEITE)), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_REVGRAL))
                    , cursor.getInt(cursor.getColumnIndex(dbLogs.CN_CORREA)), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_BOMBAAGUA)), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_FGASOLINA)), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_FAIRE)), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_BUJIAS)), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_EMBRAGUE)), cursor.getString(cursor.getColumnIndex(dbLogs.CN_CAR)),
                    cursor.getInt(cursor.getColumnIndex(dbLogs.CN_REALIZADO)), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_FMODIFICADA)), cursor.getInt(cursor.getColumnIndex(dbLogs.CN_KMS)));
            listaLogs.add(miTipoLog);

            k++;
        }

        miAdaptadorLog adapter = new miAdaptadorLog(act, listaLogs);

        listView.setAdapter(adapter);
        modificarLogPulsando(act, matricula);
        //Asociamos el menú contextual a los controles para las opciones en longClick
        registerForContextMenu(listView);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_logs, container, false);
        Activity parentActivity = getActivity();
        listView = (ObservableListView) rootView.findViewById(R.id.list);
        setDummyData(listView);
        ConsultarLogsHistoricos(parentActivity.getApplicationContext(), parentActivity);
        if (parentActivity != null) {
            listView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));
            if (parentActivity instanceof ObservableScrollViewCallbacks) {
                listView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
            }
        }
        return rootView;
    }
}
