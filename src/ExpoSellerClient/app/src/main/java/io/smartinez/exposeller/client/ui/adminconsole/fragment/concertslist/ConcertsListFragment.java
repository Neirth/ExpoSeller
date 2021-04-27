package io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;

@AndroidEntryPoint
public class ConcertsListFragment extends Fragment {
    private ConcertsListViewModel mViewModel;

    private ConstraintLayout mClConcertLayout;
    private TextView mTvConcertListTitle;
    private TextView mTvFriendlyId2;
    private EditText mEtFriendlyId2;
    private ImageView mIvSearchFreindlyId2;
    private TextView mTvDate2;
    private EditText mEtDate2;
    private ImageView mIvSearchDate2;
    private RecyclerView mRvConcertsList;
    private ImageView mIvConcertAdd;

    public static ConcertsListFragment newInstance() {
        return new ConcertsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.concerts_list_fragment, container, false);
    }

    public void initView() {
        mClConcertLayout = getActivity().findViewById(R.id.clConcertLayout);
        mTvConcertListTitle = getActivity().findViewById(R.id.tvConcertListTitle);
        mTvFriendlyId2 = getActivity().findViewById(R.id.tvFriendlyId2);
        mEtFriendlyId2 = getActivity().findViewById(R.id.etFriendlyId2);
        mIvSearchFreindlyId2 = getActivity().findViewById(R.id.ivSearchFreindlyId2);
        mTvDate2 = getActivity().findViewById(R.id.tvDate2);
        mEtDate2 = getActivity().findViewById(R.id.etDate2);
        mIvSearchDate2 = getActivity().findViewById(R.id.ivSearchDate2);
        mRvConcertsList = getActivity().findViewById(R.id.rvConcertsList);
        mIvConcertAdd = getActivity().findViewById(R.id.ivConcertAdd);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConcertsListViewModel.class);
        // TODO: Use the ViewModel
    }

}