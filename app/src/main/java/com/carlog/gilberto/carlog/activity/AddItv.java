package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.gc.materialdesign.views.ButtonRectangle;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gilberto on 23/06/2015.
 */
public class addItv extends ActionBarActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_itv);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Date fechaITV = (Date)getIntent().getExtras().getSerializable("fechaITV");
    /*    //vaciar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        DatePicker datePicker2 = (DatePicker) findViewById(R.id.date_itv);
        datePicker2.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);*/

        if(funciones.date_a_long(fechaITV) < myActivity.FIRST_DATE) {
            fechaITV = new Date();
        }
        //rellenar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaITV);

        DatePicker datePicker2 = (DatePicker) findViewById(R.id.date_itv);
        datePicker2.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

        GuardarItv();
    }

    private void GuardarItv() {
        ButtonRectangle btn_guardarItv = (ButtonRectangle) findViewById(R.id.button_guardaritv);
        btn_guardarItv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = (DatePicker) findViewById(R.id.date_itv);

                String itv_string = "";
                if ((datePicker.getDayOfMonth() < 10) && ((datePicker.getMonth()+1) < 10)) {
                    itv_string = "0"+datePicker.getDayOfMonth() +"-0"+ (datePicker.getMonth()+1) + "-" + datePicker.getYear();
                } else
                if ((datePicker.getDayOfMonth() < 10) && ((datePicker.getMonth()+1) >= 10)) {
                    itv_string = "0"+datePicker.getDayOfMonth() +"-"+ (datePicker.getMonth()+1)+ "-" + datePicker.getYear();
                } else
                if ((datePicker.getDayOfMonth() >= 10) && ((datePicker.getMonth()+1) < 10)) {
                    itv_string = datePicker.getDayOfMonth() +"-0"+ (datePicker.getMonth()+1) + "-" + datePicker.getYear();
                } else
                    itv_string = datePicker.getDayOfMonth() +"-"+ (datePicker.getMonth()+1)+ "-" + datePicker.getYear();

                Intent intent = new Intent(addItv.this, myActivity.class);
                intent.putExtra("itv_string", itv_string);
                intent.putExtra("aceite_string", itv_string);
                intent.putExtra("revgral_string", itv_string);
                intent.putExtra("correa_string", itv_string);
                intent.putExtra("bombaagua_string", itv_string);
                intent.putExtra("fgasolina_string", itv_string);
                intent.putExtra("faire_string", itv_string);
                intent.putExtra("bujias_string", itv_string);
                intent.putExtra("embrague_string", itv_string);
                intent.putExtra("addItv", true);
                setResult(Activity.RESULT_OK, intent);

                finish();
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
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(addItv.this);
            login.closeFacebookSession(addItv.this, login.class);
            Intent intent = new Intent(addItv.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
