package io.smartinez.exposeller.client.ui.mainscreen.fragment.adminlogin;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.database.collection.LLRBNode;
import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.ExpoSellerApplication;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adminconsole.AdminConsoleActivity;
import io.smartinez.exposeller.client.util.Utilities;

@AndroidEntryPoint
public class AdminLoginFragment extends Fragment {
    // Elements of fragment
    private TextView mTvAdminConsole;
    private TextView mTvAdminEmail;
    private TextView mTvAdminPassword;
    private EditText mEtAdminEmail;
    private EditText mEtAdminPassword;
    private Button mBtnAdminOk;
    private Button mBtnAdminCancel;

    // Instance of animation object
    private Animation mLoadAnimation;

    // View Model instance
    private AdminLoginViewModel mViewModel;

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
        return inflater.inflate(R.layout.admin_login_fragment, container, false);
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
        mViewModel = new ViewModelProvider(this).get(AdminLoginViewModel.class);

        // Initialize the components of the fragment
        initView();
    }

    /**
     * Private method to initialize the components of the fragment
     */
    private void initView() {
        // Bind the elements of the fragment
        mTvAdminConsole = getView().findViewById(R.id.tvAdminConsole);
        mTvAdminEmail = getView().findViewById(R.id.tvAdminEmail);
        mTvAdminPassword = getView().findViewById(R.id.tvAdminPassword);
        mEtAdminEmail = getView().findViewById(R.id.etAdminEmail);
        mEtAdminPassword = getView().findViewById(R.id.etAdminPassword);
        mBtnAdminOk = getView().findViewById(R.id.btnAdminOk);
        mBtnAdminCancel = getView().findViewById(R.id.btnAdminCancel);

        // Configure animation object
        mLoadAnimation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        mLoadAnimation.setDuration(1000);

        // Initialize the new instance of view model
        mViewModel = new ViewModelProvider(this).get(AdminLoginViewModel.class);

        // Set callback to ok button
        mBtnAdminOk.setOnClickListener(v -> {
            if (mEtAdminEmail.getText().length() >= 1 && mEtAdminPassword.getText().length() >= 1) {
                // Run login administration operation
                mViewModel.loginAdministrator(mEtAdminEmail.getText().toString(), mEtAdminPassword.getText().toString())
                    .thenApplyAsync(result -> {
                        requireActivity().runOnUiThread(() -> {
                            // Hidden the login fragment
                            getView().findViewById(R.id.fgAdminLogin).setVisibility(View.GONE);
                            getView().findViewById(R.id.fgAdminLogin).startAnimation(mLoadAnimation);

                            // Clean values of fields
                            mEtAdminEmail.setText("");
                            mEtAdminPassword.setText("");

                            // Start the activity
                            startActivity(new Intent(getContext(), AdminConsoleActivity.class));
                        });

                        // Return a null value for finish the future operation
                        return null;
                    }).exceptionally(ex -> {
                        // Inform the error in ui
                        requireActivity().runOnUiThread(() -> Utilities.showToast(getActivity(), R.string.admin_login_error));

                        // Return a null value for finish the future operation
                        return null;
                    });
            } else {
                // Inform the error in ui
                Utilities.showToast(getActivity(), R.string.admin_login_validation_error);
            }
        });

        // Set the cancellation callback
        mBtnAdminCancel.setOnClickListener(v -> {
            // Hidden the login fragment
            getActivity().findViewById(R.id.fgAdminLogin).setVisibility(View.GONE);
            getActivity().findViewById(R.id.fgAdminLogin).startAnimation(mLoadAnimation);

            // Clean values of fields
            mEtAdminEmail.setText("");
            mEtAdminPassword.setText("");
        });
    }
}