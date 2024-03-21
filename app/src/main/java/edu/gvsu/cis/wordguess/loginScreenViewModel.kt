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
    val _logInSucc = MutableLiveData<String?>(null)
    val logInSucc: LiveData<String?> get() = _logInSucc
    private val auth = Firebase.auth

    fun doLogin(email: String, pass: String){
        viewModelScope.launch(Dispatchers.IO) {
            auth.signInWithEmailAndPassword()
        }
    }


}