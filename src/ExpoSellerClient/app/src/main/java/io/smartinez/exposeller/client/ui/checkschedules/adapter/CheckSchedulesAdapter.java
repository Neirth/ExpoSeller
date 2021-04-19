package io.smartinez.exposeller.client.ui.checkschedules.adapter;

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

import java.util.List;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.util.runnable.OnAdapterClickListener;

public class CheckSchedulesAdapter extends RecyclerView.Adapter<CheckSchedulesAdapter.CheckSchedulesViewHolder> {
    private List<Concert> mConcertList;
    private OnAdapterClickListener mOnAdapterClickListener;
    private Context mContext;

    @NonNull
    @Override
    public CheckSchedulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        return new CheckSchedulesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_calendar_schedules, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckSchedulesViewHolder holder, int position) {
        if (mConcertList != null) {
            Concert concert = mConcertList.get(position);

            Glide.with(mContext).load(concert.getPhotoConcert()).into(holder.mIvPhotoArtist3);
            holder.mTvArtistName3.setText(concert.getArtistName());
            holder.mTvConcertName3.setText(concert.getName());
            holder.mTvPriceName3.setText(concert.getCost() + "€");
        }
    }

    @Override
    public int getItemCount() {
        return mConcertList != null ? mConcertList.size() : 0;
    }

    public List<Concert> getConcertList() {
        return mConcertList;
    }

    public void setConcertList(List<Concert> mConcertList) {
        this.mConcertList = mConcertList;
    }

    public OnAdapterClickListener getOnAdapterClickListener() {
        return mOnAdapterClickListener;
    }

    public void setOnAdapterClickListener(OnAdapterClickListener mOnAdapterClickListener) {
        this.mOnAdapterClickListener = mOnAdapterClickListener;

        notifyDataSetChanged();
    }

    public class CheckSchedulesViewHolder extends RecyclerView.ViewHolder {
        private CardView mCvConcertCalendar;
        private ConstraintLayout mClConcertCalendar;
        private ImageView mIvPhotoArtist3;
        private TextView mTvConcertName3;
        private TextView mTvArtistName3;
        private TextView mTvPriceName3;

        public CheckSchedulesViewHolder(@NonNull View itemView) {
            super(itemView);

            initHolder();
            itemView.setOnClickListener(v -> mOnAdapterClickListener.onAdapaterClickListener(mConcertList.get(getBindingAdapterPosition())));
        }

        public void initHolder() {
            mCvConcertCalendar = itemView.findViewById(R.id.cvConcertCalendar);
            mClConcertCalendar = itemView.findViewById(R.id.clConcertCalendar);
            mIvPhotoArtist3 = itemView.findViewById(R.id.ivPhotoArtist3);
            mTvConcertName3 = itemView.findViewById(R.id.tvConcertName3);
            mTvArtistName3 = itemView.findViewById(R.id.tvArtistName3);
            mTvPriceName3 = itemView.findViewById(R.id.tvPriceName3);
        }
    }
}
