package com.example.myapplication.activities

import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.activities.ui.main.SectionsPagerAdapter
import com.example.myapplication.restApiUtils.CardApiServices
import com.example.myapplication.restApiUtils.DrawCardResponse
import com.example.myapplication.restApiUtils.ShuffleCardResponse
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_decks_of_card.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Math.abs




class DecksOfCardActivity : AppCompatActivity() {
    private val TAG = DecksOfCardActivity::class.java.simpleName
    private var DeckId=""

    private var loaded = false
    val QUEEN_VALUE = "QUEEN"
    val KING_VALUE ="KING"
    val JACK_VALUE = "JACK"
    val ACE_VALUE = "ACE"

    private val JACK_VALUE_INT = 11
    private val QUEEN_VALUE_INT = 12
    private val KING_VALUE_INT = 13
    private val ACE_VALUE_INT = 14

    private var BASE_URL:String? = null
    private var compositeDisposale: CompositeDisposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decks_of_card)

        val intent = intent
        DeckId = intent.getStringExtra("DECK_ID")

        BASE_URL = buildBaseURLwithDeckID()

        compositeDisposale = CompositeDisposable()


        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CardApiServices::class.java)

        compositeDisposale?.add(
            requestInterface.drawACards(NUMBER_OF_CARD)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse))
    }

    fun buildBaseURLwithDeckID():String {
        return API_PREFIX + DeckId + "/"

    }

    private fun handleResponse(response: DrawCardResponse){
        val listOfCards= response.cards

        var cardsMapByValue  = mapOf<Int,MutableList<String>>().toMutableMap()
        var cardsMapBySuite = mapOf<String,MutableList<Int>>().toMutableMap()
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)
        var sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, listOfCards)
        viewPager.adapter = sectionsPagerAdapter
        if(!loaded) {


            tabs.setupWithViewPager(viewPager)
            loaded =true
        }
        else{


            viewPager.adapter =sectionsPagerAdapter
            viewPager!!.adapter!!.notifyDataSetChanged()
        }

        viewPager.adapter?.notifyDataSetChanged()

        for(card in listOfCards){

            val intValueOfCard:Int =  convertCardValueToInt(card.value)

            var listOfSuite = cardsMapByValue.get(intValueOfCard) ?: listOf<String>().toMutableList()

            listOfSuite?.add(card.suit)
            cardsMapByValue.put(intValueOfCard,listOfSuite!!)



            var listOfValues = cardsMapBySuite.get(card.suit) ?: listOf<Int>().toMutableList()

            listOfValues.add(intValueOfCard)
            cardsMapBySuite.put(card.suit,listOfValues)

        }

        if(checkTripleOfCards(cardsMapByValue)){
            Snackbar.make(csParent,"TRPLE",Snackbar.LENGTH_LONG).show()
            Log.d(TAG," ================ TRIPLE ==================")
        }
        if(checkFigures(cardsMapByValue)){
            Snackbar.make(csParent,"FIGURES",Snackbar.LENGTH_LONG).show()
            Log.d(TAG," ================ FIGURES ==================")
        }
        if(checkColor(cardsMapBySuite)){
            Snackbar.make(csParent,"COLORS",Snackbar.LENGTH_LONG).show()
            Log.d(TAG," ================ COLORS ==================")
        }
        if(checkStairwayCard(cardsMapByValue)){
            Snackbar.make(csParent,"STAIRWAY",Snackbar.LENGTH_LONG).show()
            Log.d(TAG," ================ STARWAY ==================")
        }

    }

    fun reshuffleHandleResponse(response: ShuffleCardResponse){

        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CardApiServices::class.java)

        compositeDisposale?.add(
            requestInterface.drawACards(NUMBER_OF_CARD)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }



    private fun convertCardValueToInt(value:String):Int{


        when(value){

            JACK_VALUE ->return JACK_VALUE_INT
            QUEEN_VALUE -> return QUEEN_VALUE_INT
            KING_VALUE -> return KING_VALUE_INT
            ACE_VALUE ->return ACE_VALUE_INT

            else -> return value.toInt()
        }
    }

    private fun checkTripleOfCards(map:MutableMap<Int,MutableList <String>>):Boolean{
        Log.d(TAG,"TRIPLE METHOD")
        val keys= map.keys

        for (key in keys){

            if(map.get(key)!!.size > 2)
                return true
        }

        return false
    }

    private fun checkStairwayCard(map:MutableMap<Int,MutableList <String>>):Boolean{

        Log.d(TAG,"STAIRWAY METHOD")
        val keys = map.keys.toSortedSet().toList()
        var stairwayCard = 0

        var currentKeyValue  = 0
        for (i in 1 until keys.size -2){
            currentKeyValue = keys[i-1]
            val nextValue = keys[i]

            if(abs(currentKeyValue - nextValue) == 1){
                stairwayCard ++

                if(stairwayCard > 1){
                    return true
                }
            }
            else{
                stairwayCard = 0
            }
        }

        return false
    }


    override fun onResume() {
        super.onResume()

        btnNewDeck.setOnClickListener {

            val requestInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(CardApiServices::class.java)

            compositeDisposale?.add(
                requestInterface.reshuffleACards()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::reshuffleHandleResponse, this::handleError))

        }
    }

    private fun checkFigures(map:MutableMap<Int,MutableList <String>>):Boolean {
        Log.d(TAG,"FIGURE METHOD")


        var queens=map.get(QUEEN_VALUE_INT)?.size ?: 0

        var kings = map.get(KING_VALUE_INT)?.size ?: 0

        var jacks = map.get(JACK_VALUE_INT)?.size ?: 0
        var aces = map.get(ACE_VALUE_INT)?.size ?: 0

        val countOfFigures = queens + kings + jacks + aces

        if(countOfFigures > 2)
            return true

        return false
    }


    private fun checkColor(map:MutableMap<String,MutableList <Int>>):Boolean{
        Log.d(TAG,"COLOR METHOD")
        val keys = map.keys

        for(key in keys){

            if(map.get(key)!!.size > 2)
                return true
        }

        return false
    }

    private fun handleError(error: Throwable) {
        Log.d(TAG, "ERRRORRR ====== "+error.message)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposale?.clear()
    }

    companion object{

        private val API_PREFIX:String = "https://deckofcardsapi.com/api/deck/"

        private val NUMBER_OF_CARD:Int = 5
    }
}