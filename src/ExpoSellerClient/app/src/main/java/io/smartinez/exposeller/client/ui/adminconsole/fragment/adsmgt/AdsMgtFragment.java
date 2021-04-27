package io.smartinez.exposeller.client.ui.adminconsole.fragment.adsmgt;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;

@AndroidEntryPoint
public class AdsMgtFragment extends Fragment {
    private AdsMgtViewModel mViewModel;

    private TextView mTvAdsTitleForm;
    private TextView mTvAdsName;
    private EditText mEditTextTextPersonName;
    private TextView mTvAdsDate;
    private EditText mEtAdsDate;
    private TextView mTvAdsPhotoUri;
    private ImageView mIvAdsPhotoUri;
    private EditText mEtAdsPhotoUri;
    private Button mBtnAdsFormOk;
    private Button mBtnAdsFormCancel;

    public static AdsMgtFragment newInstance() {
        return new AdsMgtFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_mgt_fragment, container, false);
    }

    public void initView() {
        mTvAdsTitleForm = getActivity().findViewById(R.id.tvAdsTitleForm);
        mTvAdsName = getActivity().findViewById(R.id.tvAdsName);
        mEditTextTextPersonName = getActivity().findViewById(R.id.editTextTextPersonName);
        mTvAdsDate = getActivity().findViewById(R.id.tvAdsDate);
        mEtAdsDate = getActivity().findViewById(R.id.etAdsDate);
        mTvAdsPhotoUri = getActivity().findViewById(R.id.tvAdsPhotoUri);
        mIvAdsPhotoUri = getActivity().findViewById(R.id.ivAdsPhotoUri);
        mEtAdsPhotoUri = getActivity().findViewById(R.id.etAdsPhotoUri);
        mBtnAdsFormOk = getActivity().findViewById(R.id.btnAdsFormOk);
        mBtnAdsFormCancel = getActivity().findViewById(R.id.btnAdsFormCancel);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdsMgtViewModel.class);
        // TODO: Use the ViewModel
    }

}