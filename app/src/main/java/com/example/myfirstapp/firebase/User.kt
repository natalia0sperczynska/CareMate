package com.example.myfirstapp.firebase

//data class
data class User (
    val id: String = "",
    val name: String? = null,
    val surname: String?=null,
    val email: String = "",
    val dateOfBirth: String = "",
    val phoneNumber: String = "",
    val profilePictureUrl: String = "",
    val address:Map<String,String> = mapOf(),
    val allergies:List<String> = listOf(),
    val diseases:List<String> = listOf(),
    val medications:List<String> = listOf()
){
    /*
    *  * Key Features of Companion Object:
    *          * Singleton by Default: There is only one instance of a companion object per class.
    *          * Access to Private Members: It can access the private members of the containing class.
    *          * Implicit Name: If you don't name the companion object, it will automatically get the name Companion.
    *          * Implements Interfaces: A companion object can implement interfaces.
    *          * Extension Functions: You can define extension functions for a companion object.
    */
    companion object {
        /**
         * Converts a map of Firestore document data into a User object.
         *
         * This method ensures type safety by checking the type of each field in the map before
         * assigning it to the corresponding property in the `User` object. If a field is missing
         * or has the wrong type, a default value is used instead.
         *
         * @param data The map containing the user data fetched from Firestore.
         * @return A `User` object containing the mapped data.
         *
         */
        fun fromMap(data: Map<String, Any?>): User {
            return User(
                // Retrieve the "id" field as a string, or use an empty string if it's missing
                id = data["id"] as? String ?: "",

                // Retrieve the "name" field as a nullable string
                name = data["name"] as? String,

                surname = data["surname"] as? String,

                // Retrieve the "email" field as a string, or use an empty string if it's missing
                email = data["email"] as? String ?: "",

                // Retrieve the "phoneNumber" field as a string, or use an empty string if it's missing
                phoneNumber = data["phoneNumber"] as? String ?: "",

                // Retrieve the "dateOfBirth" field as a string, or use an empty string if it's missing
                dateOfBirth = data["dateOfBirth"] as? String ?: "",

                // Parse the "address" field as a map of strings, ignoring any invalid entries
                address = (data["address"] as? Map<*, *>)
                    ?.mapNotNull { (key, value) ->
                        if (key is String && value is String) key to value else null
                    }?.toMap() ?: mapOf(),

                // Parse the "interests" field as a list of strings, ignoring any invalid entries
                allergies = (data["allergies"] as? List<*>)
                    ?.mapNotNull { it as? String } ?: listOf(),
                // Parse the "interests" field as a list of strings, ignoring any invalid entries
                diseases = (data["diseases"] as? List<*>)
                    ?.mapNotNull { it as? String } ?: listOf(),
                // Parse the "interests" field as a list of strings, ignoring any invalid entries
                medications = (data["medications"] as? List<*>)
                    ?.mapNotNull { it as? String } ?: listOf(),

                // Retrieve the "profilePictureUrl" field as a string, or use an empty string if it's missing
                profilePictureUrl = data["profilePictureUrl"] as? String ?: ""
            )
        }
    }
}