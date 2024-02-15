package edu.gvsu.cis.wordguess

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar






class MainActivity : AppCompatActivity() {
    val myViewModel: GameScreenViewModel by viewModels()
//    lateinit var myViewModel : GameScreenViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        myViewModel = ViewModelProvider(this).get(GameScreenViewModel::class.java)
        // Declare our widgets
        val checkButton = findViewById<Button>(R.id.checkInput)
        val doneButton = findViewById<Button>(R.id.doneButton)
        val tvCorrect = findViewById<TextView>(R.id.correctText)
        val tvIncorrect = findViewById<TextView>(R.id.incorrectText)
        val inputText = findViewById<EditText>(R.id.userInput)
        val tvScrambledWord = findViewById<TextView>(R.id.scrambledWord)



        // Set up our observers

        // if we are changing the word, we should be changing the scrambled word that's displayed
        /***
         * Why doesn't the below code work?
         */
//        myViewModel.currentWord.observe(this, Observer{
//            Log.d("Current Word Observer","Pre: tvScramText: ${tvScrambledWord.text}, shuffled word: ${myViewModel.shuffledWord}, actual word: ${myViewModel.currentWord.value}")
//            tvScrambledWord.text = myViewModel.shuffledWord
//            Log.d("Current Word Observer","post: tvScramText: ${tvScrambledWord.text}, shuffled word: ${myViewModel.shuffledWord}, actual word: ${myViewModel.currentWord.value}")
//            // look
//        })
        /***
         * Update scrambled Textbox to display the word
         */
         tvScrambledWord.text = myViewModel.shuffledWord


        // If the model's data for score was changed, then we should change the text to match
        myViewModel.model.correctScore.observe(this, Observer {
            value ->
            "Correct: ${value}".also { tvCorrect.text = it }
            // Hide keyboard to show snackbar
        })

        // If the users incorrect score changes, then the label displaying the incorrect score
        // should update and reflect this change.
        myViewModel.model.incorrectScore.observe(this, Observer {
            value ->
            "Incorrect: ${value}".also { tvIncorrect.text = it }
        })

//        // Anytime the user enters a wrong guess, we want to display a snackbar message
//        myViewModel.numWrongGuessCounter.observe(this){
//            if (myViewModel.numWrongGuessCounter.value == 0){
//
//            } else if ( myViewModel.numWrongGuessCounter.value == 3){
//                Snackbar.make(findViewById(R.id.gameScreenLayout), "Only 3 attempts per word allowed", Snackbar.LENGTH_SHORT ).show() //required to type 3 arguments
//
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

        // Let's implement the check Button function
        checkButton.setOnClickListener{
            var guessCorrect: Boolean = myViewModel.checkGuess(inputText.text.toString())
            // guess incorrect increment numWrongGuesses, if it's correct call method
            // to change word. If numIncorrectGuesses >= 3, call method to change word

            //if users guess is correct, increment the current score.
            if (guessCorrect){
                // To hide the keyboard when a snackbar is displayed
                myViewModel.model.correctScore.value = myViewModel.model.correctScore.value?.plus(1)
                myViewModel.assignMessage("You guessed correctly, Try again!")
                showSnackbarMssg()
                myViewModel.pickRandomWord()
                // Update textbox with new randomly scrambled wor
                tvScrambledWord.text = myViewModel.shuffledWord

            }
            // If user guesses the word incorrectly increment the currentWrongGuessesCounter
            else {
                myViewModel.numWrongGuessCounter.value = myViewModel.numWrongGuessCounter.value?.plus(1)
                // If user guessed wrong on this word 3 times, change to a new word.
                if (myViewModel.numWrongGuessCounter.value == 3){
                    myViewModel.model.incorrectScore.value = myViewModel.model.incorrectScore.value?.plus(1)
                    println("incorrect score = ${myViewModel.model.incorrectScore.value.toString()}")
                    myViewModel.assignMessage("Only 3 guesses allowed, try again!")
                    showSnackbarMssg()
                    myViewModel.pickRandomWord()
                    // Update textbox to display scrambled word
                    tvScrambledWord.text = myViewModel.shuffledWord
                    myViewModel.numWrongGuessCounter.value = 0
                } else{
                    myViewModel.assignMessage("Attempt ${myViewModel.numWrongGuessCounter.value} incorrect. Try again!")
                    showSnackbarMssg()
                }

            }

            // Lets implement the Done Button
            doneButton.setOnClickListener{
                finish()
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