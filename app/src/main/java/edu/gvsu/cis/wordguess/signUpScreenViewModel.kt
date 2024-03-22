package edu.gvsu.cis.wordguess

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class signUpScreenViewModel: ViewModel() {



    /**
     * Below is erroring because we haven't imported some necessary stuff
     * Nor have we inherited from viewModel .
     */
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