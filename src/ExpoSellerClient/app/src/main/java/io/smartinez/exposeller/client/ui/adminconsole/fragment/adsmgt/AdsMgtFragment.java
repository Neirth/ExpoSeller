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
package io.smartinez.exposeller.client.ui.adminconsole.fragment.adsmgt;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist.AdsListFragment;
import io.smartinez.exposeller.client.util.FragmentUtils;
import io.smartinez.exposeller.client.util.TimeUtils;
import io.smartinez.exposeller.client.util.Utilities;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.TimeZone;

@AndroidEntryPoint
public class AdsMgtFragment extends Fragment {
    // Constant with extra id
    public final static String EXTRA_DATA = "io.smartinez.exposeller.client.ui.adminconsole.fragment.adsmgt.AdsMgtFragment.data";

    // Elements of fragment
    private TextView mTvAdsTitleForm;
    private TextView mTvAdsName;
    private EditText mEtAdsName;
    private TextView mTvAdsDate;
    private EditText mEtAdsDate;
    private TextView mTvAdsPhotoUri;
    private ImageView mIvAdsPhotoUri;
    private EditText mEtAdsPhotoUri;
    private Button mBtnAdsFormOk;
    private Button mBtnAdsFormCancel;

    // View Model of fragment
    private AdsMgtViewModel mViewModel;

    // The entity object
    private Advertisement mAdvertisement;

    // The calendar instance
    private Calendar mCalendar = Calendar.getInstance();

    /**
     * Method to create the view instance
     *
     * @param inflater The inflater instance
     * @param container The parent view
     * @param savedInstanceState A saved instance of the fragment
     * @return The view instance
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_mgt_fragment, container, false);
    }

    /**
     * Method to initialize the components of the fragment
     *
     * @param view The view instance
     * @param savedInstanceState A saved instance of the fragment
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        // Call to parent method
        super.onViewCreated(view, savedInstanceState);

        // Instance the view model
        mViewModel = new ViewModelProvider(this).get(AdsMgtViewModel.class);

        // Initialize the components of the fragment
        initView();
        initButtonActions();
    }

    /**
     * Private method to initialize the components of the fragment
     */
    private void initView() {
        // Bind the elements of the fragment
        mTvAdsTitleForm = getActivity().findViewById(R.id.tvAdsTitleForm);
        mTvAdsName = getActivity().findViewById(R.id.tvAdsName);
        mEtAdsName = getActivity().findViewById(R.id.etAdsName);
        mTvAdsDate = getActivity().findViewById(R.id.tvAdsDate);
        mEtAdsDate = getActivity().findViewById(R.id.etAdsDate);
        mTvAdsPhotoUri = getActivity().findViewById(R.id.tvAdsPhotoUri);
        mIvAdsPhotoUri = getActivity().findViewById(R.id.ivAdsPhotoUri);
        mEtAdsPhotoUri = getActivity().findViewById(R.id.etAdsPhotoUri);
        mBtnAdsFormOk = getActivity().findViewById(R.id.btnAdsFormOk);
        mBtnAdsFormCancel = getActivity().findViewById(R.id.btnAdsFormCancel);

        // Set the calendar timezone
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Prepare instance of date format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);

