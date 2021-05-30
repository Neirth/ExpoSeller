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
package io.smartinez.exposeller.client.ui.buytickets.adapter;

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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.util.listener.OnAdapterClickListener;

public class BuyTicketConcertAdapter extends RecyclerView.Adapter<BuyTicketConcertAdapter.BuyTicketConcertViewHolder> {
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
    public BuyTicketConcertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Set the context instance
        mContext = parent.getContext();

        // Instance a new viewholder
        return new BuyTicketConcertViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_buy_ticket_concert, parent, false));
    }

    /**
     * Method to bind data and entity into viewholder
     *
     * @param holder The instance of viewholder
     * @param position The position in the list
     */
    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull BuyTicketConcertViewHolder holder, int position) {
        if (mConcertList != null) {
            // Get the entity object
            Concert concert = mConcertList.get(position);

            // Get the formatter instance
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
            LocalDateTime dateConcertObj = concert.getEventDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            // Bind data into textview
            holder.mTvConcertName2.setText(concert.getName());
            holder.mTvArtistName2.setText(concert.getArtistName());
            holder.mTvConcertValue2.setText(String.format(Locale.getDefault(), "%.2f", concert.getCost()) + " €");
            holder.mTvConcertDate2.setText(dateConcertObj.format(dateFormatter));

            // Load image into imageview
            Glide.with(mContext).load(concert.getPhotoConcert()).into(holder.mIvPhotoArtist2);
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
    public void setAdapterClickListener(OnAdapterClickListener onAdapterClickListener) {
        this.mOnAdapterClickListener = onAdapterClickListener;
    }

    /**
     * The view holder class
     */
    public class BuyTicketConcertViewHolder extends RecyclerView.ViewHolder {
        // Elements of viewholder
        private CardView mCvConcertCalendar;
        private ConstraintLayout mClConcertCalendar;
        private ImageView mIvPhotoArtist2;
        private TextView mTvArtistName2;
        private TextView mTvConcertDate2;
        private TextView mTvConcertValue2;
        private ImageView mIvArtistName2;
        private ImageView mIvConcertDate2;
        private ImageView mIvConcertValue2;
        private TextView mTvConcertName2;

        public BuyTicketConcertViewHolder(@NonNull View itemView) {
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
            mIvPhotoArtist2 = itemView.findViewById(R.id.ivPhotoArtist2);
            mTvArtistName2 = itemView.findViewById(R.id.tvArtistName2);
            mTvConcertDate2 = itemView.findViewById(R.id.tvConcertDate2);
            mTvConcertValue2 = itemView.findViewById(R.id.tvConcertValue2);
            mIvArtistName2 = itemView.findViewById(R.id.ivArtistName2);
            mIvConcertDate2 = itemView.findViewById(R.id.ivConcertDate2);
            mIvConcertValue2 = itemView.findViewById(R.id.ivConcertValue2);
            mTvConcertName2 = itemView.findViewById(R.id.tvConcertName2);

            // Bind the callback click
            itemView.setOnClickListener(v -> mOnAdapterClickListener.onAdapaterClickListener(mConcertList.get(getBindingAdapterPosition())));
        }
    }
}
