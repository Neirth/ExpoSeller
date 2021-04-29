package io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist;

import android.app.DatePickerDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.service.AdminService;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist.adapter.ConcertsListAdapter;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertsmgt.ConcertsMgtFragment;
import io.smartinez.exposeller.client.util.FragmentUtils;

import io.smartinez.exposeller.client.util.TimeUtils;
import io.smartinez.exposeller.client.util.Utilities;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private ConcertsListAdapter mAdapter;

    private Calendar mCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.concerts_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConcertsListViewModel.class);

        initView();
        initRecyclerView();
    }

    private void initView() {
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

        DatePickerDialog.OnDateSetListener onDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            mEtDate2.setText(sdf.format(TimeUtils.removeTimeFromDate(mCalendar.getTime())));
        };

        mEtDate2.setOnClickListener(v -> {
            Utilities.hideKeyboard(getContext(), v);
            new DatePickerDialog(getActivity(), onDateListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        mIvSearchDate2.setOnClickListener(v -> {
            if (mEtDate2.getText().length() >= 1) {
                try {
                    Date desiredDate = sdf.parse(mEtDate2.getText().toString());

                    mViewModel.searchListConcerts(AdminService.TypeSearch.TIME_BASED, desiredDate.getTime());
                } catch (ParseException e) {
                    Utilities.showToast(getActivity(), R.string.admin_error_process);
                }
            }
        });

        mIvSearchFreindlyId2.setOnClickListener(v -> {
            if (mEtFriendlyId2.getText().length() >= 1) {
                mViewModel.searchListConcerts(AdminService.TypeSearch.FRIENDLY_ID, Integer.parseInt(mEtFriendlyId2.getText().toString()));
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new ConcertsListAdapter();

        mRvConcertsList.setAdapter(mAdapter);
        mRvConcertsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.getListConcerts().observe(getViewLifecycleOwner(), concerts -> mAdapter.setEntriesList(concerts));

        mAdapter.setOnAdapterClickEditListener(model -> {
            if (model instanceof Concert) {
                Concert auxConcert = (Concert) model;

                Bundle bundle = new Bundle();
                bundle.putParcelable(ConcertsMgtFragment.EXTRA_DATA, auxConcert);

                FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, ConcertsMgtFragment.class, bundle);
            }
        });

        mAdapter.setOnAdapterClickDeleteListener(model -> {
            try {
                if (model instanceof Concert) {
                    Concert auxConcert = (Concert) model;

                    mViewModel.getConcertRepo().delete(auxConcert);
                    mViewModel.notifyListChanges();
                }
            } catch (IOException e) {
                Utilities.showToast(getActivity(), R.string.admin_entry_delete_error);
            }
        });

        mIvConcertAdd.setOnClickListener(v -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, ConcertsMgtFragment.class));
        mViewModel.searchListConcerts(AdminService.TypeSearch.NOT_BEFORE, 0);
    }
}