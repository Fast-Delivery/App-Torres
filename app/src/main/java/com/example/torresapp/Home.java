package com.example.torresapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    private TextView msg;

    private static final String TAG = com.example.torresapp.Principal.class.getSimpleName(); //Obtendo a informação

    SessionManager sessionManager;

    String getId;

    private static String URL_READ = "https://aaaaaaaaaabxbxbx.000webhostapp.com/App/logado.php";


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_dashboard:

                Intent intent = new Intent(Home.this, Lista.class);
                startActivity(intent);
                finish();
                break;

            case R.id.navigation_notifications: //error
                //launch activity
                break;

            case R.id.navigation_perfil:
                Intent i = new Intent(Home.this, PerfilActivity.class);
                startActivity(i);
                finish();

                break;

            case R.id.navigation_logout:
                sessionManager.logout();
                Toast.makeText(Home.this, "Logout com sucesso!", Toast.LENGTH_SHORT).show();

                break;

                 default:
                break;

        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();




        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);






    }



    //Obtendo dados
    private void getUserDetail(){

        final ProgressDialog progressDialog =  new ProgressDialog(this);
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if(success.equals("1")){

                                for (int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("name").trim();
                                    String strEmail = object.getString("email").trim();




                                    //txtname.setText(strName);
                                  //  TextView msg = findViewById(R.id.txtname);
                                    TextView text = (TextView)findViewById(R.id.txtname);
                                    text.setText(strName);
                                    TextView text1 = (TextView)findViewById(R.id.txtmail);
                                    text1.setText(strEmail);



                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            progressDialog.dismiss();
                            Toast.makeText(Home.this, "Não foi possível ver os detalhes."+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Toast.makeText(Home.this, "Não foi possível ver os detalhes 1."+error.toString(), Toast.LENGTH_SHORT).show();


                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params =  new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }


}

