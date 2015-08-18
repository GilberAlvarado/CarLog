package com.carlog.gilberto.carlog.formats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.carlog.gilberto.carlog.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Gilberto on 24/07/2015.
 */
public class httpClientManager {

    private Activity activity;
    private ArrayList<NameValuePair> nameValuePairs;
    private String responseBody = "";

    private ProgressDialog m_ProgressDialog = null;
    private Runnable viewOrders;

    private String UrlService = "";

    private String strMessageHeadLoading = "Por favor espera";
    public String getStrMessageHeadLoading(){ return strMessageHeadLoading; }
    public void setStrMessageHeadLoading(String message){ strMessageHeadLoading = message; }

    private String strMessageBodyLoading  = "Conectando...";
    public String getStrMessageBodyLoading(){ return strMessageBodyLoading; }
    public void setStrMessageBodyLoading(String message){ strMessageBodyLoading = message; }

    private String getUrlService(){ return UrlService;}
    private void setUrlService(String UrlService){this.UrlService = UrlService;}

    public String getResponseBody(){return responseBody;}
    private void setResponseBody(String ResponseBody){responseBody = ResponseBody;}

    public httpClientManager(Activity activity){
        this.activity = activity;
        nameValuePairs = new ArrayList<NameValuePair>();
    }

    private boolean isInternetAllowed(Activity activity){
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni!=null && ni.isAvailable() && ni.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void addNameValue(String name, String value){
        nameValuePairs.add(new BasicNameValuePair(name,value));
    }

    public void executeHttpPost(String UrlService){
        setUrlService(UrlService);

        viewOrders = new Runnable(){
            public void run() {
                try {
                    executeHttpPostAsync(activity, getUrlService());
                } catch (Exception e) {}
            }
        };
        Thread thread =  new Thread(null, viewOrders, "Background");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(activity, getStrMessageHeadLoading(), getStrMessageBodyLoading(), true);

    }

    private void executeHttpPostAsync(Activity activity, String UrlService){
        if(isInternetAllowed(activity)){
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(UrlService);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                setResponseBody(httpclient.execute(httppost, responseHandler));

                activity.runOnUiThread(returnRes);
            }catch(HttpResponseException hre){
                ListenerExecuteHttpPostAsync.onErrorHttpPostAsyncListener(activity.getString(R.string.errorConectandoServer));
            }catch(Exception e){
                ListenerExecuteHttpPostAsync.onErrorHttpPostAsyncListener(activity.getString(R.string.errorConectando));
            }
        } else{
            ListenerExecuteHttpPostAsync.onErrorHttpPostAsyncListener(activity.getString(R.string.conexionNoPosible));
        }
    }

    private Runnable returnRes = new Runnable() {
        public void run() {
            m_ProgressDialog.dismiss();
            ListenerExecuteHttpPostAsync.onExecuteHttpPostAsyncListener(getResponseBody());
        }
    };

    public interface OnExecuteHttpPostAsyncListener{
        void onExecuteHttpPostAsyncListener(String ResponseBody);
        void onErrorHttpPostAsyncListener(String message);
    }

    private OnExecuteHttpPostAsyncListener ListenerExecuteHttpPostAsync;

    public void setOnExecuteHttpPostAsyncListener(OnExecuteHttpPostAsyncListener l){ListenerExecuteHttpPostAsync = l;}

}
