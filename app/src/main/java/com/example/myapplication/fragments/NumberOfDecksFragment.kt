package com.example.myapplication.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter

import com.example.myapplication.R
import com.example.myapplication.activities.DecksOfCardActivity
import com.example.myapplication.restApiUtils.CardApiServices
import com.example.myapplication.restApiUtils.ShuffleCardResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_number_of_decks.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class NumberOfDecksFragment : Fragment() , OnItemSelectedListener{
    override fun onNothingSelected(p0: AdapterView<*>?) {
        this.selectedItem = 1
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        this.selectedItem=p2+1
    }

    private var selectedItem : Int = 0
    private val BASE_URL: String = "https://deckofcardsapi.com/api/deck/"
    private val TAG = NumberOfDecksFragment::class.java.simpleName

    private  var DECK_ID: String =""
    private var compositeDisposale: CompositeDisposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposale = CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_number_of_decks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareSpinner()

        btnStartGame.setOnClickListener {

            val requestInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(CardApiServices::class.java)

            compositeDisposale?.add(
                requestInterface.shuffleTheCards(selectedItem)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError))

        }
    }

    private fun handleError(error: Throwable) {
        Log.d(TAG, "ERRRORRR ====== "+error.localizedMessage)
    }

    private fun prepareSpinner() {

         ArrayAdapter.createFromResource(this.context!!, R.array.number_of_decks,android.R.layout.simple_spinner_item)
            .also {
                    adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                        spinCountOfDeck.onItemSelectedListener = this
                        spinCountOfDeck.adapter = adapter
            }
    }

    private fun handleResponse(response: ShuffleCardResponse){

        Log.d(TAG,"Response ID ==== " + response.deck_id)
        DECK_ID = response.deck_id

        val intent = Intent(context,DecksOfCardActivity::class.java)
        intent.putExtra("DECK_ID",DECK_ID)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposale?.clear()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NumberOfDecksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
