package io.smartinez.exposeller.client.ui.adminconsole.fragment.concertsmgt;

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
public class ConcertsMgtFragment extends Fragment {
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

    public static ConcertsMgtFragment newInstance() {
        return new ConcertsMgtFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.concerts_mgt_fragment, container, false);
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConcertsMgtViewModel.class);
        // TODO: Use the ViewModel
    }

}