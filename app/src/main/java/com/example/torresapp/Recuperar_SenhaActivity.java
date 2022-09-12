package com.example.torresapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class Recuperar_SenhaActivity extends AppCompatActivity {


    private ProgressBar loading;
    private EditText email, password;
    private TextView link_Login;
    SessionManager sessionManager;
    private static String URL_VERIFICAR = "https://fastdeliveryadm.000webhostapp.com/App/Recuperar_Senha.php";
    private Button btn_enviar, btn_verificar;
    private static String URL_REENVIAR = "https://fastdeliveryadm.000webhostapp.com/App/Recuperar_Reenviar_Senha.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar__senha);

        sessionManager = new SessionManager(this);

         btn_verificar= findViewById(R.id.btn_verificar);
         btn_enviar = findViewById(R.id.btn_enviar);
         loading = findViewById(R.id.loading);

        link_Login = findViewById(R.id.link_Login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);


        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();

                if (!mEmail.isEmpty()) {
                    Login(mEmail);

                } else {
                    email.setError("Insira o seu E-mail! ");
                }
            }
        });


        btn_verificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                String mPassword = password.getText().toString().trim();

                if (!mEmail.isEmpty() || !mPassword.isEmpty()) {

                    Verificar(mEmail, mPassword);
                }else if(mEmail.isEmpty()) {
                    email.setError("Insira o seu E-mail! ");
                }else if(mPassword.isEmpty()){
                    password.setError("Campo vázio! ");
                } else {
                    email.setError("Insira o seu E-mail! ");
                    password.setError("Campo vázio! ");
                }
            }
        });


        link_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Recuperar_SenhaActivity.this, MainActivity.class));
            }
        });
    }

    private void Login(final String email) {

        loading.setVisibility(View.VISIBLE);
        btn_verificar.setVisibility(View.GONE);
        btn_enviar.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REENVIAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {

                        loading.setVisibility(View.GONE);

                        btn_enviar.setVisibility(View.VISIBLE);
                        password.setVisibility(View.VISIBLE);
                        btn_verificar.setVisibility(View.VISIBLE);


                        Toast.makeText(Recuperar_SenhaActivity.this, "Código de Verificação enviado com Sucesso!", Toast.LENGTH_SHORT).show();


                    } else if (success.equals("2")) {

                        Toast.makeText(Recuperar_SenhaActivity.this, "E-mail inválido!", Toast.LENGTH_SHORT).show();


                        loading.setVisibility(View.GONE);
                        btn_enviar.setVisibility(View.VISIBLE);

                    } else if (success.equals("3")) {
                        loading.setVisibility(View.GONE);
                        btn_enviar.setVisibility(View.VISIBLE);

                        Toast.makeText(Recuperar_SenhaActivity.this, "", Toast.LENGTH_SHORT).show();


                    }else if(success.equals("4")) {

                        //Servidores Off
                        loading.setVisibility(View.GONE);
                        btn_enviar.setVisibility(View.VISIBLE);
                        Toast.makeText(Recuperar_SenhaActivity.this, "Servidores Desativados!!", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Recuperar_SenhaActivity.this, "Erro400" + e.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btn_enviar.setVisibility(View.VISIBLE);


                }

            }


        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Recuperar_SenhaActivity.this, "Falha de conexão!", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_enviar.setVisibility(View.VISIBLE);


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void Verificar(final String email, final String password) {

        loading.setVisibility(View.VISIBLE);
        btn_verificar.setVisibility(View.GONE);
        btn_enviar.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VERIFICAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String id = object.getString("id").trim();


                                    /**Toast.makeText(MainActivity.this,
                                     "Logado com sucesso. \nYour Name:"
                                     +name+"\nYour Email :"
                                     +email, Toast.LENGTH_SHORT)
                                     .show();**/


                                    Toast.makeText(Recuperar_SenhaActivity.this, "Código de verificação, Válido!", Toast.LENGTH_SHORT).show();


                                    Intent intent = new Intent(Recuperar_SenhaActivity.this, Home.class);

                                    sessionManager.createSession(name, email, id);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    startActivity(intent);
                                    finish();

                                    loading.setVisibility(View.GONE);

                                    loading.setVisibility(View.GONE);
                                    btn_verificar.setVisibility(View.VISIBLE);
                                    btn_enviar.setVisibility(View.VISIBLE);

                                }
                            } else if (success.equals("2")) {

                                Toast.makeText(Recuperar_SenhaActivity.this, "Código de verificação vazio!", Toast.LENGTH_SHORT).show();


                                loading.setVisibility(View.GONE);
                                btn_verificar.setVisibility(View.VISIBLE);
                                btn_enviar.setVisibility(View.VISIBLE);

                            } else if (success.equals("3")) {

                                Toast.makeText(Recuperar_SenhaActivity.this, "Preencher o E-mail", Toast.LENGTH_SHORT).show();

                                loading.setVisibility(View.GONE);
                                btn_verificar.setVisibility(View.VISIBLE);
                                btn_enviar.setVisibility(View.VISIBLE);

                            }else if(success.equals("4")) {

                                //Servidores Off
                                loading.setVisibility(View.GONE);
                                btn_enviar.setVisibility(View.VISIBLE);
                                Toast.makeText(Recuperar_SenhaActivity.this, "Servidores Desativados!!", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btn_verificar.setVisibility(View.VISIBLE);
                            btn_enviar.setVisibility(View.VISIBLE);


                            Toast.makeText(Recuperar_SenhaActivity.this, "E-mail ou Código incorreto!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btn_verificar.setVisibility(View.VISIBLE);
                        btn_enviar.setVisibility(View.VISIBLE);

                        Toast.makeText(Recuperar_SenhaActivity.this, "Não foi possível, tente de novo!", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}