package com.example.jodygryanapp.news;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtility {
    private Activity activity;

    public ImageUtility(Activity activity){
        this.activity = activity;
    }

    public Bitmap grabImage(String imagefile){
        FileInputStream fis = null;
        try {    fis = activity.openFileInput(imagefile);   }
        catch (FileNotFoundException e) {    e.printStackTrace();  }
        return BitmapFactory.decodeStream(fis);
    }

    public boolean fileExists(String fname){
        File file = activity.getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    public Bitmap downloadImage(String fname, String urlToImage) {
        Bitmap image = null;
        try {
            URL url = new URL(urlToImage);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                image = BitmapFactory.decodeStream(connection.getInputStream());
                //saveImage(fname, image);
            }
        }catch (MalformedURLException mfe){
            Log.d("DownloadException", "Malformed URL exception");
        }
        catch(IOException ioe){Log.d("DownloadException", "IO exception");}
        return image;
    }

    public Bitmap saveImage(String fname, Bitmap image){
        try {
            FileOutputStream outputStream = activity.openFileOutput(fname, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
        }catch(FileNotFoundException e){e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}
        return image;
    }
}
