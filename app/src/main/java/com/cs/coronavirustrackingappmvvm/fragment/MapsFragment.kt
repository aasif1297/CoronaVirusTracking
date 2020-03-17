package com.cs.coronavirustrackingappmvvm.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs.coronavirustrackingappmvvm.R
import com.cs.coronavirustrackingappmvvm.adapter.CoronaAdapter
import com.cs.coronavirustrackingappmvvm.component.WorkaroundMapFragment
import com.cs.coronavirustrackingappmvvm.model.Feature
import com.cs.coronavirustrackingappmvvm.model.TrackingResponseModel
import com.cs.coronavirustrackingappmvvm.viewmodel.TrackingViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MapsFragment() : Fragment(), CoroutineScope {

    private var trackingResponseList: ArrayList<Feature> = ArrayList()
    private var coronaAdapter: CoronaAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var trackingViewModel: TrackingViewModel? = null
    private lateinit var mProgressDialog: ProgressDialog
    private var mMap: GoogleMap? = null
    private var mScrollView: NestedScrollView? = null
    private var edt_search: EditText? = null
    private var search: Button? = null


    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerview)
        edt_search = view.findViewById(R.id.edt_search)
        search = view.findViewById(R.id.search)
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Loading")
        mProgressDialog.setCancelable(false)
        mProgressDialog.isIndeterminate = true
        mProgressDialog.show()

        if (mMap == null) {
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as WorkaroundMapFragment
            mapFragment.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(p0: GoogleMap?) {
                    mMap = p0;
                    mMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.style_json))
                    val sydney = LatLng(-34.0, 151.0)
                    mMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                    mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                    mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL;
                    mMap?.uiSettings!!.isZoomControlsEnabled = true;
                    mScrollView = view.findViewById(R.id.scrollView)
                    (childFragmentManager.findFragmentById(R.id.map) as WorkaroundMapFragment).setListener(object : WorkaroundMapFragment.OnTouchListener {
                        override fun onTouch() {
                            mScrollView?.requestDisallowInterceptTouchEvent(true)
                        }
                    })
                }
            })

            search!!.setOnClickListener {
                showSearchDialog()
            }
        }

        trackingViewModel = ViewModelProviders.of(this).get(TrackingViewModel::class.java)
        launch {
            trackingViewModel!!.init()
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    async {
                        trackingViewModel?.getTrackingRepository()!!.observe(
                            this@MapsFragment,
                            Observer<TrackingResponseModel?> { trackingResponse ->
                                if (trackingResponse != null) {
                                    val trackingList = trackingResponse?.features
                                    trackingResponseList.addAll(trackingList)
                                    coronaAdapter!!.notifyDataSetChanged()
                                    mProgressDialog.cancel()
                                }
                            })
                    }
                }
            }
        }


        setupRecyclerview()
    }

    private fun setupRecyclerview() {
        if (coronaAdapter == null) {
            coronaAdapter = CoronaAdapter(context!!, trackingResponseList)
            recyclerView!!.layoutManager = LinearLayoutManager(context!!)
            recyclerView!!.adapter = coronaAdapter
            recyclerView!!.itemAnimator = DefaultItemAnimator()
            recyclerView!!.isNestedScrollingEnabled = false
        } else {
            coronaAdapter!!.notifyDataSetChanged()
        }
    }

    private fun showSearchDialog() {
        val alertDialog = AlertDialog.Builder(context!!)
        val inflater = layoutInflater
        val convertView: View = inflater.inflate(R.layout.dialog_layout, null)
        alertDialog.setView(convertView)
        alertDialog.setCancelable(false)
        val ad: AlertDialog = alertDialog.show()
        val edt_search: EditText = convertView.findViewById(R.id.edt_search)
        val button: Button = convertView.findViewById(R.id.btn_search)

        button.setOnClickListener {
            coronaAdapter!!.filter.filter(edt_search.text.toString())
            ad.dismiss()
        }
    }
}