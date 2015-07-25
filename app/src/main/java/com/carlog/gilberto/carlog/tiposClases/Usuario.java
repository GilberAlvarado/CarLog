package com.carlog.gilberto.carlog.tiposClases;

import android.app.Activity;
import android.database.Cursor;

import com.carlog.gilberto.carlog.data.DBLogin;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gilberto on 24/07/2015.
 */
public class Usuario {

    private String Name = "";
    private String Email = "";
    private String UID = "";

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_USER = "user";

    private static String loginURL = "http://gilberdesign.com/Carlogserver/index.php";
    private static String registerURL = "http://gilberdesign.com/Carlogserver/index.php";

    private static String login_tag = "login";
    private static String register_tag = "register";

    private Activity activity;


    public void login(Activity activity, String email, String password){
        this.activity = activity;
        HttpClientManager httpclient = new HttpClientManager(activity);
        httpclient.addNameValue("tag", login_tag);
        httpclient.addNameValue("email", email);
        httpclient.addNameValue("password", password);

        httpclient.setOnExecuteHttpPostAsyncListener(new HttpClientManager.OnExecuteHttpPostAsyncListener() {
            @Override
            public void onExecuteHttpPostAsyncListener(String ResponseBody) {
                try {
                    JSONObject json = new JSONObject(ResponseBody);
                    if (json.getString(KEY_SUCCESS) != null) {
                        if((Integer.parseInt(json.getString(KEY_SUCCESS)) == 1)){
                            if(Usuario.this.saveLogin(Usuario.this.activity, json))
                                ListenerLoginUsuario.onLoginCorrect(json, "Login correcto");
                            else
                                ListenerLoginUsuario.onLoginWrong("Login incorrecto");
                        } else {
                            ListenerLoginUsuario.onLoginWrong("Login incorrecto");
                        }
                    } else{
                        ListenerLoginUsuario.onLoginWrong("Login incorrecto");
                    }
                } catch (JSONException e) {
                    ListenerLoginUsuario.onLoginWrong("Login incorrecto");
                }
            }

            @Override
            public void onErrorHttpPostAsyncListener(String message) {
            }
        });

        httpclient.executeHttpPost(loginURL);
    }


    public void register(Activity activity, String username, String email, String password){
        this.activity = activity;
        HttpClientManager httpclient = new HttpClientManager(activity);
        httpclient.addNameValue("tag", register_tag);
        httpclient.addNameValue("name", username);
        httpclient.addNameValue("email", email);
        httpclient.addNameValue("password", password);

        httpclient.setOnExecuteHttpPostAsyncListener(new HttpClientManager.OnExecuteHttpPostAsyncListener() {
            @Override
            public void onExecuteHttpPostAsyncListener(String ResponseBody) {
                try {
                    JSONObject json = new JSONObject(ResponseBody);
                    if (json.getString(KEY_SUCCESS) != null) {
                        if((Integer.parseInt(json.getString(KEY_SUCCESS)) == 1)){
                            if(Usuario.this.saveLogin(Usuario.this.activity, json))
                                ListenerRegisterUsuario.onRegisterFinish(json, "Registro correcto");
                            else
                                ListenerRegisterUsuario.onRegisterFail("Estás registrado pero no puedes loguearte ahora");
                        }else
                            ListenerRegisterUsuario.onRegisterFail("Error durante el registro");
                    } else {
                        ListenerRegisterUsuario.onRegisterFail("Error durante el registro");
                    }
                } catch (JSONException e) {
                    ListenerRegisterUsuario.onRegisterException(e, "Error durante el registro");
                }
            }

            @Override
            public void onErrorHttpPostAsyncListener(String message) {
            }
        });
        httpclient.executeHttpPost(registerURL);
    }

    private boolean saveLogin(Activity activity, JSONObject json){
        boolean save = false;
        this.logout(activity);
        try {
            JSONObject user = json.getJSONObject(KEY_USER);
            DBLogin dblogin = new DBLogin(activity.getApplicationContext());
            dblogin.addUser(user.getString(KEY_NAME), user.getString(KEY_EMAIL), json.getString(KEY_UID), user.getString(KEY_CREATED_AT));
            save = true;
        } catch (JSONException e) {}
        return save;
    }


    public void logout(Activity activity){
        DBLogin dblogin = new DBLogin(activity.getApplicationContext());
        dblogin.resetTables();
    }


    public boolean isUserLoggedIn(Activity activity){
        DBLogin db = new DBLogin(activity.getApplicationContext());
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }

    public void readUser(Activity activity){
        DBLogin dblogin = new DBLogin(activity.getApplicationContext());
        Cursor cursor = dblogin.getCursorUsuario();
        if(cursor.moveToNext()){
            this.setName(cursor.getString(0));
            this.setEmail(cursor.getString(1));
            this.setUID(cursor.getString(2));
        }
        cursor.close();
    }

    public interface OnRegisterUsuario{
        void onRegisterFinish(JSONObject json, String msg);
        void onRegisterException(Exception e, String msg);
        void onRegisterFail(String msg);
    }
    private OnRegisterUsuario ListenerRegisterUsuario;
    public void setOnRegisterUsuario(OnRegisterUsuario l){ListenerRegisterUsuario = l;}

    public interface OnLoginUsuario{
        void onLoginCorrect(JSONObject json, String msg);
        void onLoginWrong(String msg);
    }
    private OnLoginUsuario ListenerLoginUsuario;
    public void setOnLoginUsuario(OnLoginUsuario l){ListenerLoginUsuario = l;}

    public String getName() {return Name;}
    public void setName(String name) {Name = name;}
    public String getEmail() {return Email;}
    public void setEmail(String email) {Email = email;}
    public String getUID() {return UID;}
    public void setUID(String uID) {UID = uID;}

}