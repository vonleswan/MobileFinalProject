package com.example.jodygryanapp.news;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import androidx.fragment.app.ListFragment;

import com.example.jodygryanapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FeedListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private String query;
    private String search = "https://jodygryan.com/wp-json/wp/v2/posts";
    private FeedListAdapter adapter;
    private ProgressBar progressBar;
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feedlist, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        query = sharedPref.contains("lastQuery") ?
                sharedPref.getString("lastQuery", "") : "bitcoin";
        adapter = new FeedListAdapter(getContext(),new ArrayList<Article>());
        setListAdapter(adapter);
        progressBar = activity.findViewById(R.id.progress);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        getListView().setOnItemClickListener(this);
        //init Database
        dataBaseHelper = new DataBaseHelper(activity);
        db = dataBaseHelper.getWritableDatabase();
        new Requests().execute(search+query);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        new ArticleDialog().show(activity, (Article) adapter.getItem(position), db);
    }

    public void updateList(String in){
        this.query = in.trim();
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lastQuery", query);
        editor.apply();
        this.adapter = new FeedListAdapter(getContext(), new ArrayList<Article>());
        setListAdapter(adapter);
        new Requests().execute(search+query);
    }

    private void notifyChange(){
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }catch (NullPointerException e){e.printStackTrace();}
    }



    private class Requests extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            String search = urls[0];
            JSONObject jObject = null;
            String ret;
            try {
                URL url = new URL(search);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                buildArticles(new JSONObject(result));
                ret = "article build successful";
            }catch (IOException | JSONException e){
                e.printStackTrace();
                ret = "article build NOT successful";
            }
            return ret;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("ONPOSTRESULT", result);
        }

        @Override
        protected void onProgressUpdate(Integer... progress){
            progressBar.setProgress(progress[0]);
        }


        public void buildArticles(JSONObject jObject){
            String author, title, description, url, urlToImage;
            Bitmap image;
            ArrayList<Article> articles = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>(); //Contains loaded titles for duplicate checking
            try {
                JSONArray arr = jObject.getJSONArray("articles");
                int lenArr = arr.length();
                for(int i=0; i<lenArr;i++){
                    JSONObject article = arr.getJSONObject(i);
                    title = article.getString("title");
                    if(titles.contains(title)) continue; // Skips building article if duplicate
                    author = article.getString("author");
                    description = article.getString("description");
                    url = article.getString("url");
                    urlToImage = article.getString("urlToImage");

                    //  Image handling
                    ImageUtility imageUtility = new ImageUtility(activity);
                    String fname = (author + title.substring(0,5)).replaceAll("/","");
                    image = imageUtility.fileExists(fname) ?
                            imageUtility.grabImage(fname) :
                            imageUtility.downloadImage(fname,urlToImage);
                    if(image != null) {
                        titles.add(title);
                        if (!imageUtility.fileExists(fname)) imageUtility.saveImage(fname, image);
                        adapter.getArticles().add(new Article(title, author, description, url, urlToImage, image));
                    }
                    publishProgress(i*10);
                }
            }catch (JSONException e){e.printStackTrace();}
            notifyChange();
        }


    }


}
