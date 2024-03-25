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

    // firebase stuff
    private val _snackMsg = MutableLiveData<String>(null)
    val snackMsg: LiveData<String?> get() = _snackMsg

    private val _acctDltSucc: MutableLiveData<Boolean?> = MutableLiveData(null)
    val acctDltSucc: LiveData<Boolean?> get() = _acctDltSucc

    private val firebaseDb = Firebase.firestore

    var fireUserId: String = ""

    fun deleteAccnt(){
        viewModelScope.launch(Dispatchers.IO){
            firebaseDb.document("/users/$fireUserId")
                .delete()
                .addOnSuccessListener {
                    _snackMsg.postValue("Successfully deleted account")
                    _acctDltSucc.postValue(true)
                }
                .addOnFailureListener{
                    _snackMsg.postValue("Failed to delete Account ${it.message}")
                    _acctDltSucc.postValue(false)
                }


        }
    }



    fun isValidMinLength(minLength: Int) = minLength in 1..6

    fun isValidMaxLength(maxLength: Int) = maxLength in 1 ..6
    fun assignMessage(message: String){
        _snackMssg = message
    }

}