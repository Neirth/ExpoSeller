package io.smartinez.exposeller.client.repository.datasource;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;

import dagger.Component;
import io.smartinez.exposeller.client.domain.IModel;

@Component
public class FirebaseDataSourceImpl implements IDataSource {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    @Override
    public void insert(IModel entityObj) {
        boolean isInserted = mDb.collection(entityObj.getClass().getSimpleName()).add(entityObj).isSuccessful();

        if (!isInserted) {
            throw new IllegalStateException("Could not insert the document");
        }
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
           List<DocumentSnapshot> result = queryResult.getDocuments();

           return result.get(0).toObject(entityClass);
       } else {
           throw new IllegalAccessException("The desired list is empty");
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
