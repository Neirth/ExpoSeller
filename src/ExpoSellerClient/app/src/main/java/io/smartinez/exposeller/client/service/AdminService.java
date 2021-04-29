package io.smartinez.exposeller.client.service;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.IModel;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.repository.AdvertisementRepo;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.repository.TicketRepo;
import io.smartinez.exposeller.client.repository.datasource.IDataSource;
import io.smartinez.exposeller.client.util.Utilities;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Singleton
public class AdminService {
    private final IDataSource mDataSource;
    private final AdvertisementRepo mAdvertisementRepo;
    private final ConcertRepo mConcertRepo;
    private final TicketRepo mTicketRepo;

    private final MutableLiveData<List<Concert>> mListConcerts;
    private final MutableLiveData<List<Advertisement>> mListAdBanner;

    private Bundle mLastSearchEntity = null;

    public enum TypeSearch {
        FRIENDLY_ID, TIME_BASED, NOT_BEFORE
    };

    @Inject
    public AdminService(IDataSource dataSource, AdvertisementRepo advertisementRepo, ConcertRepo concertRepo, TicketRepo ticketRepo) {
        this.mDataSource = dataSource;
        this.mAdvertisementRepo = advertisementRepo;
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

    public AdvertisementRepo getAdBannerRepo() {
        return this.mAdvertisementRepo;
    }

    public ConcertRepo getConcertRepo() {
        return this.mConcertRepo;
    }

    public void searchListModels(Class<? extends IModel> model, TypeSearch typeSearch, long parameter) throws IOException, IllegalAccessException {
        if (typeSearch == TypeSearch.FRIENDLY_ID) {
            if (model == Concert.class) {
                mListConcerts.postValue(Collections.singletonList(mConcertRepo.getByFriendlyId(String.valueOf(parameter))));
            } else if (model == Advertisement.class) {
                mListAdBanner.postValue(Collections.singletonList(mAdvertisementRepo.getByFriendlyId(String.valueOf(parameter))));
            }

            mLastSearchEntity = new Bundle();
            mLastSearchEntity.putString("CLASS_TYPE", model.getSimpleName());
            mLastSearchEntity.putString("SEARCH_TYPE", TypeSearch.FRIENDLY_ID.name());
            mLastSearchEntity.putLong(TypeSearch.FRIENDLY_ID.name(), parameter);
        } else if (typeSearch == TypeSearch.TIME_BASED) {
            if (model == Concert.class) {
                mListConcerts.postValue(mConcertRepo.getBySpecificDate(new Date(parameter)));
            } else if (model == Advertisement.class) {
                mListAdBanner.postValue(mAdvertisementRepo.getBySpecificDate(new Date(parameter)));
            }

            mLastSearchEntity = new Bundle();
            mLastSearchEntity.putString("CLASS_TYPE", model.getSimpleName());
            mLastSearchEntity.putString("SEARCH_TYPE", TypeSearch.FRIENDLY_ID.name());
            mLastSearchEntity.putLong(TypeSearch.FRIENDLY_ID.name(), parameter);
        } else if (typeSearch == TypeSearch.NOT_BEFORE) {
            Date nowTime = new Date();

            if (model == Concert.class) {
                mListConcerts.postValue(mConcertRepo.getNotBeforeDate(nowTime));
            } else if (model == Advertisement.class) {
                mListAdBanner.postValue(mAdvertisementRepo.getNotBeforeDate(nowTime));
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
            } else if (mLastSearchEntity.getString("CLASS_TYPE").equals(Advertisement.class.getSimpleName())) {
                if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.FRIENDLY_ID.name())) {
                    mListAdBanner.postValue(Collections.singletonList(mAdvertisementRepo.getByFriendlyId(String.valueOf(mLastSearchEntity.getLong(TypeSearch.FRIENDLY_ID.name())))));
                } else if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.TIME_BASED.name())) {
                    mListAdBanner.postValue(mAdvertisementRepo.getBySpecificDate(new Date(mLastSearchEntity.getLong(TypeSearch.TIME_BASED.name()))));
                } else if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.NOT_BEFORE.name())) {
                    mListAdBanner.postValue(mAdvertisementRepo.getNotBeforeDate(new Date(mLastSearchEntity.getLong(TypeSearch.NOT_BEFORE.name()))));
                }
            }
        }
    }

    public MutableLiveData<List<Concert>> getListConcerts() {
        return mListConcerts;
    }

    public MutableLiveData<List<Advertisement>> getListAdBanner() {
        return mListAdBanner;
    }

    public int generateTicketFriendlyId(Class<? extends IModel> iModel) throws IOException {
        int friendlyId = 0;

        try {
            while (true) {
                friendlyId = Utilities.generateRandomFriendlyId();

                if (iModel == Advertisement.class) {
                    mAdvertisementRepo.getByFriendlyId(String.valueOf(friendlyId));
                } else if (iModel == Concert.class) {
                    mConcertRepo.getByFriendlyId(String.valueOf(friendlyId));
                } else if (iModel == Ticket.class) {
                    mTicketRepo.getByFriendlyId(String.valueOf(friendlyId));
                }
            }
        } catch (IllegalAccessException e) {
            // Ignoring the consequences because the value is free
        }

        return friendlyId;
    }
}
