package trendy.mina.com.trendy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import trendy.mina.com.trendy.Model.Source;
import trendy.mina.com.trendy.R;

/**
 * Created by Mena on 3/14/2018.
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceViewHolder>{

    private ArrayList<Source> mSources;
    private SourceOnClickListener mOnclickListener;


    public interface SourceOnClickListener {
        void onClick(int position);
    }

    public SourceAdapter(ArrayList<Source> sources, SourceOnClickListener listener) {
        mSources = sources;
        mOnclickListener = listener;
    }

    @Override
    public SourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.source_item, parent, false);
        return new SourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SourceViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSources.size();
    }


    class SourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.source_name)
        TextView sourceTv;

        public SourceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            sourceTv.setText(mSources.get(position).getmName());
            sourceTv.setContentDescription(mSources.get(position).getmName());
        }

        @Override
        public void onClick(View view) {
            mOnclickListener.onClick(getAdapterPosition());
        }
    }
}
