package com.example.myfirstapp.firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FireStore {
    private val mFireStore = FirebaseFirestore.getInstance()
    /**
     * Registers or updates a user in Firestore.
     *
     * @param user The user object to save.
     * @throws Exception If there is an error during the save operation.
     */
    suspend fun registerOrUpdateUser(user: User) {
        try {
            // Save or overwrite the user document with the given user ID
            mFireStore.collection("users").document(user.id).set(user).await()
        } catch (e: Exception) {
            // Throw an exception with a descriptive message if an error occurs
            throw Exception("Error saving user data: ${e.message}")
        }
    }
}