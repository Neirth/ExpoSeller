package io.smartinez.exposeller.client.ui.adminconsole.fragment.usermgt;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.smartinez.exposeller.client.R;

public class UserMgtFragment extends Fragment {

    private UserMgtViewModel mViewModel;

    public static UserMgtFragment newInstance() {
        return new UserMgtFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_mgt_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserMgtViewModel.class);
        // TODO: Use the ViewModel
    }

}