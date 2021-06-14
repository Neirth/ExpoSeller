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
package io.neirth.exposeller.client.ui.adminconsole.fragment.adslist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.hilt.android.AndroidEntryPoint;
import io.neirth.exposeller.client.R;
import io.neirth.exposeller.client.domain.Advertisement;
import io.neirth.exposeller.client.service.AdminService;
import io.neirth.exposeller.client.ui.adminconsole.fragment.adslist.adapter.AdsListAdapter;
import io.neirth.exposeller.client.ui.adminconsole.fragment.adsmgt.AdsMgtFragment;
import io.neirth.exposeller.client.util.FragmentUtils;

import io.neirth.exposeller.client.util.Utilities;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@AndroidEntryPoint
public class AdsListFragment extends Fragment {
    // Elements of fragment
    private ConstraintLayout mClAdsLayout;
    private TextView mTvAdsListTitle;
    private TextView mTvFriendlyId1;
    private EditText mEtFriendlyId1;
    private TextView mTvAdsDate1;
    private EditText mEtAdsDate1;
    private ImageView mIvSearchFreindlyId1;
    private ImageView mIvSearchDate1;
    private RecyclerView mRvAdsList;
    private ImageView mIvAdsAdd;

    // Adapter of the recycler view
    private AdsListAdapter mAdapter;

    // View Model instance
    private AdsListViewModel mViewModel;

    // Calendar instance
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
        return inflater.inflate(R.layout.ads_list_fragment, container, false);
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
        mViewModel = new ViewModelProvider(this).get(AdsListViewModel.class);

        // Initialize the components of the fragment
        initView();
        initRecyclerView();
    }

    /**
     * Private method to initialize the components of the fragment
     */
    private void initView() {
        // Bind the elements of the fragment
        mClAdsLayout = getActivity().findViewById(R.id.clAdsLayout);
        mTvAdsListTitle = getActivity().findViewById(R.id.tvAdsListTitle);
        mTvFriendlyId1 = getActivity().findViewById(R.id.tvFriendlyId1);
        mEtFriendlyId1 = getActivity().findViewById(R.id.etFriendlyId1);
        mTvAdsDate1 = getActivity().findViewById(R.id.tvAdsDate1);
        mEtAdsDate1 = getActivity().findViewById(R.id.etAdsDate1);
        mIvSearchFreindlyId1 = getActivity().findViewById(R.id.ivSearchFreindlyId1);
        mIvSearchDate1 = getActivity().findViewById(R.id.ivSearchDate1);
        mRvAdsList = getActivity().findViewById(R.id.rvAdsList);
        mIvAdsAdd = getActivity().findViewById(R.id.ivAdsAdd);

        // Prepare the date format instance
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

        // Prepare the onDateSet callback
        DatePickerDialog.OnDateSetListener onDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            // Set the values into calendar instance
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Modify the text of edit text
            mEtAdsDate1.setText(sdf.format(mCalendar.getTime()));
        };

        // Set onClick callback to edittext
        mEtAdsDate1.setOnClickListener(v -> {
            // Hide the keyboard
            Utilities.hideKeyboard(getContext(), v);

            // Open new date picker dialog
            new DatePickerDialog(getActivity(), onDateListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Set onClick callback to add entities button
        mIvAdsAdd.setOnClickListener(v -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, AdsMgtFragment.class));

        // Set onClick callback to search date button
        mIvSearchDate1.setOnClickListener(v -> {
            if (mEtAdsDate1.getText().length() >= 1) {
                mViewModel.searchListAdBanners(AdminService.TypeSearch.TIME_BASED, mCalendar.getTime().getTime());
            }
        });

        // Set onClick callback to search friendly id button
        mIvSearchFreindlyId1.setOnClickListener(v -> {
            if (mEtFriendlyId1.getText().length() >= 1) {
                mViewModel.searchListAdBanners(AdminService.TypeSearch.FRIENDLY_ID, Integer.parseInt(mEtFriendlyId1.getText().toString()));
            }
        });
    }

    /**
     * Private method for initialize the recycler view
     */
    private void initRecyclerView() {
        // Prepare a new instance of adapter
        mAdapter = new AdsListAdapter();

        // Observe the list of entities
        mViewModel.getListAdBanner().observe(getViewLifecycleOwner(), adBanners -> mAdapter.setEntitiesList(adBanners));

        // Set the adapter into recycler view with linear layout
        mRvAdsList.setAdapter(mAdapter);
        mRvAdsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set the callback edit to adapter
        mAdapter.setOnAdapterClickEditListener(model -> {
            if (model instanceof Advertisement) {
                // Cast the entity
                Advertisement auxConcert = (Advertisement) model;

                // Prepare a bundle with entity
                Bundle bundle = new Bundle();
                bundle.putParcelable(AdsMgtFragment.EXTRA_DATA, auxConcert);

                // Transfer data into a new fragment
                FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface,AdsMgtFragment.class, bundle);
            }
        });

        // Set the callback delete to adapter
        mAdapter.setOnAdapterClickDeleteListener(model -> {
            try {
                if (model instanceof Advertisement) {
                    // Cast the entity
                    Advertisement auxAdvertisement = (Advertisement) model;

                    // Delete the entity
                    mViewModel.getAdvertisementRepo().delete(auxAdvertisement);

                    // Notify the dataset is changed
                    mViewModel.notifyListChanges();
                }
            } catch (IOException e) {
                Utilities.showToast(getActivity(), R.string.admin_entry_delete_error);
            }
        });

        // Search entities with actual date
        mViewModel.searchListAdBanners(AdminService.TypeSearch.NOT_BEFORE, 0);
    }
}