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

public class MainActivity extends AppCompatActivity {


        private EditText email;
        private EditText password;
        private Button btn_login;
        private TextView link_regist, link_recuperar;
        private ProgressBar loading;
        private static String URL_LOGIN = "https://aaaaaaaaaabxbxbx.000webhostapp.com/App/login.php";
        SessionManager sessionManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            sessionManager = new SessionManager(this);

            loading = findViewById(R.id.loading);
            email = findViewById(R.id.email);
            password = findViewById(R.id.password);
            btn_login = findViewById(R.id.btn_enviar);
            link_regist = findViewById(R.id.link_regist);
            link_recuperar = findViewById(R.id.link_recuperar);


            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mEmail = email.getText().toString().trim();
                    String mPassword = password.getText().toString().trim();


                    if (!mEmail.isEmpty() || !mPassword.isEmpty()) {
                        Login(mEmail, mPassword);

                    } else {
                        email.setError("Insira o seu E-mail! ");
                        password.setError("Insira o sua Senha! ");
                    }
                }
            });

            link_regist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, RegistrarActivity.class));
                }
            });

            link_recuperar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, Recuperar_SenhaActivity.class));
                }
            });

        }



        private void Login(final String email, final String password ) {

            loading.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
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

                                        Intent intent = new Intent(MainActivity.this, Home.class);
                                        intent.putExtra("id", id);
                                        intent.putExtra("name", name);
                                        intent.putExtra("email", email);
                                        startActivity(intent);
                                        finish();

                                        loading.setVisibility(View.GONE);

                                        Toast.makeText(MainActivity.this, "Logado com sucesso!", Toast.LENGTH_SHORT).show();


                                    }




                                }else if(success.equals("2")){

                                    //Password ou email errados

                                    loading.setVisibility(View.GONE);
                                    btn_login.setVisibility(View.VISIBLE);
                                    Toast.makeText(MainActivity.this, "Usuario não encontrado!!", Toast.LENGTH_SHORT).show();

                                }else if(success.equals("3")){

                                    //Password ou email errados

                                    loading.setVisibility(View.GONE);
                                    btn_login.setVisibility(View.VISIBLE);
                                    Toast.makeText(MainActivity.this, "Password Incorreta!!", Toast.LENGTH_SHORT).show();


                                   }else if(success.equals("4")) {

                                    //Servidores Off
                                    loading.setVisibility(View.GONE);
                                    btn_login.setVisibility(View.VISIBLE);
                                    Toast.makeText(MainActivity.this, "Servidores Desativados!!", Toast.LENGTH_SHORT).show();


                                }else if(success.equals("5")) {

                                    //Atualização do Aplicativo

                                    loading.setVisibility(View.GONE);
                                    btn_login.setVisibility(View.VISIBLE);
                                    Toast.makeText(MainActivity.this, "Necessário Atualizar o Aplicativo!!" , Toast.LENGTH_SHORT).show();


                                }else if(success.equals("6")) {

                                    //Atualização do Aplicativo

                                    loading.setVisibility(View.GONE);
                                    btn_login.setVisibility(View.VISIBLE);
                                    Toast.makeText(MainActivity.this, "Usuário Bloqueado!!" , Toast.LENGTH_SHORT).show();

                                }else if(success.equals("7")) {

                                    //Atualização do Aplicativo

                                    loading.setVisibility(View.GONE);
                                    btn_login.setVisibility(View.VISIBLE);
                                    Toast.makeText(MainActivity.this, "Necessário confirmar E-mail!!" , Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(MainActivity.this, ValidacaoActivity.class);
                                    intent.putExtra("email", email);
                                    startActivity(intent);


                                }

                            }catch (JSONException e ){
                                //Error 0


                                e.printStackTrace();
                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this, "Não foi possível entrar, tente de novo!" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, "Falha na conexão!", Toast.LENGTH_SHORT).show();

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
    }
