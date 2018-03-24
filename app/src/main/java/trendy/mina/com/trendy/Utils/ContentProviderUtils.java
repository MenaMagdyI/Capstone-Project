package trendy.mina.com.trendy.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import trendy.mina.com.trendy.Model.Article;
import trendy.mina.com.trendy.Model.Source;
import trendy.mina.com.trendy.database.NewsContract;
import trendy.mina.com.trendy.database.TrendyContentProvider;

/**
 * Created by Mena on 3/24/2018.
 */

public class ContentProviderUtils {

    public static ContentValues[] convertSourcesIntoContentValues (List<Source> sources) {
        ContentValues[] cvs = new ContentValues[sources.size()];
        for (int i=0 ; i<sources.size() ; i++) {
            Source currentSource = sources.get(i);
            ContentValues cv = new ContentValues();
            cv.put(NewsContract.SourceEntry._ID, currentSource.getmId());
            cv.put(NewsContract.SourceEntry.NAME, currentSource.getmName());

            cvs[i] = cv;
        }
        return cvs;
    }

    public static int insertSourcesIntoContentProvider (Context context, List<Source> sources) {
        deleteSourcesFromProvider(context);
        ContentResolver resolver = context.getContentResolver();
        ContentValues[] cvs = convertSourcesIntoContentValues(sources);

        int rowsInserted = resolver.bulkInsert(TrendyContentProvider.Sources.SOURCES, cvs);
        return rowsInserted;
    }


    public static ContentValues[] convertArticlesIntoContentValues (List<Article> articles, String sourceId, String sortedBy) {
        ContentValues[] cvs = new ContentValues[articles.size()];
        for (int i=0 ; i<articles.size(); i++) {
            Article currentArticle = articles.get(i);
            ContentValues cv = new ContentValues();
            cv.put(NewsContract.ArticleEntry.SOURCE_ID, sourceId);
            cv.put(NewsContract.ArticleEntry.SORTED_BY, sortedBy);
            cv.put(NewsContract.ArticleEntry.AUTHOR, currentArticle.getmAuthor());
            cv.put(NewsContract.ArticleEntry.DATE, currentArticle.getmDate());
            cv.put(NewsContract.ArticleEntry.DESCRIPTION, currentArticle.getmDescription());
            cv.put(NewsContract.ArticleEntry.IMAGE_URL, currentArticle.getmImageUrl());
            cv.put(NewsContract.ArticleEntry.TITLE, currentArticle.getmTitle());
            cv.put(NewsContract.ArticleEntry.URL, currentArticle.getmUrl());
            cvs[i] = cv;
        }
        return cvs;
    }


    public static int insertArticlesIntoContentProvider (Context context, List<Article> articles, String sourceId, String sortedBy) {
        deleteArticlesFromProvider(context, sourceId, sortedBy);
        ContentResolver resolver = context.getContentResolver();
        ContentValues[] cvs = convertArticlesIntoContentValues(articles, sourceId, sortedBy);

        int rowsInserted = resolver.bulkInsert(TrendyContentProvider.Articles.ARTICLES, cvs);
        return rowsInserted;
    }

    public static List<Source> getSourcesFromCursor (Cursor cursor) {
        ArrayList<Source> sources = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(NewsContract.SourceEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(NewsContract.SourceEntry.NAME));

            sources.add(new Source(name, id));
        }
        return sources;
    }

    public static List<Source> getSourcesFromProvider (Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(TrendyContentProvider.Sources.SOURCES, null, null, null, null);
        ArrayList<Source> sources = (ArrayList<Source>) getSourcesFromCursor(cursor);
        cursor.close();
        return sources;
    }

    public static List<Article> getArticlesFromCursor(Cursor cursor) {
        ArrayList<Article> articles = new ArrayList<>();
        while (cursor.moveToNext()) {
            String author = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.AUTHOR));
            String date = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.DATE));
            String title = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.TITLE));
            String description = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.DESCRIPTION));
            String url = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.URL));
            String imageUrl = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.IMAGE_URL));

            articles.add(new Article(author, title, description, url, imageUrl, date));
        }
        return articles;
    }

    public static List<Article> getArticlesFromProvider (Context context, String sourceId, String sortedBy) {
        ContentResolver resolver = context.getContentResolver();
        String selection = NewsContract.ArticleEntry.SOURCE_ID + "=? and " + NewsContract.ArticleEntry.SORTED_BY + "=?";
        String[] selectionArgs = new String[]{sourceId, sortedBy};
        Cursor cursor = resolver.query(TrendyContentProvider.Articles.ARTICLES, null, selection, selectionArgs, null);
        ArrayList<Article> articles = (ArrayList<Article>) getArticlesFromCursor(cursor);
        cursor.close();
        return articles;
    }

    public static int deleteSourcesFromProvider (Context context) {
        return context.getContentResolver().delete(TrendyContentProvider.Sources.SOURCES, null, null);
    }

    public static int deleteArticlesFromProvider (Context context, String sourceId, String sortedBy) {
        String selection = NewsContract.ArticleEntry.SOURCE_ID + " =? and " + NewsContract.ArticleEntry.SORTED_BY + " =? ";
        String[] selectionArgs = new String[]{sourceId, sortedBy};
        return context.getContentResolver().delete(TrendyContentProvider.Articles.ARTICLES, selection, selectionArgs);
    }


    public static Article getLatestArticle(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(TrendyContentProvider.Articles.ARTICLES, null, null, null, null);
        Article article = null;
        if (cursor.moveToNext()) {
            String author = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.AUTHOR));
            String date = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.DATE));
            String title = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.TITLE));
            String description = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.DESCRIPTION));
            String url = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.URL));
            String imageUrl = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.IMAGE_URL));
            article = new Article(author, title, description, url, imageUrl, date);
        }
        cursor.close();
        return article;
    }

}
