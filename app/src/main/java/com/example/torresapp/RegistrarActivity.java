package com.example.torresapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrarActivity extends AppCompatActivity {

    private EditText name, email, password, c_password;
    private Button btn_regist;
    private ProgressBar loading;
    private static String URL_RESGIT = "https://aaaaaaaaaabxbxbx.000webhostapp.com/App/index.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        btn_regist = findViewById(R.id.btn_regist);

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Regist();


            }
        });

    }

    private void Regist() {
        loading.setVisibility(View.VISIBLE);
        btn_regist.setVisibility(View.GONE);
        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        final String c_password = this.c_password.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESGIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                  //  JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equals("1")) {

                        loading.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.VISIBLE);

                        Toast.makeText(RegistrarActivity.this, "Registrado com sucesso!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegistrarActivity.this, ValidacaoActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);

                    } else if (success.equals("2")) {

                        Toast.makeText(RegistrarActivity.this, "E-mail já existe. Tente outro!", Toast.LENGTH_SHORT).show();


                        loading.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.VISIBLE);

                    } else if (success.equals("3")) {
                        loading.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.VISIBLE);

                        Toast.makeText(RegistrarActivity.this, "Senhas não estão iguais!", Toast.LENGTH_SHORT).show();

                    }else if(success.equals("4")) {

                        //Servidores Off
                        loading.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.VISIBLE);

                        Toast.makeText(RegistrarActivity.this, "Servidores Desativados!!", Toast.LENGTH_SHORT).show();

                    }else if(success.equals("5")) {

                        //Servidores Off
                        loading.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.VISIBLE);

                        Toast.makeText(RegistrarActivity.this, "Preencher o E-mail.", Toast.LENGTH_SHORT).show();

                    }else if(success.equals("6")) {

                        //Servidores Off
                        loading.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.VISIBLE);

                        Toast.makeText(RegistrarActivity.this, "Preencher o Nome.", Toast.LENGTH_SHORT).show();



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegistrarActivity.this, "Erro400", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btn_regist.setVisibility(View.VISIBLE);


                }

            }


        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(RegistrarActivity.this, "Falha de conexão!", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.VISIBLE);


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("c_password", c_password);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

