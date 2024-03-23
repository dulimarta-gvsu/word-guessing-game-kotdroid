package edu.gvsu.cis.wordguess

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            val currentUser = fireAuth.currentUser
                currentUser?.let {
                    val newUser = userData(email, password, userName)
                        firebase.collection("users")
                            .document(it.uid)
                            .set(newUser)
                .addOnSuccessListener {
                    _signUpSucess.value = true
                    // get and assign the User ID value provided by firebase
                    _userID.postValue(currentUser.uid)
                    _snackMsg.postValue("Successfully signed in!")
            }
                .addOnFailureListener {
                    _snackMsg.postValue(it.message)
                    _signUpSucess.value = false
                }
            }
        }
    }
}