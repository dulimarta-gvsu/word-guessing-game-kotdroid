package edu.gvsu.cis.wordguess

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class statisticsActivity: AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    val vm: statisticsVewModel by viewModels()


//    init {
//        println("works up to here")
//
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics_screen)
//        vm = ViewModelProvider(this).get(GameScreenViewModel::class.java)
        val backToGameButton = findViewById<Button>(R.id.backToGameButton)
        var words: ArrayList<GuessData> = intent.getParcelableArrayListExtra<GuessData>("wordList")!!
        vm.guessedWords = words
        println("words are: ")
        recyclerView = findViewById<RecyclerView>(R.id.recycleViewer)
        println("made it to statistics activity onCreate")
        recyclerView.adapter = adapterClass(vm.guessedWords)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val sortByDurationBttn = findViewById<Button>(R.id.sortByDuration)
        val sortByWordLength = findViewById<Button>(R.id.sortByWordLength)
        val avgGuessTimeTB = findViewById<TextView>(R.id.averageGuessDurationTextView)
        avgGuessTimeTB.text = "Average Guess Time: ${vm.avgGuessTime()} seconds"
//
        sortByDurationBttn.setOnClickListener{
            vm.sortByTime()
            println("sorted Array is now: ${vm.guessedWords}")
            (recyclerView.adapter)?.notifyDataSetChanged()
        }

        sortByWordLength.setOnClickListener{
            vm.sortByWordLength()
            (recyclerView.adapter)?.notifyDataSetChanged()
        }


        backToGameButton.setOnClickListener{
            val toGame = Intent()
            toGame.putExtra("wordListGuessed", words)
            setResult(321, toGame)
            finish()
        }


        }

}