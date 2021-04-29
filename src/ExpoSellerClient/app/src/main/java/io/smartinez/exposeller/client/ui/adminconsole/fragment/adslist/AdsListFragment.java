package io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist;

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
import io.smartinez.exposeller.client.domain.AdBanner;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.service.AdminService;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist.adapter.AdsListAdapter;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.adsmgt.AdsMgtFragment;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist.ConcertsListViewModel;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist.adapter.ConcertsListAdapter;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertsmgt.ConcertsMgtFragment;
import io.smartinez.exposeller.client.util.FragmentUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static AdsListFragment newInstance() {
        return new AdsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdsListViewModel.class);

        initView();
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

        mAdapter = new AdsListAdapter();

        mViewModel.getListAdBanner().observe(getViewLifecycleOwner(), adBanners -> mAdapter.setEntriesList(adBanners));

        mRvAdsList.setAdapter(mAdapter);
        mRvAdsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter.setOnAdapterClickEditListener(model -> {
            if (model instanceof AdBanner) {
                AdBanner auxConcert = (AdBanner) model;

                Bundle bundle = new Bundle();
                bundle.putParcelable(AdsMgtFragment.EXTRA_DATA, auxConcert);

                FragmentUtils.interchangeFragement(getActivity().getSupportFragmentManager(), R.id.fgAdminLogin, AdsMgtFragment.class, bundle);
            }
        });

        mAdapter.setOnAdapterClickDeleteListener(model -> {
            try {
                if (model instanceof AdBanner) {
                    AdBanner auxAdBanner = (AdBanner) model;

                    mViewModel.getAdBannerRepo().delete(auxAdBanner);
                    mViewModel.notifyListChanges();
                }
            } catch (IOException | IllegalAccessException e) {
                Toast.makeText(getContext(), R.string.admin_entry_delete_error, Toast.LENGTH_SHORT).show();
            }
        });

        mIvAdsAdd.setOnClickListener(v -> FragmentUtils.interchangeFragement(getActivity().getSupportFragmentManager(), R.id.fgAdminLogin, AdsMgtFragment.class));

        mIvSearchDate1.setOnClickListener(v -> {
            if (mEtAdsDate1.getText().length() >= 1) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

                    Date desiredDate = sdf.parse(mEtAdsDate1.getText().toString());

                    mViewModel.searchListModels(AdminService.TypeSearch.TIME_BASED, desiredDate.getTime());
                } catch (ParseException |IllegalAccessException | IOException e) {
                    Toast.makeText(getContext(), R.string.admin_error_process, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mIvSearchFreindlyId1.setOnClickListener(v -> {
            if (mEtFriendlyId1.getText().length() >= 1) {
                try {
                    mViewModel.searchListModels(AdminService.TypeSearch.FRIENDLY_ID, Integer.parseInt(mEtFriendlyId1.getText().toString()));
                } catch (IllegalAccessException | IOException e) {
                    Toast.makeText(getContext(), R.string.admin_error_process, Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            mViewModel.searchListModels(AdminService.TypeSearch.NOT_BEFORE, 0);
        } catch (IllegalAccessException | IOException e) {
            Toast.makeText(getContext(), R.string.admin_error_process, Toast.LENGTH_SHORT).show();
        }
    }
}