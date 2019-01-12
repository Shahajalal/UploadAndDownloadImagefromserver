package com.example.shahajalal.uploadanddownloadimagefromserver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button uploadbtn,choosebtn,download;
    private EditText name;
    public ImageView imageView,imageView1;
    private  static  final int IMG_REQUEST=1;
    private Bitmap bitmap;
    private String URL="http://shahajalal.com/dev/test/api.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadbtn=findViewById(R.id.uploadbtn);
        choosebtn=findViewById(R.id.choosebtn);
        download=findViewById(R.id.uploadbtn1);
        name=findViewById(R.id.name);
        imageView1=findViewById(R.id.igageviewid1);
        imageView=findViewById(R.id.igageviewid);
        uploadbtn.setOnClickListener(this);
        choosebtn.setOnClickListener(this);
        download.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.uploadbtn){
            uploadimg();
        }else if(v.getId()==R.id.uploadbtn1){
            Toast.makeText(MainActivity.this,"DOwnload",Toast.LENGTH_SHORT).show();

            Glide
                    .with(getApplicationContext()) // replace with 'this' if it's in activity
                    .load("http://shahajalal.com/dev/test/upload/big.jpg")
                    .into(imageView1);
        }else{
            selectimg();
        }
    }

    public void selectimg(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadimg(){
        Toast.makeText(MainActivity.this,"upload calling",Toast.LENGTH_SHORT).show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String Response=jsonObject.getString("response");
                    Toast.makeText(MainActivity.this,Response,Toast.LENGTH_SHORT).show();
                    imageView.setImageResource(0);
                    imageView.setVisibility(View.GONE);
                    name.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map=new HashMap<>();
                map.put("name",name.getText().toString().trim());
                map.put("image",imagetostring(bitmap));
                return map;
            }
        };

        MySingleTon.getInstance(MainActivity.this).addToRequestQue(stringRequest);
}

    private String imagetostring(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);
        byte[] imgbyte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte,Base64.DEFAULT);
    }


    }







