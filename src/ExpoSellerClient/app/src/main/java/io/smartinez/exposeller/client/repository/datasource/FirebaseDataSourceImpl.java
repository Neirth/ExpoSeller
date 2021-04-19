package io.smartinez.exposeller.client.repository.datasource;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;
import io.smartinez.exposeller.client.domain.IModel;

@ActivityScoped
public class FirebaseDataSourceImpl implements IDataSource {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    @Inject
    public FirebaseDataSourceImpl() {
    }

    @Override
    public void insert(IModel entityObj) {
        boolean isInserted = mDb.collection(entityObj.getClass().getSimpleName()).add(entityObj).isSuccessful();

        if (!isInserted) {
            throw new IllegalStateException("Could not insert the document");
        }
    }

    @Override
    public List<IModel> getBySpecificDate(Date date, Class<? extends IModel> entityClass) {
        Query queryDate = mDb.collection(entityClass.getSimpleName()).whereEqualTo("eventDate", date);

        QuerySnapshot queryResult = queryDate.get().getResult();

        List<DocumentSnapshot> result;

        if (queryResult != null && !queryResult.isEmpty()) {
            result = queryResult.getDocuments();
        } else {
            result = Collections.emptyList();
        }

        return result.stream().map(entityClass::cast).collect(Collectors.toList());
    }

    @Override
    public List<IModel> getNotBeforeDate(Date date, Class<? extends IModel> entityClass) {
        Query queryDate = mDb.collection(entityClass.getSimpleName()).whereGreaterThan("eventDate", date);

        QuerySnapshot queryResult = queryDate.get().getResult();

        List<DocumentSnapshot> result;

        if (queryResult != null && !queryResult.isEmpty()) {
            result = queryResult.getDocuments();
        } else {
            result = Collections.emptyList();
        }

        return result.stream().map(entityClass::cast).collect(Collectors.toList());
    }

    @Override
    public IModel getByDocId(String docId, Class<? extends IModel> entityClass) throws IllegalAccessException {
        DocumentReference docRef = mDb.collection(entityClass.getSimpleName()).document(docId);

        DocumentSnapshot docSnap = docRef.get().getResult();

        if (docSnap != null && docSnap.exists()) {
            return docSnap.toObject(entityClass);
        } else {
            throw new IllegalAccessException("Entity not found in the database");
        }
    }

    @Override
    public IModel getByFriendlyId(String friendlyId, Class<? extends IModel> entityClass) throws IllegalAccessException {
       Query queryFriendlyId = mDb.collection(entityClass.getSimpleName()).whereEqualTo("friendlyId", friendlyId);

       QuerySnapshot queryResult = queryFriendlyId.get().getResult();

       if (queryResult != null && !queryResult.isEmpty()) {
           return queryResult.getDocuments().get(0).toObject(entityClass);
       } else {
           throw new IllegalAccessException("Entity not found in the database");
       }
    }

    @Override
    public void update(String docId, IModel entityObj) {
        boolean isUpdated = mDb.collection(entityObj.getClass().getSimpleName()).document(docId).set(entityObj).isSuccessful();

        if (!isUpdated) {
            throw new IllegalStateException("Could not update the document");
        }
    }

    @Override
    public void delete(String docId, Class<? extends IModel> entityClass) {
        boolean isDeleted = mDb.collection(entityClass.getSimpleName()).document(docId).delete().isSuccessful();

        if (!isDeleted) {
            throw new IllegalStateException("Could not delete the document");
        }
    }

    @Override
    public void close() throws IOException {
    }
}
