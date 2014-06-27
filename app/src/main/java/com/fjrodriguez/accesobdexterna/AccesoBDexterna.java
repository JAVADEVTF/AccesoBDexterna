package com.fjrodriguez.accesobdexterna;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class AccesoBDexterna extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso_bdexterna);
        StrictMode.enableDefaults();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.acceso_bdexterna, menu);
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
        return super.onOptionsItemSelected(item);
    }

    public void conectar (View view) {
        InputStream is = conectarhttp();
        String json = stream(is);
        String resultado = obtenerdejson(json);
        ((TextView) findViewById(R.id.textView)).setText(resultado);
    }

    private String obtenerdejson(String json) {
        String s = "";
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                s += " codigo: " + jsonObject.getString("codigo")+"\n";
                s += " descripcion: " + jsonObject.getString("descripcion")+"\n";
                s += " precio: " + String.valueOf(jsonObject.getDouble("precio"))+"\n";
                s += " origen: " + jsonObject.getString("origen")+"\n\n";
            }
        } catch (Exception e) {

        }

        return s;
    }

    private InputStream conectarhttp() {
        InputStream is = null;

        try {
            // define un cliente http.
            HttpClient httpClient = new DefaultHttpClient();
            // prepara un envio por el método post.
            HttpPost httpPost = new HttpPost("http://192.168.1.141/datos.php");
            // obtiene la respuesta del servidor.
            HttpResponse httpResponse = httpClient.execute(httpPost);
            // obtener contenido
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (Exception e) {
            is = null;
            Log.d("error", e.getMessage());
        }
        return is;
    }

    private String stream(InputStream is) {
        String result = "";
        String linea;

        try {
            InputStreamReader isr = new InputStreamReader(is, "iso-8859-1");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder stringBuilder = new StringBuilder();
            while ((linea = bufferedReader.readLine()) != null) {
                stringBuilder.append(linea);
            }
            isr.close();
            result = stringBuilder.toString();
        } catch (Exception e) {

        }
        return result;
    }
}
