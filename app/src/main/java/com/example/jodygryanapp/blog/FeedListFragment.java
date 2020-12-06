package com.example.jodygryanapp.blog;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spanned;
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
    private String search = "https://jodygryan.com/wp-json/wp/v2/posts?_embed";
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
        adapter = new FeedListAdapter(getContext(),new ArrayList<>());
        setListAdapter(adapter);
        progressBar = activity.findViewById(R.id.progress);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        getListView().setOnItemClickListener(this);
        //init Database
        dataBaseHelper = new DataBaseHelper(activity);
        db = dataBaseHelper.getWritableDatabase();
        new Requests().execute(search);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        new ArticleDialog().show(activity, (Article) adapter.getItem(position), db);
    }

    public void updateList(String in){
        this.adapter.getArticles().removeIf(article -> !article.getTitle().contains(in));
        setListAdapter(this.adapter);
        notifyChange();
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
                JSONArray result = new JSONArray(sb.toString());
                buildArticles(result);
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


        public void buildArticles(JSONArray jArr){
            String author;
            String title;
            Spanned description;
            String url;
            String urlToImage;
            String filename = "Jody RyanWhile";
            Bitmap image;
            ArrayList<String> titles = new ArrayList<>(); //Contains loaded titles for duplicate checking
            try {
                int  lenArr = jArr.length();
                for(int i=0; i<lenArr;i++){
                    JSONObject jObject = jArr.getJSONObject(i);
                    title = jObject.getJSONObject("title").getString("rendered");
                    if(titles.contains(title)) continue; // Skips building article if duplicate
                    author ="Jody Ryan";
                    description = android.text.Html.fromHtml(jObject.getJSONObject("content").getString("rendered"));
                    url = jObject.getString("link");
                    JSONObject embedded = jObject.getJSONObject("_embedded");
                    urlToImage = embedded.has("wp:featuredmedia") ?
                                 embedded.getJSONArray("wp:featuredmedia").getJSONObject(0).getString("source_url") :
                                 null;
                    if(urlToImage != null) {
                        //  Image handling
                        ImageUtility imageUtility = new ImageUtility(activity);
                        // if no featured media than use the last image downloaded
                        filename = urlToImage != null ? (author + title.substring(0,5)).replaceAll("/","") : filename;
                        image = imageUtility.fileExists(filename) ?
                                imageUtility.grabImage(filename) :
                                imageUtility.downloadImage(filename, urlToImage);
                        if (!imageUtility.fileExists(filename)) imageUtility.saveImage(filename, image);
                        adapter.getArticles().add(new Article(title, author, description, url, urlToImage, image));
                    } else {
                        adapter.getArticles().add(new Article(title, author, description, url));
                    }
                    titles.add(title);
                    publishProgress(i*10);
                }
                publishProgress(100);
            }catch (JSONException e){e.printStackTrace();}
            notifyChange();
        }


    }


}
