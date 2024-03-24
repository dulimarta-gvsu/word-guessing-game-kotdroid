package edu.gvsu.cis.wordguess

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class loginScreenActivity: AppCompatActivity() {
    val vm: loginScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        val inputMM = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        // setup widgets
        val loginBttn = findViewById<Button>(R.id.logInButton)
        val passwordTB = findViewById<TextView>(R.id.passwordTextbox)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val emailTB = findViewById<TextView>(R.id.logInEmailTextbox)


        // setup button functionality
        loginBttn.setOnClickListener{
            inputMM.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            if (emailTB.text.toString().length != 0 && passwordTB.text.toString().length != 0){
                vm.doLogin(emailTB.text.toString(), passwordTB.text.toString())
            }
        }

        /** Lets implement the Sign Up screen button launcher */
        signUpButton.setOnClickListener{
            val toSignUp = Intent(this, signUpScreenActivity::class.java)
            startActivity(toSignUp)
        }


        /**
         * Setup observers
         */
        vm.snackMsg.observe(this) {
            it?.let {
                if (it.length > 0)
                    Snackbar.make(emailTB, it, Snackbar.LENGTH_LONG).show()
            }
        }


        vm.logInSucc.observe(this){
            it?.let{
                if(it) {
                    val toGame = Intent(this, GameScreenMainActivityAlecMirambeau::class.java)
                    toGame.putExtra("userIDValue", vm.userID.value)
                    startActivity(toGame)
                }
            }
        }


    }

}