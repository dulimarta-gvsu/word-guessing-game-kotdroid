package edu.gvsu.cis.wordguess

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class loginScreenActivity: AppCompatActivity() {
    val myViewModel: loginScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        // setup widgets
        val loginBttn = findViewById<Button>(R.id.logInButton)
        val emailTB = findViewById<TextView>(R.id.usernameTextBox)
        val passwordTB = findViewById<TextView>(R.id.passwordTextbox)



        // setup button functionality
        loginBttn.setOnClickListener{
            if (emailTB.text.toString().length != 0 && passwordTB.text.toString().length != 0){

            }
        }

    }

}