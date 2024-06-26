package edu.gvsu.cis.wordguess

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlin.time.Duration
import kotlin.time.DurationUnit


class GameScreenMainActivityAlecMirambeau : AppCompatActivity() {
    val myViewModel: GameScreenViewModel by viewModels()
//    lateinit var myViewModel : GameScreenViewModel
    val fromSettings =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == 123) {
                /** Code to execute if settings activity returned okay (123) */
                var bundle = it.data?.getBundleExtra("bundle")
                var minWordLength = bundle?.getInt("minLength")
                var maxWordLength = bundle?.getInt("maxLength")
                println("max and min bundle values, max: $maxWordLength, min: $minWordLength")
                if (minWordLength != null) {
                    if (minWordLength != myViewModel.minWordLength.value) {
                        myViewModel.minWordLength.value = minWordLength
                        /** if the minimum word size changes we should change the current word to match this
                         */
                        myViewModel.pickRandomWord()
                    }
                }
                if (maxWordLength != null) {
                    if (maxWordLength != myViewModel.maxWordLength.value) {
                        myViewModel.maxWordLength.value = maxWordLength
                        /** if the maximum word size changes we should change the current word to match this
                         */
                        myViewModel.pickRandomWord()
                    }
                }

            } else if (it.resultCode == 321){
                var wordsListReturned = it.data?.getParcelableArrayListExtra<GuessData>("wordListGuessed")
                if (wordsListReturned != null) {
                    myViewModel.wordsGuessed = wordsListReturned
                }
            }
            else if (it.resultCode == 212){
                finish()
            }

        }

//    val yellowLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == RESULT_OK) {
//                val confCode = it.data?.getStringExtra("confirmation")
//                Snackbar.make(
//                    binding.root,
//                    "Your confirmation code is $confCode",
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//        }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firebase variables
        val firebaseUID = intent.getStringExtra("userIDValue")
        myViewModel.userID = firebaseUID
        myViewModel.getUserData()
//        myViewModel = ViewModelProvider(this).get(GameScreenViewModel::class.java)
        // Declare our widgets
        val checkButton = findViewById<Button>(R.id.checkInput)
        val doneButton = findViewById<Button>(R.id.gameScreenLogOutButton)
        val tvCorrect = findViewById<TextView>(R.id.correctText)
        val tvIncorrect = findViewById<TextView>(R.id.incorrectText)
        val inputText = findViewById<EditText>(R.id.userInput)
        val tvScrambledWord = findViewById<TextView>(R.id.scrambledWord)
        val settingsBttn = findViewById<Button>(R.id.gameScreenSettingsButton)
        val statisticsButton = findViewById<Button>(R.id.gameScreenViewScoreBoard)
        val progressCircle = findViewById<ProgressBar>(R.id.gameScreenProgressBarCircle)
        progressCircle.visibility = View.VISIBLE

        //firebase widget
        val userNameTV = findViewById<TextView>(R.id.gameScreenUserNameTV)

        // Set up our observers
        // if we are changing the word, we should be changing the scrambled word that's displayed
        myViewModel.shuffledWord.observe(this, Observer {
//            Log.d("Current Word Observer","Pre: tvScramText: ${tvScrambledWord.text}, shuffled word: ${myViewModel.shuffledWord}, actual word: ${myViewModel.currentWord.value}")
            tvScrambledWord.text = myViewModel.shuffledWord.value
//            Log.d("Current Word Observer","post: tvScramText: ${tvScrambledWord.text}, shuffled word: ${myViewModel.shuffledWord}, actual word: ${myViewModel.currentWord.value}")
            // look
        })
        /***
         * Update scrambled Textbox to display the word
         */


        // If the model's data for score was changed, then we should change the text to match
        myViewModel.model.correctScore.observe(this, Observer { value ->
            "Correct: ${value}".also { tvCorrect.text = it }
        })

        // If the users incorrect score changes, then the label displaying the incorrect score
        // should update and reflect this change.
        myViewModel.model.incorrectScore.observe(this, Observer { value ->
            "Incorrect: ${value}".also { tvIncorrect.text = it }
            Log.d("gameScreenGlitchCrash", "made it this far")

        })

        myViewModel.wordFetchComplete.observe(this){
            // Why does this function keep executing even though wordFetchComplete is null?
            // It causes the program to crash
//            progressCircle.visibility = View.GONE
//            Log.d("wordFetchComplete", "wordFetchValue: ${myViewModel.wordFetchComplete.value}")
//            myViewModel.pickRandomWord()
            if (myViewModel.wordFetchComplete.value != null && myViewModel.wordFetchComplete.value == true){
                progressCircle.visibility = View.GONE
                Log.d("wordFetchComplete", "wordFetchValue: ${myViewModel.wordFetchComplete.value}")
                if (myViewModel.firstFetchEver == true ){
                    myViewModel.pickRandomWord()
                    myViewModel.firstFetchEver = false
                }
            }
        }

        myViewModel.snackMssgfb.observe(this){
            if (myViewModel.snackMssgfb.value != null) {
                // I know the line below is causing the snackbar message to appear
                // when the screen gets rotated. I just ran out of time before the due date
                // to fix it, sorry. :(
                myViewModel.snackMssg = myViewModel.snackMssgfb.value!!
                showSnackbarMssg()
            }
        }

        myViewModel.userUsername.observe(this){
            userNameTV.text = myViewModel.userUsername.value
        }

        // user clicked logout, once data is uploaded, then they should be
        // logged out
        myViewModel.dataUploadedToFirebaseSuccess.observe(this){
            if (myViewModel.dataUploadedToFirebaseSuccess.value == true){
                finish()
            }
        }


