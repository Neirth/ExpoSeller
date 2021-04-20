package io.smartinez.exposeller.client.repository.datasource;

import com.google.android.gms.tasks.Task;
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
import javax.inject.Singleton;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.smartinez.exposeller.client.domain.IModel;

@ViewModelScoped
public class FirebaseDataSourceImpl implements IDataSource {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    @Inject
    public FirebaseDataSourceImpl() {
    }

    @Override
    public void insert(IModel entityObj) throws IOException {
        Task<DocumentReference> insertOperation = mDb.collection(entityObj.getClass().getSimpleName()).add(entityObj);

        while (!insertOperation.isComplete());

        boolean isInserted = insertOperation.isSuccessful();

        if (isInserted) {
            entityObj.setDocId(insertOperation.getResult().getId());
        } else {
            throw new IOException("Could not insert the document");
        }
    }

    @Override
    public List<IModel> getBySpecificDate(Date date, Class<? extends IModel> entityClass) throws IOException {
        Query queryDate = mDb.collection(entityClass.getSimpleName()).whereEqualTo("eventDate", date);

        Task<QuerySnapshot> querySnapshotTask = queryDate.get();

        while (!querySnapshotTask.isComplete());

        if (querySnapshotTask.isSuccessful()) {
            QuerySnapshot queryResult = querySnapshotTask.getResult();

            List<DocumentSnapshot> result;

            if (queryResult != null && !queryResult .isEmpty()) {
                result = queryResult.getDocuments();
            } else {
                result = Collections.emptyList();
            }

            return result.stream().map(entityClass::cast).collect(Collectors.toList());
        } else {
            throw new IOException("No could complete the operation");
        }
    }

    @Override
    public List<IModel> getNotBeforeDate(Date date, Class<? extends IModel> entityClass) throws IOException {
        Query queryDate = mDb.collection(entityClass.getSimpleName()).whereGreaterThan("eventDate", date);
        Task<QuerySnapshot> querySnapshotTask = queryDate.get();

        while (!querySnapshotTask.isComplete());

        if (querySnapshotTask.isSuccessful()) {
            QuerySnapshot queryResult = querySnapshotTask.getResult();

            List<DocumentSnapshot> result;

            if (queryResult != null && !queryResult.isEmpty()) {
                result = queryResult.getDocuments();
            } else {
                result = Collections.emptyList();
            }

            return result.stream().map(entityClass::cast).collect(Collectors.toList());
        } else {
            throw new IOException("No could complete the operation");
        }
    }

    @Override
    public IModel getByDocId(String docId, Class<? extends IModel> entityClass) throws IllegalAccessException, IOException {
        DocumentReference docRef = mDb.collection(entityClass.getSimpleName()).document(docId);

        Task<DocumentSnapshot> taskDocSnap = docRef.get();

        while (!taskDocSnap.isComplete());

        if (taskDocSnap.isSuccessful()) {
            DocumentSnapshot docSnap = taskDocSnap.getResult();

            if (docSnap != null && docSnap.exists()) {
                return docSnap.toObject(entityClass);
            } else {
                throw new IllegalAccessException("Entity not found in the database");
            }
        } else {
            throw new IOException("No could complete the operation");
        }
    }

    @Override
    public IModel getByFriendlyId(String friendlyId, Class<? extends IModel> entityClass) throws IllegalAccessException, IOException {
       Query queryFriendlyId = mDb.collection(entityClass.getSimpleName()).whereEqualTo("friendlyId", friendlyId);

        Task<QuerySnapshot> querySnapshotTask = queryFriendlyId.get();

        while (!querySnapshotTask.isComplete());

        if (querySnapshotTask.isSuccessful()) {
            QuerySnapshot queryResult = querySnapshotTask.getResult();

            if (queryResult != null && !queryResult.isEmpty()) {
                return queryResult.getDocuments().get(0).toObject(entityClass);
            } else {
                throw new IllegalAccessException("Entity not found in the database");
            }
        } else {
            throw new IOException("No could complete the operation");
        }
    }

    @Override
    public void update(String docId, IModel entityObj) throws IOException {
        Task<Void> updateOperation = mDb.collection(entityObj.getClass().getSimpleName()).document(docId).set(entityObj);

        while (!updateOperation.isComplete());

        boolean isUpdated = updateOperation.isSuccessful();

        if (!isUpdated) {
            throw new IOException("Could not update the document");
        }
    }

    @Override
    public void delete(String docId, Class<? extends IModel> entityClass) throws IOException {
        Task<Void> deleteOperation = mDb.collection(entityClass.getSimpleName()).document(docId).delete();

        while (!deleteOperation.isComplete());

        boolean isDeleted = deleteOperation.isSuccessful();

        if (!isDeleted) {
            throw new IOException("Could not delete the document");
        }
    }

    @Override
    public void close() throws IOException {
    }
}
