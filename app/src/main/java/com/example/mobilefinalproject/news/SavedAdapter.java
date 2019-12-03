package com.example.mobilefinalproject.news;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilefinalproject.R;

import java.util.ArrayList;

public class SavedAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Article> articles;
    private SQLiteDatabase db;

    public SavedAdapter(Context context, ArrayList<Article> articles, SQLiteDatabase db){
        super();
        this.context = context;
        this.articles = articles;
        this.db = db;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView rowText;
        ImageView rowImage, rowDeleteImage;
        final Article article = articles.get(i);
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.saved_element, null);
        rowText = rowView.findViewById(R.id.saved_title);
        rowText.setText(article.getTitle());
        rowImage = rowView.findViewById(R.id.saved_image);
        rowImage.setImageBitmap(article.getImage());
        rowDeleteImage = rowView.findViewById(R.id.delete);
        rowDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArticle(article.getId());
                articles.remove(article);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }

    @Override
    public Object getItem(int i) {
        return articles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount(){
        return articles.size();
    }

    public ArrayList<Article> getArticles(){
        return articles;
    }

    public boolean deleteArticle(long id){
        Log.i("Delete", "_id="+id);
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
//        Log.i("success","value= "+Integer.toString(success));
        return db.delete(DataBaseHelper.TABLE_NAME, whereClause, whereArgs) > 0;
    }
}
