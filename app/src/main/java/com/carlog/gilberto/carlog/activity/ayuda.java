package com.carlog.gilberto.carlog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.tiposClases.usuario;

/**
 * Created by Gilberto on 14/08/2015.
 */
public class ayuda  extends ActionBarActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
            Intent i = new Intent(ayuda.this, settings.class);
            ayuda.this.startActivity(i);
        }
        if (id == R.id.action_info) {
            Intent i = new Intent(ayuda.this, info.class);
            ayuda.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            usuario u = new usuario();
            u.logout(ayuda.this);
            login.deleteParamsAnonimo(ayuda.this);
            login.closeFacebookSession(ayuda.this, login.class);
            Intent intent = new Intent(ayuda.this, login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
