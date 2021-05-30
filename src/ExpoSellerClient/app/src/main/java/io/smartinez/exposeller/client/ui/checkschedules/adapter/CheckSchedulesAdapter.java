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
package io.smartinez.exposeller.client.ui.checkschedules.adapter;

import android.annotation.SuppressLint;
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
import java.util.Locale;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.util.listener.OnAdapterClickListener;

public class CheckSchedulesAdapter extends RecyclerView.Adapter<CheckSchedulesAdapter.CheckSchedulesViewHolder> {
    // Context instance
    private Context mContext;

    // Callback of adapter
    private OnAdapterClickListener mOnAdapterClickListener;

    // List of entities
    private List<Concert> mConcertList;

    /**
     * Method to create the instance of view holder
     *
     * @param parent The view group
     * @param viewType The view type
     * @return The view holder instanced
     */
    @NonNull
    @Override
    public CheckSchedulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Set the context instance
        mContext = parent.getContext();

        // Instance a new viewholder
        return new CheckSchedulesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_calendar_schedules, parent, false));
    }

    /**
     * Method to bind data and entity into viewholder
     *
     * @param holder The instance of viewholder
     * @param position The position in the list
     */
    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull CheckSchedulesViewHolder holder, int position) {
        if (mConcertList != null) {
            // Get the entity object
            Concert concert = mConcertList.get(position);

            // Bind data into textview
            holder.mTvArtistName3.setText(concert.getArtistName());
            holder.mTvConcertName3.setText(concert.getName());
            holder.mTvPriceName3.setText(String.format(Locale.getDefault(), "%.2f", concert.getCost()) + " €");

            // Load image into imageview
            Glide.with(mContext).load(concert.getPhotoConcert()).into(holder.mIvPhotoArtist3);
        }
    }

    /**
     * Method to return the item count of the list
     *
     * @return The item count of the list
     */
    @Override
    public int getItemCount() {
        return mConcertList != null ? mConcertList.size() : 0;
    }

    /**
     * Method for get the list instance
     *
     * @return The list instance
     */
    public List<Concert> getConcertList() {
        return mConcertList;
    }

    /**
     * Method to set the list of entities
     *
     * @param concertList The list of entities
     */
    public void setConcertList(List<Concert> concertList) {
        // Set the list of concerts
        this.mConcertList = concertList;

        // Notify the dataset has changed
        notifyDataSetChanged();
    }

    /**
     * Method to set on click callback
     *
     * @param onAdapterClickListener The click callback
     */
    public void setOnAdapterClickListener(OnAdapterClickListener onAdapterClickListener) {
        this.mOnAdapterClickListener = onAdapterClickListener;
    }

    /**
     * The view holder class
     */
    public class CheckSchedulesViewHolder extends RecyclerView.ViewHolder {
        // Elements of viewholder
        private CardView mCvConcertCalendar;
        private ConstraintLayout mClConcertCalendar;
        private ImageView mIvPhotoArtist3;
        private TextView mTvConcertName3;
        private TextView mTvArtistName3;
        private TextView mTvPriceName3;

        public CheckSchedulesViewHolder(@NonNull View itemView) {
            // Call to parent constructor
            super(itemView);

            // Initialize the view holder
            initHolder();
        }

        /**
         * Private method for initialize the view holder
         */
        public void initHolder() {
            // Bind the elements of the view holder
            mCvConcertCalendar = itemView.findViewById(R.id.cvConcertCalendar);
            mClConcertCalendar = itemView.findViewById(R.id.clConcertCalendar);
            mIvPhotoArtist3 = itemView.findViewById(R.id.ivPhotoArtist3);
            mTvConcertName3 = itemView.findViewById(R.id.tvConcertName3);
            mTvArtistName3 = itemView.findViewById(R.id.tvArtistName3);
            mTvPriceName3 = itemView.findViewById(R.id.tvPriceName3);

            // Bind the callback click
            itemView.setOnClickListener(v -> mOnAdapterClickListener.onAdapaterClickListener(mConcertList.get(getBindingAdapterPosition())));
        }
    }
}
