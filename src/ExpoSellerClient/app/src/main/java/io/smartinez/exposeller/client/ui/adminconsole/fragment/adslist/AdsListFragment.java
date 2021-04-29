package io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.service.AdminService;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist.adapter.AdsListAdapter;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.adsmgt.AdsMgtFragment;
import io.smartinez.exposeller.client.util.FragmentUtils;

import io.smartinez.exposeller.client.util.Utilities;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@AndroidEntryPoint
public class AdsListFragment extends Fragment {
    private AdsListViewModel mViewModel;

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
    private AdsListAdapter mAdapter;

    private Calendar mCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdsListViewModel.class);

        initView();
        initRecyclerView();
    }

    private void initView() {
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

        DatePickerDialog.OnDateSetListener onDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            mEtAdsDate1.setText(sdf.format(mCalendar.getTime()));
        };

        mEtAdsDate1.setOnClickListener(v -> {
            Utilities.hideKeyboard(getContext(), v);
            new DatePickerDialog(getActivity(), onDateListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        mIvAdsAdd.setOnClickListener(v -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface,AdsMgtFragment.class));

        mIvSearchDate1.setOnClickListener(v -> {
            if (mEtAdsDate1.getText().length() >= 1) {
                try {
                    Date desiredDate = sdf.parse(mEtAdsDate1.getText().toString());

                    mViewModel.searchListAdBanners(AdminService.TypeSearch.TIME_BASED, desiredDate.getTime());
                } catch (ParseException e) {
                    Utilities.showToast(getActivity(), R.string.admin_error_process);
                }
            }
        });

        mIvSearchFreindlyId1.setOnClickListener(v -> {
            if (mEtFriendlyId1.getText().length() >= 1) {
                mViewModel.searchListAdBanners(AdminService.TypeSearch.FRIENDLY_ID, Integer.parseInt(mEtFriendlyId1.getText().toString()));
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new AdsListAdapter();

        mViewModel.getListAdBanner().observe(getViewLifecycleOwner(), adBanners -> mAdapter.setEntriesList(adBanners));

        mRvAdsList.setAdapter(mAdapter);
        mRvAdsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter.setOnAdapterClickEditListener(model -> {
            if (model instanceof Advertisement) {
                Advertisement auxConcert = (Advertisement) model;

                Bundle bundle = new Bundle();
                bundle.putParcelable(AdsMgtFragment.EXTRA_DATA, auxConcert);

                FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface,AdsMgtFragment.class, bundle);
            }
        });

        mAdapter.setOnAdapterClickDeleteListener(model -> {
            try {
                if (model instanceof Advertisement) {
                    Advertisement auxAdvertisement = (Advertisement) model;

                    mViewModel.getAdBannerRepo().delete(auxAdvertisement);
                    mViewModel.notifyListChanges();
                }
            } catch (IOException e) {
                Utilities.showToast(getActivity(), R.string.admin_entry_delete_error);
            }
        });

        mViewModel.searchListAdBanners(AdminService.TypeSearch.NOT_BEFORE, 0);
    }
}