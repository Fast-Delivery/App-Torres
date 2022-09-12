package com.example.torresapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity {
    private TextView mTextMessage;

    private static final String TAG = com.example.torresapp.Principal.class.getSimpleName(); //Obtendo a informação

    SessionManager sessionManager;

    String getId;
    private static String URL_READ = "https://aaaaaaaaaabxbxbx.000webhostapp.com/App/login.php";

    String urladdress = "https://fastdeliveryadm.000webhostapp.com/App/Detalhes/Visualizar.php";
    String[] name;
    String[] email;
    String[] imagepath;
    ListView listView;
    BufferedInputStream is;
    String line = null;
    String result = null;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {




        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);

                    return true;

                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);



                    return true;

                case R.id.navigation_perfil:

                    Intent intent = new Intent(Principal.this, PerfilActivity.class);
                    startActivity(intent);
                    finish();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        listView = (ListView) findViewById(R.id.lview);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        CustomListView customListView = new CustomListView(this, name, email, imagepath);
        listView.setAdapter(customListView);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);





        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.txtname);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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


                                    mTextMessage.setText(strName);



                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            progressDialog.dismiss();
                            Toast.makeText(Principal.this, "Não foi possível ver os detalhes."+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Toast.makeText(Principal.this, "Não foi possível ver os detalhes 1."+error.toString(), Toast.LENGTH_SHORT).show();


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



    //List View//


    private void collectData()
    {
//Connection
        try{

            URL url=new URL(urladdress);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        //content
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();
            while ((line=br.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            result=sb.toString();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }

//JSON
        try{
            JSONArray ja=new JSONArray(result);
            JSONObject jo = null;
            name=new String[ja.length()];
            email=new String[ja.length()];
            imagepath=new String[ja.length()];

            for(int i=0;i<=ja.length();i++){
                jo=ja.getJSONObject(i);
                name[i] = jo.getString("name");
                email[i] = jo.getString("email");
                imagepath[i] = jo.getString("photo");
            }
        }
        catch (Exception ex)
        {

            ex.printStackTrace();
        }


    }
}




