package io.smartinez.exposeller.client.service;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
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
    }

    @Inject
    public AdminService(IDataSource dataSource, AdvertisementRepo advertisementRepo, ConcertRepo concertRepo, TicketRepo ticketRepo) {
        this.mDataSource = dataSource;
        this.mAdvertisementRepo = advertisementRepo;
        this.mConcertRepo = concertRepo;
        this.mTicketRepo = ticketRepo;

        this.mListConcerts = new MutableLiveData<>();
        this.mListAdBanner = new MutableLiveData<>();
    }

    /**
     * Wrapper to datasource to login into administrator mode
     *
     * @param email The administrator email
     * @param password The administrator password
     * @throws IOException In case of not being able to access the database
     */
    public void loginAdministrator(String email, String password) throws IOException {
        mDataSource.loginDatabase(email, password);
    }

    /**
     * Wrapper to datasource to logout from administrator mode
     *
     * @throws IOException In case of not being able to access the database
     */
    public void logoutAdministrator() throws IOException {
        mDataSource.logoutDatabase();
    }

    /**
     * Method to get the AdvertisementRepo instance
     *
     * @return The AdvertisementRepo instance
     */
    public AdvertisementRepo getAdvertisementRepo() {
        return this.mAdvertisementRepo;
    }

    /**
     * Method to get the ConcertRepo instance
     *
     * @return The ConcertRepo instance
     */
    public ConcertRepo getConcertRepo() {
        return this.mConcertRepo;
    }

    /**
     * Method to search models using query types such as TypeSearch,
     * the parameter and class from run the query search
     *
     * @param model Entity class
     * @param typeSearch Type of Query
     * @param parameter The value of query
     * @throws IOException In case of not being able to access the database
     * @throws IllegalAccessException In case the entity cannot be found
     */
    public void searchListModels(Class<? extends IModel> model, TypeSearch typeSearch, long parameter) throws IOException, IllegalAccessException {
        // Detect the type of query
        if (typeSearch == TypeSearch.FRIENDLY_ID) {
            // Detect the class to query and run
            if (model == Concert.class) {
                mListConcerts.postValue(Collections.singletonList(mConcertRepo.getByFriendlyId(String.valueOf(parameter))));
            } else if (model == Advertisement.class) {
                mListAdBanner.postValue(Collections.singletonList(mAdvertisementRepo.getByFriendlyId(String.valueOf(parameter))));
            }

            // Save the parameters of search inside a map
            mLastSearchEntity = new Bundle();
            mLastSearchEntity.putString("CLASS_TYPE", model.getSimpleName());
            mLastSearchEntity.putString("SEARCH_TYPE", TypeSearch.FRIENDLY_ID.name());
            mLastSearchEntity.putLong(TypeSearch.FRIENDLY_ID.name(), parameter);
        } else if (typeSearch == TypeSearch.TIME_BASED) {
            // Detect the class to query and run
            if (model == Concert.class) {
                mListConcerts.postValue(mConcertRepo.getBySpecificDate(new Date(parameter)));
            } else if (model == Advertisement.class) {
                mListAdBanner.postValue(mAdvertisementRepo.getBySpecificDate(new Date(parameter)));
            }

            // Save the parameters of search inside a map
            mLastSearchEntity = new Bundle();
            mLastSearchEntity.putString("CLASS_TYPE", model.getSimpleName());
            mLastSearchEntity.putString("SEARCH_TYPE", TypeSearch.FRIENDLY_ID.name());
            mLastSearchEntity.putLong(TypeSearch.FRIENDLY_ID.name(), parameter);
        } else if (typeSearch == TypeSearch.NOT_BEFORE) {
            // Read the now time
            Date nowTime = new Date();

            // Detect the class to query and run
            if (model == Concert.class) {
                mListConcerts.postValue(mConcertRepo.getNotBeforeDate(nowTime));
            } else if (model == Advertisement.class) {
                mListAdBanner.postValue(mAdvertisementRepo.getNotBeforeDate(nowTime));
            }

            // Save the parameters of search inside a map
            mLastSearchEntity = new Bundle();
            mLastSearchEntity.putString("CLASS_TYPE", model.getSimpleName());
            mLastSearchEntity.putString("SEARCH_TYPE", TypeSearch.NOT_BEFORE.name());
            mLastSearchEntity.putLong(TypeSearch.NOT_BEFORE.name(), nowTime.getTime());
        }
    }

    /**
     * Notify the list changes using the last parameters used in the search
     *
     * @throws IOException In case of not being able to access the database
     * @throws IllegalAccessException In case the entity cannot be found
     */
    public void notifyListChanges() throws IOException, IllegalAccessException {
        if(mLastSearchEntity != null) {
            // Detect the class type
            if (mLastSearchEntity.getString("CLASS_TYPE").equals(Concert.class.getSimpleName())) {
                // Detect the last parameters and re-run the query
                if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.FRIENDLY_ID.name())) {
                    mListConcerts.postValue(Collections.singletonList(mConcertRepo.getByFriendlyId(String.valueOf(mLastSearchEntity.getLong(TypeSearch.FRIENDLY_ID.name())))));
                } else if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.TIME_BASED.name())) {
                    mListConcerts.postValue(mConcertRepo.getBySpecificDate(new Date(mLastSearchEntity.getLong(TypeSearch.TIME_BASED.name()))));
                } else if (mLastSearchEntity.getString("SEARCH_TYPE").equals(TypeSearch.NOT_BEFORE.name())) {
                    mListConcerts.postValue(mConcertRepo.getNotBeforeDate(new Date(mLastSearchEntity.getLong(TypeSearch.NOT_BEFORE.name()))));
                }
            } else if (mLastSearchEntity.getString("CLASS_TYPE").equals(Advertisement.class.getSimpleName())) {
                // Detect the last parameters and re-run the query
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

    /**
     * Method to get the live data concert list
     *
     * @return The live data of concert List
     */
    public LiveData<List<Concert>> getConcertList() {
        return mListConcerts;
    }

    /**
     * Method to get the live data advertisement list
     *
     * @return The live data of advertisement List
     */
    public LiveData<List<Advertisement>> getAdvertisementList() {
        return mListAdBanner;
    }

    /**
     * Method for generate the friendly id from passed entity
     *
     * @param iModel The entity class
     * @return The free id of entity class
     * @throws IOException In case of not being able to access the database
     */
    public int generateFriendlyId(Class<? extends IModel> iModel) throws IOException {
        // Prepare the friendlyId
        int friendlyId = 0;

        try {
            while (true) {
                // Generate a random friendly id
                friendlyId = Utilities.generateRandomFriendlyId();

                // Check if no exist, if no exist, the method throw a exception.
                if (iModel == Advertisement.class) {
                    mAdvertisementRepo.getByFriendlyId(String.valueOf(friendlyId));
                } else if (iModel == Concert.class) {
                    mConcertRepo.getByFriendlyId(String.valueOf(friendlyId));
                } else if (iModel == Ticket.class) {
                    mTicketRepo.getByFriendlyId(String.valueOf(friendlyId));
                }
            }
        } catch (IllegalAccessException e) {
            // INFO: Ignoring the consequences because the value is free
        }

        // Return the value
        return friendlyId;
    }
}