//        // Anytime the user enters a wrong guess, we want to display a snackbar message
//        myViewModel.numWrongGuessCounter.observe(this){
//            if (myViewModel.numWrongGuessCounter.value == 0){
//
//            } else if ( myViewModel.numWrongGuessCounter.value == 3){
//                Snackbar.make(findViewById(R.id.gameScreenLayout), "Only 3 attempts per word allowed", Snackbar.LENGTH_SHORT ).show() //required to type 3 arguments
// //
//            }
//            else {
//                Snackbar.make(
//                    findViewById(R.id.gameScreenLayout), "Attempt " +
//                            "${myViewModel.numWrongGuessCounter.value.toString()} incorrect. Try again",
//                    Snackbar.LENGTH_SHORT
//                ).show() //required to type 3 arguments
//            }
//        }

        // End of observers
        //

        //Lets give the buttons functionality

        /** Let's implement the check Button function */
        checkButton.setOnClickListener {
            myViewModel.checkButtn(inputText.text.toString())
            showSnackbarMssg()
        }

        /** Lets implement the Done Button */
        doneButton.setOnClickListener {
            myViewModel.doLogout()
        }


        /** Let's implement the settings button */
        settingsBttn.setOnClickListener {
            val toSettings = Intent(this, settingsScreenActivity::class.java)
            toSettings.putExtra("minSize", myViewModel.minWordLength.value.toString())
            toSettings.putExtra("maxSize", myViewModel.maxWordLength.value.toString())
            toSettings.putExtra("userIDValue", firebaseUID)
            fromSettings.launch(toSettings)
        }


        /** Lets implement the Statistics screen button */
        statisticsButton.setOnClickListener{
            if (myViewModel.model.correctScore.value!! > 0 || myViewModel.model.incorrectScore.value!! > 0) {
                val toStatistics = Intent(this, statisticsActivity::class.java)
                toStatistics.putParcelableArrayListExtra("wordList", myViewModel.wordsGuessed)
//                toStatistics.putExtra("sumSecondsToGuess", myViewModel.sumSecondsToGuess)
                startActivity(toStatistics)
            }
        }




    }




    fun showSnackbarMssg(): Unit{
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
        }
        // show snackbar
        Snackbar.make(
            findViewById(R.id.gameScreenLayout), myViewModel.snackMssg, Snackbar.LENGTH_SHORT
        ).show() //required to type 3 arguments

    }



}