package trendy.mina.com.trendy.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RemoteViews;

import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;

import trendy.mina.com.trendy.Model.Article;
import trendy.mina.com.trendy.R;
import trendy.mina.com.trendy.Utils.ContentProviderUtils;

/**
 * Created by Mena on 3/25/2018.
 */

public class AppWidget extends AppWidgetProvider {

    public static final String TAG = AppWidget.class.getSimpleName();
    private static Target target;
    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        final Article article = ContentProviderUtils.getLatestArticle(context);
        if (article == null) {
            showEmptyArticlesView(views);
        } else {
            hideEmptyArticlesView(views);



            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        InputStream is = new java.net.URL(article.getmImageUrl()).openStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        return bitmap;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if(isCancelled()) {
                        bitmap = null;
                    }
                    views.setImageViewBitmap(R.id.widget_article_image, bitmap);
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            }.execute();

            views.setImageViewResource(R.id.widget_article_image, R.drawable.error_image);
            views.setTextViewText(R.id.widget_article_title, article.getmTitle());
            views.setTextViewText(R.id.widget_article_author, article.getmAuthor());
            views.setTextViewText(R.id.widget_article_date, article.getmDate());
            views.setTextViewText(R.id.widget_article_description, article.getmDescription());
            // Instruct the widget manager to update the widget
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void showEmptyArticlesView(RemoteViews views) {
        views.setViewVisibility(R.id.widget_article_image, View.GONE);
        views.setViewVisibility(R.id.widget_article_title, View.GONE);
        views.setViewVisibility(R.id.widget_article_author, View.GONE);
        views.setViewVisibility(R.id.widget_article_date, View.GONE);
        views.setViewVisibility(R.id.widget_article_description, View.GONE);
        views.setViewVisibility(R.id.empty_widget_tv, View.VISIBLE);
    }

    static void hideEmptyArticlesView(RemoteViews views) {
        views.setViewVisibility(R.id.empty_widget_tv, View.GONE);
        views.setViewVisibility(R.id.widget_article_image, View.VISIBLE);
        views.setViewVisibility(R.id.widget_article_title, View.VISIBLE);
        views.setViewVisibility(R.id.widget_article_author, View.VISIBLE);
        views.setViewVisibility(R.id.widget_article_date, View.VISIBLE);
        views.setViewVisibility(R.id.widget_article_description, View.VISIBLE);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


}
