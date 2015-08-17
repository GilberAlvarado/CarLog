package com.carlog.gilberto.carlog.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.formats.sharedPreferencesUtils;
import com.carlog.gilberto.carlog.formats.utilities;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.facebook.LoginActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.gc.materialdesign.views.ButtonRectangle;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Gilberto on 24/07/2015.
 */
public class login extends Activity {

    private TextView lblGotoRegister;
    private ButtonRectangle btnLogin;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView loginErrorMsg;

    public void openFacebookSession(final Activity activity, final Class goTo){
        // set the permissions in the third parameter of the call
        String[] perm = {"public_profile", "user_friends"};
        List permissions = Arrays.asList(perm);
        openActiveSessionFacebook(activity, true, permissions, new Session.StatusCallback() {
            @Override
            public void call(final Session session, SessionState state, Exception exception) {
            if (exception != null) {
                Log.d("Facebook", exception.getMessage());
            }
            Log.d("Facebook", "Session State: " + session.getState());
            // you can make request to the /me API or do other stuff like post, etc. here
            if (state.equals(SessionState.OPENED)) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Request.newMeRequest(session, new Request.GraphUserCallback() {

                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                // Display the parsed user info
                                try {
                                    Intent intent = new Intent(activity, goTo);
                                    // System.out.println("DATOS FB"+ user.getId()+ " "+ user.getName()+ " "+user.asMap().get("email").toString()); //user.getProperty("email")
                                    saveParamsFacebook(activity, user.getId(), user.getName());
                                    usuario usuario = new com.carlog.gilberto.carlog.tiposClases.usuario();
                                    usuario.login(login.this, user.getId(), "PASS_IGNORE_FB", true);
                                    registerUsuarioFacebook(user.getName(), user.getId());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    activity.startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.getStackTrace();
                                }
                            }

                        } else {
                            if (response.getError() != null) {
                                // Handle errors, will do so later.
                            }
                        }
                    }
                }).executeAndWait();
            }
            }
        });
    }


    private static Session openActiveSessionFacebook(Activity activity, boolean allowLoginUI, List permissions, Session.StatusCallback callback) {
        Session.OpenRequest openRequest = new Session.OpenRequest(activity).setPermissions(permissions).setCallback(callback);
        Session session = new Session.Builder(activity).build();
        if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
            Session.setActiveSession(session);
            session.openForRead(openRequest);
            return session;
        }
        return null;
    }

    public static void saveParamsFacebook (final Activity activity, final String idFacebook, String name){
        sharedPreferencesUtils.addString(activity, activity.getString(R.string.sp_fb_id), idFacebook);
        sharedPreferencesUtils.addString(activity, activity.getString(R.string.sp_fb_name), name);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
            try {
                URL imageURL = new URL("https://graph.facebook.com/" + idFacebook + "/picture?type=large");
                Bitmap imgProfile = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                if (imgProfile != null){
                    String imgProfileStr = utilities.ImageToBase64(imgProfile);
                    if (imgProfileStr != null){
                        sharedPreferencesUtils.addString(activity, activity.getString(R.string.sp_fb_img_profile), imgProfileStr);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        });
        thread.start();
    }

    public static void saveParamsAnonimo (final Activity activity){
        sharedPreferencesUtils.addString(activity, activity.getString(R.string.sp_fb_id), "a@a.es");
        sharedPreferencesUtils.addString(activity, activity.getString(R.string.sp_fb_name), "Anónimoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
    }

    public static void deleteParamsFacebook (Activity activity){
        sharedPreferencesUtils.addString(activity, activity.getString(R.string.sp_fb_id), null);
        sharedPreferencesUtils.addString(activity, activity.getString(R.string.sp_fb_name), null);
        sharedPreferencesUtils.addString(activity, activity.getString(R.string.sp_fb_img_profile), null);
    }

    public static void deleteParamsAnonimo (Activity activity){
        sharedPreferencesUtils.addString(activity, activity.getString(R.string.sp_fb_id), null);
        sharedPreferencesUtils.addString(activity, activity.getString(R.string.sp_fb_name), null);
    }

    public static String getNameAnonimo (Activity activity){
        return sharedPreferencesUtils.getString(activity, activity.getString(R.string.sp_fb_name));
    }

    public static String getIdFacebook (Activity activity){
        return sharedPreferencesUtils.getString(activity, activity.getString(R.string.sp_fb_id));
    }

    public static String getNameFacebook (Activity activity){
        return sharedPreferencesUtils.getString(activity, activity.getString(R.string.sp_fb_name));
    }

    public static Bitmap getImgProfileFacebook (Activity activity){
        String imgProfileStr = sharedPreferencesUtils.getString(activity, activity.getString(R.string.sp_fb_img_profile));
        if (imgProfileStr != null){
            return utilities.Base64ToImage(imgProfileStr);
        }
        return null;
    }
    public static void closeFacebookSession(final Activity activity, final Class goTo){
     /*   Session session = Session.getActiveSession();
        session.closeAndClearTokenInformation();
        Intent intent = new Intent(activity, goTo);
        deleteParamsFacebook(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);*/
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                Intent intent = new Intent(activity, goTo);
                deleteParamsFacebook(activity);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                usuario u = new usuario();
                u.logout(activity);
                //clear your preferences if saved
            }
        } else {
            session = new Session(activity.getApplicationContext());
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
            Intent intent = new Intent(activity, goTo);
            deleteParamsFacebook(activity);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            usuario u = new usuario();
            u.logout(activity);
            //clear your preferences if saved
        }
    }

    public static void goToLoginScreen (Activity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static boolean checkLogin (final Activity activity){
        if (getIdFacebook(activity) != null){
            return true;
        }

        final CharSequence[] items = { activity.getString(R.string.login), activity.getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.login_required));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
            if (items[item].equals(activity.getString(R.string.login))) {
                goToLoginScreen(activity);
            } else if (items[item].equals(activity.getString(R.string.cancel))) {
                dialog.dismiss();
            }
            }
        });
        builder.show();
        return false;
    }


    public void loginfacebook() {
        ButtonRectangle loginBtn = (ButtonRectangle) findViewById(R.id.btnLoginFacebook);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookSession(login.this, myActivity.class);
            }
        });
    }

    public void loginAnonimo() {
        ButtonRectangle loginBtn = (ButtonRectangle) findViewById(R.id.btnLoginAnonimo);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            saveParamsAnonimo(login.this);
            usuario u = new usuario();
            u.login(login.this, "a@a.es", "PASS_IGNORE_FB", true);
            Intent i = new Intent(login.this, myActivity.class);
            i.putExtra("anonimo", true);
            login.this.startActivity(i);
            finish();
            //registerUsuarioFacebook("Anonimo", "a@a.es");
            }
        });

    }

  /*  public void loginUsuario() {
        inputEmail = (EditText) findViewById(R.id.txtEmail);
        inputPassword = (EditText) findViewById(R.id.txtPass);
      //  btnLogin = (ButtonRectangle) findViewById(R.id.btnLogin);
      //  loginErrorMsg = (TextView) findViewById(R.id.login_error);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                usuario usuario = new com.carlog.gilberto.carlog.tiposClases.usuario();

                usuario.setOnLoginUsuario(new com.carlog.gilberto.carlog.tiposClases.usuario.OnLoginUsuario() {
                    @Override
                    public void onLoginWrong(String msg) {loginErrorMsg.setText(msg);}
                    @Override
                    public void onLoginCorrect(JSONObject json, String msg) {
                        loginErrorMsg.setText("");
                        Intent itemintent = new Intent(login.this, myActivity.class);
                        login.this.startActivity(itemintent);
                        finish();
                    }
                });
                usuario.login(login.this, email, password, false);
            }
        });

        lblGotoRegister = (TextView) findViewById(R.id.link_to_register);
        lblGotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemintent = new Intent(login.this, register.class);
                login.this.startActivity(itemintent);
            }
        });
    }*/



    public void registerUsuarioFacebook(String name , String id) {
        usuario u = new usuario();
        u.register(login.this, name, id, "PASS_IGNORE_FB", true);
        Intent itemintent = new Intent(login.this, myActivity.class);
        login.this.startActivity(itemintent);
        finish();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginfacebook();
        loginAnonimo();
       // loginUsuario();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
}