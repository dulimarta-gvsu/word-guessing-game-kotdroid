package edu.gvsu.cis.wordguess

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class loginScreenViewModel: ViewModel() {
    val d = ""
    private val _userID = MutableLiveData<String?>(null)

    private val _logInSucc: MutableLiveData<Boolean?> = MutableLiveData(null)
    val logInSucc: LiveData<Boolean?> get() = _logInSucc
    private val auth = Firebase.auth
    private val _snackMsg = MutableLiveData<String>(null)
    val snackMsg: LiveData<String?> get() = _snackMsg

    val userID: LiveData<String?> get() = _userID

    // Why is this init needed?
    init {
        _userID.postValue(auth.uid)
    }

    fun doLogin(email: String, pass: String){
        viewModelScope.launch(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    _userID.postValue(it.user?.uid)
                    Log.d("correctUserID", "email: ${email}, pass: ${pass}")
                    Log.d("correctUserID", "UserID is: ${_userID.value}")

                    _snackMsg.postValue("Successfully logged In")
                    _logInSucc.postValue(true)
                }
                .addOnFailureListener{
                    _snackMsg.postValue("Unable to Login: " + it.message)
                    _logInSucc.postValue(false)
                }
        }
    }


}