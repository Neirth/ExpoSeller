package io.smartinez.exposeller.client.ui.mainscreen.fragment.adminlogin;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.smartinez.exposeller.client.R;

public class AdminLoginFragment extends Fragment {
    private AdminLoginViewModel mViewModel;

    private TextView mTvAdminConsole;
    private TextView mTvAdminEmail;
    private TextView mTvAdminPassword;
    private EditText mEtAdminEmail;
    private EditText mEtAdminPassword;
    private Button mBtnAdminOk;
    private Button mBtnAdminCancel;

    public static AdminLoginFragment newInstance() {
        return new AdminLoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdminLoginViewModel.class);

        initView();
    }

    private void initView() {
        mTvAdminConsole = getView().findViewById(R.id.tvAdminConsole);
        mTvAdminEmail = getView().findViewById(R.id.tvAdminEmail);
        mTvAdminPassword = getView().findViewById(R.id.tvAdminPassword);
        mEtAdminEmail = getView().findViewById(R.id.etAdminEmail);
        mEtAdminPassword = getView().findViewById(R.id.etAdminPassword);
        mBtnAdminOk = getView().findViewById(R.id.btnAdminOk);
        mBtnAdminCancel = getView().findViewById(R.id.btnAdminCancel);

        mBtnAdminOk.setOnClickListener(v -> {

        });

        mBtnAdminCancel.setOnClickListener(v -> {
            Animation mLoadAnimation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
            mLoadAnimation.setDuration(1000);

            getActivity().findViewById(R.id.fgAdminLogin).setVisibility(View.GONE);
            getActivity().findViewById(R.id.fgAdminLogin).startAnimation(mLoadAnimation);
        });
    }
}