package edu.gvsu.cis.wordguess

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
    private val _snackMsg = MutableLiveData<String>("")
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
                    _snackMsg.value = "Sucessfully logged In"
                    _logInSucc.value = true
                }
                .addOnFailureListener{
                    _snackMsg.postValue("Unable to Login: " + it.message)
                    _logInSucc.value = false
                }
        }
    }


}