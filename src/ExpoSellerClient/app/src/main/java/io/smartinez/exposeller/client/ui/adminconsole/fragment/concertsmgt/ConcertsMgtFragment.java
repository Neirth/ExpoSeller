package io.smartinez.exposeller.client.ui.adminconsole.fragment.concertsmgt;

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
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist.ConcertsListFragment;
import io.smartinez.exposeller.client.util.FragmentUtils;
import io.smartinez.exposeller.client.util.Utilities;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.TimeZone;

@AndroidEntryPoint
public class ConcertsMgtFragment extends Fragment {
    // Constant with extra id
    public final static String EXTRA_DATA = "io.smartinez.exposeller.client.ui.adminconsole.fragment.concertsmgt.ConcertsMgtFragments.data";

    // Elements of fragment
    private TextView mTvTitleConcertForm;
    private TextView mTvConcertName;
    private EditText mEtConcertName;
    private TextView mTvConcertTextValue;
    private EditText mEtConcertTextValue;
    private TextView mTvArtistTextName;
    private EditText mEtArtistTextName;
    private TextView mTvConciertDate;
    private EditText mEtConcertDate;
    private TextView mTvOrganizationTextName;
    private EditText mEtOrganizationTextName;
    private TextView mTvConcertPhotoTextUri;
    private ImageView mIvConcertPhotoTextUri;
    private EditText mEtConcertPhotoTextUri;
    private Button mBtnConcertFormOk;
    private Button mBtnConcertFormCancel;

    // View Model of fragment
    private ConcertsMgtViewModel mViewModel;

