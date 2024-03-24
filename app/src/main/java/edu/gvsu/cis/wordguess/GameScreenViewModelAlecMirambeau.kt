package edu.gvsu.cis.wordguess

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.time.TimeSource

// Coroutine imports
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Retrofit2 imports
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
    private val _wordFetchComplete = MutableLiveData<Boolean?>(false)
    val wordFetchComplete: LiveData<Boolean?> get() = _wordFetchComplete

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
    var alreadyPickedRandomWord = false

    // API Related variables
    val apiEndpoint: WordAPI
    var listOfWord: MutableList<String> = mutableListOf()
     init {
         println(" Picking a random word")
         apiEndpoint = wordClient.getInstance().create(WordAPI::class.java)
         genWords(10, maxWordLength.value)
         pickRandomWord()
         alreadyPickedRandomWord = true
     }
    val currentWord: MutableLiveData<String> get() = _currentWord
    val shuffledWord: MutableLiveData<String> get() = _shuffledWord
    val model: Model get() = _model


    fun genWords(numWords: Int = 10, maxWordLength: Int?): Unit{
        if (maxWordLength != null){
        viewModelScope.launch(Dispatchers.IO) {
            var rWords = apiEndpoint.getMultipleWords(numWords, maxWordLength)
            rWords.body().let{
                if (it != null) {
                    Log.d("Values Returned from API: ", "$it")
                    listOfWord = it as MutableList<String>

                }
            }
            }
        }
    }

    fun genNewWordCandidate(): String{
        if (listOfWord.size == 4){
            genWords(maxWordLength = maxWordLength.value)
        }
        val randomIndex = (0 until listOfWord.size).random()
        val randomWord = listOfWord[randomIndex]
        listOfWord.removeAt(randomIndex)
        return randomWord
    }
//Updates the current word with a randomly picked word from our mutable list of words.
    fun pickRandomWord(){
        var new_word: String
        if (!alreadyPickedRandomWord){
            new_word = model.allWords.random()
            println("picking new word: $new_word")
            // Continue to call random function until we get a word that is different than our
            // current word
            while (new_word == currentWord.value || new_word.length !in minWordLength.value!! ..maxWordLength.value!!){
                new_word = model.allWords.random()
                println("picking new word: $new_word")
            }

        }
    else{
            // Continue to call genNewWordCandidate until we get a word that is different than our
            // current word
            new_word = genNewWordCandidate()
            while (new_word == currentWord.value || new_word.length !in minWordLength.value!! ..maxWordLength.value!!){
//            new_word = genNewWordCandidate()
                new_word = listOfWord.random()
                println("picking new word: $new_word")
            }

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

/** API Stuff Below

 */


