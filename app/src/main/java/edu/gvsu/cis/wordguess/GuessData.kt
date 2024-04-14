package edu.gvsu.cis.wordguess

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuessData(
    var scrambledWord: String,
    var secondsToGuess: Long,
    var successOrFailure: Boolean,
    var actualWord: String
) : Parcelable {
    var secretWord: String = ""
    var displayActualWord: Boolean = false

    init {
        for (i in 1..scrambledWord.length) {
            secretWord += "*"
        }
    }
}