    // The entity object
    private Concert mConcert;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.concerts_mgt_fragment, container, false);
    }

    /**
     * Method to initialize the components of the fragment
     *
     * @param view The view instance
     * @param savedInstanceState A saved instance of the fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Call to parent method
        super.onViewCreated(view, savedInstanceState);

        // Instance the view model
        mViewModel = new ViewModelProvider(this).get(ConcertsMgtViewModel.class);

        // Initialize the components of the fragment
        initView();
        initButtonActions();
    }

    public void initView() {
        // Bind the elements of the fragment
        mTvTitleConcertForm = getActivity().findViewById(R.id.tvTitleConcertForm);
        mTvConcertName = getActivity().findViewById(R.id.tvConcertName);
        mEtConcertName = getActivity().findViewById(R.id.etConcertName);
        mTvConcertTextValue = getActivity().findViewById(R.id.tvConcertTextValue);
        mEtConcertTextValue = getActivity().findViewById(R.id.etConcertTextValue);
        mTvArtistTextName = getActivity().findViewById(R.id.tvArtistTextName);
        mEtArtistTextName = getActivity().findViewById(R.id.etArtistTextName);
        mTvConciertDate = getActivity().findViewById(R.id.tvConciertDate);
        mEtConcertDate = getActivity().findViewById(R.id.etConcertDate);
        mTvOrganizationTextName = getActivity().findViewById(R.id.tvOrganizationTextName);
        mEtOrganizationTextName = getActivity().findViewById(R.id.etOrganizationTextName);
        mTvConcertPhotoTextUri = getActivity().findViewById(R.id.tvConcertPhotoTextUri);
        mIvConcertPhotoTextUri = getActivity().findViewById(R.id.ivConcertPhotoTextUri);
        mEtConcertPhotoTextUri = getActivity().findViewById(R.id.etConcertPhotoTextUri);
        mBtnConcertFormOk = getActivity().findViewById(R.id.btnConcertFormOk);
        mBtnConcertFormCancel = getActivity().findViewById(R.id.btnConcertFormCancel);

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
            LocalDateTime dateConcertObj = mCalendar.toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            // Format the date
            mEtConcertDate.setText(dateConcertObj.format(dateFormatter));
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
        mEtConcertDate.setOnClickListener(v -> {
            // Hide the keyboard
            Utilities.hideKeyboard(getActivity(), v);

            // Open the date picker dialog
            new DatePickerDialog(getActivity(), onDateSetListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Set the callback for detect stop typing
        mEtConcertPhotoTextUri.setOnEditorActionListener((v, actionId, event) -> {
            // Detect if is stop typing and load the image
            if (actionId == EditorInfo.IME_ACTION_DONE && (event == null || !event.isShiftPressed())) {
                Glide.with(this).load(mEtConcertPhotoTextUri.getText().toString()).into(mIvConcertPhotoTextUri);
                return true;
            } else {
                return false;
            }
        });

        // Detect if the fragment gets arguments
        if (getArguments() != null && getArguments().getParcelable(ConcertsMgtFragment.EXTRA_DATA) != null) {
            // Recover the argument
            mConcert = getArguments().getParcelable(ConcertsMgtFragment.EXTRA_DATA);

            // Prepare the instance of localdatetime
            LocalDateTime dateConcertObj = mConcert.getEventDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            // Set the title text to edit entity
            mTvTitleConcertForm.setText(R.string.admin_edit_concert);

            // Bind data into textview
            mEtConcertName.setText(mConcert.getName());
            mEtConcertTextValue.setText(String.valueOf(mConcert.getCost()));
            mEtArtistTextName.setText(mConcert.getArtistName());
            mEtConcertDate.setText(dateConcertObj.format(dateFormatter));
            mEtOrganizationTextName.setText(mConcert.getOrganizationName());
            mEtConcertPhotoTextUri.setText(mConcert.getPhotoConcert());
            mCalendar.setTime(mConcert.getEventDate());

            // Load image into image view
            Glide.with(this).load(mEtConcertPhotoTextUri.getText().toString()).into(mIvConcertPhotoTextUri);
        } else {
            // Set the title text to new entity
            mTvTitleConcertForm.setText(R.string.admin_new_concert);

            // Set the advertisement object to null
            mConcert = null;
        }
    }

    /**
     * Private method to initialize the actions of buttons
     */
    private void initButtonActions() {
        // Set the callback of Ok button
        mBtnConcertFormOk.setOnClickListener(v -> {
            try {
                if (mConcert != null && mConcert.getDocId() != null) {
                    // Set the value into advertisement object
                    mConcert.setName(mEtConcertName.getText().toString());
                    mConcert.setCost(Float.parseFloat(mEtConcertTextValue.getText().toString()));
                    mConcert.setPhotoConcert(mEtConcertPhotoTextUri.getText().toString());
                    mConcert.setArtistName(mEtArtistTextName.getText().toString());
                    mConcert.setEventDate(mCalendar.getTime());
                    mConcert.setOrganizationName(mEtOrganizationTextName.getText().toString());

                    // Update the registry in background
                    mViewModel.getExecutorService().submit(() -> {
                        try {
                            // Update the registry
                            mViewModel.getConcertRepo().update(mConcert);

                            // Interchange the fragment container to list
                            getActivity().runOnUiThread(() -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, ConcertsListFragment.class));
                        } catch (IOException e) {
                            // Show the operation error
                            getActivity().runOnUiThread(() -> Utilities.showToast(getActivity(), R.string.admin_error_process));
                        }
                    });
                } else {
                    // Create new instance of entity
                    mConcert = new Concert(null, mEtConcertName.getText().toString(),
                            Float.parseFloat(mEtConcertTextValue.getText().toString()), mEtConcertPhotoTextUri.getText().toString(),
                            mEtArtistTextName.getText().toString(), mCalendar.getTime(), null, mEtOrganizationTextName.getText().toString());

                    // Insert the registry in background
                    mViewModel.getExecutorService().submit(() -> {
                        try {
                            // Generate a new friendlyid
                            mConcert.setFriendlyId(mViewModel.generateFriendlyId());

                            // Insert the registry
                            mViewModel.getConcertRepo().insert(mConcert);

                            // Interchange the fragment container to list
                            getActivity().runOnUiThread(() -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, ConcertsListFragment.class));
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
        mBtnConcertFormCancel.setOnClickListener(v -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, ConcertsListFragment.class));
    }
}