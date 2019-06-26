package com.example.myapplication


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import com.example.myapplication.fragments.WelcomeFragment
import android.os.StrictMode



class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        addFragment(WelcomeFragment.newInstance("",""),R.id.fragmentContainer)
    }

    fun replaceFragmentFromActivity(fragment :Fragment){

        replaceFragment(fragment,R.id.fragmentContainer)
    }
}
