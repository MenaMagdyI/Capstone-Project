package trendy.mina.com.trendy;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import trendy.mina.com.trendy.Model.Source;
import trendy.mina.com.trendy.adapters.SortPagerAdapter;

public class ArticlesActivity extends AppCompatActivity {

    private SortPagerAdapter mPagerAdapter;

    @BindView(R.id.sort_vp)
    ViewPager sortViewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        ButterKnife.bind(this);

        Source extraSource =  getIntent().getParcelableExtra(MainActivity.EXTRA_SOURCE);
        String sourceName = extraSource.getmName();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(sourceName);
        mPagerAdapter = new SortPagerAdapter(getSupportFragmentManager(), this, extraSource);

        sortViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(sortViewPager);
    }
}
