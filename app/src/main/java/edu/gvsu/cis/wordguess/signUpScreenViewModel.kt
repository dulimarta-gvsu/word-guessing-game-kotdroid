package edu.gvsu.cis.wordguess

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import com.google.firebase.firestore.FirebaseFirestore


class signUpScreenViewModel: ViewModel() {

    val fireAuth = Firebase.auth

    val firebase = Firebase.firestore
    private val _signUpSucess = MutableLiveData<Boolean?>(null)
    val signUpSuccess: LiveData<Boolean?> get() = _signUpSucess

    val _snackMsg = MutableLiveData<String?>(null)
    val snackMsg: LiveData<String?> get() = _snackMsg

    val _userID = MutableLiveData<String?>(null)
    val userID: LiveData<String?> get() = _userID

    /**
     * Coroutine to signUp
     */
    fun createNewAccount(email:String, password: String, userName: String) {

        viewModelScope.launch(Dispatchers.IO) {
            fireAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Log.d("Firebase", "sucess creatino")
                    _userID.postValue(fireAuth.currentUser?.uid)
                    _snackMsg.postValue("Successfully signed up!")
                    _signUpSucess.postValue(true)
                    // use set not add, it'll create document automatically for you
                    val newUser = userData(email, password, userName, it.user?.uid!!)
                    firebase.document("/users/${it.user?.uid}")
                        .set(newUser)
                    Log.d("Firebase", "Successfully added user")

                }
                .addOnFailureListener {
                    Log.d("Firebase", "Failure creation")
                    _snackMsg.postValue(it.message)
                    Log.d("Firebase", "failed to add user")
                    _snackMsg.postValue("unable to login ${it.message}")
                    _signUpSucess.postValue(false)
                }
        }

    }
//            val created = fireAuth.createUserWithEmailAndPassword(email, password)
//            val currentUser = fireAuth.currentUser
//            Log.d("Firebase", "got current user: ${currentUser.toString()}\n" +
//                    "Created new user: ${created.toString()}")
//            Log.d("Firebase", "is currentUser null?: ${currentUser == null}")
//            Log.d("Firebase", "current userID: ${currentUser?.uid}")
//            currentUser?.let {
//                val newUser = userData(email, password, userName)
//                firebase.document("/users/${currentUser.uid}")
//                    // use set not add, it'll create document automatically for you
//                    .set(newUser)
//
//                .addOnSuccessListener {
//                    Log.d("Firebase", "Successfully added user")
//                    // get and assign the User ID value provided by firebase
//                    _userID.postValue(currentUser.uid)
//                    _snackMsg.postValue("Successfully signed up!")
//                    _signUpSucess.postValue(true)
//                }
//
//
//                .addOnFailureListener {
//                    Log.d("Firebase", "failed to add user")
//                    _snackMsg.postValue("unable to login ${it.message}")
//                    _signUpSucess.postValue(false)
//                }
//                Log.d("Firebase", "Function this far")
//                Log.d("Firebase", "SnackMsg is: $_snackMsg")
//            }
//        }
//    }
    }
