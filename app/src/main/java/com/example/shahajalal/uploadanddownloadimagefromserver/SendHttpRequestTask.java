package com.example.shahajalal.uploadanddownloadimagefromserver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {

    Context ctx;
    SendHttpRequestTask(Context ctx){
        this.ctx=ctx;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL("http://shahajalal.com/dev/test/upload/amr.jpg");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(result!=null){
            Log.d(TAG, "onPostExecute: yes successful");
        }
        //ImageView imageView = (ImageView) findViewById(ID OF YOUR IMAGE VIEW);
        //imageView.setImageBitmap(result);
    }
}
