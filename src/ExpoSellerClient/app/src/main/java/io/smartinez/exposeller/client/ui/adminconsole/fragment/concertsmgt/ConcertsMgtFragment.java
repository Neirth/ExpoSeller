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
import io.smartinez.exposeller.client.util.TimeUtils;
import io.smartinez.exposeller.client.util.Utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

@AndroidEntryPoint
public class ConcertsMgtFragment extends Fragment {
    public final static String EXTRA_DATA = "io.smartinez.exposeller.client.ui.adminconsole.fragment.concertsmgt.ConcertsMgtFragments.data";
    private ConcertsMgtViewModel mViewModel;

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

    private Concert mConcert;
    private Calendar mCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.concerts_mgt_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConcertsMgtViewModel.class);

        initView();
        initButtonActions();
    }

    public void initView() {
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

        Glide.with(this).load(mEtConcertPhotoTextUri.getText().toString()).into(mIvConcertPhotoTextUri);

        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);

        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, hour, minute) -> {
            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);

            LocalDateTime dateConcertObj = mCalendar.toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            mEtConcertDate.setText(dateConcertObj.format(dateFormatter));
        };

        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(getActivity(), onTimeSetListener, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true).show();
        };

        mEtConcertDate.setOnClickListener(v -> {
            Utilities.hideKeyboard(getActivity(), v);
            new DatePickerDialog(getActivity(), onDateSetListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        mEtConcertPhotoTextUri.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (event == null || !event.isShiftPressed()) {
                    Glide.with(this).load(mEtConcertPhotoTextUri.getText().toString()).into(mIvConcertPhotoTextUri);
                    return true;
                }
            }

            return false;
        });

        if (getArguments() != null && getArguments().getParcelable(ConcertsMgtFragment.EXTRA_DATA) != null) {
            mConcert = getArguments().getParcelable(ConcertsMgtFragment.EXTRA_DATA);

            mTvTitleConcertForm.setText(R.string.admin_edit_concert);

            LocalDateTime dateConcertObj = mConcert.getEventDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            mEtConcertName.setText(mConcert.getName());
            mEtConcertTextValue.setText(String.valueOf(mConcert.getCost()));
            mEtArtistTextName.setText(mConcert.getArtistName());
            mEtConcertDate.setText(dateConcertObj.format(dateFormatter));
            mEtOrganizationTextName.setText(mConcert.getOrganizationName());
            mEtConcertPhotoTextUri.setText(mConcert.getPhotoConcert());

            mCalendar.setTime(mConcert.getEventDate());
        } else {
            mTvTitleConcertForm.setText(R.string.admin_new_concert);

            mConcert = null;
        }
    }

    private void initButtonActions() {
        mBtnConcertFormOk.setOnClickListener(v -> {
            try {
                if (mConcert != null && mConcert.getDocId() != null) {
                    mConcert.setName(mEtConcertName.getText().toString());
                    mConcert.setCost(Float.parseFloat(mEtConcertTextValue.getText().toString()));
                    mConcert.setPhotoConcert(mEtConcertPhotoTextUri.getText().toString());
                    mConcert.setArtistName(mEtArtistTextName.getText().toString());
                    mConcert.setEventDate(mCalendar.getTime());
                    mConcert.setOrganizationName(mEtOrganizationTextName.getText().toString());

                    mViewModel.getExecutorService().submit(() -> {
                        try {
                            mViewModel.getConcertRepo().update(mConcert);
                            getActivity().runOnUiThread(() -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, ConcertsListFragment.class));
                        } catch (IOException e) {
                            getActivity().runOnUiThread(() -> Utilities.showToast(getActivity(), R.string.admin_error_process));
                        }
                    });
                } else {
                    mConcert = new Concert(null, mEtConcertName.getText().toString(),
                            Float.parseFloat(mEtConcertTextValue.getText().toString()), mEtConcertPhotoTextUri.getText().toString(),
                            mEtArtistTextName.getText().toString(), mCalendar.getTime(), null, mEtOrganizationTextName.getText().toString());

                    mViewModel.getExecutorService().submit(() -> {
                        try {
                            mConcert.setFriendlyId(mViewModel.generateTicketFriendlyId());
                            mViewModel.getConcertRepo().insert(mConcert);
                            getActivity().runOnUiThread(() -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, ConcertsListFragment.class));
                        } catch (IOException e) {
                            getActivity().runOnUiThread(() -> Utilities.showToast(getActivity(), R.string.admin_error_process));
                        }
                    });
                }
            } catch (NumberFormatException e) {
                Utilities.showToast(getActivity(), R.string.admin_error_process);
            }
        });

        mBtnConcertFormCancel.setOnClickListener(v -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, ConcertsListFragment.class));
    }
}