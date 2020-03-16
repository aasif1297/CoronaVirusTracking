package com.cs.coronavirustrackingappmvvm.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.cs.coronavirustrackingappmvvm.fragment.MapsFragment
import com.cs.coronavirustrackingappmvvm.R


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setScreenTitle("Maps Fragment")
        getBackButton()
        replaceFragment(MapsFragment(), false, "Maps Fragment")
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean, tag: String){
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.replace(R.id.container_frame_back, fragment, tag)
        ft.commitAllowingStateLoss()
    }
}
