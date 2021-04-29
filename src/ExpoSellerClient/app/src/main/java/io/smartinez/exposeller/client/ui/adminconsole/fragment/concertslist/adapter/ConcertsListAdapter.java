package io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.util.listener.OnAdapterClickDeleteListener;
import io.smartinez.exposeller.client.util.listener.OnAdapterClickEditListener;

public class ConcertsListAdapter extends RecyclerView.Adapter<ConcertsListAdapter.ConcertsListViewHolder> {
    private OnAdapterClickEditListener mClickEditListener;
    private OnAdapterClickDeleteListener mClickDeleteListener;
    private Context mContext;

    private List<Concert> mEntriesList;

    @NonNull
    @Override
    public ConcertsListAdapter.ConcertsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        return new ConcertsListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_concert, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertsListAdapter.ConcertsListViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

        if (mEntriesList != null) {
            Concert concert = mEntriesList.get(position);

            Glide.with(mContext).load(concert.getPhotoConcert()).into(holder.mIvPhotoArtist4);

            holder.mTvFriendlyId4.setText(concert.getFriendlyId());
            holder.mTvConcertName4.setText(concert.getName());
            holder.mTvEventDate3.setText(sdf.format(concert.getEventDate()));
        }
    }

    @Override
    public int getItemCount() {
        return mEntriesList.size();
    }

    public List<Concert> getEntriesList() {
        return mEntriesList;
    }

    public void setEntriesList (List<Concert> entriesList) {
        mEntriesList = entriesList;

        notifyDataSetChanged();
    }

    public void setOnAdapterClickEditListener(OnAdapterClickEditListener clickEditListener) {
        this.mClickEditListener = clickEditListener;
    }

    public void setOnAdapterClickDeleteListener(OnAdapterClickDeleteListener clickDeleteListener){
        this.mClickDeleteListener = clickDeleteListener;
    }

    public class ConcertsListViewHolder extends RecyclerView.ViewHolder{
        private CardView mCvAdminConcert;
        private ConstraintLayout mClConcertCalendar;
        private ImageView mIvPhotoArtist4;
        private TextView mTvConcertName4;
        private TextView mTvFriendlyId4;
        private TextView mTvEventDate3;
        private ImageView mIvEditConcert4;
        private ImageView mIvDeleteConcert4;

        public ConcertsListViewHolder(@NonNull View itemView) {
            super(itemView);

            initView();
        }

        private void initView() {
            mCvAdminConcert = itemView.findViewById(R.id.cvAdminConcert);
            mClConcertCalendar = itemView.findViewById(R.id.clConcertCalendar);
            mIvPhotoArtist4 = itemView.findViewById(R.id.ivPhotoArtist4);
            mTvConcertName4 = itemView.findViewById(R.id.tvConcertName4);
            mTvFriendlyId4 = itemView.findViewById(R.id.tvFriendlyId4);
            mTvEventDate3 = itemView.findViewById(R.id.tvEventDate3);
            mIvEditConcert4 = itemView.findViewById(R.id.ivEditConcert4);
            mIvDeleteConcert4 = itemView.findViewById(R.id.ivDeleteConcert4);

            itemView.setOnClickListener(v -> {
                if (mClickEditListener != null)
                    mClickEditListener.onAdapterClickEditListener(mEntriesList.get(getBindingAdapterPosition()));
            });

            mIvEditConcert4.setOnClickListener(v -> {
                if (mClickEditListener != null)
                    mClickEditListener.onAdapterClickEditListener(mEntriesList.get(getBindingAdapterPosition()));
            });

            mIvDeleteConcert4.setOnClickListener(v -> {
                if (mClickDeleteListener != null)
                    mClickDeleteListener.onAdapterClickDeleteListener(mEntriesList.get(getBindingAdapterPosition()));
            });
        }
    }
}
