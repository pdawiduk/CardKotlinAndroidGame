package com.example.myapplication.restApiUtils


import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CardApiServices {

@GET("new/shuffle/")
fun shuffleTheCards(@Query("deck_count") count:Int): Observable<ShuffleCardResponse>
//    @GET("/")
//    fun shuffleTheCards( ): Observable<ShuffleCardResponse>


    @GET("draw/")
    fun drawACards(@Query("count") count:Int):Observable<DrawCardResponse>

    @GET("shuffle/")
    fun reshuffleACards():Observable<ShuffleCardResponse>

}
