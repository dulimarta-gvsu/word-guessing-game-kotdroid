package edu.gvsu.cis.wordguess

import androidx.lifecycle.ViewModel

class settingsScreenViewModel: ViewModel() {

    var _snackMssg: String = ""

    fun isValidMinLength(minLength: Int) = minLength in 1..6

    fun isValidMaxLength(maxLength: Int) = maxLength in 1 ..6
    fun assignMessage(message: String){
        _snackMssg = message
    }

}