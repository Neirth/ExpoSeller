package io.smartinez.exposeller.client.ui.mainscreen;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class MainScreenViewModel extends ViewModel {
    private UserService mUsersService;
    private ExecutorService mExecutorService;

    @Inject
    public MainScreenViewModel(UserService mUsersService, ExecutorService executorService) {
        this.mUsersService = mUsersService;
        this.mExecutorService = executorService;
    }
}
