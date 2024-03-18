package edu.gvsu.cis.wordguess

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


//
// API to be used: https://random-word-api.herokuapp.com/word

val logInterceptor = HttpLoggingInterceptor()
interface WordAPI{
    @GET("word")
    suspend fun getWord(): Array<String>
   // https://random-word-api.herokuapp.com/word?number=10&length=7
    @GET("word?number={numberWords}&length={maxWordlength}")
    suspend fun getMultipleWords(@Path("numberWords") numWords: Int,
                                 @Path("maxWordLength") maxLength: Int): Response<List<String>>

}

object wordClient{
    val BASE_URL = "https://random-word-api.herokuapp.com/"
    val okHttpClientBuilder = OkHttpClient.Builder()

    fun getInstance(): Retrofit {
        logInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        okHttpClientBuilder.addInterceptor(logInterceptor)
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()

    }
}
