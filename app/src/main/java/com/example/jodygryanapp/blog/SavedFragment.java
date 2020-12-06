package com.example.jodygryanapp.blog;

import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.example.jodygryanapp.R;

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

        adapter = new SavedAdapter(getContext(), new ArrayList<>(), db);
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
            results.getCount();
            results.moveToFirst();
            while(results.moveToNext())
            {
                long id = results.getLong(idColIndex);
                String title = results.getString(titleColIndex);
                String author = results.getString(authorColIndex);
                Spanned description = Html.fromHtml(results.getString(descriptionColIndex));
                String url = results.getString(urlColIndex);
                String urlToImage = results.getString(urlToImageColIndex);

                if(urlToImage != null) {
                    //  Image handling
                    ImageUtility imageUtility = new ImageUtility(getActivity());
                    // if no featured media than use the last image downloaded
                    String filename = urlToImage != null ? (author + title.substring(0,5)).replaceAll("/","") : "Jody RyanWhile";
                    Bitmap image = imageUtility.fileExists(filename) ?
                            imageUtility.grabImage(filename) :
                            imageUtility.downloadImage(filename, urlToImage);
                    if (!imageUtility.fileExists(filename)) imageUtility.saveImage(filename, image);
                    adapter.getArticles().add(new Article(title, author, description, url, urlToImage, image));
                } else {
                    adapter.getArticles().add(new Article(title, author, description, url));
                }
                publishProgress(i*10);
                i++;
            }
            publishProgress(100);
            notifyChange();
        }
    }
}
