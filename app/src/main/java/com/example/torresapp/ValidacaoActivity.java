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

public class ValidacaoActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView link_reenviar;
    private Button btn_login;
    SessionManager sessionManager;
    private ProgressBar loading;
    private static String URL_VERIFICAR = "https://fastdeliveryadm.000webhostapp.com/App/Validacao.php";
    private static String URL_ENVIAR = "https://fastdeliveryadm.000webhostapp.com/App/Reenviando.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacao);



        sessionManager = new SessionManager(this);

        loading = findViewById(R.id.loading);
        email = findViewById(R.id.email);
        link_reenviar = findViewById(R.id.link_reenviar);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_enviar);

        Intent i = getIntent();
        String msg = i.getStringExtra("email");
        email.setText(msg);

        link_reenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                if (!mEmail.isEmpty()) {

                    Reenviar(mEmail);

                } else {
                    email.setError("Insira o seu E-mail! ");
                    password.setError("Insira o sua Senha! ");
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                String mPassword = password.getText().toString().trim();

                if (mEmail.isEmpty() || mPassword.isEmpty()) {

                    email.setError("Insira o seu E-mail! ");
                    password.setError("Insira o sua senha! ");

                }else if(mEmail.isEmpty()){
                    email.setError("Insira o seu E-mail! ");

                }else if(mPassword.isEmpty()){

                    password.setError("Insira o sua senha! ");


                }else if(mEmail.isEmpty()){

                    email.setError("Insira o seu E-mail! ");

                } else {

                    Login(mEmail, mPassword);

                }
            }
        });

    }



    private void Login(final String email, final String password ) {

        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VERIFICAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if(success.equals("1")){

                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String id = object.getString("id").trim();


                                    /**Toast.makeText(MainActivity.this,
                                     "Logado com sucesso. \nYour Name:"
                                     +name+"\nYour Email :"
                                     +email, Toast.LENGTH_SHORT)
                                     .show();**/

                                    sessionManager.createSession(name, email, id);

                                    Intent intent = new Intent(ValidacaoActivity.this, Home.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    startActivity(intent);
                                    finish();

                                    loading.setVisibility(View.GONE);

                                    Toast.makeText(ValidacaoActivity.this, "Logado com sucesso!", Toast.LENGTH_SHORT).show();


                                }




                            }else if(success.equals("2")){

                                //Password ou email errados

                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(ValidacaoActivity.this, "E-mail ou Password Incorreta!!", Toast.LENGTH_SHORT).show();


                            }else if(success.equals("3")) {

                                //Servidores Off
                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(ValidacaoActivity.this, "Servidores Desativados!!", Toast.LENGTH_SHORT).show();


                            }else if(success.equals("4")) {

                                //Atualização do Aplicativo

                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(ValidacaoActivity.this, "Necessário Atualizar o Aplicativo!!" , Toast.LENGTH_SHORT).show();
                            }else if(success.equals("8")) {

                                //Atualização do Aplicativo

                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(ValidacaoActivity.this, "Campo de E-mail vazio!!" , Toast.LENGTH_SHORT).show();

                            }else if(success.equals("9")) {

                                //Atualização do Aplicativo

                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(ValidacaoActivity.this, "Campo de Password vazio!!" , Toast.LENGTH_SHORT).show();

                            }

                        }catch (JSONException e ){
                            //Error 0


                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(ValidacaoActivity.this, "Não foi possível entrar, tente de novo!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(ValidacaoActivity.this, "Falha na conexão!", Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password",password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void Reenviar(final String email) {
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ENVIAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {

                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);

                        Toast.makeText(ValidacaoActivity.this, "Enviado com sucesso!", Toast.LENGTH_SHORT).show();

                    } else if (success.equals("2")) {

                        Toast.makeText(ValidacaoActivity.this, "Não foi possivel de enviar!", Toast.LENGTH_SHORT).show();


                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);

                    } else if (success.equals("3")) {
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);

                        Toast.makeText(ValidacaoActivity.this, "Senhas não estão iguais!", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ValidacaoActivity.this, "Erro400" + e.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);


                }

            }


        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ValidacaoActivity.this, "Falha de conexão!", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);


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

}


