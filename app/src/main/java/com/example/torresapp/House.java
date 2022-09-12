package com.example.torresapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedInputStream;

import static com.example.torresapp.R.id.navigation_perfil;

public class House extends AppCompatActivity {

    String urladdress = "https://fastdeliveryadm.000webhostapp.com/App/Detalhes/Visualizar.php";
    String[] name;
    String[] email;
    String[] imagepath;
    ListView listView;
    BufferedInputStream is;
    String line = null;
    String result = null;
    MenuItem navigation_home, navigation_dashboard, navigation_notifications, navigation_perfil, navigation_logout;
    SessionManager sessionManager;

    String getId;
    private static String URL_READ = "https://fastdeliveryadm.000webhostapp.com/App/Ler_dados.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        sessionManager = new SessionManager(this);

        navigation_home = findViewById(R.id.navigation_home);
        navigation_dashboard = findViewById(R.id.navigation_dashboard);
        navigation_notifications = findViewById(R.id.navigation_notifications);
        navigation_perfil = findViewById(R.id.navigation_perfil);
        navigation_logout = findViewById(R.id.navigation_logout);


  //      if (R.id.navigation_perfil) {

        //    }else{
      //          Intent i = new Intent(this, HomeActivity.class);
      //          startActivity(i);
            }


    }

