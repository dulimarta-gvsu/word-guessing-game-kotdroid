package edu.gvsu.cis.wordguess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

class settingsScreenActivity: AppCompatActivity() {
    val vm : settingsScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val inputMM = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager


        /** Declare widet creations */
        val minWordSize = findViewById<EditText>(R.id.minimumWordSizeInput)
        val maxWordSize = findViewById<EditText>(R.id.maximumWordSizeInput)
        val backBttn = findViewById<Button>(R.id.backToGameBttn)
        val deleteAccntBttn = findViewById<Button>(R.id.deleteAccntBttn)

        /** Setup default min and max word sizes that was passed in from game Activity */
        var minWordSizeValueForTB = intent.getStringExtra("minSize")
        var maxWordSizeValeuForTB = intent.getStringExtra("maxSize")
        minWordSize.setText(minWordSizeValueForTB)
        maxWordSize.setText(maxWordSizeValeuForTB)

        val accountID = intent.getStringExtra("userIDValue")
        // allow for account deletion
        if(accountID != null) {
            Log.d("userIDValue", accountID)
        }
        deleteAccntBttn.setOnClickListener{
            vm.deleteAccnt()
            setResult(212)
            finish()
        }



        /** Let's set some button functionalities */

        backBttn.setOnClickListener{
            var validMinLength: Boolean = vm.isValidMinLength(minWordSize.text.toString().toInt())
            var validMaxLength: Boolean = vm.isValidMaxLength(maxWordSize.text.toString().toInt())
            // if both lengths are within allowed ranges, return to main game screen
            if (validMaxLength && validMinLength){
                val toGame = Intent()
                var minLength = minWordSize.text.toString().toInt()
                var maxLength = maxWordSize.text.toString().toInt()
                var bundle = Bundle()
                bundle.putInt("minLength", minLength)
                bundle.putInt("maxLength", maxLength)
                toGame.putExtra("bundle", bundle)
                setResult(123, toGame)
                finish()
//                val toGame = Intent(this, GameScreenMainActivityAlecMirambeau::class.java)
//                startActivity(toGame)
            }
//
//            // Display snackbar letting user know what bounds are invalid
            else if (!validMinLength && !validMaxLength){
                vm.assignMessage("Minimum and Maximum length must be between 1 - 6")
                showSnackbarMssg()
            }
//
            else if (!validMaxLength){
                vm.assignMessage("Maximum Length must be between 1 - 6")
                showSnackbarMssg()
            }
            else if (!validMinLength){
                vm.assignMessage("Minimum length must be between 1 - 6")
                showSnackbarMssg()
            }
        }

        /**
         * Lets setup observers
         */
        vm.snackMsg.observe(this){
            it?.let {
                if (it.length > 0)
                    Snackbar.make(minWordSize, it, Snackbar.LENGTH_LONG).show()
            }
        }




//        wordSizeSB.setProgress(4)
//        wordSizeSB.setMax(8)

    }

//    override fun onResume() {
//        super.onResume()
//        var minWordSizeValueForTB = intent.getStringExtra("minSize")
//        var maxWordSizeValeuForTB = intent.getStringExtra("maxSize")
//    }

    fun showSnackbarMssg(): Unit{
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
        }
        // show snackbar
        Snackbar.make(
            findViewById(R.id.settingsUI), vm._snackMssg, Snackbar.LENGTH_SHORT
        ).show() //required to type 3 arguments

    }

}