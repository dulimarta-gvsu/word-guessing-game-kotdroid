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
    private val _logInSucc = MutableLiveData<String?>(null)
    val logInSucc: LiveData<String?> get() = _logInSucc
    private val auth = Firebase.auth
    private val _snackMsg = MutableLiveData<String>("")
    val snackMsg: LiveData<String?> get() = _snackMsg

    // Why is this init needed?
    init {
        _logInSucc.postValue(auth.uid)
    }

    fun doLogin(email: String, pass: String){
        viewModelScope.launch(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener { _logInSucc.postValue(it.user?.uid) }
                .addOnFailureListener{
                    _snackMsg.postValue(it.message)
                }
        }
    }

    fun createNewAccount(email:String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _logInSucc.postValue(it.user?.uid)
                }
                .addOnFailureListener {
                    _snackMsg.postValue(it.message)
                }
        }
    }



}