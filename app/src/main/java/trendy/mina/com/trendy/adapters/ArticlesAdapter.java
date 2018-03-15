package trendy.mina.com.trendy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import trendy.mina.com.trendy.Model.Article;
import trendy.mina.com.trendy.R;

/**
 * Created by Mena on 3/15/2018.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>{
    private ArrayList<Article> mArticles;
    private ArticleOnCLickListener mArticleOnCLickListener;

    public ArticlesAdapter(ArrayList<Article> articles, ArticleOnCLickListener listener) {
        mArticles = articles;
        mArticleOnCLickListener = listener;
    }

    public interface ArticleOnCLickListener {
        void onCLick(int position);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.articles_item_title)
        TextView articleTitle;
        @BindView(R.id.articles_item_image)
        ImageView articleImage;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Article currentArticle = mArticles.get(position);

            articleTitle.setText(currentArticle.getmTitle());
            articleTitle.setContentDescription(currentArticle.getmTitle());
            Picasso.with(itemView.getContext())
                    .load(currentArticle.getmImageUrl())
                    .placeholder(R.drawable.error_image)
                    .error(R.drawable.error_image)
                    .into(articleImage);
        }

        @Override
        public void onClick(View view) {
            mArticleOnCLickListener.onCLick(getAdapterPosition());
        }
    }
}
