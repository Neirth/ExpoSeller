package io.smartinez.exposeller.client.service;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.smartinez.exposeller.client.domain.AdBanner;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.IModel;
import io.smartinez.exposeller.client.repository.AdBannerRepo;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.repository.TicketRepo;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ViewModelScoped
public class AdminService {
    private IDataSource mDataSource;
    private AdBannerRepo mAdBannerRepo;
    private ConcertRepo mConcertRepo;
    private TicketRepo mTicketRepo;

    private MutableLiveData<List<Concert>> mListConcerts;
    private MutableLiveData<List<AdBanner>> mListAdBanner;

    private Bundle mLastSearchEntity = null;

    public enum TypeSearch {
        FRIENDLY_ID, TIME_BASED, NOT_BEFORE;
    }

    @Inject
    public AdminService(IDataSource dataSource, AdBannerRepo adBannerRepo, ConcertRepo concertRepo, TicketRepo ticketRepo) {
        this.mDataSource = dataSource;
        this.mAdBannerRepo = adBannerRepo;
        this.mConcertRepo = concertRepo;
        this.mTicketRepo = ticketRepo;

        this.mListConcerts = new MutableLiveData<>();
        this.mListAdBanner = new MutableLiveData<>();
    }

    public void loginAdministrator(String email, String password) throws IOException {
        mDataSource.loginDatabase(email, password);
    }

    public void logoutAdministrator() throws IOException {
        mDataSource.logoutDatabase();
    }

    public AdBannerRepo getAdBannerRepo() {
        return this.mAdBannerRepo;
    }

    public ConcertRepo getConcertRepo() {
        return this.mConcertRepo;
    }

    public void searchListModels(Class<? extends IModel> model, TypeSearch typeSearch, long parameter) throws IOException, IllegalAccessException {
        if (typeSearch == TypeSearch.FRIENDLY_ID) {
            if (model == Concert.class) {
                mListConcerts.postValue(Collections.singletonList(mConcertRepo.getByFriendlyId(String.valueOf(parameter))));
            } else if (model == AdBanner.class) {
                mListAdBanner.postValue(Collections.singletonList(mAdBannerRepo.getByFriendlyId(String.valueOf(parameter))));
            }

            mLastSearchEntity = new Bundle();
            mLastSearchEntity.putString("CLASS_TYPE", model.getSimpleName());
            mLastSearchEntity.putString("SEARCH_TYPE", TypeSearch.FRIENDLY_ID.name());
            mLastSearchEntity.putLong(TypeSearch.FRIENDLY_ID.name(), parameter);
        } else if (typeSearch == TypeSearch.TIME_BASED) {
            if (model == Concert.class) {
                mListConcerts.postValue(mConcertRepo.getBySpecificDate(new Date(parameter)));
            } else if (model == AdBanner.class) {
                mListAdBanner.postValue(mAdBannerRepo.getBySpecificDate(new Date(parameter)));
            }

            mLastSearchEntity = new Bundle();
            mLastSearchEntity.putString("CLASS_TYPE", model.getSimpleName());
            mLastSearchEntity.putString("SEARCH_TYPE", TypeSearch.FRIENDLY_ID.name());
            mLastSearchEntity.putLong(TypeSearch.FRIENDLY_ID.name(), parameter);
        } else if (typeSearch == TypeSearch.NOT_BEFORE) {
            Date nowTime = new Date();

            if (model == Concert.class) {
                mListConcerts.postValue(mConcertRepo.getNotBeforeDate(nowTime));
            } else if (model == AdBanner.class) {
                mListAdBanner.postValue(mAdBannerRepo.getNotBeforeDate(nowTime));
            }

            mLastSearchEntity = new Bundle();
            mLastSearchEntity.putString("CLASS_TYPE", model.getSimpleName());
            mLastSearchEntity.putString("SEARCH_TYPE", TypeSearch.NOT_BEFORE.name());
            mLastSearchEntity.putLong(TypeSearch.NOT_BEFORE.name(), nowTime.getTime());
        }
    }

    public void notifyListChanges() throws IOException, IllegalAccessException {
        if(mLastSearchEntity != null) {
            if (mLastSearchEntity.getString("CLASS_TYPE").equals(Concert.class.getSimpleName())) {
                if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.FRIENDLY_ID.name())) {
                    mListConcerts.postValue(Collections.singletonList(mConcertRepo.getByFriendlyId(String.valueOf(mLastSearchEntity.getLong(TypeSearch.FRIENDLY_ID.name())))));
                } else if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.TIME_BASED.name())) {
                    mListConcerts.postValue(mConcertRepo.getBySpecificDate(new Date(mLastSearchEntity.getLong(TypeSearch.TIME_BASED.name()))));
                } else if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.NOT_BEFORE.name())) {
                    mListConcerts.postValue(mConcertRepo.getNotBeforeDate(new Date(mLastSearchEntity.getLong(TypeSearch.NOT_BEFORE.name()))));
                }
            } else if (mLastSearchEntity.getString("CLASS_TYPE").equals(AdBanner.class.getSimpleName())) {
                if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.FRIENDLY_ID.name())) {
                    mListAdBanner.postValue(Collections.singletonList(mAdBannerRepo.getByFriendlyId(String.valueOf(mLastSearchEntity.getLong(TypeSearch.FRIENDLY_ID.name())))));
                } else if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.TIME_BASED.name())) {
                    mListAdBanner.postValue(mAdBannerRepo.getBySpecificDate(new Date(mLastSearchEntity.getLong(TypeSearch.TIME_BASED.name()))));
                } else if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.NOT_BEFORE.name())) {
                    mListAdBanner.postValue(mAdBannerRepo.getNotBeforeDate(new Date(mLastSearchEntity.getLong(TypeSearch.NOT_BEFORE.name()))));
                }
            }
        }
    }

    public MutableLiveData<List<Concert>> getListConcerts() {
        return mListConcerts;
    }

    public MutableLiveData<List<AdBanner>> getListAdBanner() {
        return mListAdBanner;
    }
}
