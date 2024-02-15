package edu.gvsu.cis.wordguess

import androidx.lifecycle.MutableLiveData

class Model {
    var allWords: MutableList<String> = mutableListOf<String>("apples", "bananas", "oranges")

    private val _incorrectScore: MutableLiveData<Int> = MutableLiveData(0)
    // When the game is first ran a new word is made, so this value gets incremented
    // and becomes 0.
    private val _correctScore = MutableLiveData(0)

    val incorrectScore: MutableLiveData<Int> get() = _incorrectScore
    val correctScore: MutableLiveData<Int> get() = _correctScore



}