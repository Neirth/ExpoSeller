package io.smartinez.exposeller.client.ui.mainscreen.fragment.adminlogin;

import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.ExpoSellerApplication;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adminconsole.AdminConsoleActivity;

@AndroidEntryPoint
public class AdminLoginFragment extends Fragment {
    private AdminLoginViewModel mViewModel;

    private TextView mTvAdminConsole;
    private TextView mTvAdminEmail;
    private TextView mTvAdminPassword;
    private EditText mEtAdminEmail;
    private EditText mEtAdminPassword;
    private Button mBtnAdminOk;
    private Button mBtnAdminCancel;
    private Animation mLoadAnimation;

    public static AdminLoginFragment newInstance() {
        return new AdminLoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdminLoginViewModel.class);

        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() throws Exception {
        mTvAdminConsole = getView().findViewById(R.id.tvAdminConsole);
        mTvAdminEmail = getView().findViewById(R.id.tvAdminEmail);
        mTvAdminPassword = getView().findViewById(R.id.tvAdminPassword);
        mEtAdminEmail = getView().findViewById(R.id.etAdminEmail);
        mEtAdminPassword = getView().findViewById(R.id.etAdminPassword);
        mBtnAdminOk = getView().findViewById(R.id.btnAdminOk);
        mBtnAdminCancel = getView().findViewById(R.id.btnAdminCancel);

        mLoadAnimation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        mLoadAnimation.setDuration(1000);

        mViewModel = new ViewModelProvider(this).get(AdminLoginViewModel.class);

        mBtnAdminOk.setOnClickListener(v -> {
            if (mEtAdminEmail.getText().length() >= 1 && mEtAdminPassword.getText().length() >= 1) {
                mViewModel.loginAdministrator(mEtAdminEmail.getText().toString(), mEtAdminPassword.getText().toString())
                    .thenApplyAsync(result -> {
                        requireActivity().runOnUiThread(() -> {
                            Log.d(ExpoSellerApplication.LOG_TAG, "The callback is called!");
                            getView().findViewById(R.id.fgAdminLogin).setVisibility(View.GONE);
                            getView().findViewById(R.id.fgAdminLogin).startAnimation(mLoadAnimation);

                            mEtAdminEmail.setText("");
                            mEtAdminPassword.setText("");

                            startActivity(new Intent(getContext(), AdminConsoleActivity.class));
                        });

                        return null;
                    }).exceptionally(ex -> {
                        requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), R.string.admin_login_error, Toast.LENGTH_SHORT).show());
                        return null;
                    });
            } else {
                Toast.makeText(getContext(), R.string.admin_login_validation_error, Toast.LENGTH_SHORT).show();
            }
        });

        mBtnAdminCancel.setOnClickListener(v -> {
            getActivity().findViewById(R.id.fgAdminLogin).setVisibility(View.GONE);
            getActivity().findViewById(R.id.fgAdminLogin).startAnimation(mLoadAnimation);
        });
    }
}