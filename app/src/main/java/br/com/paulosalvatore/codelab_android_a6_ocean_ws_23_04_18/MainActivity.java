package br.com.paulosalvatore.codelab_android_a6_ocean_ws_23_04_18;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    private TextView tvTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTexto = findViewById(R.id.tvTexto);
    }

    private RequestQueue getQueue(Context c) {
        if (queue == null) {
            queue = Volley.newRequestQueue(c.getApplicationContext());
        }

        return queue;
    }

    public void buscarDados(View view) {
        queue = getQueue(this);

        Toast.makeText(this, "Carregando requisição...", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=1a820804691945ee81cb42d64dab5f99",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objeto = new JSONObject(response);
                            Previsao previsao = new Previsao();

                            JSONObject main = objeto.getJSONObject("main");
                            double temperatura = main.getDouble("temp");
                            previsao.setTemperatura(temperatura);

                            JSONArray weather = objeto.getJSONArray("weather");
                            JSONObject firstWeather = weather.getJSONObject(0);
                            String condicao = firstWeather.getString("description");
                            previsao.setCondicao(condicao);

                            tvTexto.setText(
                                    "Temperatura: " + previsao.getTemperatura() + "\n\n" +
                                    "Condição: " + previsao.getCondicao()
                            );

                            Toast.makeText(MainActivity.this, "Informações carregadas com sucesso.", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Algum erro ocorreu...", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Algum erro ocorreu...", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(stringRequest);
    }
}
