package edu.gvsu.cis.wordguess

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class signUpScreenActivity: AppCompatActivity() {
    val vm: signUpScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_ui)
        val inputMM = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager


        // Setup widget connections
        // text Widgets
        val emailTB = findViewById<TextView>(R.id.signUpEmailTB)
        val passwordInitialTB = findViewById<TextView>(R.id.signUpPasswordEnter)
        val passwordConfirmTB = findViewById<TextView>(R.id.signUpPasswordConfirmation)
        val userNameTB = findViewById<TextView>(R.id.signUpUserNameTB)
        // Button Widgets
        val cancelBttn = findViewById<Button>(R.id.cancelButton)
        val createBttn = findViewById<Button>(R.id.createButton)

        // Setup On Click Listeners

        createBttn.setOnClickListener{
            if (passwordConfirmTB.text.toString() == passwordInitialTB.text.toString()) {
                vm.createNewAccount(emailTB.text.toString(), passwordConfirmTB.text.toString(), userNameTB.text.toString())
            } else{
                vm._snackMsg.value = "Passwords don't match"
            }
        }

        cancelBttn.setOnClickListener {
            finish()
        }


        // Setup observers
        vm.snackMsg.observe(this){
            it?.let{
                if (it.length > 0){
                    Snackbar.make(emailTB, it, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        vm.signUpSuccess.observe(this){
            if (it != null && it == true){
                val toGame = Intent(this, GameScreenMainActivityAlecMirambeau::class.java)
                if (vm.userID.value != null) {
                    toGame.putExtra("userIDValue", vm.userID.value)
                    finish()
                    startActivity(toGame)
                }
            }
        }








    }
}