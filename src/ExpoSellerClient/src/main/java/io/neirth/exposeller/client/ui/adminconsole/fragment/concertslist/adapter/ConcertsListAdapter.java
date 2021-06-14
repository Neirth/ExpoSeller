/*
 * Copyright 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.neirth.exposeller.client.ui.adminconsole.fragment.concertslist.adapter;

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

import io.neirth.exposeller.client.R;
import io.neirth.exposeller.client.domain.Concert;
import io.neirth.exposeller.client.util.listener.OnAdapterClickDeleteListener;
import io.neirth.exposeller.client.util.listener.OnAdapterClickEditListener;

public class ConcertsListAdapter extends RecyclerView.Adapter<ConcertsListAdapter.ConcertsListViewHolder> {
    // Context
    private Context mContext;

    // Callbacks
    private OnAdapterClickEditListener mClickEditListener;
    private OnAdapterClickDeleteListener mClickDeleteListener;

    // List of entities
    private List<Concert> mEntitiesList = Collections.emptyList();

    /**
     * Method to create the instance of view holder
     *
     * @param parent The view group
     * @param viewType The view type
     * @return The view holder instanced
     */
    @NonNull
    @Override
    public ConcertsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Set the context instance
        mContext = parent.getContext();

        // Instance a new viewholder
        return new ConcertsListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_concert, parent, false));
    }

    /**
     * Method to bind data and entity into viewholder
     *
     * @param holder The instance of viewholder
     * @param position The position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ConcertsListViewHolder holder, int position) {
        if (mEntitiesList != null) {
            // Get the entity object
            Concert concert = mEntitiesList.get(position);

            // Get the formatter instance
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
            LocalDateTime dateConcertObj = concert.getEventDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            // Load image into imageview
            Glide.with(mContext).load(concert.getPhotoConcert()).into(holder.mIvPhotoArtist4);

            // Bind data into textview
            holder.mTvFriendlyId4.setText(String.valueOf(concert.getFriendlyId()));
            holder.mTvConcertName4.setText(concert.getName());
            holder.mTvEventDate3.setText(dateConcertObj.format(dateFormatter));
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
    public List<Concert> getEntitiesList() {
        return mEntitiesList;
    }

    /**
     * Method to set the list of entities
     *
     * @param entitiesList The list of entities
     */
    public void setEntitiesList(List<Concert> entitiesList) {
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
    public class ConcertsListViewHolder extends RecyclerView.ViewHolder {
        // Elements of viewholder
        private CardView mCvAdminConcert;
        private ConstraintLayout mClConcertCalendar;
        private ImageView mIvPhotoArtist4;
        private TextView mTvConcertName4;
        private TextView mTvFriendlyId4;
        private TextView mTvEventDate3;
        private ImageView mIvEditConcert4;
        private ImageView mIvDeleteConcert4;

        public ConcertsListViewHolder(@NonNull View itemView) {
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
            mCvAdminConcert = itemView.findViewById(R.id.cvAdminConcert);
            mClConcertCalendar = itemView.findViewById(R.id.clConcertCalendar);
            mIvPhotoArtist4 = itemView.findViewById(R.id.ivPhotoArtist4);
            mTvConcertName4 = itemView.findViewById(R.id.tvConcertName4);
            mTvFriendlyId4 = itemView.findViewById(R.id.tvFriendlyId4);
            mTvEventDate3 = itemView.findViewById(R.id.tvEventDate3);
            mIvEditConcert4 = itemView.findViewById(R.id.ivEditConcert4);
            mIvDeleteConcert4 = itemView.findViewById(R.id.ivDeleteConcert4);

            // Bind the callback edit
            itemView.setOnClickListener(v -> {
                if (mClickEditListener != null)
                    mClickEditListener.onAdapterClickEditListener(mEntitiesList.get(getBindingAdapterPosition()));
            });

            // Bind the callback edit
            mIvEditConcert4.setOnClickListener(v -> {
                if (mClickEditListener != null)
                    mClickEditListener.onAdapterClickEditListener(mEntitiesList.get(getBindingAdapterPosition()));
            });

            // Bind the callback delete
            mIvDeleteConcert4.setOnClickListener(v -> {
                if (mClickDeleteListener != null)
                    mClickDeleteListener.onAdapterClickDeleteListener(mEntitiesList.get(getBindingAdapterPosition()));
            });
        }
    }
}
