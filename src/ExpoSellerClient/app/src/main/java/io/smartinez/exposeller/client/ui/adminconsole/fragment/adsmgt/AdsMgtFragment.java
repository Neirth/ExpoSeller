package io.smartinez.exposeller.client.ui.adminconsole.fragment.adsmgt;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
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
import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist.AdsListFragment;
import io.smartinez.exposeller.client.util.FragmentUtils;
import io.smartinez.exposeller.client.util.TimeUtils;
import io.smartinez.exposeller.client.util.Utilities;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
    private Advertisement mAdvertisement;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_mgt_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdsMgtViewModel.class);

        initView();
        initButtonActions();
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

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);

        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, hour, minute) -> {
            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);

            LocalDateTime adBannerDateObj = mCalendar.toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            mEtAdsDate.setText(adBannerDateObj.format(dateFormatter));
        };

        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(getActivity(), onTimeSetListener, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true).show();
        };

        mEtAdsDate.setOnClickListener(v -> {
            Utilities.hideKeyboard(getContext(), v);
            new DatePickerDialog(getActivity(), onDateSetListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        mEtAdsPhotoUri.setOnEditorActionListener(
            (v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (event == null || !event.isShiftPressed()) {
                        Glide.with(this).load(mEtAdsPhotoUri.getText().toString()).into(mIvAdsPhotoUri);
                        return true;
                    }
                }

                return false;
            }
        );

        if (getArguments() != null && getArguments().getParcelable(AdsMgtFragment.EXTRA_DATA) != null) {
            mAdvertisement = getArguments().getParcelable(AdsMgtFragment.EXTRA_DATA);

            mTvAdsTitleForm.setText(R.string.admin_edit_ads);

            LocalDateTime adBannerDateObj = mAdvertisement.getEventDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();

            mEtAdsName.setText(mAdvertisement.getName());
            mEtAdsDate.setText(adBannerDateObj.format(dateFormatter));
            mEtAdsPhotoUri.setText(mAdvertisement.getPhotoAd());
            mCalendar.setTime(mAdvertisement.getEventDate());
        } else {
            mTvAdsTitleForm.setText(R.string.admin_new_ads);

            mAdvertisement = null;
        }
    }

    private void initButtonActions() {
        mBtnAdsFormOk.setOnClickListener(v -> {
            try {
                if (mAdvertisement != null && mAdvertisement.getDocId() != null) {
                    mAdvertisement.setName(mEtAdsName.getText().toString());
                    mAdvertisement.setEventDate(TimeUtils.removeTimezone(mCalendar.getTime()));
                    mAdvertisement.setPhotoAd(mEtAdsPhotoUri.getText().toString());

                    mViewModel.getExecutorService().submit(() -> {
                        try {
                            mViewModel.getAdBannerRepo().update(mAdvertisement);
                            getActivity().runOnUiThread(() -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, AdsListFragment.class));
                        } catch (IOException e) {
                            getActivity().runOnUiThread(() -> Utilities.showToast(getActivity(), R.string.admin_error_process));
                        }
                    });
                } else {
                    mAdvertisement = new Advertisement(null, mEtAdsName.getText().toString(), mEtAdsPhotoUri.getText().toString(), null, TimeUtils.removeTimezone(mCalendar.getTime()));

                    mViewModel.getExecutorService().submit(() -> {
                        try {
                            mAdvertisement.setFriendlyId(mViewModel.generateTicketFriendlyId());
                            mViewModel.getAdBannerRepo().insert(mAdvertisement);
                            getActivity().runOnUiThread(() -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, AdsListFragment.class));
                        } catch (IOException e) {
                            getActivity().runOnUiThread(() -> Utilities.showToast(getActivity(), R.string.admin_error_process));
                        }
                    });
                }

            } catch (NumberFormatException e) {
                Utilities.showToast(getActivity(), R.string.admin_error_process);
            }
        });

        mBtnAdsFormCancel.setOnClickListener(v -> FragmentUtils.interchangeFragement(getParentFragmentManager(), R.id.flFragmentSurface, AdsListFragment.class));
    }
}