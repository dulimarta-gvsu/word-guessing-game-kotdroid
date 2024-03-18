package edu.gvsu.cis.wordguess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.time.TimeSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


class GameScreenViewModel: ViewModel() {
    private val _model = Model()
    val timeSource = TimeSource.Monotonic
    var startTime: TimeSource.Monotonic.ValueTimeMark = timeSource.markNow()
    var endTime = timeSource.markNow()

    private val _currentWord: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    var wordsGuessed: ArrayList<GuessData> = ArrayList()

    var numWrongGuessCounter: MutableLiveData<Int> = MutableLiveData<Int>(0)
    private val _shuffledWord: MutableLiveData<String> = MutableLiveData("")
    var snackMssg: String = ""
    var minWordLength = MutableLiveData<Int>(3)
    var maxWordLength = MutableLiveData<Int>(6)
    var sumSecondsToGuess: Long = 0
     init {
         println(" Picking a random word")
         pickRandomWord()
     }
    val currentWord: MutableLiveData<String> get() = _currentWord
    val shuffledWord: MutableLiveData<String> get() = _shuffledWord
    val model: Model get() = _model


//Updates the current word with a randomly picked word from our mutable list of words.
    fun pickRandomWord(){
        var new_word: String = model.allWords.random()
    // Continue to call random word until we get a word that is different than our
    // current word
        while (new_word == currentWord.value || new_word.length !in minWordLength.value!! ..maxWordLength.value!!){
            new_word = model.allWords.random()
            println("picking new word: $new_word")
        }

    // Assign the new word
        currentWord.value = new_word
        shuffledWord.value = shuffleWord()
    var cmpr = shuffledWord.value == currentWord.value
    while (cmpr){
        shuffledWord.value = shuffleWord()
        cmpr = shuffledWord.value == currentWord.value
    }

    println("shuffled word: ${shuffledWord.value}, actual word: ${currentWord.value}")

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

// API to be used: https://random-word-api.herokuapp.com/word
interface WordAPI{
    @GET("")
    suspend fun getWord(@Query(""))
}

