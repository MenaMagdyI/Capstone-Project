package trendy.mina.com.trendy;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import trendy.mina.com.trendy.Model.Source;
import trendy.mina.com.trendy.Utils.JsonUtils;
import trendy.mina.com.trendy.Utils.NetworkUtils;
import trendy.mina.com.trendy.adapters.SourceAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Source>>, SourceAdapter.SourceOnClickListener {


    public static final int SOURCES_MANAGER_ID = 666;
    public static final String EXTRA_SOURCE = "source";
    public boolean isConnected = false;
    private Toast mToast;
    private ArrayList<Source> mSources;
    private SourceAdapter mAdapter;

    @BindView(R.id.sources_rv)
    RecyclerView sourcesRecyclerView;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;
    @BindView(R.id.empty_sources_list)
    TextView emptyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = MainActivity.this;
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mSources = new ArrayList<>();
        mAdapter = new SourceAdapter(mSources, this);
        sourcesRecyclerView.setLayoutManager(layoutManager);
        sourcesRecyclerView.setAdapter(mAdapter);

        if(!NetworkUtils.isConnected(context)) {
            if(mToast != null)
                mToast.cancel();

            mToast = Toast.makeText(MainActivity.this, getString(R.string.no_network_message), Toast.LENGTH_SHORT);
            mToast.show();
            getSupportLoaderManager().initLoader(SOURCES_MANAGER_ID, null, this);
//            showEmptyListView();
        } else {
            isConnected = true;
            getSupportLoaderManager().initLoader(SOURCES_MANAGER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Source>> onCreateLoader(int id, Bundle args) {
        final Context context = MainActivity.this;

        return new AsyncTaskLoader<List<Source>>(context) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
                showLoadingIndicator();
            }

            @Override
            public List<Source> loadInBackground() {
                if (isConnected) {
                    String jsonResponse = NetworkUtils.getSourcesJsonResponse(context);
                    return JsonUtils.extractSourcesFromJson(jsonResponse, context);
                } else {
                   // will return data from provider.
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Source>> loader, List<Source> data) {

        if(data == null || data.size() == 0) {
            sourcesRecyclerView.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
        } else {
            if(isConnected) {
                // Adding Downloaded Sources into the Provider
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                       // will insert to provider in the future
                        return null;
                    }
                }.execute();
            }
            mSources.clear();
            mSources.addAll(data);
            mAdapter.notifyDataSetChanged();
            hideLoadingIndicator();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Source>> loader) {

    }

    @Override
    public void onClick(int position) {

        Intent articleIntent = new Intent(MainActivity.this, ArticlesActivity.class);
        articleIntent.putExtra(EXTRA_SOURCE, mSources.get(position));
        startActivity(articleIntent);
    }


    void showLoadingIndicator() {
        sourcesRecyclerView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        sourcesRecyclerView.setVisibility(View.VISIBLE);

    }
}
