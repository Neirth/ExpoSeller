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
    // Elements of fragment
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

    // Adapter of the recycler view
    private ConcertsListAdapter mAdapter;

    // View Model instance
    private ConcertsListViewModel mViewModel;

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
        return inflater.inflate(R.layout.concerts_list_fragment, container, false);
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
        mViewModel = new ViewModelProvider(this).get(ConcertsListViewModel.class);

        // Initialize the components of the fragment
        initView();
        initRecyclerView();
    }

    /**
     * Private method to initialize the components of the fragment
     */
    private void initView() {
        // Bind the elements of the fragment
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

        // Prepare the date format instance
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

        // Prepare the onDateSet callback
        DatePickerDialog.OnDateSetListener onDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            // Set the values into calendar instance
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Modify the text of edit text
            mEtDate2.setText(sdf.format(TimeUtils.removeTimeFromDate(mCalendar.getTime())));
        };

        // Set onClick callback to edittext
        mEtDate2.setOnClickListener(v -> {
            // Hide the keyboard
            Utilities.hideKeyboard(getContext(), v);

            // Open new date picker dialog
            new DatePickerDialog(getActivity(), onDateListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Set onClick callback to add entities button
        mIvConcertAdd.setOnClickListener(v -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, ConcertsMgtFragment.class));

        // Set onClick callback to search date button
        mIvSearchDate2.setOnClickListener(v -> {
            if (mEtDate2.getText().length() >= 1) {
                mViewModel.searchListConcerts(AdminService.TypeSearch.TIME_BASED, mCalendar.getTime().getTime());
            }
        });

        // Set onClick callback to search friendly id button
        mIvSearchFreindlyId2.setOnClickListener(v -> {
            if (mEtFriendlyId2.getText().length() >= 1) {
                mViewModel.searchListConcerts(AdminService.TypeSearch.FRIENDLY_ID, Integer.parseInt(mEtFriendlyId2.getText().toString()));
            }
        });
    }

    /**
     * Private method for initialize the recycler view
     */
    private void initRecyclerView() {
        // Prepare a new instance of adapter
        mAdapter = new ConcertsListAdapter();

        // Observe the list of entities
        mViewModel.getListConcerts().observe(getViewLifecycleOwner(), concerts -> mAdapter.setEntitiesList(concerts));

        // Set the adapter into recycler view with linear layout
        mRvConcertsList.setAdapter(mAdapter);
        mRvConcertsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set the callback edit to adapter
        mAdapter.setOnAdapterClickEditListener(model -> {
            if (model instanceof Concert) {
                // Cast the entity
                Concert auxConcert = (Concert) model;

                // Prepare a bundle with entity
                Bundle bundle = new Bundle();
                bundle.putParcelable(ConcertsMgtFragment.EXTRA_DATA, auxConcert);

                // Transfer data into a new fragment
                FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, ConcertsMgtFragment.class, bundle);
            }
        });

        // Set the callback delete to adapter
        mAdapter.setOnAdapterClickDeleteListener(model -> {
            try {
                if (model instanceof Concert) {
                    // Cast the entity
                    Concert auxConcert = (Concert) model;

                    // Delete the entity
                    mViewModel.getConcertRepo().delete(auxConcert);

                    // Notify the dataset is changed
                    mViewModel.notifyListChanges();
                }
            } catch (IOException e) {
                Utilities.showToast(getActivity(), R.string.admin_entry_delete_error);
            }
        });

        // Search entities with actual date
        mViewModel.searchListConcerts(AdminService.TypeSearch.NOT_BEFORE, 0);
    }
}