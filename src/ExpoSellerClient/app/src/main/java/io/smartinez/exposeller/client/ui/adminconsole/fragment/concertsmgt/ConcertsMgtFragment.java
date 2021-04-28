package io.smartinez.exposeller.client.ui.adminconsole.fragment.concertsmgt;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.widget.*;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist.ConcertsListFragment;
import io.smartinez.exposeller.client.ui.mainscreen.fragment.adminlogin.AdminLoginViewModel;
import io.smartinez.exposeller.client.util.FragmentUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConcertsMgtViewModel.class);

        initView();
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

        DatePickerDialog.OnDateSetListener onDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            mEtConcertDate.setText(sdf.format(mCalendar.getTime()));
        };

        mEtConcertDate.setOnClickListener(v -> new DatePickerDialog(getActivity(), onDateListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show());

        if (getArguments() != null) {
            mConcert = getArguments().getParcelable(ConcertsMgtFragment.EXTRA_DATA);

            mTvTitleConcertForm.setText(R.string.admin_edit_concert);

            mEtConcertName.setText(mConcert.getName());
            mEtConcertTextValue.setText(String.valueOf(mConcert.getCost()));
            mEtArtistTextName.setText(mConcert.getArtistName());
            mEtConcertDate.setText(sdf.format(mConcert.getEventDate()));
            mEtOrganizationTextName.setText(mConcert.getOrganizationName());
            mEtConcertPhotoTextUri.setText(mConcert.getPhotoConcert().getPath());
        } else {
            mTvTitleConcertForm.setText(R.string.admin_new_concert);
            
            mConcert = null;
        }

        mBtnConcertFormOk.setOnClickListener(v -> {
            try {
                if (mConcert != null && mConcert.getDocId() != null) {
                    mConcert.setName(mEtConcertName.getText().toString());
                    mConcert.setCost(Float.parseFloat(mEtConcertTextValue.getText().toString()));
                    mConcert.setPhotoConcert(Uri.parse(mEtConcertPhotoTextUri.getText().toString()));
                    mConcert.setArtistName(mEtArtistTextName.getText().toString());
                    mConcert.setEventDate(mCalendar.getTime());
                    mConcert.setOrganizationName(mEtOrganizationTextName.getText().toString());

                    mViewModel.getConcertRepo().update(mConcert);
                } else {
                    mConcert = new Concert(null, mEtConcertName.getText().toString(), Float.parseFloat(mEtConcertTextValue.getText().toString()),
                            Uri.parse(mEtConcertPhotoTextUri.getText().toString()), mEtArtistTextName.getText().toString(), mCalendar.getTime(),
                            null, mEtOrganizationTextName.getText().toString());

                    mViewModel.getConcertRepo().insert(mConcert);
                }

                FragmentUtils.interchangeFragement(getActivity().getSupportFragmentManager(), R.id.fgAdminLogin, ConcertsListFragment.class);
            } catch (IOException e) {
                Toast.makeText(getActivity(), R.string.admin_error_process, Toast.LENGTH_LONG).show();
            }
        });

        mBtnConcertFormCancel.setOnClickListener(v -> FragmentUtils.interchangeFragement(getActivity().getSupportFragmentManager(), R.id.fgAdminLogin, ConcertsListFragment.class));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConcertsMgtViewModel.class);
        // TODO: Use the ViewModel
    }
}