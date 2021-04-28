package io.smartinez.exposeller.client.repository.datasource;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.smartinez.exposeller.client.domain.IModel;

@Singleton
public class FirebaseDataSourceImpl implements IDataSource {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private BlockingQueue<Boolean> mThreadBus = new LinkedBlockingDeque<>();

    @Inject
    public FirebaseDataSourceImpl() {
    }

    @Override
    public void insert(IModel entityObj) throws IOException {
        try {
            Task<DocumentReference> insertOperation = mDb.collection(entityObj.getClass().getSimpleName()).add(entityObj);

            insertOperation.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            boolean isInserted = insertOperation.isSuccessful();

            if (isInserted) {
                entityObj.setDocId(insertOperation.getResult().getId());
            } else {
                throw new IOException("Could not insert the document");
            }
        } catch (InterruptedException e) {
            throw new IOException("No could complete the operation");
        }
    }

    @Override
    public List<IModel> getBySpecificDate(Date date, Class<? extends IModel> entityClass) throws IOException {
        try {
            Query queryDate = mDb.collection(entityClass.getSimpleName()).whereEqualTo("eventDate", date);
            Task<QuerySnapshot> querySnapshotTask = queryDate.get();

            querySnapshotTask.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

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
        } catch (InterruptedException e) {
            throw new IOException("No could complete the operation");
        }
    }

    @Override
    public List<IModel> getNotBeforeDate(Date date, Class<? extends IModel> entityClass) throws IOException {
        try {
            Query queryDate = mDb.collection(entityClass.getSimpleName()).whereGreaterThan("eventDate", date);
            Task<QuerySnapshot> querySnapshotTask = queryDate.get();

            querySnapshotTask.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

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
        } catch (InterruptedException e) {
            throw new IOException("No could complete the operation");
        }
    }

    @Override
    public IModel getByDocId(String docId, Class<? extends IModel> entityClass) throws IllegalAccessException, IOException {
        try {
            DocumentReference docRef = mDb.collection(entityClass.getSimpleName()).document(docId);

            Task<DocumentSnapshot> taskDocSnap = docRef.get();

            taskDocSnap.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

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
        } catch (InterruptedException e) {
            throw new IOException("No could complete the operation");
        }
    }

    @Override
    public IModel getByFriendlyId(String friendlyId, Class<? extends IModel> entityClass) throws IllegalAccessException, IOException {
        try {
            Query queryFriendlyId = mDb.collection(entityClass.getSimpleName()).whereEqualTo("friendlyId", friendlyId);

            Task<QuerySnapshot> querySnapshotTask = queryFriendlyId.get();

            querySnapshotTask.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

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
        } catch (InterruptedException e) {
            throw new IOException("No could complete the operation");
        }
    }

    @Override
    public void update(String docId, IModel entityObj) throws IOException {
        try {
            Task<Void> updateOperation = mDb.collection(entityObj.getClass().getSimpleName()).document(docId).set(entityObj);

            updateOperation.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            boolean isUpdated = updateOperation.isSuccessful();

            if (!isUpdated) {
                throw new IOException("Could not update the document");
            }
        } catch (InterruptedException e) {
            throw new IOException("Could not update the document");
        }
    }

    @Override
    public void delete(String docId, Class<? extends IModel> entityClass) throws IOException {
        try {
            Task<Void> deleteOperation = mDb.collection(entityClass.getSimpleName()).document(docId).delete();

            deleteOperation.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            boolean isDeleted = deleteOperation.isSuccessful();

            if (!isDeleted) {
                throw new IOException("Could not delete the document");
            }
        } catch (InterruptedException e) {
            throw new IOException("Could not delete the document");
        }
    }

    @Override
    public void loginDatabase(String email, String password) throws IOException {
        try {
            if(mAuth.getCurrentUser() != null){
                mAuth.signOut();
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            boolean isLogged = mAuth.getCurrentUser() != null;

            if (!isLogged) {
                throw new IOException("Could not login with this user");
            }
        } catch (InterruptedException e) {
            throw new IOException("Could not login with this user");
        }
    }

    @Override
    public void logoutDatabase() throws IOException {
        if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
        }
    }
}
