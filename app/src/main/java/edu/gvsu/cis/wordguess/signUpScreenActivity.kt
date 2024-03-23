package edu.gvsu.cis.wordguess

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

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
            if (passwordConfirmTB == passwordInitialTB) {
                vm.createNewAccount(emailTB.toString(), passwordConfirmTB.toString(), userNameTB.toString())
            } else{
                vm._snackMsg.value = "Passwords don't match"
            }
        }








    }
}