package com.example.torresapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class Modal<onRequestPermissio> extends AppCompatActivity {


    SessionManager sessionManager;
    String getId;
    private Button btn_logout, btn_regist;
    private ImageView TirarFoto, Escolher, Imagens;
    private final int GALERIA_DE_IMAGENS = 1;
    private final int PERMISSAO_REQUEST = 2;
    private Bitmap bitmap;
    private static String URL_READ = "https://fastdeliveryadm.000webhostapp.com/App/Ler_dados.php";
    private static String URL_EDIT = "https://fastdeliveryadm.000webhostapp.com/App/Edit_dados.php";
    private static String URL_UPLOAD = "https://fastdeliveryadm.000webhostapp.com/App/Upload.php";
    private static final String TAG = PerfilActivity.class.getSimpleName(); //Obtendo a informação


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modal);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }

        btn_logout = findViewById(R.id.btn_logout);
        btn_regist = findViewById(R.id.btn_regist);
        TirarFoto = findViewById(R.id.TirarFoto);
        Escolher = findViewById(R.id.Escolher);
        Imagens = (ImageView) findViewById(R.id.Imagens);


        sessionManager = new SessionManager(this);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // EnviarFoto();

                Intent intent = new Intent(Modal.this, PerfilActivity.class);
                startActivity(intent);
                finish();



            }



        });


        Escolher.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALERIA_DE_IMAGENS);

            }
        });

        btn_regist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(Modal.this, "Não foi possivel enviar, está desenvolvimento...", Toast.LENGTH_SHORT).show();


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALERIA_DE_IMAGENS) {

            btn_regist.setVisibility(View.VISIBLE);
            Uri filePatha = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePatha);
                Imagens.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            Imagens.setImageBitmap(thumbnail);







        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSAO_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // A permissão foi concedida. Pode continuar
            } else {
                // A permissão foi negada. Precisa ver o que deve ser desabilitado
            }
            return;
        }
    }





   // private void EnviarFoto() {



    //    if(Imagens.getDrawable() != null){

            //Intent intent = new Intent(Intent.ACTION_SEND);
            //intent.setType("image/jpeg");
           // BitmapDrawable drawable =

     //   }else{

           // Toast.makeText(Modal.this, "Não existe Imagem a ser enviada!", Toast.LENGTH_SHORT).show();
     //       Toast.makeText(Modal.this, "Não foi possivel enviar, está desenvolvimento...", Toast.LENGTH_SHORT).show();

    //    }



      //  Intent intent = new Intent(Modal.this, PerfilActivity.class);
      //  startActivity(intent);
      //  finish();






    }





//}


