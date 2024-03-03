package edu.gvsu.cis.wordguess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class statisticsVewModel: ViewModel() {
    var num = 10
    lateinit var guessedWords: ArrayList<GuessData>
    val update = MutableLiveData(0)

    fun avgGuessTime(): Float{
        var sum: Float = 0.0F
        for (i in 0 until guessedWords.size){
            sum += guessedWords[i].secondsToGuess
        }
        return sum / guessedWords.size
    }

    fun sortByTime(){
        guessedWords.sortBy {
            it.secondsToGuess
        }
        update.value = 1
    }

    fun sortByWordLength(){
        guessedWords.sortBy{
            it.scrambledWord.length
        }
    }


}