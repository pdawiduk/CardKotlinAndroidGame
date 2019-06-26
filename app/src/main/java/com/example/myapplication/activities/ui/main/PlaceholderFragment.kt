package com.example.myapplication.activities.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_decks_of_card.*


/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    private var imageURL =""
    private var cardValue = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val boundle = arguments

        imageURL = boundle!!.getString(ARG_IMG_URL,"")
        cardValue = boundle!!.getString(ARG_CARD_VALUE,"")
        pageViewModel = ViewModelProviders.of(this)
            .get(PageViewModel::class.java)
            .apply {
                setValue(arguments?.getString(ARG_IMG_URL) ?: "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_decks_of_card, container, false)

        pageViewModel.text.observe(this, Observer<String> {

        })


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvNameOfCard.text = cardValue
        Picasso.get().load(imageURL).into(ivImageOfCard)
    }

    companion object {

        private const val ARG_IMG_URL = "IMAGE_URL"
        private const val ARG_CARD_VALUE = "CARD_VALUE"


        @JvmStatic
        fun newInstance(url: String, value:String): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMG_URL, url)
                    putString(ARG_CARD_VALUE, value)
                }
            }
        }
    }
}