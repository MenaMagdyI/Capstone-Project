package trendy.mina.com.trendy.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import trendy.mina.com.trendy.R;

/**
 * Created by Mena on 3/14/2018.
 */

public class NetworkUtils {

    public static final String REQUEST_METHOD_GET = "GET";


    private NetworkUtils(){
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    public static String getSourcesJsonResponse(Context context) {
        String sourcesUrlString = buildSourcesUrl(context);
        URL sourcesUrl = getUrl(sourcesUrlString);
        return getJsonResponse(sourcesUrl);
    }


    public static String buildSourcesUrl(Context context) {
        Uri uri = Uri.parse(context.getString(R.string.api_sources_url))
                .buildUpon()
                .appendQueryParameter(context.getString(R.string.url_apikey_parameter),
                        context.getString(R.string.url_apikey_value))
                .appendQueryParameter(context.getString(R.string.url_language_parameter),
                        context.getString(R.string.url_language_value))
                .build();
        Log.v("api_sources_URL",uri.toString());
        return uri.toString();
    }

    public static URL getUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getJsonResponse(URL url) {
        InputStream stream = getInputStream(url);
        if (stream == null)
            return null;

        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();

        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    public static InputStream getInputStream(URL url) {
        HttpURLConnection urlConnection;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(REQUEST_METHOD_GET);
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }





}



