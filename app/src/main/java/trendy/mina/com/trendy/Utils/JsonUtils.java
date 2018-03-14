package trendy.mina.com.trendy.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import trendy.mina.com.trendy.Model.Source;
import trendy.mina.com.trendy.R;

/**
 * Created by Mena on 3/14/2018.
 */

public class JsonUtils {
    public static final String STATUS_OK = "ok";
    public static final String TAG = "JsonUtils";

    private JsonUtils() {
    }

    public static List<Source> extractSourcesFromJson(String jsonResponce, Context context) {
        ArrayList<Source> sources = new ArrayList<>();
        try {
            if (jsonResponce == null || TextUtils.equals(jsonResponce, "")) {
                return sources;
            }

            JSONObject rootObject = new JSONObject(jsonResponce);
            String status = rootObject.getString(context.getString(R.string.json_status));

            if (!STATUS_OK.equals(status)) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "status!=ok");
                FirebaseCrash.report(new Exception("Sources Api request status: " + status));
                return sources;
            }

            JSONArray sourcesJson = rootObject.getJSONArray(context.getString(R.string.json_sources));

            for (int i=0 ; i<sourcesJson.length() ; i++) {
                JSONObject currentSource = sourcesJson.getJSONObject(i);
                String sourceId = currentSource.getString(context.getString(R.string.json_sources_id));
                String sourceName = currentSource.getString(context.getString(R.string.json_source_name));

                sources.add(new Source(sourceName, sourceId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sources;
    }

}

