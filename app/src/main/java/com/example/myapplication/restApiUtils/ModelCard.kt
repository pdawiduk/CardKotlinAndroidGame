package com.example.myapplication.restApiUtils

import retrofit2.http.Query

data class ModelCard(val image:String, val value:String, val suit:String, val code:String)
data class ShuffleCardResponse(val success:Boolean,val shuffled: Boolean, val deck_id: String,  val remaining:Int)
data class DrawCardResponse(val success: Boolean, val cards:List<ModelCard>,val decks_id:String, val remaining: Int)