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

// Firebase imports
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlin.time.Duration
import kotlin.time.DurationUnit


class GameScreenViewModel: ViewModel() {
    private val _model = Model()
    val timeSource = TimeSource.Monotonic
    var startTime: TimeSource.Monotonic.ValueTimeMark = timeSource.markNow()
    var endTime = timeSource.markNow()
    private val _wordFetchComplete = MutableLiveData<Boolean?>(null)
    var firstFetchEver = true
    val wordFetchComplete: LiveData<Boolean?> get() = _wordFetchComplete

    private val _currentWord: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    var wordsGuessed: ArrayList<GuessData> = ArrayList()

    var numWrongGuessCounter: MutableLiveData<Int> = MutableLiveData<Int>(0)
    private val _shuffledWord: MutableLiveData<String?> = MutableLiveData(null)
    var snackMssg: String = ""
    var minWordLength = MutableLiveData<Int>(3)
    var maxWordLength = MutableLiveData<Int>(6)
    var sumSecondsToGuess: Long = 0
    var alreadyPickedRandomWord = false

    // API Related variables
    val apiEndpoint: WordAPI
    var listOfWord: MutableList<String> = mutableListOf()

    // firebase variables
    private val firebaseDB = Firebase.firestore
    var userID: String? = null
    private val _snackMssg = MutableLiveData<String?>(null)
    val snackMssgfb: LiveData<String?> get() = _snackMssg

    private val _userUsername = MutableLiveData<String?>()
    val userUsername: LiveData<String?> get() = _userUsername
     init {
         apiEndpoint = wordClient.getInstance().create(WordAPI::class.java)
         genWords(10, maxWordLength.value)
         println(" Picking a random word")
         startTime = timeSource.markNow()
     }
    val currentWord: MutableLiveData<String> get() = _currentWord
    val shuffledWord: MutableLiveData<String?> get() = _shuffledWord
    val model: Model get() = _model




    fun getUserName(){
        firebaseDB.document("/users/$userID")
            .get()
            .addOnSuccessListener {
                val user: userData? = it.toObject(userData::class.java)
                Log.d("userData", "UserID is ${user?.userName}")
                _snackMssg.postValue("Successfully got Users data")
                _userUsername.postValue(user?.userName)
            }
            .addOnFailureListener{
                Log.d("userData", "Failed to fetch document\n" +
                        "mssg: ${it.message}")
                _snackMssg.postValue("Failed to get user Data ${it.message}")
            }

    }

    fun checkButtn(guess: String){
        var guessCorrect: Boolean = checkGuess(guess)
        // guess incorrect increment numWrongGuesses, if it's correct call method
        // to change word. If numIncorrectGuesses >= 3, call method to change word

        //if users guess is correct, increment the current score.
        if (guessCorrect) {
            /**
             * once we know the boolean value within the viewmodel just have the game logic there, and same idea for if the guess is false.
             */
            endTime = timeSource.markNow()
            val elapsedTime: Duration = endTime - startTime
            var elapsedTimeSeconds = elapsedTime.toLong(DurationUnit.SECONDS)
            sumSecondsToGuess += elapsedTimeSeconds
            var word = GuessData(shuffledWord.value.toString(), elapsedTimeSeconds, true, currentWord.value.toString())
            wordsGuessed.add(word)
            model.correctScore.value = model.correctScore.value?.plus(1)
            assignMessage("You guessed correctly, Try again!")
            pickRandomWord()
            endTime = timeSource.markNow()
            startTime = timeSource.markNow()
        }
        // If user guesses the word incorrectly increment the currentWrongGuessesCounter
        else {
            numWrongGuessCounter.value =
                numWrongGuessCounter.value?.plus(1)
            // If user guessed wrong on this word 3 times, change to a new word.
            if (numWrongGuessCounter.value == 3) {
                endTime = timeSource.markNow()
                val elapsedTime: Duration = endTime - startTime
                val elapsedTimeSeconds = elapsedTime.toLong(DurationUnit.SECONDS)
                sumSecondsToGuess += elapsedTimeSeconds
                var word = GuessData(shuffledWord.value.toString(), elapsedTimeSeconds, false, currentWord.value.toString())
                wordsGuessed.add(word)
                model.incorrectScore.value =
                    model.incorrectScore.value?.plus(1)
                println("incorrect score = ${model.incorrectScore.value.toString()}")
                assignMessage("Only 3 guesses allowed, try again!")
                pickRandomWord()
                numWrongGuessCounter.value = 0
                startTime = timeSource.markNow()
            } else {
                assignMessage("Attempt ${numWrongGuessCounter.value} incorrect. Try again!")
            }
        }
    }

    fun genWords(numWords: Int = 10, maxWordLength: Int?): Unit{
        if (maxWordLength != null){
        viewModelScope.launch(Dispatchers.IO) {
            var rWords = apiEndpoint.getMultipleWords(numWords, maxWordLength)
            rWords.body().let{
                if (it != null) {
                    Log.d("Values Returned from API: ", "$it")
                    listOfWord = it as MutableList<String>
                    _wordFetchComplete.postValue(true)
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

    fun pullPreviousData(){

    }

    fun pushDataBeforeLogout(){

    }
//Updates the current word with a randomly picked word from our mutable list of words.
    fun pickRandomWord(){
        var new_word: String
            // Continue to call genNewWordCandidate until we get a word that is different than our
            // current word
            new_word = genNewWordCandidate()
            while (new_word == currentWord.value || new_word.length !in minWordLength.value!! ..maxWordLength.value!!){
//            new_word = genNewWordCandidate()
                new_word = listOfWord.random()
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

/** API Stuff Below

 */


