package io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.smartinez.exposeller.client.R;

public class AdsListFragment extends Fragment {

    private AdsListViewModel mViewModel;

    public static AdsListFragment newInstance() {
        return new AdsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdsListViewModel.class);
        // TODO: Use the ViewModel
    }

}