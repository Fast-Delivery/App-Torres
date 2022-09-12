package com.example.torresapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class Lista extends AppCompatActivity {

    String urladdress = "https://fastdeliveryadm.000webhostapp.com/App/Detalhes/Visualizar.php";
    String[] name;
    String[] email;
    String[] imagepath;
    ListView listView;
    BufferedInputStream is;
    String line = null;
    String result = null;

    SessionManager sessionManager;

    String getId;
    private static String URL_READ = "https://fastdeliveryadm.000webhostapp.com/App/Ler_dados.php";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {





        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(Lista.this, Home.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_dashboard:




                    return true;
                case R.id.navigation_notifications:


                    return true;


                case R.id.navigation_perfil:

                    Intent i = new Intent(Lista.this, PerfilActivity.class);
                    startActivity(i);
                    finish();
                    return true;

                case R.id.navigation_logout:


                    sessionManager.logout();
                    Toast.makeText(Lista.this, "Logout com sucesso!", Toast.LENGTH_SHORT).show();


                    return true;

            }


            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);


        listView = (ListView) findViewById(R.id.lview);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();


        CustomListView customListView = new CustomListView(this, name, email, imagepath);
        listView.setAdapter(customListView);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }




    private void collectData() {
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


    }





