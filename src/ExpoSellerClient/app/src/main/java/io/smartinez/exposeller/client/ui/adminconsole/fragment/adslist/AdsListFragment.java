package io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist;

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
import androidx.recyclerview.widget.RecyclerView;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist.ConcertsListViewModel;
import org.jetbrains.annotations.NotNull;

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
    }
}