/*
 * Copyright 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.neirth.exposeller.client.repository.datasource;

import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.neirth.exposeller.client.ExpoSellerApplication;
import io.neirth.exposeller.client.domain.IModel;
import io.neirth.exposeller.client.util.TimeUtils;

@Singleton
public class FirebaseDataSourceImpl implements IDataSource {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore mDb;
    private final BlockingQueue<Boolean> mThreadBus;

    @Inject
    public FirebaseDataSourceImpl() {
        this.mDb = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.mThreadBus = new LinkedBlockingDeque<>();
    }

    /**
     * Method to insert a entity in the database
     *
     * @param entityObj The entity object
     * @throws IOException In case of not being able to access the database
     */
    @Override
    public void insert(IModel entityObj) throws IOException {
        try {
            // Prepare the task to add the entity to the database
            Task<DocumentReference> insertOperation = mDb.collection(entityObj.getClass().getSimpleName()).add(entityObj);

            // Wait for complete the operation
            insertOperation.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            // Check if is inserted
            boolean isInserted = insertOperation.isSuccessful();

            // Set the docId if is inserted, or throw exception if error occurs
            if (isInserted) {
                entityObj.setDocId(insertOperation.getResult().getId());
            } else {
                throw new IOException("Could not insert the document");
            }
        } catch (InterruptedException e) {
            throw new IOException("No could complete the operation");
        }
    }

    /**
     * Method to update a entity in the database
     *
     * @param docId The entity id
     * @param entityObj The entity object
     * @throws IOException In case of not being able to access the database
     */
    @Override
    public void update(String docId, IModel entityObj) throws IOException {
        try {
            // Prepare the task to update the entity to the database
            Task<Void> updateOperation = mDb.collection(entityObj.getClass().getSimpleName()).document(docId).set(entityObj);

            // Wait for complete the operation
            updateOperation.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            // Check if is updated
            boolean isUpdated = updateOperation.isSuccessful();

            // Throw the exception if the update isn't complete.
            if (!isUpdated) {
                throw new IOException("Could not update the document");
            }
        } catch (InterruptedException e) {
            throw new IOException("Could not update the document");
        }
    }

    /**
     * Method to delete a entity in the database
     *
     * @param docId The entity id
     * @param entityClass The entity class
     * @throws IOException In case of not being able to access the database
     */
    @Override
    public void delete(String docId, Class<? extends IModel> entityClass) throws IOException {
        try {
            // Prepare the task to delete the entity to the database
            Task<Void> deleteOperation = mDb.collection(entityClass.getSimpleName()).document(docId).delete();

            // Wait for complete the operation
            deleteOperation.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            // Check if is delete
            boolean isDeleted = deleteOperation.isSuccessful();

            // Throw the exception if the delete isn't complete.
            if (!isDeleted) {
                throw new IOException("Could not delete the document");
            }
        } catch (InterruptedException e) {
            throw new IOException("Could not delete the document");
        }
    }

    /**
     * Method to obtain the records from a date passed by parameter
     *
     * @param date The date from where to start
     * @param entityClass The entity class
     * @return The list of entities that have been returned from the list
     * @throws IOException In case of not being able to access the database
     */
    @Override
    public List<IModel> getNotBeforeDate(Date date, Class<? extends IModel> entityClass) throws IOException {
        try {
            // Prepare the query
            Query queryDate = mDb.collection(entityClass.getSimpleName()).orderBy("eventDate", Query.Direction.ASCENDING)
                    .startAt(TimeUtils.fixTimezone(date));

            // Prepare the task getting the query
            Task<QuerySnapshot> querySnapshotTask = queryDate.get();

            // Wait for complete the operation
            querySnapshotTask.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            // Run the following operations if the operation is complete, or throw exception if not
            if (querySnapshotTask.isSuccessful()) {
                // Get query result from task
                QuerySnapshot queryResult = querySnapshotTask.getResult();

                // Prepare the variable result
                List<DocumentSnapshot> result;

                // Detect if the query result is empty or not
                if (queryResult != null && !queryResult.isEmpty()) {
                    result = queryResult.getDocuments();
                } else {
                    result = Collections.emptyList();
                }

                // Return the processed list
                return result.stream().map(obj -> {
                    IModel entity = obj.toObject(entityClass);
                    entity.setDocId(obj.getId());

                    return entity;
                }).collect(Collectors.toList());
            } else {
                throw new IOException("No could complete the operation");
            }
        } catch (InterruptedException e) {
            throw new IOException("No could complete the operation");
        }
    }

    /**
     * Method to obtain a list of entities of a specific date
     *
     * @param date The date to search
     * @param entityClass The entity class
     * @return The list of entities that have been returned from the list
     * @throws IOException In case of not being able to access the database
     */
    @Override
    public List<IModel> getBySpecificDate(Date date, Class<? extends IModel> entityClass) throws IOException {
        try {
            // Get the collection reference
            CollectionReference collectionRef = mDb.collection(entityClass.getSimpleName());

            // Clean the date parameter
            Date dateWithoutTime = TimeUtils.removeTimeFromDate(date);
            Date lastMinuteTime = TimeUtils.endOfDay(dateWithoutTime);

            // Prepare the query
            Query queryDate = collectionRef.orderBy("eventDate", Query.Direction.ASCENDING)
                    .startAt(dateWithoutTime).endAt(lastMinuteTime);

            // Prepare the task getting the query
            Task<QuerySnapshot> querySnapshotTask = queryDate.get();

            // Wait for complete the operation
            querySnapshotTask.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            // Run the following operations if the operation is complete, or throw exception if not
            if (querySnapshotTask.isSuccessful()) {
                // Get query result from task
                QuerySnapshot queryResult = querySnapshotTask.getResult();

                // Prepare the variable result
                List<DocumentSnapshot> result;

                // Detect if the query result is empty or not
                if (queryResult != null && !queryResult.isEmpty()) {
                    result = queryResult.getDocuments();
                } else {
                    result = Collections.emptyList();
                }

                // Return the processed list
                return result.stream().map(obj -> {
                    IModel entity = obj.toObject(entityClass);
                    entity.setDocId(obj.getId());

                    return entity;
                }).collect(Collectors.toList());
            } else {
                throw new IOException("No could complete the operation");
            }
        } catch (InterruptedException e) {
            throw new IOException("No could complete the operation");
        }
    }

    /**
     * Method to obtain an entity from its id
     *
     * @param docId The entity id
     * @param entityClass The entity class
     * @return The entity object
     * @throws IllegalAccessException In case the entity cannot be found
     * @throws IOException In case of not being able to access the database
     */
    @Override
    public IModel getByDocId(String docId, Class<? extends IModel> entityClass) throws IllegalAccessException, IOException {
        try {
            // Get the document reference
            DocumentReference docRef = mDb.collection(entityClass.getSimpleName()).document(docId);

            // Prepare the task getting the document
            Task<DocumentSnapshot> taskDocSnap = docRef.get();

            // Wait for complete the operation
            taskDocSnap.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            // Run the following operations if the operation is complete, or throw exception if not
            if (taskDocSnap.isSuccessful()) {
                // Get document result from task
                DocumentSnapshot docSnap = taskDocSnap.getResult();

                // Detect if the document result is empty or not
                if (docSnap != null && docSnap.exists()) {
                    IModel entity = docSnap.toObject(entityClass);
                    entity.setDocId(docSnap.getId());

                    return entity;
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

    /**
     * Method to obtain an entity from its friendlyId
     *
     * @param friendlyId The entity friendlyId
     * @param entityClass The entity class
     * @return The entity object
     * @throws IllegalAccessException In case the entity cannot be found
     * @throws IOException In case of not being able to access the database
     */
    @Override
    public IModel getByFriendlyId(String friendlyId, Class<? extends IModel> entityClass) throws IllegalAccessException, IOException {
        try {
            // Prepare the query
            Query queryFriendlyId = mDb.collection(entityClass.getSimpleName()).whereEqualTo("friendlyId", Integer.parseInt(friendlyId));

            // Prepare the task getting the query
            Task<QuerySnapshot> querySnapshotTask = queryFriendlyId.get();

            // Wait for complete the operation
            querySnapshotTask.addOnCompleteListener(command -> mThreadBus.add(true));
            mThreadBus.take();

            // Run the following operations if the operation is complete, or throw exception if not
            if (querySnapshotTask.isSuccessful()) {
                // Get query result from task
                QuerySnapshot queryResult = querySnapshotTask.getResult();

                // Detect if the document result is empty or not
                if (queryResult != null && !queryResult.isEmpty()) {
                    IModel entity = queryResult.getDocuments().get(0).toObject(entityClass);
                    entity.setDocId(queryResult.getDocuments().get(0).getId());

                    return entity;
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

    /**
     * Method to access the system administrator mode
     *
     * @param email The user administrator email
     * @param password The user administrator password
     * @throws IOException In case of not being able to access the database
     */
    @Override
    public void loginDatabase(String email, String password) throws IOException {
        try {
            // Check if is previously signed account
            if(mAuth.getCurrentUser() != null){
                mAuth.signOut();
            }

            // Login account with user and password into firebase
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(command -> {
                // Write the operation into logcat
                if (command.isSuccessful()) {
                    Log.d(ExpoSellerApplication.LOG_TAG, "Login successful with " + email);
                } else {
                    Log.d(ExpoSellerApplication.LOG_TAG, "Login fail " + command.getException());
                }

                // Set the complete operation
                mThreadBus.add(true);
            });

            // Wait for complete operation
            mThreadBus.take();

            // Check if isn't logged
            if (mAuth.getCurrentUser() == null) {
                throw new IOException("Could not login with this user");
            }
        } catch (InterruptedException e) {
            throw new IOException("Could not login with this user");
        }
    }

    /**
     * Method to be able to close the system administrator mode
     *
     * @throws IOException In case of not being able to access the database
     */
    @Override
    public void logoutDatabase() throws IOException {
        if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
        }
    }
}
