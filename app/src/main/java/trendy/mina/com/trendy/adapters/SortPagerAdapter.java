package trendy.mina.com.trendy.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import trendy.mina.com.trendy.ArticlesFragment;
import trendy.mina.com.trendy.Model.Source;
import trendy.mina.com.trendy.R;

/**
 * Created by Mena on 3/15/2018.
 */

public class SortPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private Source mSource;
    private String[] mTabs;

    public static final String SOURCE = "source";
    public static final String SORT = "sort";
    public static final String LOADER_ID = "loader_id";

    public static final int ARTICLES_LOADER_BASE_ID = 777;
    public SortPagerAdapter(FragmentManager fm, Context context, Source source) {
        super(fm);

        mContext = context;
        mSource = source;

        mTabs = new String[]{mContext.getString(R.string.url_sortedby_top),
                mContext.getString(R.string.url_sortedby_latest),
                mContext.getString(R.string.url_sortedby_popular)};
    }

    @Override
    public Fragment getItem(int position) {
        ArticlesFragment fragment = new ArticlesFragment();
        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putParcelable(SOURCE, mSource);
        fragmentBundle.putString(SORT, mTabs[position]);
        fragmentBundle.putInt(LOADER_ID, ARTICLES_LOADER_BASE_ID+position);
        fragment.setArguments(fragmentBundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs[position];
    }


}
