package com.carlog.gilberto.carlog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Gilberto on 29/10/2014.
 */
public class DatosIniciales extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.siguiente);

        Coche miCoche = (Coche)getIntent().getExtras().getSerializable("miCoche");

        String marca = miCoche.getMarca(miCoche);
        String modelo = miCoche.getModelo(miCoche);
        String year = miCoche.getYear(miCoche);
        String kms = miCoche.getKms(miCoche);
        String itv = miCoche.getItv(miCoche);

       /* String marca = getIntent().getStringExtra("marca");  //Ya no guardamos las variables sueltas sino en un objeto Coche
        String modelo = getIntent().getStringExtra("modelo");
        String year = getIntent().getStringExtra("year");
        String kms = getIntent().getStringExtra("kms");
        String itv = getIntent().getStringExtra("itv"); */

        TextView text=(TextView)findViewById(R.id.marca2);
        text.setText(marca);

        text=(TextView)findViewById(R.id.modelo2);
        text.setText(modelo);

        text=(TextView)findViewById(R.id.year2);
        text.setText(year);

        text=(TextView)findViewById(R.id.kms2);
        text.setText(kms);

        text=(TextView)findViewById(R.id.itv2);
        text.setText(itv);
    }
}
