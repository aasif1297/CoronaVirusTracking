package com.cs.coronavirustrackingappmvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs.coronavirustrackingappmvvm.R
import com.cs.coronavirustrackingappmvvm.adapter.CoronaAdapter
import com.cs.coronavirustrackingappmvvm.model.Feature
import com.cs.coronavirustrackingappmvvm.viewmodel.TrackingViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {

    var trackingResponseList: ArrayList<Feature> = ArrayList()
    var coronaAdapter: CoronaAdapter? = null
    var recyclerView: RecyclerView? = null
    var trackingViewModel: TrackingViewModel? = null

    private val callback = OnMapReadyCallback { googleMap ->
        val success = googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                context, R.raw.style_json
            )
        )
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerview)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        trackingViewModel = ViewModelProviders.of(this).get(TrackingViewModel::class.java)
        trackingViewModel!!.init()

        trackingViewModel!!.getTrackingRepository()!!.observe(this, Observer{trackingResponse ->
            val trackingList = trackingResponse!!.features
            trackingResponseList.addAll(trackingList)
            coronaAdapter!!.notifyDataSetChanged()

        })

        setupRecyclerview()
    }

    private fun setupRecyclerview(){
        if (coronaAdapter == null){
            coronaAdapter = CoronaAdapter(context!!, trackingResponseList)
            recyclerView!!.layoutManager = LinearLayoutManager(context!!)
            recyclerView!!.adapter = coronaAdapter
            recyclerView!!.itemAnimator = DefaultItemAnimator()
            recyclerView!!.isNestedScrollingEnabled = true
        }else{
            coronaAdapter!!.notifyDataSetChanged()
        }
    }
}