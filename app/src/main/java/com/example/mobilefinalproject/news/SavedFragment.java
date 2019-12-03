package com.example.mobilefinalproject.news;

import androidx.fragment.app.ListFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.example.mobilefinalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SavedFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private ProgressBar progressBar;
    private SavedAdapter adapter;
    private SQLiteDatabase db;
    private DataBaseHelper dataBaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feedlist, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //init Database
        dataBaseHelper = new DataBaseHelper(getActivity());
        db = dataBaseHelper.getWritableDatabase();

        adapter = new SavedAdapter(getContext(),new ArrayList<Article>(), db);
        setListAdapter(adapter);

        progressBar = getActivity().findViewById(R.id.progress);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        getListView().setOnItemClickListener(this);
        new LoadImages().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(adapter.getArticles().get(position).getUrl())));
    }

    private void notifyChange(){
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }catch (NullPointerException e){e.printStackTrace();}
    }

    private class LoadImages extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            fillList();
            return "Success";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("ONPOSTRESULT", result);
        }

        @Override
        protected void onProgressUpdate(Integer... progress){
            progressBar.setProgress(progress[0]);
        }

        public Cursor getAllData(){
            //query all the results from the database:
            String [] columns = {DataBaseHelper.COL_ID, DataBaseHelper.COL_TITLE, DataBaseHelper.COL_AUTHOR,
                        DataBaseHelper.COL_DESCRIPTION, DataBaseHelper.COL_URL, DataBaseHelper.COL_URLTOIMAGE};
            return db.query(false, DataBaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        }

        public void fillList(){
            Cursor results = getAllData();
            //printCursor(results);
            //find column indexes
            int idColIndex = results.getColumnIndex(DataBaseHelper.COL_ID);
            int titleColIndex = results.getColumnIndex(DataBaseHelper.COL_TITLE);
            int authorColIndex = results.getColumnIndex(DataBaseHelper.COL_AUTHOR);
            int descriptionColIndex = results.getColumnIndex(DataBaseHelper.COL_DESCRIPTION);
            int urlColIndex = results.getColumnIndex(DataBaseHelper.COL_URL);
            int urlToImageColIndex = results.getColumnIndex(DataBaseHelper.COL_URLTOIMAGE);
            //iterate over the results, return true if there is a next item:
            int i =0;
            results.moveToFirst();
            while(results.moveToNext())
            {
                long id = results.getLong(idColIndex);
                String title = results.getString(titleColIndex);
                String author = results.getString(authorColIndex);
                String description = results.getString(descriptionColIndex);
                String url = results.getString(urlColIndex);
                String urlToImage = results.getString(urlToImageColIndex);

                ImageUtility imageUtility = new ImageUtility(getActivity());
                String fname = (author + title.substring(0,5)).replaceAll("/","");
                Bitmap image =  imageUtility.fileExists(fname) ?
                                imageUtility.grabImage(fname) :
                                imageUtility.downloadImage(fname,urlToImage);
                if(image != null){
                    //add the new Article to the array list:
                    adapter.getArticles().add(new Article(title, author, description, url, urlToImage, image));
                    i++;
                    publishProgress(i*10);
                }
            }
            notifyChange();
        }
    }
}
