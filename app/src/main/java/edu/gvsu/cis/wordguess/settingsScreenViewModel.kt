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

class settingsScreenViewModel: ViewModel() {

    var _snackMssg: String = ""

    private val auth = Firebase.auth

    // firebase stuff
    private val _snackMsg = MutableLiveData<String>(null)
    val snackMsg: LiveData<String?> get() = _snackMsg

    private val _acctDltSucc: MutableLiveData<Boolean?> = MutableLiveData(null)
    val acctDltSucc: LiveData<Boolean?> get() = _acctDltSucc

    private val firebaseDb = Firebase.firestore

    fun deleteAccnt(accountID: String){
        viewModelScope.launch(Dispatchers.IO){
            firebaseDb.document("/users/$accountID")
                .delete()
                .addOnSuccessListener {
                    _snackMsg.postValue("Successfully deleted account")
                    _acctDltSucc.postValue(true)
                }
                .addOnFailureListener{
                    _snackMsg.postValue("Failed to delete Account ${it.message}")
                    _acctDltSucc.postValue(false)
                }
            auth.currentUser?.delete()


        }
    }



    fun isValidMinLength(minLength: Int) = minLength in 1..6

    fun isValidMaxLength(maxLength: Int) = maxLength in 1 ..6
    fun assignMessage(message: String){
        _snackMssg = message
    }

}