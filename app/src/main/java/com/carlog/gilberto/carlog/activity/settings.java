package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.adapter.miAdaptadorSettings;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.data.dbSettings;
import com.carlog.gilberto.carlog.tiposClases.tipoLog;
import com.carlog.gilberto.carlog.tiposClases.tipoSettings;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.gc.materialdesign.views.CheckBox;

import java.util.ArrayList;

/**
 * Created by Gilberto on 12/08/2015.
 */
public class settings extends ActionBarActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ListView listview = (ListView) findViewById(R.id.listview_settings);
        String[] values = new String[] { "Ayuda", "Notificaciones", "Predecir " +  tipoLog.TIPO_ACEITE, "Predecir " +  tipoLog.TIPO_AMORTIGUADORES, "Predecir " +  tipoLog.TIPO_ANTICONGELANTE, "Predecir " +  tipoLog.TIPO_BATERIA
                , "Predecir " +  tipoLog.TIPO_BOMBA_AGUA, "Predecir " +  tipoLog.TIPO_BUJIAS, "Predecir " +  tipoLog.TIPO_CORREA, "Predecir " +  tipoLog.TIPO_EMBRAGUE, "Predecir " +  tipoLog.TIPO_FILTRO_ACEITE
                , "Predecir " +  tipoLog.TIPO_FILTRO_AIRE, "Predecir " +  tipoLog.TIPO_FILTRO_GASOLINA, "Predecir " +  tipoLog.TIPO_FRENOS, "Predecir " +  tipoLog.TIPO_ITV, "Predecir " +  tipoLog.TIPO_LIMPIAPARABRISAS
                , "Predecir " +  tipoLog.TIPO_LIQUIDO_FRENOS, "Predecir " +  tipoLog.TIPO_LUCES, "Predecir " +  tipoLog.TIPO_REV_GENERAL, "Predecir " +  tipoLog.TIPO_RUEDAS/*,"Perfil"*/};



        final miAdaptadorSettings adapter = new miAdaptadorSettings(settings.this, values);
        listview.setAdapter(adapter);

        modificarSettingPulsando(settings.this, listview, adapter);



    }



    public void modificarSettingPulsando(final Activity act, final ListView list, final miAdaptadorSettings adapter) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final dbSettings manager = new dbSettings(act.getApplicationContext());
            Cursor c = manager.getSettings();
            int activo = tipoSettings.ACTIVO;

            for (int i = 0; i < list.getCount(); i++) {
                if(i == position) {
                    if(i == 0) {
                        Intent intent = new Intent(settings.this, ayuda.class);
                        startActivity(intent);
                        break;
                    }
                    else if(i == 1) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_NOTIFICACIONES));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarNotificaciones(activo);
                        break;
                    }
                    else if(i == 2) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_ACEITE));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarAceite(activo);
                        break;
                    }
                    else if(i == 3) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_AMORTIGUADORES));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarAmortiguadores(activo);
                        break;
                    }
                    else if(i == 4) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_ANTICONGELANTE));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarAnticongelante(activo);
                        break;
                    }
                    else if(i == 5) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_BATERIA));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarBateria(activo);
                        break;
                    }
                    else if(i == 6) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_BOMBAAGUA));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarBombaagua(activo);
                        break;
                    }
                    else if(i == 7) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_BUJIAS));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarBujias(activo);
                        break;
                    }
                    else if(i == 8) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_CORREA));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarCorrea(activo);
                        break;
                    }
                    else if(i == 9) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_EMBRAGUE));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarEmbrague(activo);
                        break;
                    }
                    else if(i == 10) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_FILACEITE));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarFilaceite(activo);
                        break;
                    }
                    else if(i == 11) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_FILAIRE));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarFilaire(activo);
                        break;
                    }
                    else if(i == 12) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_FILGASOLINA));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarFilgasolina(activo);
                        break;
                    }
                    else if(i == 13) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_FRENOS));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarFrenos(activo);
                        break;
                    }
                    else if(i == 14) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_ITV));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarItv(activo);
                        break;
                    }
                    else if(i == 15) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_LIMPIAPARABRISAS));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarLimpiaparabrisas(activo);
                        break;
                    }
                    else if(i == 16) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_LIQFRENOS));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarLiqfrenos(activo);
                        break;
                    }
                    else if(i == 17) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_LUCES));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarLuces(activo);
                        break;
                    }
                    else if(i == 18) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_REVGEN));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarRevgen(activo);
                        break;
                    }
                    else if(i == 19) {
                        if(c.moveToFirst() == true) {
                            activo = c.getInt(c.getColumnIndex(dbSettings.CN_RUEDAS));
                            if (activo == tipoSettings.ACTIVO) activo = tipoSettings.INACTIVO;
                            else activo = tipoSettings.ACTIVO;
                        }
                        manager.actualizarRuedas(activo);
                        break;
                    }
                    break;
                }
            }
                adapter.notifyDataSetChanged();

            }
        });
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
            Intent i = new Intent(settings.this, info.class);
            settings.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(settings.this);
            login.deleteParamsAnonimo(settings.this);
            login.closeFacebookSession(settings.this, login.class);
            Intent intent = new Intent(settings.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}