        // Prepare callback of onTimeSet
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, hour, minute) -> {
            // Set the hour and the minute in calendar instance
            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);

            // Prepare a localdatetime instance from calendar
            LocalDateTime adBannerDateObj = mCalendar.toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            // Format the date
            mEtAdsDate.setText(adBannerDateObj.format(dateFormatter));
        };

        // Prepare callback of onDateSet
        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            // Set the date into calendar instance
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Open the time picker dialog
            new TimePickerDialog(getActivity(), onTimeSetListener, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true).show();
        };

        // Set the onClickListener callback
        mEtAdsDate.setOnClickListener(v -> {
            // Hide the keyboard
            Utilities.hideKeyboard(getContext(), v);

            // Open the date picker dialog
            new DatePickerDialog(getActivity(), onDateSetListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Set the callback for detect stop typing
        mEtAdsPhotoUri.setOnEditorActionListener((v, actionId, event) -> {
            // Detect if is stop typing and load the image
            if (actionId == EditorInfo.IME_ACTION_DONE && (event == null || !event.isShiftPressed())) {
                Glide.with(this).load(mEtAdsPhotoUri.getText().toString()).into(mIvAdsPhotoUri);
                return true;
            } else {
                return false;
            }
        });

        // Detect if the fragment gets arguments
        if (getArguments() != null && getArguments().getParcelable(AdsMgtFragment.EXTRA_DATA) != null) {
            // Recover the argument
            mAdvertisement = getArguments().getParcelable(AdsMgtFragment.EXTRA_DATA);

            // Prepare the instance of localdatetime
            LocalDateTime adBannerDateObj = mAdvertisement.getEventDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            // Set the title text to edit entity
            mTvAdsTitleForm.setText(R.string.admin_edit_ads);

            // Bind data into textview
            mEtAdsName.setText(mAdvertisement.getName());
            mEtAdsDate.setText(adBannerDateObj.format(dateFormatter));
            mEtAdsPhotoUri.setText(mAdvertisement.getPhotoAd());
            mCalendar.setTime(mAdvertisement.getEventDate());

            // Load image into image view
            Glide.with(this).load(mEtAdsPhotoUri.getText().toString()).into(mIvAdsPhotoUri);
        } else {
            // Set the title text to new entity
            mTvAdsTitleForm.setText(R.string.admin_new_ads);

            // Set the advertisement object to null
            mAdvertisement = null;
        }
    }

    /**
     * Private method to initialize the actions of buttons
     */
    private void initButtonActions() {
        // Set the callback of Ok button
        mBtnAdsFormOk.setOnClickListener(v -> {
            try {
                if (mAdvertisement != null && mAdvertisement.getDocId() != null) {
                    // Set the value into advertisement object
                    mAdvertisement.setName(mEtAdsName.getText().toString());
                    mAdvertisement.setEventDate(TimeUtils.fixTimezone(mCalendar.getTime()));
                    mAdvertisement.setPhotoAd(mEtAdsPhotoUri.getText().toString());

                    // Update the registry in background
                    mViewModel.getExecutorService().submit(() -> {
                        try {
                            // Update the registry
                            mViewModel.getAdvertisementRepo().update(mAdvertisement);

                            // Interchange the fragment container to list
                            getActivity().runOnUiThread(() -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, AdsListFragment.class));
                        } catch (IOException e) {
                            // Show the operation error
                            getActivity().runOnUiThread(() -> Utilities.showToast(getActivity(), R.string.admin_error_process));
                        }
                    });
                } else {
                    // Create new instance of entity
                    mAdvertisement = new Advertisement(null, mEtAdsName.getText().toString(), mEtAdsPhotoUri.getText().toString(), null, TimeUtils.fixTimezone(mCalendar.getTime()));

                    // Insert the registry in background
                    mViewModel.getExecutorService().submit(() -> {
                        try {
                            // Generate a new friendlyid
                            mAdvertisement.setFriendlyId(mViewModel.generateFriendlyId());

                            // Insert the registry
                            mViewModel.getAdvertisementRepo().insert(mAdvertisement);

                            // Interchange the fragment container to list
                            getActivity().runOnUiThread(() -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, AdsListFragment.class));
                        } catch (IOException e) {
                            // Show the operation error
                            getActivity().runOnUiThread(() -> Utilities.showToast(getActivity(), R.string.admin_error_process));
                        }
                    });
                }
            } catch (NumberFormatException e) {
                // Show the operation error
                Utilities.showToast(getActivity(), R.string.admin_error_process);
            }
        });

        // Set Cancel callback
        mBtnAdsFormCancel.setOnClickListener(v -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, AdsListFragment.class));
    }
}