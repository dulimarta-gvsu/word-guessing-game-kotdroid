package edu.gvsu.cis.wordguess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import android.util.Log

class GameScreenViewModel: ViewModel() {
    private val _model = Model()
    private val _currentWord: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    var numWrongGuessCounter: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var shuffledWord: String = ""
    var snackMssg: String = ""
     init {
         pickRandomWord()
         println("In init output shuffled word: $shuffledWord, actual word: ${currentWord.value}")

     }

    val currentWord: MutableLiveData<String> get() = _currentWord
    val model: Model get() = _model


//Updates the current word with a randomly picked word from our mutable list of words.
    fun pickRandomWord(){
        var new_word: String = model.allWords.random()
    // Continue to call random word until we get a word that is different than our
    // current word
        while (new_word == currentWord.value){
            new_word = model.allWords.random()
        }
    // Assign the new word
        currentWord.value = new_word
        shuffledWord = shuffleWord()
        println("shuffled word: $shuffledWord, actual word: ${currentWord.value}")

    }

    fun shuffleWord(): String {
        var characters = currentWord.value.toString().toCharArray()
        characters.shuffle()
        var scrambledWord = characters.joinToString("")
        return scrambledWord
    }

    fun checkGuess(guess: String): Boolean = guess == currentWord.value

    fun assignMessage(message: String){
        snackMssg = message
    }



}