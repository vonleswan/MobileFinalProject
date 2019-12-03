package com.example.mobilefinalproject.news;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilefinalproject.R;

public class ArticleDialog{
    private SQLiteDatabase db;

    public void show(Activity activity, final Article article, SQLiteDatabase db){
        final String search = article.getUrl();
        final Activity activity1 = activity;
        final Dialog dialog = new Dialog(activity);
        this.db = db;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.article_dialog);
        dialog.getWindow()
                .setLayout((int) (getScreenWidth(activity) * .9), ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView title = dialog.findViewById(R.id.dialog_title);
        title.setText(article.getTitle());
        TextView description = dialog.findViewById(R.id.dialog_description);
        description.setText(article.getDescription());
        ImageView image = dialog.findViewById(R.id.dialog_image);
        image.setImageBitmap(article.getImage());

        Button saveButton = dialog.findViewById(R.id.dialog_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertArticle(article);
                dialog.dismiss();
            }
        });
        Button openButton = dialog.findViewById(R.id.dialog_browse);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                activity1.startActivity(
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse(search)));}
            });

        dialog.show();
    }

    public static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    public void insertArticle(Article article){
        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.COL_TITLE, article.getTitle());
        cv.put(DataBaseHelper.COL_AUTHOR, article.getAuthor());
        cv.put(DataBaseHelper.COL_DESCRIPTION, article.getDescription());
        cv.put(DataBaseHelper.COL_URL, article.getUrl());
        cv.put(DataBaseHelper.COL_URLTOIMAGE, article.getUrlToImage());
        article.setId(db.insert(DataBaseHelper.TABLE_NAME, null, cv));
    }
}
