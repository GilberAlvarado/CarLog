package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.tiposClases.Usuario;
import com.gc.materialdesign.views.ButtonRectangle;

import org.json.JSONObject;

/**
 * Created by Gilberto on 24/07/2015.
 */
public class Register extends Activity {

    private TextView lblGotoLogin;
    private ButtonRectangle btnRegister;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView registerErrorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.txtUserName);
        inputEmail = (EditText) findViewById(R.id.txtEmail);
        inputPassword = (EditText) findViewById(R.id.txtPass);
        btnRegister = (ButtonRectangle) findViewById(R.id.btnRegister);

        registerErrorMsg = (TextView) findViewById(R.id.register_error);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                Usuario usuario = new Usuario();
                usuario.setOnRegisterUsuario(new Usuario.OnRegisterUsuario() {
                    @Override
                    public void onRegisterFinish(JSONObject json, String msg) {
                        registerErrorMsg.setText("");
                        Intent itemintent = new Intent(Register.this, MyActivity.class);
                        Register.this.startActivity(itemintent);
                        finish();
                    }

                    @Override
                    public void onRegisterFail(String msg) {
                        registerErrorMsg.setText(msg);
                    }

                    @Override
                    public void onRegisterException(Exception e, String msg) {
                        registerErrorMsg.setText(msg);
                    }
                });
                usuario.register(Register.this, name, email, password);
            }
        });

        lblGotoLogin = (TextView) findViewById(R.id.link_to_login);
        lblGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemintent = new Intent(Register.this, Login.class);
                Register.this.startActivity(itemintent);
            }
        });

    }

}