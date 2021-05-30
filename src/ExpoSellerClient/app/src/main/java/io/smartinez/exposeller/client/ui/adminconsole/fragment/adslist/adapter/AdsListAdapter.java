/*
 * MIT License
 *
 * Copyright (c) 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.util.listener.OnAdapterClickDeleteListener;
import io.smartinez.exposeller.client.util.listener.OnAdapterClickEditListener;

public class AdsListAdapter extends RecyclerView.Adapter<AdsListAdapter.AdsListViewHolder> {
    // Context
    private Context mContext;

    // Callbacks
    private OnAdapterClickEditListener mClickEditListener;
    private OnAdapterClickDeleteListener mClickDeleteListener;

    // List of entities
    private List<Advertisement> mEntitiesList = Collections.emptyList();

    /**
     * Method to create the instance of view holder
     *
     * @param parent The view group
     * @param viewType The view type
     * @return The view holder instanced
     */
    @NonNull
    @Override
    public AdsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Set the context instance
        mContext = parent.getContext();

        // Instance a new viewholder
        return new AdsListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_adbanner, parent, false));
    }

    /**
     * Method to bind data and entity into viewholder
     *
     * @param holder The instance of viewholder
     * @param position The position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull AdsListViewHolder holder, int position) {
        if (mEntitiesList != null) {
            // Get the entity object
            Advertisement advertisement = mEntitiesList.get(position);

            // Get the formatter instance
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
            LocalDateTime adBannerDateObj = advertisement.getEventDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            // Load image into imageview
            Glide.with(mContext).load(advertisement.getPhotoAd()).into(holder.mIvPhotoArtist5);

            // Bind data into textview
            holder.mTvFriendlyId5.setText(String.valueOf(advertisement.getFriendlyId()));
            holder.mTvAdBannerName5.setText(advertisement.getName());
            holder.mTvEventDate3.setText(adBannerDateObj.format(dateFormatter));
        }
    }

    /**
     * Method to return the item count of the list
     *
     * @return The item count of the list
     */
    @Override
    public int getItemCount() {
        return mEntitiesList.size();
    }

    /**
     * Method for get the list instance
     *
     * @return The list instance
     */
    public List<Advertisement> getEntitiesList() {
        return mEntitiesList;
    }

    /**
     * Method to set the list of entities
     *
     * @param entitiesList The list of entities
     */
    public void setEntitiesList(List<Advertisement> entitiesList) {
        // Set the elements in the list
        mEntitiesList = entitiesList;

        // Notify the data set is changed
        notifyDataSetChanged();
    }

    /**
     * Method to set on click edit callback
     *
     * @param clickEditListener The edit callback
     */
    public void setOnAdapterClickEditListener(OnAdapterClickEditListener clickEditListener) {
        this.mClickEditListener = clickEditListener;
    }

    /**
     * Method to set on click delete callback
     *
     * @param clickDeleteListener The delete callback
     */
    public void setOnAdapterClickDeleteListener(OnAdapterClickDeleteListener clickDeleteListener){
        this.mClickDeleteListener = clickDeleteListener;
    }

    /**
     * The view holder class
     */
    public class AdsListViewHolder extends RecyclerView.ViewHolder {
        // Elements of viewholder
        private CardView mCvAdminAdBanner;
        private ConstraintLayout mClConcertCalendar;
        private ImageView mIvPhotoArtist5;
        private TextView mTvAdBannerName5;
        private TextView mTvFriendlyId5;
        private TextView mTvEventDate3;
        private ImageView mIvDeleteAdBanner5;
        private ImageView mIvEditAdBanner5;

        public AdsListViewHolder(@NonNull View itemView) {
            // Call to parent constructor
            super(itemView);

            // Initialize the view holder
            initView();
        }

        /**
         * Private method for initialize the view holder
         */
        private void initView() {
            // Bind the elements of the view holder
            mCvAdminAdBanner = itemView.findViewById(R.id.cvAdminAdBanner);
            mClConcertCalendar = itemView.findViewById(R.id.clConcertCalendar);
            mIvPhotoArtist5 = itemView.findViewById(R.id.ivPhotoArtist5);
            mTvAdBannerName5 = itemView.findViewById(R.id.tvAdBannerName5);
            mTvFriendlyId5 = itemView.findViewById(R.id.tvFriendlyId5);
            mTvEventDate3 = itemView.findViewById(R.id.tvEventDate3);
            mIvDeleteAdBanner5 = itemView.findViewById(R.id.ivDeleteAdBanner5);
            mIvEditAdBanner5 = itemView.findViewById(R.id.ivEditAdBanner5);

            // Bind the callback edit
            itemView.setOnClickListener(v -> {
                if (mClickEditListener != null)
                    mClickEditListener.onAdapterClickEditListener(mEntitiesList.get(getBindingAdapterPosition()));
            });

            // Bind the callback edit
            mIvEditAdBanner5.setOnClickListener(v -> {
                if (mClickEditListener != null)
                    mClickEditListener.onAdapterClickEditListener(mEntitiesList.get(getBindingAdapterPosition()));
            });

            // Bind the callback delete
            mIvDeleteAdBanner5.setOnClickListener(v -> {
                if (mClickDeleteListener != null)
                    mClickDeleteListener.onAdapterClickDeleteListener(mEntitiesList.get(getBindingAdapterPosition()));
            });
        }
    }
}
