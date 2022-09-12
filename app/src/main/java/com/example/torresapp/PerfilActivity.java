package com.example.torresapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class PerfilActivity extends AppCompatActivity {

    SessionManager sessionManager;
    String getId;

    private static final String TAG = PerfilActivity.class.getSimpleName(); //Obtendo a informação
    private TextView name, email;
    private Button btn_logout, btn_photo_upload;
    private static String URL_READ = "https://fastdeliveryadm.000webhostapp.com/App/Ler_dados.php";
    private static String URL_EDIT = "https://fastdeliveryadm.000webhostapp.com/App/Edit_dados.php";
    private static String URL_UPLOAD = "https://fastdeliveryadm.000webhostapp.com/App/Upload.php";
    private Menu action;
    private Bitmap bitmap;
    CircleImageView profile_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        Intent i = getIntent();











        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        btn_logout = findViewById(R.id.btn_logout);
        btn_photo_upload = findViewById(R.id.btn_photo_upload);
        profile_image = findViewById(R.id.profile_image);


        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);



        btn_photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           chooseFile();

            }

        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PerfilActivity.this, Home.class);
                startActivity(intent);
                finish();

            }

        });
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

                                    name.setText(strName);
                                    email.setText(strEmail);

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            progressDialog.dismiss();
                            Toast.makeText(PerfilActivity.this, "Não foi possível ver os detalhes."+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Toast.makeText(PerfilActivity.this, "Não foi possível ver os detalhes 1."+error.toString(), Toast.LENGTH_SHORT).show();


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action, menu);

        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_edit:

                name.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;

            case R.id.menu_save:

                SaveEditDetail();

                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);

                name.setFocusableInTouchMode(false);
                email.setFocusableInTouchMode(false);
                name.setFocusable(false);
                email.setFocusable(false);

                return true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }

    //Salvando
    private void SaveEditDetail() {

        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Salvando..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(PerfilActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(name, email, id);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(PerfilActivity.this, "Erro de salvamento..", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(PerfilActivity.this, "Falha na conexao..", Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void chooseFile(){

        Intent intent = new Intent(PerfilActivity.this, Modal.class);
        startActivity(intent);
        finish();





        //Intent i = new  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(i, 1);

        //Intent i = new  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
     //   startActivityForResult(Intent.createChooser(i,
           //     "Select Picture"), CAMERA );


   //     Intent i = new   Intent(Intent.ACTION_PICK,
      //          android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

      //  i.setType("Imagens/*");
     //   startActivityForResult(Intent.createChooser(i,"Selecione a Imagem"), 1);


        // TIRAR FOTO

       // Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      // if (takePictureIntent.resolveActivity(getPackageManager()) != null){
     //       startActivityForResult(takePictureIntent, CAMERA);
     //   }

        //--------------------------------------------------------------------//

        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent,
                //Select Picture//), CAMERA );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == CAMERA && data != null && data.getData() != null){
            Uri filePath = data.getData();

            try {
                bitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }






    private void UploadPicture(final String id, final String photo) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(PerfilActivity.this, "Enviado com Sucesso!", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(PerfilActivity.this, "Falha!" +e.toString() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(PerfilActivity.this, "Impossivel!" +error.toString(), Toast.LENGTH_SHORT).show();


                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("photo", photo);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }
}
