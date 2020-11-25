package com.example.jodygryanapp.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jodygryanapp.R;

import java.util.ArrayList;

public class FeedListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Article> articles;

    public FeedListAdapter(Context context, ArrayList<Article> articles){
        super();
        this.context = context;
        this.articles = articles;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView rowText;
        ImageView rowImage;
        Article article = articles.get(i);
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.feedlist_element, null);
        rowText = rowView.findViewById(R.id.feed_title);
        rowText.setText(article.getTitle());
        rowImage = rowView.findViewById(R.id.feed_image);
        rowImage.setImageBitmap(article.getImage());
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

}
