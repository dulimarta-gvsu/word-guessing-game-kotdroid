package edu.gvsu.cis.wordguess

data class userData(val email: String = "empty",
               val password: String = "empty",
               val userName: String = "empty",
               val guessedWords: ArrayList<GuessData> = ArrayList()
)