package io.smartinez.exposeller.client.ui.buytickets.adapter;

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

import java.text.DateFormat;
import java.util.List;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.util.runnable.OnAdapterClickListener;

public class BuyTicketConcertAdapter extends RecyclerView.Adapter<BuyTicketConcertAdapter.BuyTicketConcertViewHolder> {
    private List<Concert> mConcertsList;
    private OnAdapterClickListener mOnAdapterClickListener;
    private Context mContext;

    @NonNull
    @Override
    public BuyTicketConcertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        return new BuyTicketConcertViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_buy_ticket_concert, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BuyTicketConcertViewHolder holder, int position) {
        if (mConcertsList != null) {
            Concert concert = mConcertsList.get(position);

            Glide.with(mContext).load(concert.getPhotoConcert()).into(holder.mTvPhotoArtist);

            holder.mTvConcertName2.setText(concert.getName());
            holder.mTvArtistName2.setText(concert.getArtistName());
            holder.mTvConcertValue2.setText(concert.getCost() + "€");

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext.getApplicationContext());

            holder.mTvConcertDate2.setText(dateFormat.format(concert.getEventDate()));
        }
    }

    public List<Concert> getConcertsList() {
        return mConcertsList;
    }

    public void setConcertsList(List<Concert> concertsList) {
        this.mConcertsList = concertsList;
    }

    public OnAdapterClickListener getAdapterClickListener() {
        return mOnAdapterClickListener;
    }

    public void setAdapterClickListener(OnAdapterClickListener mOnAdapterClickListener) {
        this.mOnAdapterClickListener = mOnAdapterClickListener;
    }

    @Override
    public int getItemCount() {
        return mConcertsList != null ? mConcertsList.size() : 0;
    }

    public class BuyTicketConcertViewHolder extends RecyclerView.ViewHolder {
        private CardView mCvConcertCalendar;
        private ConstraintLayout mClConcertCalendar;
        private ImageView mTvPhotoArtist;
        private TextView mTvArtistName2;
        private TextView mTvConcertDate2;
        private TextView mTvConcertValue2;
        private ImageView mIvArtistName2;
        private ImageView mIvConcertDate2;
        private ImageView mIvConcertValue2;
        private TextView mTvConcertName2;

        public BuyTicketConcertViewHolder(@NonNull View itemView) {
            super(itemView);

            initHolder();
            itemView.setOnClickListener(v -> mOnAdapterClickListener.onAdapaterClickListener(mConcertsList.get(getBindingAdapterPosition())));
        }

        public void initHolder() {
            mCvConcertCalendar = itemView.findViewById(R.id.cvConcertCalendar);
            mClConcertCalendar = itemView.findViewById(R.id.clConcertCalendar);
            mTvPhotoArtist = itemView.findViewById(R.id.tvPhotoArtist);
            mTvArtistName2 = itemView.findViewById(R.id.tvArtistName2);
            mTvConcertDate2 = itemView.findViewById(R.id.tvConcertDate2);
            mTvConcertValue2 = itemView.findViewById(R.id.tvConcertValue2);
            mIvArtistName2 = itemView.findViewById(R.id.ivArtistName2);
            mIvConcertDate2 = itemView.findViewById(R.id.ivConcertDate2);
            mIvConcertValue2 = itemView.findViewById(R.id.ivConcertValue2);
            mTvConcertName2 = itemView.findViewById(R.id.tvConcertName2);
        }
    }
}
