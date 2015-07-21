package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.formats.funciones;
import com.carlog.gilberto.carlog.tiposClases.TipoLog;
import com.carlog.gilberto.carlog.view.SimpleDataView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.melnykov.fab.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gilberto on 23/06/2015.
 */
public class AddItv extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_itv);

        Date fechaITV = (Date)getIntent().getExtras().getSerializable("fechaITV");
    /*    //vaciar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        DatePicker datePicker2 = (DatePicker) findViewById(R.id.date_itv);
        datePicker2.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);*/

        if(funciones.date_a_long(fechaITV) < MyActivity.FIRST_DATE) {
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

                Intent intent = new Intent(AddItv.this, MyActivity.class);
                intent.putExtra("itv_string", itv_string);
                intent.putExtra("addItv", true);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
