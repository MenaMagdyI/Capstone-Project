package trendy.mina.com.trendy;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import trendy.mina.com.trendy.Model.Article;

public class DetailsActivity extends AppCompatActivity {


    @BindView(R.id.article_title)
    TextView articleTitleTV;
    @BindView(R.id.article_description)
    TextView articleDesTV;
    @BindView(R.id.article_date)
    TextView articleDateTV;
    @BindView(R.id.article_image)
    ImageView articleImageIV;
    @BindView(R.id.article_url)
    TextView articleUrlTV;
    @BindView(R.id.article_author)
    TextView articleAuthorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Article mainArticle = getIntent().getParcelableExtra(ArticlesFragment.EXTRA_ARTICLE);


        ButterKnife.bind(this);

        setViews(mainArticle);
        setContentDescription(mainArticle);

    }


    void setContentDescription(Article mainArticle) {
        String articleTitle = mainArticle.getmTitle();
        String articleDate = mainArticle.getmDate();
        String articleAuthor = mainArticle.getmAuthor();
        String description = mainArticle.getmDescription();

        articleTitleTV.setContentDescription(articleTitle);
        articleTitleTV.setContentDescription(articleTitle);
        articleDesTV.setContentDescription(description);
        if(articleDate != null) {
            articleDateTV.setContentDescription(articleDate);
        }
        if (articleAuthor != null) {
            articleAuthorTV.setContentDescription(articleAuthor);
        }
    }




    void setViews(Article mainArticle) {
        String articleTitle = mainArticle.getmTitle();
        String articleDate = mainArticle.getmDate();
        String articleAuthor = mainArticle.getmAuthor();
        String imageUrl = mainArticle.getmImageUrl();
        final String articleUrl = mainArticle.getmUrl();
        String description = mainArticle.getmDescription();

        articleTitleTV.setText(articleTitle);
        articleDesTV.setText(description);
        if(articleDate != null) {
            String date;
            if (articleDate.indexOf('T')>0) {
                date = articleDate.substring(0, articleDate.indexOf('T'));
            } else {
                date = articleDate;
            }
            articleDateTV.setText(date);
        } else {
            articleDateTV.setVisibility(View.GONE);
        }

        if(articleAuthor != null) {
            articleAuthorTV.setText(articleAuthor);
        } else {
            articleAuthorTV.setVisibility(View.GONE);
        }
        Picasso.with(DetailsActivity.this)
                .load(imageUrl)
                .error(R.drawable.error_image)
                .into(articleImageIV);

        articleUrlTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewOnlineIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl));
                if(getPackageManager().resolveActivity(viewOnlineIntent, 0) != null) {
                    startActivity(viewOnlineIntent);
                }
            }
        });
    }
}
