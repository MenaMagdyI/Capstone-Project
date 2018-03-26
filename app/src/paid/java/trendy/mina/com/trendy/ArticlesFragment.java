package trendy.mina.com.trendy;

/**
 * Created by Mena on 3/15/2018.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import trendy.mina.com.trendy.Model.Article;
import trendy.mina.com.trendy.Model.Source;
import trendy.mina.com.trendy.Utils.ContentProviderUtils;
import trendy.mina.com.trendy.Utils.JsonUtils;
import trendy.mina.com.trendy.Utils.NetworkUtils;
import trendy.mina.com.trendy.adapters.ArticlesAdapter;
import trendy.mina.com.trendy.adapters.SortPagerAdapter;


public class ArticlesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Article>>,
        ArticlesAdapter.ArticleOnCLickListener{

    public static final String TAG = ArticlesFragment.class.getSimpleName();
    public static final String SOURCE = "source";
    public static final String SORTED_BY = "sorted_by";
    public static final String EXTRA_ARTICLE = "article";
    public boolean isConnected = false;

    private Source mSource;
    private String mSortBy;
    private ArticlesAdapter mAdapter;
    private ArrayList<Article> mArticles;
    private int mLoaderId;

    @BindView(R.id.articles_rv) RecyclerView articlesRecyclerView;
    @BindView(R.id.empty_article_tv) TextView emptyArticleTV;
    

    public ArticlesFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        mSource = args.getParcelable(SortPagerAdapter.SOURCE);
        mSortBy = args.getString(SortPagerAdapter.SORT);
        mLoaderId = args.getInt(SortPagerAdapter.LOADER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article, container, false);
        ButterKnife.bind(this, rootView);

       

       


        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(SOURCE)) {
                mSource = savedInstanceState.getParcelable(SOURCE);
            }
            if (savedInstanceState.containsKey(SORTED_BY)) {
                mSortBy = savedInstanceState.getString(SORTED_BY);
            }
        }

        mArticles = new ArrayList<>();
        mAdapter = new ArticlesAdapter(mArticles, this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns(),
                StaggeredGridLayoutManager.VERTICAL);

        articlesRecyclerView.setLayoutManager(layoutManager);
        articlesRecyclerView.setAdapter(mAdapter);

        if (!NetworkUtils.isConnected(getContext())) {
            Toast.makeText(getContext(), getString(R.string.no_network_message), Toast.LENGTH_SHORT).show();
        } else {
            isConnected = true;
        }
        Bundle loaderBundle = new Bundle();
        loaderBundle.putString(SOURCE, mSource.getmId());
        loaderBundle.putString(SORTED_BY, mSortBy);

        getActivity().getSupportLoaderManager().initLoader(mLoaderId, loaderBundle, this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SOURCE, mSource);
        outState.putString(SORTED_BY, mSortBy);
        super.onSaveInstanceState(outState);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, final Bundle args) {
        final Context context = getContext();
        return new AsyncTaskLoader<List<Article>>(context) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public List<Article> loadInBackground() {
                String jsonResponse = NetworkUtils.getArticlesJsonResponse(context,
                        args.getString(SOURCE),
                        args.getString(SORTED_BY));
                if(isConnected) {
                    return JsonUtils.extractArticlesFromJson(jsonResponse, context);
                } else {
                    return ContentProviderUtils.getArticlesFromProvider(getContext(), mSource.getmId(), mSortBy);
                }
            }
        };
    }

    void showEmptyListView() {
        articlesRecyclerView.setVisibility(View.GONE);
        emptyArticleTV.setVisibility(View.VISIBLE);
    }

    void hideEmptyListView() {
        emptyArticleTV.setVisibility(View.GONE);
        articlesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, final List<Article> data) {
        if(data ==null || data.size() == 0) {
            showEmptyListView();
        } else {
            if (isConnected) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        ContentProviderUtils.insertArticlesIntoContentProvider(getContext(), data, mSource.getmId(), mSortBy);
                        return null;
                    }
                }.execute();
            }
            mArticles.clear();
            mArticles.addAll(data);
            mAdapter.notifyDataSetChanged();
            hideEmptyListView();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
    }

    @Override
    public void onCLick(int position) {
        Article currentArticle = mArticles.get(position);
        Intent detailIntent = new Intent(getContext(), DetailsActivity.class);
        detailIntent.putExtra(EXTRA_ARTICLE, currentArticle);

        startActivity(detailIntent);
    }









}
