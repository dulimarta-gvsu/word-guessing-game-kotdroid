package edu.gvsu.cis.wordguess

import androidx.lifecycle.MutableLiveData

class Model {

    private val _incorrectScore: MutableLiveData<Int?> = MutableLiveData(null)
    // When the game is first ran a new word is made, so this value gets incremented
    // and becomes 0.
    private val _correctScore: MutableLiveData<Int?> = MutableLiveData(null)

    val incorrectScore: MutableLiveData<Int?> get() = _incorrectScore
    val correctScore: MutableLiveData<Int?> get() = _correctScore

    init {
        _incorrectScore.value = 0
        _correctScore.value = 0
    }


}