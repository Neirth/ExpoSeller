package io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist.adapter;

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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.util.listener.OnAdapterClickDeleteListener;
import io.smartinez.exposeller.client.util.listener.OnAdapterClickEditListener;

public class AdsListAdapter extends RecyclerView.Adapter<AdsListAdapter.AdsListViewHolder> {
    private OnAdapterClickEditListener mClickEditListener;
    private OnAdapterClickDeleteListener mClickDeleteListener;
    private Context mContext;

    private List<Advertisement> mEntriesList = Collections.emptyList();

    @NonNull
    @Override
    public AdsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        return new AdsListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_adbanner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdsListViewHolder holder, int position) {
        if (mEntriesList != null) {
            Advertisement advertisement = mEntriesList.get(position);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
            LocalDateTime adBannerDateObj = advertisement.getEventDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            Glide.with(mContext).load(advertisement.getPhotoAd()).into(holder.mIvPhotoArtist5);

            holder.mTvFriendlyId5.setText(String.valueOf(advertisement.getFriendlyId()));
            holder.mTvAdBannerName5.setText(advertisement.getName());
            holder.mTvEventDate3.setText(adBannerDateObj.format(dateFormatter));
        }
    }

    @Override
    public int getItemCount() {
        return mEntriesList.size();
    }

    public List<Advertisement> getEntriesList() {
        return mEntriesList;
    }

    public void setEntriesList (List<Advertisement> entriesList) {
        mEntriesList = entriesList;

        notifyDataSetChanged();
    }

    public void setOnAdapterClickEditListener(OnAdapterClickEditListener clickEditListener) {
        this.mClickEditListener = clickEditListener;
    }

    public void setOnAdapterClickDeleteListener(OnAdapterClickDeleteListener clickDeleteListener){
        this.mClickDeleteListener = clickDeleteListener;
    }

    public class AdsListViewHolder extends RecyclerView.ViewHolder {
        private CardView mCvAdminAdBanner;
        private ConstraintLayout mClConcertCalendar;
        private ImageView mIvPhotoArtist5;
        private TextView mTvAdBannerName5;
        private TextView mTvFriendlyId5;
        private TextView mTvEventDate3;
        private ImageView mIvDeleteAdBanner5;
        private ImageView mIvEditAdBanner5;

        public AdsListViewHolder(@NonNull View itemView) {
            super(itemView);

            initView();
        }

        private void initView() {
            mCvAdminAdBanner = itemView.findViewById(R.id.cvAdminAdBanner);
            mClConcertCalendar = itemView.findViewById(R.id.clConcertCalendar);
            mIvPhotoArtist5 = itemView.findViewById(R.id.ivPhotoArtist5);
            mTvAdBannerName5 = itemView.findViewById(R.id.tvAdBannerName5);
            mTvFriendlyId5 = itemView.findViewById(R.id.tvFriendlyId5);
            mTvEventDate3 = itemView.findViewById(R.id.tvEventDate3);
            mIvDeleteAdBanner5 = itemView.findViewById(R.id.ivDeleteAdBanner5);
            mIvEditAdBanner5 = itemView.findViewById(R.id.ivEditAdBanner5);

            itemView.setOnClickListener(v -> {
                if (mClickEditListener != null)
                    mClickEditListener.onAdapterClickEditListener(mEntriesList.get(getBindingAdapterPosition()));
            });

            mIvEditAdBanner5.setOnClickListener(v -> {
                if (mClickEditListener != null)
                    mClickEditListener.onAdapterClickEditListener(mEntriesList.get(getBindingAdapterPosition()));
            });

            mIvDeleteAdBanner5.setOnClickListener(v -> {
                if (mClickDeleteListener != null)
                    mClickDeleteListener.onAdapterClickDeleteListener(mEntriesList.get(getBindingAdapterPosition()));
            });
        }
    }
}
