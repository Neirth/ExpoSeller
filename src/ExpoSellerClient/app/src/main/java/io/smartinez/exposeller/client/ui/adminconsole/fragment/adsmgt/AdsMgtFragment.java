package io.smartinez.exposeller.client.ui.adminconsole.fragment.adsmgt;

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
import io.smartinez.exposeller.client.domain.AdBanner;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist.ConcertsListFragment;
import io.smartinez.exposeller.client.util.FragmentUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@AndroidEntryPoint
public class AdsMgtFragment extends Fragment {
    public final static String EXTRA_DATA = "io.smartinez.exposeller.client.ui.adminconsole.fragment.adsmgt.AdsMgtFragment.data";

    private AdsMgtViewModel mViewModel;

    private TextView mTvAdsTitleForm;
    private TextView mTvAdsName;
    private EditText mEtAdsName;
    private TextView mTvAdsDate;
    private EditText mEtAdsDate;
    private TextView mTvAdsPhotoUri;
    private ImageView mIvAdsPhotoUri;
    private EditText mEtAdsPhotoUri;
    private Button mBtnAdsFormOk;
    private Button mBtnAdsFormCancel;

    private Calendar mCalendar = Calendar.getInstance();
    private AdBanner mAdBanner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_mgt_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdsMgtViewModel.class);

        initView();
    }

    public void initView() {
        mTvAdsTitleForm = getActivity().findViewById(R.id.tvAdsTitleForm);
        mTvAdsName = getActivity().findViewById(R.id.tvAdsName);
        mEtAdsName = getActivity().findViewById(R.id.etAdsName);
        mTvAdsDate = getActivity().findViewById(R.id.tvAdsDate);
        mEtAdsDate = getActivity().findViewById(R.id.etAdsDate);
        mTvAdsPhotoUri = getActivity().findViewById(R.id.tvAdsPhotoUri);
        mIvAdsPhotoUri = getActivity().findViewById(R.id.ivAdsPhotoUri);
        mEtAdsPhotoUri = getActivity().findViewById(R.id.etAdsPhotoUri);
        mBtnAdsFormOk = getActivity().findViewById(R.id.btnAdsFormOk);
        mBtnAdsFormCancel = getActivity().findViewById(R.id.btnAdsFormCancel);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

        DatePickerDialog.OnDateSetListener onDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            mEtAdsDate.setText(sdf.format(mCalendar.getTime()));
        };

        mEtAdsDate.setOnClickListener(v -> new DatePickerDialog(getActivity(), onDateListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show());

        if (getArguments() != null) {
            mAdBanner = getArguments().getParcelable(AdsMgtFragment.EXTRA_DATA);

            mTvAdsTitleForm.setText(R.string.admin_edit_ads);

            mEtAdsName.setText(mAdBanner.getName());
            mEtAdsDate.setText(sdf.format(mAdBanner.getEventDate()));
            mEtAdsPhotoUri.setText(mAdBanner.getPhotoAd().getPath());
        } else {
            mTvAdsTitleForm.setText(R.string.admin_new_ads);

            mAdBanner = null;
        }

        mBtnAdsFormOk.setOnClickListener(v -> {
            try {
                if (mAdBanner != null && mAdBanner.getDocId() != null) {
                    mAdBanner.setName(mEtAdsName.getText().toString());
                    mAdBanner.setEventDate(mCalendar.getTime());
                    mAdBanner.setPhotoAd(Uri.parse(mEtAdsPhotoUri.getText().toString()));

                    mViewModel.getAdBannerRepo().update(mAdBanner);
                } else {
                    mAdBanner = new AdBanner(null, mEtAdsName.getText().toString(),
                            Uri.parse(mEtAdsPhotoUri.getText().toString()), null, mCalendar.getTime());

                    mViewModel.getAdBannerRepo().insert(mAdBanner);
                }

                FragmentUtils.interchangeFragement(getActivity().getSupportFragmentManager(), R.id.fgAdminLogin, ConcertsListFragment.class);
            } catch (IOException e) {
                Toast.makeText(getActivity(), R.string.admin_error_process, Toast.LENGTH_LONG).show();
            }
        });

        mBtnAdsFormCancel.setOnClickListener(v -> FragmentUtils.interchangeFragement(getActivity().getSupportFragmentManager(), R.id.fgAdminLogin, AdsMgtFragment.class));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdsMgtViewModel.class);
        // TODO: Use the ViewModel
    }

}